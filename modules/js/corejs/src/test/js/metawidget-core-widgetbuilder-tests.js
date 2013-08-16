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

	describe( "The CompositeWidgetBuilder", function() {

		it( "starts WidgetBuilders", function() {

			var started = 0;

			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ {

				onStartBuild: function() {

					started++;
				}
			}, {

				onStartBuild: function() {

					started++;
				}
			} ] );

			widgetBuilder.onStartBuild();

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

				onEndBuild: function() {

					ended++;
				}
			}, {

				onEndBuild: function() {

					ended++;
				}
			} ] );

			widgetBuilder.onEndBuild();

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
			}, mw ).toString() ).toBe( 'stub' );
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
				type: "date"
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
			expect( select.childNodes[0].toString() ).toBe( 'option value="foo"' );
			expect( select.childNodes[0].innerHTML ).toBe( 'foo' );
			expect( select.childNodes[1].toString() ).toBe( 'option value="bar"' );
			expect( select.childNodes[1].innerHTML ).toBe( 'bar' );
			expect( select.childNodes[2].toString() ).toBe( 'option value="baz"' );
			expect( select.childNodes[2].innerHTML ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 3 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				enumTitles: [ "Foo", "Bar", "Baz" ],
				required: "true"
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option value="foo"' );
			expect( select.childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( select.childNodes[1].toString() ).toBe( 'option value="bar"' );
			expect( select.childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( select.childNodes[2].toString() ).toBe( 'option value="baz"' );
			expect( select.childNodes[2].innerHTML ).toBe( 'Baz' );
			expect( select.childNodes.length ).toBe( 3 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ]
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option' );
			expect( select.childNodes[0].innerHTML ).toBeUndefined();
			expect( select.childNodes[1].toString() ).toBe( 'option value="foo"' );
			expect( select.childNodes[1].innerHTML ).toBe( 'foo' );
			expect( select.childNodes[2].toString() ).toBe( 'option value="bar"' );
			expect( select.childNodes[2].innerHTML ).toBe( 'bar' );
			expect( select.childNodes[3].toString() ).toBe( 'option value="baz"' );
			expect( select.childNodes[3].innerHTML ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 4 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				enumTitles: []
			}, mw );

			expect( select.toString() ).toBe( 'select' );
			expect( select.childNodes[0].toString() ).toBe( 'option' );
			expect( select.childNodes[0].innerHTML ).toBeUndefined();
			expect( select.childNodes[1].toString() ).toBe( 'option value="foo"' );
			expect( select.childNodes[1].innerHTML ).toBe( 'foo' );
			expect( select.childNodes[2].toString() ).toBe( 'option value="bar"' );
			expect( select.childNodes[2].innerHTML ).toBe( 'bar' );
			expect( select.childNodes[3].toString() ).toBe( 'option value="baz"' );
			expect( select.childNodes[3].innerHTML ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 4 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				enumTitles: [],
				type: "array"
			}, mw );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'foo' );
			expect( select.childNodes[1].toString() ).toBe( 'label' );
			expect( select.childNodes[1].childNodes[0].toString() ).toBe( 'input type="checkbox" value="bar"' );
			expect( select.childNodes[1].childNodes[1].toString() ).toBe( 'bar' );
			expect( select.childNodes[2].toString() ).toBe( 'label' );
			expect( select.childNodes[2].childNodes[0].toString() ).toBe( 'input type="checkbox" value="baz"' );
			expect( select.childNodes[2].childNodes[1].toString() ).toBe( 'baz' );
			expect( select.childNodes.length ).toBe( 3 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo" ],
				enumTitles: [ "Foo" ],
				type: "array"
			}, mw );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo" ],
				enumTitles: [ "Foo" ],
				componentType: "radio"
			}, mw );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="radio" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			var button = widgetBuilder.buildWidget( "property", {
				name: "clickMe",
				type: "function"
			}, mw );

			expect( button.toString() ).toBe( 'button' );
			expect( button.innerHTML ).toBe( 'Click Me' );
			expect( button.getAttribute( 'class' ) ).toBe( null );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number"
			}, mw ).toString() ).toBe( 'input type="number"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				minimum: "2"
			}, mw ).toString() ).toBe( 'input type="number" min="2"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				maximum: "4"
			}, mw ).toString() ).toBe( 'input type="number" max="4"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				minimum: "2",
				maximum: "4"
			}, mw ).toString() ).toBe( 'input type="range" min="2" max="4"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "boolean"
			}, mw ).toString() ).toBe( 'input type="checkbox"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "date"
			}, mw ).toString() ).toBe( 'input type="date"' );

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
				dontExpand: "true"
			}, mw ).toString() ).toBe( 'input type="text"' );
		} );

		it( "supports simple arrays", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = new metawidget.Metawidget( element );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Undefined array

			mw.toInspect = undefined;
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'tbody' );
			expect( table.childNodes.length ).toBe( 1 );
			expect( table.childNodes[0].childNodes.length ).toBe( 0 );

			// Empty array

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
			}

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
					}
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
								type: 'string'
							}
						}
					}
				} )
			} );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Empty array

			mw.toInspect = [];
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes[0].toString() ).toBe( 'thead' );
			expect( table.childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
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
			expect( table.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'th' );
			expect( table.childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( 'Bar' );
			expect( table.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].toString() ).toBe( 'tbody' );
			expect( table.childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'FooValue' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( table.childNodes[1].childNodes[0].childNodes[1].innerHTML ).toBeUndefined();
			expect( table.childNodes[1].childNodes[0].childNodes.length ).toBe( 2 );
			expect( table.childNodes[1].childNodes.length ).toBe( 1 );
			expect( table.childNodes.length ).toBe( 2 );

			// Partially populated array items without JSON Schema

			mw = new metawidget.Metawidget( element );
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
		} );

		it( "has addRow method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			var tbody = simpleDocument.createElement( 'tbody' );

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
			expect( tr.childNodes[0].childNodes[0].innerHTML ).toBe( 'root[0].foo' );
			expect( tr.childNodes[1].toString() ).toBe( 'td' );
			expect( tr.childNodes[1].childNodes[0].toString() ).toBe( 'metawidget' );
			expect( tr.childNodes[1].childNodes[0].innerHTML ).toBe( 'root[0].bar' );
			expect( tr.childNodes.length ).toBe( 2 );

		} );

		it( "has addColumn method for subclasses to override", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			var tr = simpleDocument.createElement( 'tr' );

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

			var td = widgetBuilder.addColumn( tr, [ {
				bar: 'Bar'
			} ], 0, {
				name: 'bar'
			}, 'property', {
				name: 'root'
			}, mw );

			expect( td.childNodes[0].toString() ).toBe( 'metawidget' );
			expect( td.childNodes[0].innerHTML ).toBe( 'root[0].bar' );
			expect( tr.childNodes[1] ).toBe( td );
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
					}, {
						name: "Bar",
						description: {
							nestedName: "Nested Bar",
							nestedDescription: "A Nested Bar"
						}
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

					// Nested table

					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div' );
					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'table id="table-0Description"' );
					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-0Description-row"' );
					expect( table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe(
							'td id="table-0Description-cell" colspan="2"' );

					var nestedTable = table.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0];
					expect( nestedTable.toString() ).toBe( 'table id="0Description"' );
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

					expect( table.childNodes[1].childNodes[1].toString() ).toBe( 'tr' );
					expect( table.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'td' );
					expect( table.childNodes[1].childNodes[1].childNodes[0].innerHTML ).toBe( 'Bar' );
					expect( table.childNodes[1].childNodes[1].childNodes[1].toString() ).toBe( 'td' );

					// Nested object

					expect( table.childNodes[1].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'div' );

					var nestedObject = table.childNodes[1].childNodes[1].childNodes[1].childNodes[0].childNodes[0];

					expect( nestedObject.toString() ).toBe( 'table id="table-1Description"' );
					expect( nestedObject.childNodes[0].toString() ).toBe( 'tbody' );
					expect( nestedObject.childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-1DescriptionNestedName-row"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-1DescriptionNestedName-label-cell"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="1DescriptionNestedName" id="table-1DescriptionNestedName-label"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Name:' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-1DescriptionNestedName-cell"' );
					expect( nestedObject.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="1DescriptionNestedName" name="1DescriptionNestedName"' );

					expect( nestedObject.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-1DescriptionNestedDescription-label-cell"' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe(
							'label for="1DescriptionNestedDescription" id="table-1DescriptionNestedDescription-label"' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Description:' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-1DescriptionNestedDescription-cell"' );
					expect( nestedObject.childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe(
							'input type="text" id="1DescriptionNestedDescription" name="1DescriptionNestedDescription"' );
				} );
	} );
} )();