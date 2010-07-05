// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.inspector.composite;

import static org.metawidget.inspector.InspectionResultConstants.*;

import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
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
 * <li>the ordering of elements in the existing DOM is preserved. New child elements are added
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
 * @author Richard Kennard
 */

public class CompositeInspector
	implements Inspector, DomInspector<Element> {

	//
	// Private statics
	//

	private final static Log				LOG	= LogUtils.getLog( CompositeInspector.class );

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

	public String inspect( Object toInspect, String type, String... names ) {

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
	 */

	public String inspect( String master, Object toInspect, String type, String... names ) {

		Element element = inspectAsDom( XmlUtils.documentFromString( master ), toInspect, type, names );

		if ( element == null ) {
			return null;
		}

		return XmlUtils.nodeToString( element, false );
	}

	public Element inspectAsDom( Object toInspect, String type, String... names ) {

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
			Document masterDocumentToUse = masterDocument;

			// Run each Inspector...

			for ( Inspector inspector : mInspectors ) {

				// ...parse the result...

				Document inspectionDocument;

				if ( inspector instanceof DomInspector<?> ) {

					@SuppressWarnings( "unchecked" )
					DomInspector<Element> domInspector = (DomInspector<Element>) inspector;
					Element element = domInspector.inspectAsDom( toInspect, type, names );

					if ( element == null ) {
						continue;
					}

					inspectionDocument = element.getOwnerDocument();
				} else {
					String xml = inspector.inspect( toInspect, type, names );

					if ( xml == null ) {
						continue;
					}

					inspectionDocument = parseInspectionResult( xml );
				}

				// ...(trace)...

				if ( LOG.isTraceEnabled() ) {
					String formattedXml = XmlUtils.documentToString( inspectionDocument, true );
					LOG.trace( "{0} inspected {1}{2}\r\n{3}", inspector.getClass(), type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), formattedXml );
				}

				// ...and combine them

				if ( inspectionDocument == null || !inspectionDocument.hasChildNodes() ) {
					continue;
				}

				if ( masterDocumentToUse == null || !masterDocumentToUse.hasChildNodes() ) {
					masterDocumentToUse = inspectionDocument;
					continue;
				}

				XmlUtils.combineElements( masterDocumentToUse.getDocumentElement(), inspectionDocument.getDocumentElement(), TYPE, NAME );
			}

			if ( masterDocumentToUse == null || !masterDocumentToUse.hasChildNodes() ) {
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "No inspectors matched path == {0}{1}", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) );
				}

				return null;
			}

			// (debug)

			if ( LOG.isDebugEnabled() ) {
				String formattedXml = XmlUtils.documentToString( masterDocumentToUse, true );
				LOG.debug( "Inspected {0}{1}\r\n{2}", type, ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ), formattedXml );
			}

			return masterDocumentToUse.getDocumentElement();
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected Document parseInspectionResult( String xml )
		throws Exception {

		return XmlUtils.documentFromString( xml );
	}
}
