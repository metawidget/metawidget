// Metawidget ${project.version}
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
						"populates itself with widgets to match the properties of domain objects",
						function() {

							var firedBuildEndEvent = 0;

							// Defaults

							$( '#metawidget' ).metawidget( {
								styleClass: 'metawidget-class'
							} );
							$( '#metawidget' ).on( 'buildEnd', function() {

								firedBuildEndEvent++;
							} );
							$( '#metawidget' ).metawidget( 'buildWidgets', {
								foo: "Foo",
								bar: {
									baz: "Baz",
									abc: "Abc"
								}
							} );

							expect( firedBuildEndEvent ).toBe( 1 );
							expect( $( "metawidget" ).data( "metawidget" ) ).toBeDefined();
							expect( $( "metawidget" ).data( "metawidget" ).toInspect ).toBeDefined();
							var element = $( '#metawidget' )[0];

							expect( element.getMetawidget() ).toBeDefined();
							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<div class="ui-field-contain"><label for="foo" id="foo-label" class="ui-input-text">Foo:</label><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="foo" name="foo" class="ui-input-text ui-body-c"/></div></div>' );
							expect( element.childNodes[1].outerHTML )
									.toBe(
											'<div class="ui-field-contain"><label for="bar" id="bar-label">Bar:</label><div class="metawidget-class" id="bar"><div class="ui-field-contain"><label for="barBaz" id="barBaz-label" class="ui-input-text">Baz:</label><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="barBaz" name="barBaz" class="ui-input-text ui-body-c"/></div></div><div class="ui-field-contain"><label for="barAbc" id="barAbc-label" class="ui-input-text">Abc:</label><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="barAbc" name="barAbc" class="ui-input-text ui-body-c"/></div></div></div></div>' );

							// Configured

							$( '#metawidget' ).metawidget( "option", "layout", new metawidget.layout.SimpleLayout() );
							$( '#metawidget' ).metawidget( 'buildWidgets' );

							expect( firedBuildEndEvent ).toBe( 2 );
							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<span><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="foo" name="foo" class="ui-input-text ui-body-c"/></div></span>' );
							expect( element.childNodes[0].childNodes[0].childNodes[0].value ).toBe( 'Foo' );
							expect( element.childNodes[1].outerHTML )
									.toBe(
											'<span><div class="metawidget-class" id="bar"><span><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="barBaz" name="barBaz" class="ui-input-text ui-body-c"/></div></span><span><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="barAbc" name="barAbc" class="ui-input-text ui-body-c"/></div></span></div></span>' );

							// Read-only

							$( '#metawidget' ).metawidget( "setReadOnly", true );
							expect( element.childNodes[0].outerHTML )
									.toBe(
											'<span><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="foo" name="foo" class="ui-input-text ui-body-c"/></div></span>' );
							expect( element.childNodes[1].outerHTML )
									.toBe(
											'<span><div class="metawidget-class" id="bar"><span><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="barBaz" name="barBaz" class="ui-input-text ui-body-c"/></div></span><span><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="barAbc" name="barAbc" class="ui-input-text ui-body-c"/></div></span></div></span>' );

							$( '#metawidget' ).metawidget( "buildWidgets" );
							expect( firedBuildEndEvent ).toBe( 3 );
							expect( element.childNodes[0].outerHTML ).toBe( '<span><output id="foo">Foo</output></span>' );
							expect( element.childNodes[1].outerHTML ).toBe(
									'<span><div class="metawidget-class" id="bar"><span><output id="barBaz">Baz</output></span><span><output id="barAbc">Abc</output></span></div></span>' );
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
											'<div class="ui-field-contain"><label for="foo" id="foo-label">Foo:</label><div id="foo"><div class="ui-field-contain"><label for="fooBar" id="fooBar-label" class="ui-input-text">Bar:</label><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="fooBar" name="fooBar" class="ui-input-text ui-body-c"/></div></div></div></div>' );

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
											'<div class="ui-field-contain"><label for="fooBar" id="fooBar-label" class="ui-input-text">Bar:</label><div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="fooBar" name="fooBar" class="ui-input-text ui-body-c"/></div></div>' );
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

					expect( element.innerHTML ).toContain(
							'<div class="ui-input-text ui-shadow-inset ui-corner-all ui-btn-shadow ui-body-c"><input type="text" id="foo" name="foo" class="ui-input-text ui-body-c"/></div>' );
					expect( element.innerHTML ).toContain( '<div class="ui-field-contain"><label for="bar" id="bar-label">Bar:</label><span id="bar"/></div>' );
					expect( element.innerHTML ).toContain( '<div class="ui-field-contain"><span id="baz"/></div>' );
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

					expect( element.innerHTML ).toContain( '<div class="ui-field-contain"><span id="bar"/></div>' );
					expect( element.innerHTML ).toContain( '<div class="ui-field-contain"><span id="baz"/></div>' );
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
						appendInspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

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

					expect( element.innerHTML ).toContain( '<label for="bar" id="bar-label">Bar:</label>' );
					expect( element.innerHTML ).toContain( '<table id="bar"' );
					expect( element.innerHTML ).toContain( '<thead><tr><th>Firstname</th><th>Surname</th></tr></thead>' );
					expect( element.innerHTML ).toContain(
							'<tbody><tr><td>firstname1</td><td>surname1</td></tr><tr><td>firstname2</td><td>surname2</td></tr><tr><td>firstname3</td><td>surname3</td></tr></tbody>' );
				} );

				it( "has a convenience method for saving", function() {

					var toInspect = {
						foo: 'Foo',
						bar: 'Bar',
						baz: 'Baz'
					};

					$( '#metawidget' ).metawidget();
					$( '#metawidget' ).metawidget( 'buildWidgets', toInspect );

					expect( $( "metawidget" ).data( "metawidget" ) ).toBeDefined();
					expect( $( "metawidget" ).data( "metawidget" ).toInspect ).toBeDefined();

					$( "#foo" ).val( 'Foo 1' );
					$( "#bar" ).val( 'Bar 1' );
					$( "#baz" ).val( 'Baz 1' );

					$( '#metawidget' ).metawidget( 'save' );

					expect( toInspect.foo ).toBe( 'Foo 1' );
					expect( toInspect.bar ).toBe( 'Bar 1' );
					expect( toInspect.baz ).toBe( 'Baz 1' );
				} );
			} );

	describe(
			"The JQueryMobileWidgetProcessor",
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
						"wraps arrays",
						function() {

							var processor = new metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor();
							$( '#metawidget' ).metawidget();

							var widget = $( '<div id="myArray"><label><input type="checkbox"/>Foo</label><label><input type="checkbox"/>Bar</label></div>' )[0];
							attributes = {
								type: 'array'
							}
							widget = processor.processWidget( widget, "property", attributes, $( '#metawidget' ).data( 'metawidget' ) );
							expect( widget.outerHTML )
									.toBe(
											'<fieldset data-role="controlgroup"><input type="checkbox" id="myArray2"/><label for="myArray2">Foo</label><input type="checkbox" id="myArray1"/><label for="myArray1">Bar</label></fieldset>' );
							expect( widget.outerHTML ).toContain( '<fieldset data-role="controlgroup">' );
							expect( widget.outerHTML ).toContain( '<input type="checkbox" id="myArray2"/><label for="myArray2">Foo</label>' );
							expect( widget.outerHTML ).toContain( '<input type="checkbox" id="myArray1"/><label for="myArray1">Bar</label>' );
						} );

				it( "does not wrap non-arrays", function() {

					var processor = new metawidget.jquerymobile.widgetprocessor.JQueryMobileWidgetProcessor();
					$( '#metawidget' ).metawidget();

					var widget = $( '<div id="myArray">foo</div>' )[0];
					attributes = {
						type: 'array'
					}
					widget = processor.processWidget( widget, "property", attributes, $( '#metawidget' ).data( 'metawidget' ) );
					expect( widget.outerHTML ).toBe( '<div id="myArray">foo</div>' );
				} );
			} );

	describe( "The JQueryMobileSimpleBindingProcessor", function() {

		beforeEach( function() {

			var element = document.createElement( 'metawidget' );
			element.setAttribute( 'id', 'metawidget' );
			document.body.appendChild( element );
		} );

		afterEach( function() {

			document.body.removeChild( $( '#metawidget' )[0] );
		} );

		xit( "binds to and from arrays", function() {

			var processor = new metawidget.jquerymobile.widgetprocessor.JQueryMobileSimpleBindingProcessor();
			$( '#metawidget' ).metawidget();
			var mwData = $( '#metawidget' ).data( 'metawidget' );
			processor.onStartBuild( mwData )

			var widgetToBind = $( '<fieldset><input type="checkbox" value="Apples"/><input type="checkbox" value="Bananas"/></fieldset>' )[0];
			attributes = {
				name: 'fruit',
				type: 'array'
			};

			mwData.toInspect = {
				fruit: [ 'Apples' ]
			};

			// Bind to

			processor.processWidget( widgetToBind, "property", attributes, mwData );
			expect( widgetToBind.childNodes[0].checked ).toBe( true );
			expect( widgetToBind.childNodes[1].checked ).toBe( false );

			// Save from

			var widgetToSave = $( '<fieldset><div><div><input type="checkbox" value="Apples" checked/></div><div><input type="checkbox" value="Bananas" checked/></div></div></fieldset>' )[0];
			var saved = processor.saveFromWidget( {
				widget: widgetToSave,
				attributes: attributes
			} );

			expect( saved[0] ).toBe( 'Apples' );
			expect( saved[1] ).toBe( 'Bananas' );
			expect( saved.length ).toBe( 2 );
		} );
		it( "binds to and from search widgets", function() {

			var processor = new metawidget.jquerymobile.widgetprocessor.JQueryMobileSimpleBindingProcessor();
			$( '#metawidget' ).metawidget();
			var mwData = $( '#metawidget' ).data( 'metawidget' );
			processor.onStartBuild( mwData )

			var widgetToBind = $( '<input id="foo" type="search"/>' )[0];
			attributes = {
				name: 'foo'
			};
			mwData.toInspect = {
				foo: 'TheFoo'
			};

			// Bind to

			processor.processWidget( widgetToBind, "property", attributes, mwData );
			expect( widgetToBind.value ).toBe( 'TheFoo' );

			// Save from (confusingly, saves from a different search widget
			// entirely)

			$( '#metawidget' ).append( $( '<input id="foo" type="text" value="TheBar"/>' ) );
			var saved = processor.saveFromWidget( {
				widget: widgetToBind,
				attributes: attributes
			}, mwData );
			expect( saved ).toBe( 'TheBar' );
		} );
	} );
} )();