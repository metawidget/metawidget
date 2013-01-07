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

describe(
		"The JQuery UI Metawidget",
		function() {

			beforeEach( function() {

				var element = document.createElement( 'metawidget' );
				element.setAttribute( 'id', 'metawidget' );
				document.body.appendChild( element );
			} );

			afterEach( function() {

				document.body.removeChild( $( '#metawidget' )[0] );
			} );

			it(
					"populates itself with widgets to match the properties of business objects",
					function() {

						// Defaults

						$( '#metawidget' ).metawidget();
						$( '#metawidget' ).metawidget( "buildWidgets", {
							"foo": "Foo"
						} );

						var element = $( '#metawidget' )[0];

						expect( element.childNodes[0].outerHTML )
								.toBe(
										'<table><tbody><tr id="table-foo-row"><th id="table-foo-label-cell"><label for="foo" id="table-foo-label">Foo:</label></th><td id="table-foo-cell"><input type="text" id="foo" name="foo" value="Foo"/></td><td/></tr></tbody></table>' );

						// Configured

						$( '#metawidget' ).metawidget( "option", "layout", new metawidget.layout.SimpleLayout() );

						expect( element.childNodes[0].outerHTML ).toBe( '<input type="text" id="foo" name="foo" value="Foo"/>' );
					} );

			it( "defensively copies overridden widgets", function() {

				$( '#metawidget' ).metawidget();
				var mw = $( '#metawidget' ).data( 'metawidget' );

				var bar = document.createElement( 'span' );
				bar.setAttribute( 'id', 'bar' );
				mw._overriddenNodes = [ bar ];

				$( '#metawidget' ).metawidget( "buildWidgets", {
					"foo": "Foo",
					"bar": "Bar"
				} );

				var element = $( '#metawidget' )[0];
				expect( element.outerHTML ).toContain( '<span id="bar"/>' );

				expect( mw._overriddenNodes.length ).toBe( 1 );
				expect( mw.overriddenNodes.length ).toBe( 1 );
				mw.overriddenNodes.push( "defensive" );
				expect( mw.overriddenNodes.length ).toBe( 2 );
				expect( mw._overriddenNodes.length ).toBe( 1 );
			} );

		} );

describe( "The JQueryUIBindingProcessor", function() {

	it( "processes widgets and binds them", function() {

		var processor = new metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor();
		var attributes = {
			"name": "foo",
		};
		var mw = {
			"toInspect": {
				"slider": "42",
				"spinner": "43",
				"nested": {
					"nestedSlider": "44"
				}
			},
		};

		processor.onStartBuild( mw );

		// Slider

		var widget = document.createElement( 'div' );
		attributes = {
			"name": "slider"
		}
		$( widget ).slider();
		processor.processWidget( widget, attributes, mw );
		expect( $( widget ).slider( 'value' ) ).toBe( 42 );

		// Spinner

		var widget = document.createElement( 'input' );
		attributes = {
			"name": "spinner"
		}
		$( widget ).spinner();
		processor.processWidget( $( widget ).spinner( 'widget' )[0], attributes, mw );
		expect( $( widget ).spinner( 'value' ) ).toBe( 43 );

		// Nested widgets

		mw.path = 'object.nested';
		attributes = {
			"name": "nestedSlider"
		};
		widget = document.createElement( 'div' );
		$( widget ).slider();
		processor.processWidget( widget, attributes, mw );
		expect( $( widget ).slider( 'value' ) ).toBe( 44 );
	} );
} );
