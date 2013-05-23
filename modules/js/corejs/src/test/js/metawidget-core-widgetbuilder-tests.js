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

				return document.createElement( 'span' );
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

					return document.createElement( 'span' );
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

			var widget1 = document.createElement( 'widget1' );
			widget1.setAttribute( 'id', 'abcDefGhi' );
			var widget2 = document.createElement( 'widget2' );
			widget2.setAttribute( 'id', 'baz' );
			var widget3 = document.createElement( 'widget3' );
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
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				masked: "true",
				type: "string"
			}, {} ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "function"
			}, {} ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				enum: [ "foo", "bar", "baz" ]
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "string"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "boolean"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "number"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				type: "date"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( "property", {
				hidden: "true",
				readOnly: "true",
				type: "string"
			}, {} ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( "property", {
				readOnly: "true",
				dontExpand: "true"
			}, {} ).toString() ).toBe( 'output' );
		} );
	} );

	describe( "The HtmlWidgetBuilder", function() {

		it( "returns HTML widgets", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			expect( widgetBuilder.buildWidget( "property", {}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( "property", {
				hidden: "true"
			}, {} ).toString() ).toBe( 'stub' );

			var select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo", "bar", "baz" ],
				required: "true"
			}, {} );

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
			}, {} );

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
			}, {} );

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
			}, {} );

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
			}, {} );

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
			}, {} );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			select = widgetBuilder.buildWidget( "property", {
				enum: [ "foo" ],
				enumTitles: [ "Foo" ],
				componentType: "radio"
			}, {} );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="radio" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			var button = widgetBuilder.buildWidget( "property", {
				name: "clickMe",
				type: "function"
			}, {} );

			expect( button.toString() ).toBe( 'button' );
			expect( button.innerHTML ).toBe( 'Click Me' );
			expect( button.getAttribute( 'class' ) ).toBe( null );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number"
			}, {} ).toString() ).toBe( 'input type="number"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				min: "2"
			}, {} ).toString() ).toBe( 'input type="number"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "number",
				minimum: "2",
				maximum: "4"
			}, {} ).toString() ).toBe( 'input type="range" min="2" max="4"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "boolean"
			}, {} ).toString() ).toBe( 'input type="checkbox"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "date"
			}, {} ).toString() ).toBe( 'input type="date"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				large: "true"
			}, {} ).toString() ).toBe( 'textarea' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				masked: "true",
			}, {} ).toString() ).toBe( 'input type="password"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				masked: "true",
				maxLength: "30"
			}, {} ).toString() ).toBe( 'input type="password" maxlength="30"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				enum: undefined,
				masked: "",
				large: ""
			}, {} ).toString() ).toBe( 'input type="text"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string"
			}, {} ).toString() ).toBe( 'input type="text"' );

			expect( widgetBuilder.buildWidget( "property", {
				type: "string",
				maxLength: "32"
			}, {} ).toString() ).toBe( 'input type="text" maxlength="32"' );

			expect( widgetBuilder.buildWidget( "property", {
				dontExpand: "true"
			}, {} ).toString() ).toBe( 'input type="text"' );
		} );

		it( "supports simple collections", function() {

			var element = document.createElement( 'metawidget' );
			var mw = new metawidget.Metawidget( element );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Empty collection

			mw.toInspect = [];
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes.length ).toBe( 0 );

			// Inspect headers

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
		} );
		
		it( "supports object collections", function() {

			var element = document.createElement( 'metawidget' );
			var mw = new metawidget.Metawidget( element );
			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			// Empty collection

			mw.toInspect = [];
			table = widgetBuilder.buildWidget( "entity", {
				type: "array"
			}, mw );

			expect( table.toString() ).toBe( 'table' );
			expect( table.childNodes.length ).toBe( 0 );

			// Inspect headers

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
		} );		
	} );
} )();