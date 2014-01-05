// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.composite;

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Delegates inspection to one or more sub-inspectors, then combines the resulting DOMs.
 * <p>
 * The combining algorithm works as follows. For each element:
 * <ul>
 * <li>top-level elements with the same <code>type</code> attribute in both DOMs are combined
 * <li>child elements with the same <code>name</code> attribute in both DOMs are combined
 * <li>the ordering of child elements in the existing DOM is preserved. New child elements are added
 * either at the end or immediately after the last combined child
 * <li>element attributes from the new DOM override ones in the existing DOM
 * </ul>
 * <p>
 * This algorithm should be suitable for most use cases, but one benefit of having a separate
 * CompositeInspector is that developers can replace it with their own version, with its own
 * combining algorithm, if required.
 * <p>
 * Note: the name <em>Composite</em>Inspector refers to the Composite design pattern.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CompositeInspector
	implements DomInspector<Element> {

	//
	// Private statics
	//

	private static final Log				LOG	= LogUtils.getLog( CompositeInspector.class );

	//
	// Private members
	//

	/* package private */final Inspector[]	mInspectors;

	//
	// Constructor
	//

	public CompositeInspector( CompositeInspectorConfig config ) {

		Inspector[] inspectors = config.getInspectors();

		// Must have at least one Inspector. At least two, really, but one can be useful
		// if we want to validate what the sub-Inspector is returning (ie. using LOG.debug)

		if ( inspectors == null || inspectors.length == 0 ) {
			throw InspectorException.newException( "CompositeInspector needs at least one Inspector" );
		}

		// Defensive copy

		mInspectors = new Inspector[inspectors.length];

		for ( int loop = 0, length = inspectors.length; loop < length; loop++ ) {
			Inspector inspector = inspectors[loop];

			for ( int checkDuplicates = 0; checkDuplicates < loop; checkDuplicates++ ) {
				if ( mInspectors[checkDuplicates].equals( inspector ) ) {
					throw InspectorException.newException( "CompositeInspector's list of Inspectors contains two of the same " + inspector.getClass().getName() );
				}
			}

			mInspectors[loop] = inspector;
		}
	}

	//
	// Public methods
	//

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * This method is marked <code>final</code> because most Metawidget implementations will call
	 * <code>inspectAsDom</code> directly instead. So subclasses need to override
	 * <code>inspectAsDom</code>, not <code>inspect</code>.
	 */

	public final String inspect( Object toInspect, String type, String... names ) {

		return inspect( null, toInspect, type, names );
	}

	/**
	 * If your architecture is strongly separated, some metadata may only be available in one tier
	 * (eg. JPA annotations in the backend) and some only available in another tier (eg.
	 * struts-config.xml in the front-end).
	 * <p>
	 * For this, <code>CompositeInspector</code> supplies this overloaded method outside the normal
	 * <code>Inspector</code> interface. It takes an additional XML string of inspection results,
	 * and merges forthcoming inspection results with it.
	 * <p>
	 * This method is marked <code>final</code> because most Metawidget implementations will call
	 * <code>inspectAsDom</code> directly instead. So subclasses need to override
	 * <code>inspectAsDom</code>, not <code>inspect</code>.
	 */

	public final String inspect( String master, Object toInspect, String type, String... names ) {

		Element element = inspectAsDom( XmlUtils.documentFromString( master ), toInspect, type, names );

		if ( element == null ) {
			return null;
		}

		return XmlUtils.nodeToString( element, false );
	}

	/**
	 * This method is marked <code>final</code> as it delegates directly to
	 * <code>inspectAsDom( Document, Object, String, String... )</code>. Subclasses should override
	 * that method instead.
	 */

	public final Element inspectAsDom( Object toInspect, String type, String... names ) {

		return inspectAsDom( null, toInspect, type, names );
	}

	/**
	 * If your architecture is strongly separated, some metadata may only be available in one tier
	 * (eg. JPA annotations in the backend) and some only available in another tier (eg.
	 * struts-config.xml in the front-end).
	 * <p>
	 * For this, <code>CompositeInspector</code> supplies this overloaded method outside the normal
	 * <code>DomInspector</code> interface. It takes an additional DOM of inspection results, and
	 * merges forthcoming inspection results with it.
	 */

	public Element inspectAsDom( Document masterDocument, Object toInspect, String type, String... names ) {

		try {
			Document masterDocumentToUse = runInspectors( masterDocument, toInspect, type, names );

			if ( masterDocumentToUse == null || !masterDocumentToUse.hasChildNodes() ) {
				if ( toInspect != null && type != null && LOG.isWarnEnabled() ) {
					LOG.warn( "No inspectors matched path == {0}{1}", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) );
				}

				return null;
			}

			// (debug)

			if ( LOG.isDebugEnabled() ) {
				String formattedXml = XmlUtils.documentToString( masterDocumentToUse, true );
				LOG.debug( "Inspected {0}{1}\r\n{2}", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), formattedXml );
			}

			// (warn)

			Element root = masterDocumentToUse.getDocumentElement();

			if ( toInspect != null && type != null && LOG.isWarnEnabled() && !root.hasChildNodes() ) {
				LOG.warn( "No inspectors matched path == {0}{1}", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) );
				return root;
			}

			return root;
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	/**
	 * Run the sub-Inspectors on the given toInspect and combine the result.
	 * <p>
	 * Subclasses may override this method to, say, run some other Inspectors concurrently.
	 */

	protected Document runInspectors( Document masterDocument, Object toInspect, String type, String... names )
		throws Exception {

		Document masterDocumentToUse = masterDocument;

		// Run each Inspector...

		for ( Inspector inspector : mInspectors ) {

			// ...parse the result...

			Document inspectionDocument = runInspector( inspector, toInspect, type, names );

			// ...combine them...

			masterDocumentToUse = combineInspectionResult( masterDocumentToUse, inspectionDocument );
		}

		// ...and return them

		return masterDocumentToUse;
	}

	protected Document runInspector( Inspector inspector, Object toInspect, String type, String... names )
		throws Exception {

		// DomInspector...

		if ( inspector instanceof DomInspector<?> ) {

			@SuppressWarnings( "unchecked" )
			DomInspector<Element> domInspector = (DomInspector<Element>) inspector;
			Element element = domInspector.inspectAsDom( toInspect, type, names );

			if ( element == null ) {
				return null;
			}

			if ( LOG.isTraceEnabled() ) {
				String xml = XmlUtils.nodeToString( element, true );
				LOG.trace( "{0} inspected {1}{2}\r\n{3}", inspector.getClass(), type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), xml );
			}

			return element.getOwnerDocument();
		}

		// ...or just regular Inspector

		String xml = inspector.inspect( toInspect, type, names );

		if ( xml == null ) {
			return null;
		}

		LOG.trace( "{0} inspected {1}{2}\r\n{3}", inspector.getClass(), type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), xml );
		return XmlUtils.documentFromString( xml );
	}

	protected Document combineInspectionResult( Document masterDocument, Document inspectionDocument ) {

		// Short circuit...

		if ( inspectionDocument == null || !inspectionDocument.hasChildNodes() ) {
			return masterDocument;
		}

		if ( masterDocument == null || !masterDocument.hasChildNodes() ) {
			return inspectionDocument;
		}

		// ...or full combine

		XmlUtils.combineElements( masterDocument.getDocumentElement(), inspectionDocument.getDocumentElement(), TYPE, NAME );
		return masterDocument;
	}
}
