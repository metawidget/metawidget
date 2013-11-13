// Metawidget ${project.version}
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

( function() {

	'use strict';

	describe(
			"The JQuery Mobile Metawidget",
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
							$( '#metawidget' ).metawidget( 'buildWidgets', {
								foo: "Foo"
							} );

							expect( $( "metawidget" ).data( "metawidget" )).toBeDefined();
							expect( $( "metawidget" ).data( "metawidget" ).toInspect ).toBeDefined();
							var element = $( '#metawidget' )[0];

							expect( element.getMetawidget() ).toBeDefined();
							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<div><div><label for="foo" class="ui-input-text">Foo:</label></div><div><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="foo" name="foo" class="ui-input-text ui-body-c"/></div></div></div>' );

							// Configured

							$( '#metawidget' ).metawidget( "option", "layout", new metawidget.layout.SimpleLayout() );
							$( '#metawidget' ).metawidget( 'buildWidgets' );

							expect( element.childNodes[0].outerHTML ).toBe( '<input type="text" id="foo" name="foo"/>' );
							expect( element.childNodes[0].value ).toBe( 'Foo' );
						} );

				it(
						"supports sub names",
						function() {

							// Just type

							$( '#metawidget' ).metawidget();
							$( '#metawidget' ).metawidget( "buildWidgets", {
								foo: {
									bar: "Bar"
								}
							}, "object" );

							var element = $( '#metawidget' )[0];

							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<div><div><label for="foo">Foo:</label></div><div><div id="foo"><div><div><label for="fooBar" class="ui-input-text">Bar:</label></div><div><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="fooBar" name="fooBar" class="ui-input-text ui-body-c"/></div></div></div></div></div></div>' );

							// Type and sub name

							$( '#metawidget' ).metawidget();
							$( '#metawidget' ).metawidget( "buildWidgets", {
								foo: {
									bar: "Bar"
								}
							}, "object.foo" );

							var element = $( '#metawidget' )[0];

							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<div><div><label for="fooBar" class="ui-input-text">Bar:</label></div><div><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="fooBar" name="fooBar" class="ui-input-text ui-body-c"/></div></div></div>' );
						} );

				it( "defensively copies overridden widgets", function() {

					var element = $( '#metawidget' )[0];
					var bar = document.createElement( 'span' );
					bar.setAttribute( 'id', 'bar' );
					element.appendChild( bar );
					var baz = document.createElement( 'span' );
					baz.setAttribute( 'id', 'baz' );
					element.appendChild( baz );

					$( '#metawidget' ).metawidget();
					var mw = $( '#metawidget' ).data( 'metawidget' );

					$( '#metawidget' ).metawidget( "buildWidgets", {
						foo: "Foo",
						bar: "Bar"
					} );

					expect( element.innerHTML ).toContain( '<div><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="foo" name="foo" class="ui-input-text ui-body-c"/></div></div>' );
					expect( element.innerHTML ).toContain( '<div><span id="bar"/></div>' );
					expect( element.innerHTML ).toContain( '<div><span id="baz"/></div>' );
					expect( element.childNodes[0].childNodes.length ).toBe( 2 );
					expect( element.childNodes.length ).toBe( 3 );

					expect( mw.overriddenNodes.length ).toBe( 0 );
					mw.overriddenNodes.push( document.createElement( 'defensive' ) );
					expect( mw.overriddenNodes.length ).toBe( 1 );
					mw.buildWidgets();
					expect( mw.overriddenNodes.length ).toBe( 0 );
					expect( element.childNodes[0].childNodes.length ).toBe( 2 );
				} );

				it( "can be used purely for layout", function() {

					var element = $( '#metawidget' )[0];
					var bar = document.createElement( 'span' );
					bar.setAttribute( 'id', 'bar' );
					element.appendChild( bar );
					var baz = document.createElement( 'span' );
					baz.setAttribute( 'id', 'baz' );
					element.appendChild( baz );
					var ignore = document.createTextNode( 'ignore' );
					element.appendChild( ignore );

					$( '#metawidget' ).metawidget();
					$( '#metawidget' ).metawidget( "buildWidgets" );

					expect( element.innerHTML ).toContain( '<div><span id="bar"/></div>' );
					expect( element.innerHTML ).toContain( '<div><span id="baz"/></div>' );
					expect( element.innerHTML ).toNotContain( 'ignore' );
					expect( element.childNodes.length ).toBe( 2 );
				} );

				it( "ignores embedded text nodes", function() {

					var element = document.getElementById( 'metawidget' );
					element.appendChild( document.createTextNode( 'text1' ) );
					element.appendChild( document.createElement( 'span' ) );
					element.appendChild( document.createTextNode( 'text2' ) );

					$( '#metawidget' ).metawidget();
					var mw = $( '#metawidget' ).data( 'metawidget' );
					mw.onEndBuild = function() {

						// Do not clean up overriddenNodes
					};
					$( '#metawidget' ).metawidget( 'buildWidgets' );

					expect( mw.overriddenNodes[0].tagName ).toBe( 'SPAN' );
					expect( mw.overriddenNodes.length ).toBe( 1 );
				} );

				it( "guards against infinite loops", function() {

					$( '#metawidget' ).metawidget( {
						addInspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

							mw._refresh( undefined );
						} ]
					} );

					try {
						$( '#metawidget' ).metawidget( "buildWidgets", {} );
						expect( true ).toBe( false );
					} catch ( e ) {
						expect( e.message ).toBe( "Calling _refresh( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
					}
				} );

				it( "supports collections", function() {

					// Defaults

					$( '#metawidget' ).metawidget();
					$( '#metawidget' ).metawidget( "buildWidgets", {
						bar: [ {
							firstname: 'firstname1',
							surname: 'surname1'
						}, {
							firstname: 'firstname2',
							surname: 'surname2'
						}, {
							firstname: 'firstname3',
							surname: 'surname3'
						} ],
					} );

					var element = $( '#metawidget' )[0];

					expect( element.innerHTML ).toContain( '<label for="bar">Bar:</label>' );
					expect( element.innerHTML ).toContain( '<table id="bar"' );
					expect( element.innerHTML ).toContain( '<thead><tr><th>Firstname</th><th>Surname</th></tr></thead>' );
					expect( element.innerHTML ).toContain(
							'<tbody><tr><td>firstname1</td><td>surname1</td></tr><tr><td>firstname2</td><td>surname2</td></tr><tr><td>firstname3</td><td>surname3</td></tr></tbody>' );
				} );

			} );
	
	describe( "The JQueryMobileWidgetProcessor", function() {

		beforeEach( function() {

			var element = document.createElement( 'metawidget' );
			element.setAttribute( 'id', 'metawidget' );
			document.body.appendChild( element );
		} );

		afterEach( function() {

			document.body.removeChild( $( '#metawidget' )[0] );
		} );

		it( "wraps arrays", function() {

			var processor = new metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor();
			$( '#metawidget' ).metawidget();

			var widget = $( '<div id="myArray"><label><input type="checkbox"/>Foo</label><label><input type="checkbox"/>Bar</label></div>' )[0];
			attributes = {
				type: 'array'
			}
			widget = processor.processWidget( widget, "property", attributes, $( '#metawidget' ).data( 'metawidget' ));
			expect( widget.outerHTML ).toBe( '<fieldset data-role="controlgroup"><input type="checkbox" id="myArray2"/><label for="myArray2">Foo</label><input type="checkbox" id="myArray1"/><label for="myArray1">Bar</label></fieldset>' );
			expect( widget.outerHTML ).toContain( '<fieldset data-role="controlgroup">' );
			expect( widget.outerHTML ).toContain( '<input type="checkbox" id="myArray2"/><label for="myArray2">Foo</label>' );
			expect( widget.outerHTML ).toContain( '<input type="checkbox" id="myArray1"/><label for="myArray1">Bar</label>' );
		} );
	} );
} )();