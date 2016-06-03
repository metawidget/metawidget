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

	describe( "The CompositeWidgetBuilder", function() {

		it( "starts WidgetBuilders", function() {

			var started = 0;

			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ {

				onStartBuild: function( mw ) {

					if ( mw !== undefined ) {
						started++;
					}
				}
			}, {

				onStartBuild: function( mw ) {

					if ( mw !== undefined ) {
						started++;
					}
				}
			} ] );

			widgetBuilder.onStartBuild( {} );

			expect( started ).toBe( 2 );
		} );

		it( "composes WidgetBuilders", function() {

			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ function( elementName, attributes, mw ) {

				if ( attributes.widgetBuilder == '1' ) {
					return {
						name: "widgetBuilder1"
					};
				}

			}, function( elementName, attributes, mw ) {

				if ( attributes.widgetBuilder == '2' ) {
					return {
						name: "widgetBuilder2"
					};
				}
			}, function( elementName, attributes, mw ) {

				return {
					name: "widgetBuilder3"
				};
			} ] );

			expect( widgetBuilder.buildWidget( "property", {} ).name ).toBe( 'widgetBuilder3' );
			expect( widgetBuilder.buildWidget( "property", {
				widgetBuilder: "1"
			} ).name ).toBe( 'widgetBuilder1' );
			expect( widgetBuilder.buildWidget( "property", {
				widgetBuilder: "2"
			} ).name ).toBe( 'widgetBuilder2' );
		} );

		it( "ends WidgetBuilders", function() {

			var ended = 0;

			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ {

				onEndBuild: function( mw ) {

					if ( mw !== undefined ) {
						ended++;
					}
				}
			}, {

				onEndBuild: function( mw ) {

					if ( mw !== undefined ) {
						ended++;
					}
				}
			} ] );

			widgetBuilder.onEndBuild( {} );

			expect( ended ).toBe( 2 );
		} );

		it( "defensively copies widgetBuilders", function() {

			// Direct

			var widgetBuilders = [ function() {

				return simpleDocument.createElement( 'span' );
			} ];

			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( widgetBuilders );
			var widget = widgetBuilder.buildWidget();
			expect( widget.toString() ).toBe( 'span' );

			expect( widgetBuilders.length ).toBe( 1 );
			widgetBuilders.splice( 0, 1 );
			expect( widgetBuilders.length ).toBe( 0 );
			widget = widgetBuilder.buildWidget();
			expect( widget.toString() ).toBe( 'span' );

			// Via config

			var config = {
				widgetBuilders: [ function() {

					return simpleDocument.createElement( 'span' );
				} ]
			};

			widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( config );
			widget = widgetBuilder.buildWidget();
			expect( widget.toString() ).toBe( 'span' );

			expect( config.widgetBuilders.length ).toBe( 1 );
			config.widgetBuilders.splice( 0, 1 );
			expect( config.widgetBuilders.length ).toBe( 0 );
			widget = widgetBuilder.buildWidget();
			expect( widget.toString() ).toBe( 'span' );
		} );
	} );

	describe( "The OverriddenWidgetBuilder", function() {

		it( "looks for child widgets with the same id", function() {

			var widgetBuilder = new metawidget.widgetbuilder.OverriddenWidgetBuilder();

			var attributes = {
				name: "baz"
			};
			var mw = {
				path: "foo.bar"
			};
			expect( widgetBuilder.buildWidget( "property", attributes, mw ) ).toBeUndefined();

			var widget1 = simpleDocument.createElement( 'widget1' );
			widget1.setAttribute( 'id', 'abcDefGhi' );
			var widget2 = simpleDocument.createElement( 'widget2' );
			widget2.setAttribute( 'id', 'baz' );
			var widget3 = simpleDocument.createElement( 'widget3' );
			widget3.setAttribute( 'id', 'fooBarBaz' );
			mw.overriddenNodes = [ widget1, widget2, widget3 ];
			expect( widgetBuilder.buildWidget( "property", attributes, mw ) ).toBe( widget3 );
		} );
	} );

	describe( "The ReadOnlyWidgetBuilder", function() {

		it( "returns read-only widgets", function() {

			var widgetBuilder = new metawidget.widgetbuilder.ReadOnlyWidgetBuilder();

			expect( widgetBuilder.buildWidget( "property", {}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true"
			}, {} ) ).toBeUndefined();

			var mw = {
				getElement: function() {

					return {
						ownerDocument: simpleDocument
					};
				}
			};
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				masked: "true",
				type: "string"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "function"
			}, mw ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				enum: [ "foo", "bar", "baz" ]
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "string"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "boolean"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "number"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "integer"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "date"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "color"
			}, mw ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				hidden: "true",
				readOnly: "true",
				type: "string"
			}, mw ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				dontExpand: "true"
			}, mw ).toString() ).toBe( 'output' );
		} );
	} );

	describe( "The HtmlWidgetBuilder", function() {

		it( "returns HTML widgets", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			expect( widgetBuilder.buildWidget( "property", {}, {} ) ).toBeUndefined();

			var mw = {
				getElement: function() {

					return {
						ownerDocument: simpleDocument
					};
				}
			};

			expect( widgetBuilder.buildWidget( "property", {
				hidden: "true"
			}, mw ).toString() ).toBe( 'stub' );

			var select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				required: "true"
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option' );
			expect( select.childNodes[0].value ).toBe( 'foo' );
			expect( select.childNodes[0].innerHTML ).toBe( 'foo' );
			expect( select.childNodes[1].toString() ).toBe( 'option' );
			expect( select.childNodes[1].value ).toBe( 'bar' );
			expect( select.childNodes[1].innerHTML ).toBe( 'bar' );
			expect( select.childNodes[2].toString() ).toBe( 'option' );
			expect( select.childNodes[2].value ).toBe( 'baz' );
			expect( select.childNodes[2].innerHTML ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 3 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				enumTitles: [ "Foo", "Bar", "Baz" ],
				required: "true"
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option' );
			expect( select.childNodes[0].value ).toBe( 'foo' );
			expect( select.childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( select.childNodes[1].toString() ).toBe( 'option' );
			expect( select.childNodes[1].value ).toBe( 'bar' );
			expect( select.childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( select.childNodes[2].toString() ).toBe( 'option' );
			expect( select.childNodes[2].value ).toBe( 'baz' );
			expect( select.childNodes[2].innerHTML ).toBe( 'Baz' );
			expect( select.childNodes.length ).toBe( 3 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ]
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option' );
			expect( select.childNodes[0].innerHTML ).toBeUndefined();
			expect( select.childNodes[1].toString() ).toBe( 'option' );
			expect( select.childNodes[1].value ).toBe( 'foo' );
			expect( select.childNodes[1].innerHTML ).toBe( 'foo' );
			expect( select.childNodes[2].toString() ).toBe( 'option' );
			expect( select.childNodes[2].value ).toBe( 'bar' );
			expect( select.childNodes[2].innerHTML ).toBe( 'bar' );
			expect( select.childNodes[3].toString() ).toBe( 'option' );
			expect( select.childNodes[3].value ).toBe( 'baz' );
			expect( select.childNodes[3].innerHTML ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 4 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				enumTitles: []
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option' );
			expect( select.childNodes[0].innerHTML ).toBeUndefined();
			expect( select.childNodes[1].toString() ).toBe( 'option' );
			expect( select.childNodes[1].value ).toBe( 'foo' );
			expect( select.childNodes[1].innerHTML ).toBe( 'foo' );
			expect( select.childNodes[2].toString() ).toBe( 'option' );
			expect( select.childNodes[2].value ).toBe( 'bar' );
			expect( select.childNodes[2].innerHTML ).toBe( 'bar' );
			expect( select.childNodes[3].toString() ).toBe( 'option' );
			expect( select.childNodes[3].value ).toBe( 'baz' );
			expect( select.childNodes[3].innerHTML ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 4 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				enumTitles: [],
				type: "array"
			}, mw );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label class="checkbox"' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox"' );
			expect( select.childNodes[0].childNodes[0].value ).toBe( 'foo' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'foo' );
			expect( select.childNodes[1].toString() ).toBe( 'label class="checkbox"' );
			expect( select.childNodes[1].childNodes[0].toString() ).toBe( 'input type="checkbox"' );
			expect( select.childNodes[1].childNodes[0].value ).toBe( 'bar' );
			expect( select.childNodes[1].childNodes[1].toString() ).toBe( 'bar' );
			expect( select.childNodes[2].toString() ).toBe( 'label class="checkbox"' );
			expect( select.childNodes[2].childNodes[0].toString() ).toBe( 'input type="checkbox"' );
			expect( select.childNodes[2].childNodes[0].value ).toBe( 'baz' );
			expect( select.childNodes[2].childNodes[1].toString() ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 3 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo" ],
				enumTitles: [ "Foo" ],
				type: "array"
			}, mw );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label class="checkbox"' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox"' );
			expect( select.childNodes[0].childNodes[0].value ).toBe( 'foo' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo" ],
				enumTitles: [ "Foo" ],
				componentType: "radio"
			}, mw );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label class="radio"' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="radio"' );
			expect( select.childNodes[0].childNodes[0].value ).toBe( 'foo' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			var button = widgetBuilder.buildWidget( "property", {
				name: "clickMe",
				type: "function"
			}, mw );

			expect( button.toString() ).toBe( 'input type="button" value="Click Me"' );
			expect( button.getAttribute( 'class' ) ).toBe( null );

			expect( widgetBuilder.buildWidget( "property", {
				name: "clickMe",
				type: "function",
				submit: true
			}, mw ).toString() ).toBe( 'input type="submit" value="Click Me"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number"
			}, mw ).toString() ).toBe( 'input type="number" step="any"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				minimum: "2"
			}, mw ).toString() ).toBe( 'input type="number" step="any" min="2"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				maximum: "4"
			}, mw ).toString() ).toBe( 'input type="number" step="any" max="4"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				minimum: "2",
				maximum: "4"
			}, mw ).toString() ).toBe( 'input type="range" min="2" max="4"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "integer"
			}, mw ).toString() ).toBe( 'input type="number"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "boolean"
			}, mw ).toString() ).toBe( 'input type="checkbox"' );

			var radio = widgetBuilder.buildWidget( "property", {
				type: "boolean",
				componentType: "radio"
			}, mw );

			expect( radio.toString() ).toBe( 'div' );
			expect( radio.childNodes[0].toString() ).toBe( 'label class="radio"' );
			expect( radio.childNodes[0].childNodes[0].toString() ).toBe( 'input type="radio"' );
			expect( radio.childNodes[0].childNodes[0].value ).toBe( true );
			expect( radio.childNodes[0].childNodes[1].toString() ).toBe( 'Yes' );
			expect( radio.childNodes[1].toString() ).toBe( 'label class="radio"' );
			expect( radio.childNodes[1].childNodes[0].toString() ).toBe( 'input type="radio"' );
			expect( radio.childNodes[1].childNodes[0].value ).toBe( false );
			expect( radio.childNodes[1].childNodes[1].toString() ).toBe( 'No' );
			expect( radio.childNodes.length ).toBe( 2 );

			expect( widgetBuilder.buildWidget( "property", {
				type: "date"
			}, mw ).toString() ).toBe( 'input type="date"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "color"
			}, mw ).toString() ).toBe( 'input type="color"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				large: "true"
			}, mw ).toString() ).toBe( 'textarea' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				masked: "true",
			}, mw ).toString() ).toBe( 'input type="password"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				masked: "true",
				maxLength: "30"
			}, mw ).toString() ).toBe( 'input type="password" maxlength="30"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				enum: undefined,
				masked: "",
				large: ""
			}, mw ).toString() ).toBe( 'input type="text"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string"
			}, mw ).toString() ).toBe( 'input type="text"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				maxLength: "32"
			}, mw ).toString() ).toBe( 'input type="text" maxlength="32"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				componentType: "search",
				maxLength: "33"
			}, mw ).toString() ).toBe( 'input type="search" maxlength="33"' );

			expect( widgetBuilder.buildWidget( "property", {
				dontExpand: "true"
			}, mw ).toString() ).toBe( 'input type="text"' );
		} );

		it( "supports simple arrays", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = new metawidget.Metawidget( element );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Undefined array (entity level)

			mw.toInspect = undefined;
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes.length ).toBe( 0 );

			// Undefined array (property level)

			mw.toInspect = undefined;
			table = widgetBuilder.buildWidget( "property", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes.length ).toBe( 0 );

			// inspectionResult undefined

			mw.inspect = function() {

				return undefined;
			};
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes.length ).toBe( 0 );

			// Empty array

			mw = new metawidget.Metawidget( element );
			mw.toInspect = [];
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes.length ).toBe( 0 );

			// Array without headers

			mw.toInspect = [ "Foo", "Bar" ];

			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[0].childNodes[1].childNodes[0].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[0].childNodes[1].childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes.length ).toBe( 1 );

			// Nested array

			mw.toInspect = {
				nested: [ "Foo", "Bar" ]
			};

			table = widgetBuilder.buildWidget( "property", {
				type: "array",
				name: "nested"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[0].childNodes[1].childNodes[0].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[0].childNodes[1].childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes.length ).toBe( 1 );

			// has createTable method for subclasses to override

			widgetBuilder.createTable = function() {

				return metawidget.util.createElement( mw, 'not-a-table' );
			};

			table = widgetBuilder.buildWidget( "property", {
				type: "array",
				name: "nested"
			}, mw );

			expect( table.toString() ).toBe( 'not-a-table' );
		} );

		it( "supports object arrays", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							name: {
								type: 'string'
							},
							description: {
								type: 'string'
							}
						}
					};
				}
			} );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Undefined array

			mw.toInspect = undefined;
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Description' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes.length ).toBe( 0 );
			expect( table.childNodes.length ).toBe( 2 );

			// Empty array

			mw.toInspect = [];
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Description' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes.length ).toBe( 0 );
			expect( table.childNodes.length ).toBe( 2 );

			// Inspect headers

			mw = new metawidget.Metawidget( element );
			mw.toInspect = [ {
				name: "Foo",
				description: "A Foo"
			}, {
				name: "Bar",
				description: "A Bar"
			} ];

			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Description' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBe( 'A Foo' );
			expect( table.childNodes[1].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].childNodes[1].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].innerHTML ).toBe( 'A Bar' );
			expect( table.childNodes[1].childNodes[1].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].childNodes.length ).toBe( 2 );
			expect( table.childNodes.length ).toBe( 2 );

			// Nested array

			mw.toInspect = {
				nested: [ {
					name: "Foo",
					description: "A Foo"
				}, {
					name: "Bar",
					description: "A Bar"
				} ]
			};

			table = widgetBuilder.buildWidget( "property", {
				type: "array",
				name: "nested"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Description' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBe( 'A Foo' );
			expect( table.childNodes[1].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].childNodes[1].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].innerHTML ).toBe( 'A Bar' );
			expect( table.childNodes[1].childNodes[1].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].childNodes.length ).toBe( 2 );
			expect( table.childNodes.length ).toBe( 2 );
		} );

		it( "supports JSON schema rendering", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = new metawidget.Metawidget( element, {
				inspector: new metawidget.inspector.JsonSchemaInspector( {
					items: {
						properties: {
							id: {
								type: 'string',
								hidden: true
							},
							foo: {
								type: 'string'
							},
							bar: {
								type: 'string',
								columnWidth: '10%',
								columnAlign: 'right'
							},
							baz: {
								type: 'function'
							}
						}
					}
				} )
			} );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Empty array

			mw.toInspect = [];
			table = widgetBuilder.buildWidget( 'entity', {
				type: 'array'
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th style="width:10%;text-align:right;"' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[2].innerHTML ).toBeUndefined();
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes.length ).toBe( 0 );
			expect( table.childNodes.length ).toBe( 2 );

			// Partially populated array items

			mw.toInspect = [ {
				id: 0,
				foo: 'FooValue'
			} ];
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th style="width:10%;text-align:right;"' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[2].innerHTML ).toBeUndefined();
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'FooValue' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td style="width:10%;text-align:right;"' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBeUndefined();
			expect( table.childNodes[1].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'div' );
			expect( table.childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'input type="button" value="Baz" id="object0Baz"' );
			expect( table.childNodes[1].childNodes[0].childNodes.length ).toBe( 3 );
			expect( table.childNodes[1].childNodes.length ).toBe( 1 );
			expect( table.childNodes.length ).toBe( 2 );

			// Partially populated array items without JSON
			// Schema

			mw = new metawidget.Metawidget( element );
			mw.toInspect = [ {
				id: 0,
				foo: 'FooValue'
			} ];
			table = widgetBuilder.buildWidget( 'entity', {
				type: 'array'
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Id' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( '0' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBe( 'FooValue' );
			expect( table.childNodes[1].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].childNodes.length ).toBe( 1 );
			expect( table.childNodes.length ).toBe( 2 );
		} );

		it( "has addHeaderRow method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			var thead = simpleDocument.createElement( 'thead' );

			var mw = {
				getElement: function() {

					return {
						ownerDocument: simpleDocument
					};
				}
			};
			var columnAttributes = widgetBuilder.addHeaderRow( thead, [ {
				name: 'foo'
			}, {
				name: 'bar',
				hidden: true
			}, {
				name: 'baz'
			} ], mw );
			expect( thead.childNodes[0].toString() ).toBe( 'tr' );
			expect( thead.childNodes.length ).toBe( 1 );
			expect( thead.childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( thead.childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( thead.childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( thead.childNodes[0].childNodes[1].innerHTML ).toBe( 'Baz' );
			expect( thead.childNodes[0].childNodes.length ).toBe( 2 );

			expect( columnAttributes[0].name ).toBe( 'foo' );
			expect( columnAttributes[1].name ).toBe( 'baz' );
			expect( columnAttributes.length ).toBe( 2 );
		} );

		it( "has addHeader method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			var tr = simpleDocument.createElement( 'tr' );

			var mw = {
				getElement: function() {

					return {
						ownerDocument: simpleDocument
					};
				}
			};
			widgetBuilder.addHeader( tr, {
				name: 'Foo'
			}, mw );
			expect( tr.childNodes[0].toString() ).toBe( 'th' );
			expect( tr.childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( tr.childNodes.length ).toBe( 1 );

			expect( widgetBuilder.addHeader( tr, {
				name: 'Foo',
				hidden: true
			}, mw ) ).toBe( false );

			expect( widgetBuilder.addHeader( tr, {
				name: 'Foo',
				hidden: 'true'
			}, mw ) ).toBe( false );
		} );

		it( "has addFooterRow method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();
			widgetBuilder.addFooterRow = function( tfoot, columnAttributes ) {

				var tr = simpleDocument.createElement( 'tr' );
				tfoot.appendChild( tr );

				for ( var loop = 0; loop < columnAttributes.length; loop++ ) {

					var td = simpleDocument.createElement( 'td' );
					tr.appendChild( td );
					td.innerHTML = columnAttributes[loop].name + ' Footer';
				}
			};

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				widgetBuilder: widgetBuilder,
				layout: new metawidget.layout.SimpleLayout()
			} );

			mw.toInspect = {
				tableData: [ {
					foo: 'Foo',
					bar: 'Bar'
				} ]
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table id="tableData"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'thead' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'tfoot' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'foo Footer' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBe( 'bar Footer' );
		} );

		it( "has addRow method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			var tbody = simpleDocument.createElement( 'tbody' );
			var table = simpleDocument.createElement( 'table' );
			table.appendChild( tbody );

			var mw = {
				getElement: function() {

					return {
						ownerDocument: simpleDocument
					};
				},
				buildNestedMetawidget: function( attributes ) {

					var nestedMetawidget = simpleDocument.createElement( 'metawidget' );
					nestedMetawidget.innerHTML = attributes.name;
					return nestedMetawidget;
				}
			};

			// Entity level

			var tr = widgetBuilder.addRow( tbody, [ {
				foo: 'Foo',
				bar: 'Bar'
			} ], 0, [ {
				name: 'foo'
			}, {
				name: 'bar'
			} ], 'entity', {}, mw );
			expect( tbody.childNodes[0] ).toBe( tr );
			expect( tbody.childNodes.length ).toBe( 1 );
			expect( tr.childNodes[0].toString() ).toBe( 'td' );
			expect( tr.childNodes[0].childNodes[0].toString() ).toBe( 'metawidget' );
			expect( tr.childNodes[0].childNodes[0].innerHTML ).toBe( '[0].foo' );
			expect( tr.childNodes[1].toString() ).toBe( 'td' );
			expect( tr.childNodes[1].childNodes[0].toString() ).toBe( 'metawidget' );
			expect( tr.childNodes[1].childNodes[0].innerHTML ).toBe( '[0].bar' );
			expect( tr.childNodes.length ).toBe( 2 );

			// Property level

			tbody = simpleDocument.createElement( 'tbody' );
			table.appendChild( tbody );
			tr = widgetBuilder.addRow( tbody, [ {
				foo: 'Foo',
				bar: 'Bar'
			} ], 0, [ {
				name: 'foo'
			}, {
				name: 'bar'
			} ], 'property', {
				name: 'root'
			}, mw );
			expect( tbody.childNodes[0] ).toBe( tr );
			expect( tbody.childNodes.length ).toBe( 1 );
			expect( tr.childNodes[0].toString() ).toBe( 'td' );
			expect( tr.childNodes[0].childNodes[0].toString() ).toBe( 'metawidget' );
			expect( tr.childNodes[0].childNodes[0].innerHTML ).toBe( '.root[0].foo' );
			expect( tr.childNodes[1].toString() ).toBe( 'td' );
			expect( tr.childNodes[1].childNodes[0].toString() ).toBe( 'metawidget' );
			expect( tr.childNodes[1].childNodes[0].innerHTML ).toBe( '.root[0].bar' );
			expect( tr.childNodes.length ).toBe( 2 );

		} );

		it( "has addColumn method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			var tr = simpleDocument.createElement( 'tr' );
			var tbody = simpleDocument.createElement( 'tbody' );
			tbody.appendChild( tr );
			var table = simpleDocument.createElement( 'tbody' );
			table.appendChild( tbody );

			// Root level

			var mw = {
				getElement: function() {

					return {
						ownerDocument: simpleDocument
					};
				},
				buildNestedMetawidget: function( attributes ) {

					var nestedMetawidget = simpleDocument.createElement( 'metawidget' );
					nestedMetawidget.innerHTML = attributes.name;
					return nestedMetawidget;
				}
			};

			var td = widgetBuilder.addColumn( tr, [ 'Foo' ], 0, {}, 'entity', {}, mw );
			expect( td.childNodes[0].toString() ).toBe( 'metawidget' );
			expect( td.childNodes[0].innerHTML ).toBe( '[0]' );
			expect( tr.childNodes[0] ).toBe( td );

			// Child level

			td = widgetBuilder.addColumn( tr, [ {
				bar: 'Bar'
			} ], 0, {
				name: 'bar'
			}, 'property', {
				name: 'root'
			}, mw );

			expect( td.childNodes[0].toString() ).toBe( 'metawidget' );
			expect( td.childNodes[0].innerHTML ).toBe( '.root[0].bar' );
			expect( tr.childNodes[1] ).toBe( td );

			// Support enumTitles

			td = widgetBuilder.addColumn( tr, [ {
				bar: 'Bar'
			} ], 0, {
				name: 'bar',
				type: 'string',
				'enum': [ 'Bar', 'Foo', 'Baz' ],
				enumTitles: [ 'The Bar', 'The Foo', 'The Baz' ]
			}, 'property', {
				name: 'root'
			}, mw );

			expect( td.innerHTML ).toBe( 'The Bar' );
			expect( tr.childNodes[2] ).toBe( td );
		} );

		it( "supports nested tables",
				function() {

					var element = simpleDocument.createElement( 'metawidget' );
					var mw = new metawidget.Metawidget( element );
					var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

					// Root level

					mw.toInspect = [ {
						name: "Foo",
						description: [ {
							nestedName: "Nested Foo",
							nestedDescription: "A Nested Foo"
						} ]
					} ];

					table = widgetBuilder.buildWidget( "entity", {
						type: "array"
					}, mw );

					expect( table.toString() ).toBe( 'table' );
					expect( table.childNodes[0].toString() ).toBe( 'thead' );
					expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
					expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
					expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name' );
					expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
					expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Description' );
					expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
					expect( table.childNodes[1].toString() ).toBe( 'tbody' );
					expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
					expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
					expect( table.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
					expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
					expect( table.childNodes[1].childNodes[0].childNodes.length ).toBe( 2 );

					// Description is a nested table

					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div' );

					var nestedTable = table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0];
					expect( nestedTable.toString() ).toBe( 'table id="object0Description"' );
					expect( nestedTable.childNodes[0].toString() ).toBe( 'thead' );
					expect( nestedTable.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
					expect( nestedTable.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
					expect( nestedTable.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Name' );
					expect( nestedTable.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
					expect( nestedTable.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Nested Description' );
					expect( nestedTable.childNodes[1].toString() ).toBe( 'tbody' );
					expect( nestedTable.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
					expect( nestedTable.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
					expect( nestedTable.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Foo' );
					expect( nestedTable.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
					expect( nestedTable.childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBe( 'A Nested Foo' );
					expect( nestedTable.childNodes[1].childNodes.length ).toBe( 1 );

					// Description is a nested object

					mw.toInspect = [ {
						name: "Bar",
						description: {
							nestedName: "Nested Bar",
							nestedDescription: "A Nested Bar"
						}
					} ];

					table = widgetBuilder.buildWidget( "entity", {
						type: "array"
					}, mw );

					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div' );

					var nestedObject = table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0];

					expect( nestedObject.toString() ).toBe( 'table id="table-object0Description"' );
					expect( nestedObject.childNodes[0].toString() ).toBe( 'tbody' );
					expect( nestedObject.childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-object0DescriptionNestedName-row"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-object0DescriptionNestedName-label-cell"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe(
							'label for="object0DescriptionNestedName" id="table-object0DescriptionNestedName-label"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Name:' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-object0DescriptionNestedName-cell"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe(
							'input type="text" id="object0DescriptionNestedName" name="object0DescriptionNestedName"' );

					expect( nestedObject.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-object0DescriptionNestedDescription-label-cell"' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe(
							'label for="object0DescriptionNestedDescription" id="table-object0DescriptionNestedDescription-label"' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Description:' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-object0DescriptionNestedDescription-cell"' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe(
							'input type="text" id="object0DescriptionNestedDescription" name="object0DescriptionNestedDescription"' );
				} );

		it( "supports alwaysUseNestedMetawidgetInTables", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder( {
				alwaysUseNestedMetawidgetInTables: true
			} );
			var mw = new metawidget.Metawidget( element, {
				widgetBuilder: widgetBuilder
			} );

			// Root level

			mw.toInspect = [ {
				name: "Foo",
				description: "Foo Description"
			}, {
				name: "Bar",
				description: "Bar Description"
			} ];

			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( mw.nestedMetawidgets[0].toString() ).toBe( 'div' );
			expect( mw.nestedMetawidgets[0].getMetawidget().path ).toBe( 'object[0].name' );
			expect( mw.nestedMetawidgets[1].toString() ).toBe( 'div' );
			expect( mw.nestedMetawidgets[1].getMetawidget().path ).toBe( 'object[0].description' );
			expect( mw.nestedMetawidgets[2].toString() ).toBe( 'div' );
			expect( mw.nestedMetawidgets[2].getMetawidget().path ).toBe( 'object[1].name' );
			expect( mw.nestedMetawidgets[3].toString() ).toBe( 'div' );
			expect( mw.nestedMetawidgets[3].getMetawidget().path ).toBe( 'object[1].description' );
			expect( mw.nestedMetawidgets.length ).toBe( 4 );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Description' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="object0Name" name="object0Name"' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].value ).toBe( 'Foo' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="object0Description" name="object0Description"' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].value ).toBe( 'Foo Description' );
			expect( table.childNodes[1].childNodes[1].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="object1Name" name="object1Name"' );
			expect( table.childNodes[1].childNodes[1].childNodes[0].childNodes[0].childNodes[0].value ).toBe( 'Bar' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="object1Description" name="object1Description"' );
			expect( table.childNodes[1].childNodes[1].childNodes[1].childNodes[0].childNodes[0].value ).toBe( 'Bar Description' );
			expect( table.childNodes[1].childNodes[1].childNodes.length ).toBe( 2 );
		} );

		it( "supports alwaysUseNestedMetawidgetInTables with readOnly tables", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), new metawidget.widgetbuilder.HtmlWidgetBuilder( {
				alwaysUseNestedMetawidgetInTables: true
			} ) ] );
			var mw = new metawidget.Metawidget( element, {
				inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), new metawidget.inspector.JsonSchemaInspector( {
					properties: {
						description: {
							readOnly: true
						}
					}
				} ) ] ),
				widgetBuilder: widgetBuilder
			} );

			// Root level

			mw.toInspect = {
				name: "Bar",
				description: {
					nestedName: "Nested Bar",
					nestedDescription: "A Nested Bar"
				}
			};

			mw.buildWidgets();

			expect( element.toString() ).toBe( 'metawidget' );
			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-name-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-name-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="name" id="table-name-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Name:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="name" name="name"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].value ).toBe( 'Bar' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="description" id="table-description-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Description:' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'div id="description"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].getMetawidget().path ).toBe( 'object.description' );

			var table = element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].childNodes[0];
			expect( table.toString() ).toBe( 'table id="table-description"' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-descriptionNestedName-cell"' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'output id="descriptionNestedName"' );
			expect( table.childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-descriptionNestedDescription-cell"' );
			expect( table.childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'output id="descriptionNestedDescription"' );
		} );
	} );
} )();