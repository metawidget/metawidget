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

describe( "The Metawidget", function() {

	it( "populates itself with widgets to match the properties of business objects", function() {

		// Defaults

		var mw = new metawidget.Metawidget();
		expect( mw.inspector instanceof metawidget.inspector.PropertyTypeInspector ).toBeTruthy();
		expect( mw.inspectionResultProcessors.length ).toBe( 0 );
		expect( mw.widgetBuilder instanceof metawidget.widgetbuilder.CompositeWidgetBuilder ).toBeTruthy();
		expect( mw.widgetProcessors[0] instanceof metawidget.widgetprocessor.IdWidgetProcessor ).toBeTruthy();
		expect( mw.widgetProcessors.length ).toBe( 1 );
		expect( mw.layout instanceof metawidget.layout.TableLayout ).toBeTruthy();

		mw.toInspect = {
			"foo": "Foo"
		};
		var element = mw.buildWidgets();
		
		expect( element.children[0].toString() ).toBe( 'table' );
		expect( element.children[0].children[0].toString() ).toBe( 'tbody' );
		expect( element.children[0].children[0].children[0].toString() ).toBe( 'tr id="table-foo-row"' );
		expect( element.children[0].children[0].children[0].children[0].toString() ).toBe( 'th' );
		expect( element.children[0].children[0].children[0].children[0].children[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
		expect( element.children[0].children[0].children[0].children[0].children[0].innerHTML ).toBe( 'Foo:' );
		expect( element.children[0].children[0].children[0].children[1].toString() ).toBe( 'td' );
		expect( element.children[0].children[0].children[0].children[1].children[0].toString() ).toBe( 'input type="text" id="foo"' );
		expect( element.children[0].children[0].children[0].children[2].toString() ).toBe( 'td' );
		expect( element.children[0].children[0].children[0].children.length ).toBe( 3 );
		expect( element.children[0].children[0].children.length ).toBe( 1 );
		expect( element.children[0].children.length ).toBe( 1 );
		expect( element.children.length ).toBe( 1 );

		// Configured

		var config = {
			layout: new metawidget.layout.SimpleLayout()
		}
		mw = new metawidget.Metawidget( config );

		mw.toInspect = {
			"bar": "Bar"
		};
		var element = mw.buildWidgets();
		
		expect( element.children[0].toString() ).toBe( 'input type="text" id="bar"' );
		expect( element.children.length ).toBe( 1 );
	} );
} );
