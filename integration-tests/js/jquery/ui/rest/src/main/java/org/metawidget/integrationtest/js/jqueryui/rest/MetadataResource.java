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

package org.metawidget.integrationtest.js.jqueryui.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Example of retrieving <em>metadata</em> using REST. Retrieving <em>data</em> using REST is
 * usually much simpler, as it is done outside of Metawidget.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Path( "/metadata" )
public class MetadataResource {

	@GET
	@Path( "/get" )
	public String getMetadata() {

		StringBuilder metadata = new StringBuilder();
		metadata.append( "{ \"properties\": {" );
		metadata.append( "\"name\": { \"type\": \"string\" }," );
		metadata.append( "\"age\": { \"type\": \"number\" }" );
		metadata.append( "}}" );

		return metadata.toString();
	}
}