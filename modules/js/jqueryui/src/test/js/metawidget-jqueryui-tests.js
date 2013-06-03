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

( function() {

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
								foo: "Foo"
							} );

							var element = $( '#metawidget' )[0];

							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<table><tbody><tr id="table-foo-row"><th id="table-foo-label-cell"><label for="foo" id="table-foo-label">Foo:</label></th><td id="table-foo-cell"><input type="text" id="foo" name="foo"/></td><td/></tr></tbody></table>' );

							// Configured

							$( '#metawidget' ).metawidget( "option", "layout", new metawidget.layout.SimpleLayout() );

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
											'<table><tbody><tr id="table-foo-row"><th id="table-foo-label-cell"><label for="foo" id="table-foo-label">Foo:</label></th><td id="table-foo-cell"><div id="foo"><table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" name="fooBar"/></td><td/></tr></tbody></table></div></td><td/></tr></tbody></table>' );

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
											'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" name="fooBar"/></td><td/></tr></tbody></table>' );
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

					expect( element.innerHTML ).toContain( '<td id="table-foo-cell"><input type="text" id="foo" name="foo"/></td>' );
					expect( element.innerHTML ).toContain( '<td id="table-bar-cell"><span id="bar"/></td>' );
					expect( element.innerHTML ).toContain( '<td colspan="2"><span id="baz"/></td>' );
					expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
					expect( element.childNodes.length ).toBe( 1 );

					expect( mw.overriddenNodes.length ).toBe( 0 );
					mw.overriddenNodes.push( document.createElement( 'defensive' ) );
					expect( mw.overriddenNodes.length ).toBe( 1 );
					mw.buildWidgets();
					expect( mw.overriddenNodes.length ).toBe( 0 );
					expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
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

					expect( element.innerHTML ).toContain( '<td colspan="2"><span id="bar"/></td>' );
					expect( element.innerHTML ).toContain( '<td colspan="2"><span id="baz"/></td>' );
					expect( element.innerHTML ).toNotContain( 'ignore' );
					expect( element.childNodes.length ).toBe( 1 );
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

					expect( element.innerHTML ).toContain( '<label for="bar" id="table-bar-label">Bar:</label>' );
					expect( element.innerHTML ).toContain( '<table id="bar"' );
					expect( element.innerHTML ).toContain( '<thead><tr><th>Firstname</th><th>Surname</th></tr></thead>' );
					expect( element.innerHTML ).toContain(
							'<tbody><tr><td>firstname1</td><td>surname1</td></tr><tr><td>firstname2</td><td>surname2</td></tr><tr><td>firstname3</td><td>surname3</td></tr></tbody>' );
				} );

			} );

	describe( "The JQueryUIWidgetBuilder", function() {

		it( "builds JQuery UI widgets", function() {

			var widgetBuilder = new metawidget.jqueryui.widgetbuilder.JQueryUIWidgetBuilder();

			var mw = {
				getElement: function() {

					return {
						ownerDocument: document
					}
				}
			}
			expect( widgetBuilder.buildWidget( "property", {}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true"
			}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( "property", {
				hidden: "true"
			}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "false",
				type: "number"
			}, mw ).innerHTML ).toContain( '<input class="ui-spinner-input"' );
			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				minimum: 10,
				maximum: 90
			}, mw ).innerHTML ).toContain( '<a class="ui-slider-handle' );
			expect( widgetBuilder.buildWidget( "property", {
				type: "date"
			}, mw ).outerHTML ).toContain( 'class="hasDatepicker"/>' );
		} );
	} );

	describe( "The JQueryUIBindingProcessor", function() {

		it( "processes widgets and binds them", function() {

			var processor = new metawidget.jqueryui.widgetprocessor.JQueryUIBindingProcessor();
			var attributes = {
				name: "foo",
			};
			var mw = {
				toInspect: {
					slider: "42",
					spinner: "43",
					nested: {
						nestedSlider: "44"
					}
				},
			};

			processor.onStartBuild( mw );

			// Slider

			var widget = document.createElement( 'div' );
			attributes = {
				name: "slider"
			}
			$( widget ).slider();
			processor.processWidget( widget, "property", attributes, mw );
			expect( $( widget ).slider( 'value' ) ).toBe( 42 );

			// Spinner

			var widget = document.createElement( 'input' );
			attributes = {
				name: "spinner"
			}
			$( widget ).spinner();
			processor.processWidget( $( widget ).spinner( 'widget' )[0], "property", attributes, mw );
			expect( $( widget ).spinner( 'value' ) ).toBe( 43 );

			// Nested widgets

			mw.path = 'object.nested';
			attributes = {
				name: "nestedSlider"
			};
			widget = document.createElement( 'div' );
			$( widget ).slider();
			processor.processWidget( widget, "property", attributes, mw );
			expect( $( widget ).slider( 'value' ) ).toBe( 44 );
		} );
	} );

	describe(
			"The TabLayoutDecorator",
			function() {

				it(
						"decorates sections with tabs",
						function() {

							var onEndBuildCalled = false;
							var simpleLayout = new metawidget.layout.SimpleLayout();
							simpleLayout.onEndBuild = function( mw ) {

								onEndBuildCalled = true;
							}
							var layout = new metawidget.jqueryui.layout.TabLayoutDecorator( simpleLayout );

							var container = document.createElement( 'metawidget' );
							var mw = {
								"path": "testPath",
								getElement: function() {

									return container;
								}
							};

							layout.onStartBuild( mw );
							layout.startContainerLayout( container, mw );
							layout.layoutWidget( document.createElement( 'widget1' ), "property", {
								"name": "widget1",
							}, container, mw );
							layout.layoutWidget( document.createElement( 'widget2.1' ), "property", {
								"name": "widget2.1",
								"section": "Section 1"
							}, container, mw );
							layout.layoutWidget( document.createElement( 'widget2.2' ), "property", {
								"name": "widget2.2",
							}, container, mw );
							layout.layoutWidget( document.createElement( 'widget2.3.1' ), "property", {
								"name": "widget2.3.1",
								"section": [ "Section 1", "Section 1.1" ]
							}, container, mw );
							layout.layoutWidget( document.createElement( 'widget3' ), "property", {
								"name": "widget3",
								"section": "Section 2"
							}, container, mw );
							layout.layoutWidget( document.createElement( 'widget4' ), "property", {
								"name": "widget4",
								"section": ""
							}, container, mw );
							layout.layoutWidget( document.createElement( 'widget5' ), "property", {
								"name": "widget5"
							}, container, mw );
							layout.endContainerLayout( container, mw );
							layout.onEndBuild( mw );

							expect( container.innerHTML )
									.toBe(
											'<widget1/><div id="testPathWidget2.1-tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all"><ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist"><li class="ui-state-default ui-corner-top ui-tabs-active ui-state-active ui-tabs-loading" role="tab" tabindex="0" aria-controls="ui-tabs-1" aria-labelledby="ui-id-1" aria-selected="true"><a href="#testPathWidget2.1-tabs1" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-1">Section 1</a></li><li class="ui-state-default ui-corner-top" role="tab" tabindex="-1" aria-controls="ui-tabs-2" aria-labelledby="ui-id-2" aria-selected="false"><a href="#testPathWidget2.1-tabs2" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-2">Section 2</a></li></ul><div id="ui-tabs-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-live="polite" aria-labelledby="ui-id-1" role="tabpanel" aria-expanded="true" aria-hidden="false" aria-busy="true"/><div id="ui-tabs-2" class="ui-tabs-panel ui-widget-content ui-corner-bottom" aria-live="polite" aria-labelledby="ui-id-2" role="tabpanel" aria-expanded="false" aria-hidden="true"/><div id="testPathWidget2.1-tabs1"><widget2.1/><widget2.2/><widget2.3.1/></div><div id="testPathWidget2.1-tabs2"><widget3/></div></div><widget4/><widget5/>' );
							expect( onEndBuildCalled ).toBe( true );
						} );
			} );
} )();