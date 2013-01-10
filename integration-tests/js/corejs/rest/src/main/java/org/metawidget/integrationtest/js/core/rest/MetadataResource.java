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

package org.metawidget.integrationtest.js.core.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Example of retrieving <em>metadata</em> using REST. Retrieving <em>data</em> using REST is
 * usually much simpler, as it is done outside of Metawidget.
 */

@Path( "/metadata" )
public class MetadataResource {

	@GET
	@Path( "/get" )
	public String getMetadata() {

		StringBuilder metadata = new StringBuilder();
		metadata.append( "[" );
		metadata.append( "{ \"name\": \"name\", \"type\": \"string\" }," );
		metadata.append( "{ \"name\": \"age\", \"type\": \"number\" }" );
		metadata.append( "]" );

		return metadata.toString();
	}
}