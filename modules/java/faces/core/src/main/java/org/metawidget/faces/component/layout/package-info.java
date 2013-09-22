// Metawidget (licensed under LGPL)
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

/**
 * Java Server Faces support: standard layouts.
 * <p>
 * JSF already supports pluggable Renderers, and we use that mechanism for rendering Metawidgets in
 * different ways (eg. <code>HtmlDivLayoutRenderer</code> and <code>HtmlTableLayoutRenderer</code>).
 * However in some cases we want to take advantage of Layout controls within third-party component
 * libraries.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

package org.metawidget.faces.component.layout;