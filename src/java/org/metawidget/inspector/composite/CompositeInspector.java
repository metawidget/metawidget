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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.Inspector;
import org.metawidget.inspector.InspectorException;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.LogUtils.Log;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	//
	// Private statics
	//
	//

	private static Object		TRANSFORMER_FACTORY_DEBUG;

	private static Object		SCHEMA_METADATA;

	private final static Log	LOG	= LogUtils.getLog( CompositeInspector.class );

	//
	//
	// Private members
	//
	//

	private Inspector[]			mInspectors;

	private boolean				mValidating;

	//
	//
	// Constructor
	//
	//

	public CompositeInspector( CompositeInspectorConfig config )
	{
		this( config, new ConfigReader() );
	}

	public CompositeInspector( CompositeInspectorConfig config, ResourceResolver resolver )
	{
		Inspector[] inspectors = config.getInspectors();

		if ( inspectors == null || inspectors.length == 0 )
			throw InspectorException.newException( "CompositeInspector needs at least one Inspector" );

		// Defensive copy

		mInspectors = new Inspector[inspectors.length];
		System.arraycopy( inspectors, 0, mInspectors, 0, inspectors.length );

		mValidating = config.isValidating();

		// If validating, fetch the schema

		if ( mValidating )
		{
			// (J2SE1.4 and Android don't support java.xml.validation)

			if ( SCHEMA_METADATA == null && ClassUtils.classExists( "javax.xml.validation.SchemaFactory" ) )
			{
				InputStream in = resolver.openResource( "org/metawidget/inspector/inspection-result-1.0.xsd" );

				try
				{
					SCHEMA_METADATA = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI ).newSchema( new StreamSource( in ) );
				}
				catch( SAXException e )
				{
					throw InspectorException.newException( e );
				}
			}
		}
	}

	//
	//
	// Public methods
	//
	//

	public Document inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		return inspect( null, toInspect, type, names );
	}

	public Document inspect( Document master, Object toInspect, String type, String... names )
		throws InspectorException
	{
		try
		{
			Object transformer = null;

			// (for debugging)

			if ( LOG.isDebugEnabled() )
			{
				// (Android doesn't support java.xml.transform)

				if ( TRANSFORMER_FACTORY_DEBUG == null && ClassUtils.classExists( "javax.xml.transform.TransformerFactory" ) )
					TRANSFORMER_FACTORY_DEBUG = TransformerFactory.newInstance();

				if ( TRANSFORMER_FACTORY_DEBUG != null )
				{
					Transformer newTransformer = ( (TransformerFactory) TRANSFORMER_FACTORY_DEBUG ).newTransformer();
					newTransformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
					newTransformer.setOutputProperty( OutputKeys.INDENT, "yes" );

					transformer = newTransformer;
				}
			}

			// Run each Inspector...

			Document documentMaster = master;

			for ( Inspector inspector : mInspectors )
			{
				Document documentInspector = inspector.inspect( toInspect, type, names );

				if ( documentInspector == null )
					continue;

				// ...(for tracing)...

				if ( transformer != null && LOG.isTraceEnabled() )
				{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					( (Transformer) transformer ).transform( new DOMSource( documentInspector ), new StreamResult( out ) );
					LOG.trace( type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + "\r\n" + inspector.getClass() + "\r\n" + out.toString() );
				}

				// ...validate the result...

				if ( mValidating && SCHEMA_METADATA != null )
					( (Schema) SCHEMA_METADATA ).newValidator().validate( new DOMSource( documentInspector ) );

				// ...and combine them

				if ( !documentInspector.hasChildNodes() )
					continue;

				if ( documentMaster == null || !documentMaster.hasChildNodes() )
				{
					documentMaster = documentInspector;
					continue;
				}

				XmlUtils.combineElements( documentMaster.getDocumentElement(), documentInspector.getDocumentElement(), TYPE, NAME );
			}

			if ( documentMaster == null || !documentMaster.hasChildNodes() )
			{
				LOG.warn( "No inspectors matched " + type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) );
				return null;
			}

			// (for debugging)

			if ( transformer != null )
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				( (Transformer) transformer ).transform( new DOMSource( documentMaster ), new StreamResult( out ) );
				LOG.debug( type + ArrayUtils.toString( names, StringUtils.SEPARATOR_FORWARD_SLASH, true, false ) + "\r\n" + out.toString() );
			}

			return documentMaster;
		}
		catch ( Exception e )
		{
			throw InspectorException.newException( e );
		}
	}
}
