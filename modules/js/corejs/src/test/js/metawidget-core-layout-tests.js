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

'use strict';

describe( "The SimpleLayout", function() {

	it( "appends children to the container", function() {

		var layout = new metawidget.layout.SimpleLayout();

		var widget1 = "widget1";
		var widget2 = "widget2";
		var container = {
			"appendChild": function( child ) {

				this.children = this.children || [];
				this.children.push( child );
			}
		};
		layout.layoutWidget( widget1, {}, container );
		layout.layoutWidget( widget2, {}, container );

		expect( container.children[0] ).toBe( widget1 );
		expect( container.children[1] ).toBe( widget2 );
		expect( container.children.length ).toBe( 2 );
	} );
} );

describe( "The TableLayout", function() {

	it( "arranges children in a table", function() {

		var layout = new metawidget.layout.TableLayout();

		var widget1 = "widget1";
		var widget2 = "widget2";
		var container = {
			"appendChild": function( child ) {

				this.children = this.children || [];
				this.children.push( child );
			}
		};
		var mw = {
			"path": "testPath"
		};

		layout.startContainerLayout( container );
		layout.layoutWidget( widget1, {
			"name": "widget1",
			"required": "true"
		}, container, mw );
		layout.layoutWidget( widget2, {
			"name": "widget2",
			"label": "widgetLabel 2"
		}, container, mw );

		expect( container.children[0].toString() ).toBe( 'table' );
		expect( container.children[0].children[0].toString() ).toBe( 'tbody' );
		expect( container.children[0].children[0].children[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
		expect( container.children[0].children[0].children[0].children[0].toString() ).toBe( 'th' );
		expect( container.children[0].children[0].children[0].children[0].children[0].toString() ).toBe( 'label for="widget1" id="table-testPathWidget1-label"' );
		expect( container.children[0].children[0].children[0].children[0].children[0].innerHTML ).toBe( 'Widget 1:' );
		expect( container.children[0].children[0].children[0].children[1].toString() ).toBe( 'td' );
		expect( container.children[0].children[0].children[0].children[1].children[0] ).toBe( widget1 );
		expect( container.children[0].children[0].children[0].children[2].toString() ).toBe( 'td' );
		expect( container.children[0].children[0].children[0].children[2].innerHTML ).toBe( '*' );
		expect( container.children[0].children[0].children[0].children.length ).toBe( 3 );
		expect( container.children[0].children[0].children[1].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
		expect( container.children[0].children[0].children[1].children[0].toString() ).toBe( 'th' );
		expect( container.children[0].children[0].children[1].children[0].children[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );		
		expect( container.children[0].children[0].children[1].children[0].children[0].innerHTML ).toBe( 'widgetLabel 2:' );
		expect( container.children[0].children[0].children[1].children[1].toString() ).toBe( 'td' );
		expect( container.children[0].children[0].children[1].children[1].children[0] ).toBe( widget2 );
		expect( container.children[0].children[0].children[1].children[2].toString() ).toBe( 'td' );
		expect( container.children[0].children[0].children[1].children[2].innerHTML ).toBeUndefined();
		expect( container.children[0].children[0].children[1].children.length ).toBe( 3 );
		expect( container.children[0].children[0].children.length ).toBe( 2 );
		expect( container.children[0].children.length ).toBe( 1 );
		expect( container.children.length ).toBe( 1 );
	} );
} );
