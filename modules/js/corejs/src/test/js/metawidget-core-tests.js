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
		// TODO: expect( mw.inspector instanceof metawidget.inspector.PropertyTypeInspector ).toBeTruthy();
		// TODO: expect( mw.inspectionResultProcessors.length ).toBe( 0 );
		// TODO: expect( mw.widgetBuilder instanceof metawidget.widgetbuilder.CompositeWidgetBuilder ).toBeTruthy();
		// TODO: expect( mw.widgetProcessors[0] instanceof metawidget.widgetprocessor.IdProcessor ).toBeTruthy();
		// TODO: expect( mw.widgetProcessors.length ).toBe( 1 );
		// TODO: expect( mw.layout instanceof metawidget.layout.TableLayout ).toBeTruthy();

		mw.toInspect = {
			"foo": "Foo"
		};
		var element = mw.buildWidgets();
		
		expect( element.childNodes[0].toString() ).toBe( 'table' );
		expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" value="Foo"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes.length ).toBe( 1 );

		// Configured
		
		// TODO: test defensive copy

		var config = {
			layout: new metawidget.layout.SimpleLayout()
		}
		mw = new metawidget.Metawidget( config );

		mw.toInspect = {
			"bar": "Bar"
		};
		var element = mw.buildWidgets();
		
		expect( element.childNodes[0].toString() ).toBe( 'input type="text" id="bar" value="Bar"' );
		expect( element.childNodes.length ).toBe( 1 );
	} );
} );
