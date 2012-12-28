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

				this.childNodes = this.childNodes || [];
				this.childNodes.push( child );
			}
		};
		layout.layoutWidget( widget1, {}, container );
		layout.layoutWidget( widget2, {}, container );

		expect( container.childNodes[0] ).toBe( widget1 );
		expect( container.childNodes[1] ).toBe( widget2 );
		expect( container.childNodes.length ).toBe( 2 );
	} );
} );

describe( "The DivLayout", function() {

	it( "arranges widgets using divs", function() {

		var layout = new metawidget.layout.DivLayout();

		var widget1 = document.createElement( 'input' );
		widget1.setAttribute( 'id', 'widget1' );
		var widget2 = document.createElement( 'input' );
		widget2.setAttribute( 'id', 'widget2' );
		var container = document.createElement( 'metawidget' );
		var mw = {
			"path": "testPath"
		};

		layout.layoutWidget( widget1, {
			"name": "widget1",
		}, container, mw );
		layout.layoutWidget( widget2, {
			"name": "widget2",
			"label": "widgetLabel 2"
		}, container, mw );

		expect( container.childNodes[0].toString() ).toBe( 'div' );
		expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
		expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
		expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
		expect( container.childNodes[0].childNodes.length ).toBe( 2 );
		expect( container.childNodes[1].toString() ).toBe( 'div' );
		expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'div' );
		expect( container.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2"' );
		expect( container.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
		expect( container.childNodes[1].childNodes[1].toString() ).toBe( 'div' );
		expect( container.childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
		expect( container.childNodes[1].childNodes.length ).toBe( 2 );
		expect( container.childNodes.length ).toBe( 2 );
	} );
} );

describe( "The TableLayout", function() {

	it( "arranges widgets in a table", function() {

		var layout = new metawidget.layout.TableLayout();

		var widget1 = document.createElement( 'input' );
		widget1.setAttribute( 'id', 'widget1' );
		var widget2 = document.createElement( 'input' );
		widget2.setAttribute( 'id', 'widget2' );
		var container = document.createElement( 'metawidget' );
		var mw = {
			"path": "testPath"
		};

		layout.startContainerLayout( container, mw );
		layout.layoutWidget( widget1, {
			"name": "widget1",
			"required": "true"
		}, container, mw );
		layout.layoutWidget( widget2, {
			"name": "widget2",
			"label": "widgetLabel 2"
		}, container, mw );

		expect( container.childNodes[0].toString() ).toBe( 'table id="table-testPath"' );
		expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget1-label-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-testPathWidget1-label"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].innerHTML ).toBe( '*' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].innerHTML ).toBeUndefined();
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
		expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
		expect( container.childNodes[0].childNodes.length ).toBe( 1 );
		expect( container.childNodes.length ).toBe( 1 );
	} );
	
	// TODO: test supporting facets
} );

describe( "The HeadingTagLayoutDecorator", function() {

	it( "decorates sections with a heading tag", function() {

		var layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );

		var widget1 = document.createElement( 'input' );
		widget1.setAttribute( 'id', 'widget1' );
		var widget2 = document.createElement( 'input' );
		widget2.setAttribute( 'id', 'widget2' );
		var container = document.createElement( 'metawidget' );
		var mw = {
			"path": "testPath"
		};

		layout.onStartBuild( mw );
		layout.startContainerLayout( container, mw );
		layout.layoutWidget( widget1, {
			"name": "widget1",
		}, container, mw );
		layout.layoutWidget( widget2, {
			"name": "widget2",
			"section": "New Section"
		}, container, mw );

		expect( container.childNodes[0].toString() ).toBe( 'table id="table-testPath"' );
		expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget1-label-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-testPathWidget1-label"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
		expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		
		expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'h1' );
		expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'New Section' );

		expect( container.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
		expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );
		expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 2:' );
		expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
		expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].childNodes[0] ).toBe( widget2 );
		expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes.length ).toBe( 3 );
		expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		expect( container.childNodes[0].childNodes.length ).toBe( 1 );
	} );
} );
