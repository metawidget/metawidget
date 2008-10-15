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

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;

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
 * Note: the name <em>Composite</em>Inspector refers to the Composite design pattern.
 *
 * @author Richard Kennard
 */

public class CompositeInspector
	implements Inspector
{
	//
	// Private statics
	//

	private final static Log					LOG							= LogUtils.getLog( CompositeInspector.class );

	//
	// Private members
	//

	private Inspector[]							mInspectors;

	//
	// Constructor
	//

	public CompositeInspector( CompositeInspectorConfig config )
	{
		this( config, new ConfigReader() );
	}

	public CompositeInspector( CompositeInspectorConfig config, ResourceResolver resolver )
	{
		Inspector[] inspectors = config.getInspectors();

		// Must have at least one Inspector. At least two, really, but one can be useful
		// if we want to validate what the sub-Inspector is returning

		if ( inspectors == null || inspectors.length == 0 )
			throw InspectorException.newException( "CompositeInspector needs at least one Inspector" );

		// Defensive copy

		mInspectors = new Inspector[inspectors.length];
		System.arraycopy( inspectors, 0, mInspectors, 0, inspectors.length );
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		return inspect( null, toInspect, type, names );
	}

	public String inspect( String master, Object toInspect, String type, String... names )
		throws InspectorException
	{
		try
		{
			// Run each Inspector...

			Document masterDocument = XmlUtils.documentFromString( master );

			for ( Inspector inspector : mInspectors )
			{
				String xml = inspector.inspect( toInspect, type, names );

				if ( xml == null )
					continue;

				// ...parse the result...

				Document inspectionDocument = parseInspectionResult( xml );

				// ...(trace)...

				if ( LOG.isTraceEnabled() )
				{
					String formattedXml = XmlUtils.documentToString( inspectionDocument, true );
					LOG.trace( inspector.getClass() + " inspected " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + "\r\n" + formattedXml );
				}

				// ...and combine them

				if ( !inspectionDocument.hasChildNodes() )
					continue;

				if ( masterDocument == null || !masterDocument.hasChildNodes() )
				{
					masterDocument = inspectionDocument;
					continue;
				}

				XmlUtils.combineElements( masterDocument.getDocumentElement(), inspectionDocument.getDocumentElement(), TYPE, NAME );
			}

			if ( masterDocument == null || !masterDocument.hasChildNodes() )
			{
				LOG.warn( "No inspectors matched path == " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) );
				return null;
			}

			// (debug)

			if ( LOG.isDebugEnabled() )
			{
				String formattedXml = XmlUtils.documentToString( masterDocument, true );
				LOG.debug( "Inspected " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + "\r\n" + formattedXml );
			}

			return XmlUtils.documentToString( masterDocument, false );
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	protected Document parseInspectionResult( String xml )
		throws Exception
	{
		return XmlUtils.documentFromString( xml );
	}
}
