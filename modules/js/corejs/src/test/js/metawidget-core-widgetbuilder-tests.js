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

			var widgetBuilder = new metawidget.widgetbuilder.CompositeWidgetBuilder( [ function( attributes, mw ) {

				if ( attributes.widgetBuilder == '1' ) {
					return {
						name: "widgetBuilder1"
					};
				}

			}, function( attributes, mw ) {

				if ( attributes.widgetBuilder == '2' ) {
					return {
						name: "widgetBuilder2"
					};
				}
			}, function( attributes, mw ) {

				return {
					name: "widgetBuilder3"
				};
			} ] );

			expect( widgetBuilder.buildWidget( {} ).name ).toBe( 'widgetBuilder3' );
			expect( widgetBuilder.buildWidget( {
				widgetBuilder: "1"
			} ).name ).toBe( 'widgetBuilder1' );
			expect( widgetBuilder.buildWidget( {
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
			expect( widgetBuilder.buildWidget( attributes, mw ) ).toBeUndefined();

			var widget1 = document.createElement( 'widget1' );
			widget1.setAttribute( 'id', 'abcDefGhi' );
			var widget2 = document.createElement( 'widget2' );
			widget2.setAttribute( 'id', 'baz' );
			var widget3 = document.createElement( 'widget3' );
			widget3.setAttribute( 'id', 'fooBarBaz' );
			mw.overriddenNodes = [ widget1, widget2, widget3 ];
			expect( widgetBuilder.buildWidget( attributes, mw ) ).toBe( widget3 );
		} );
	} );

	describe( "The ReadOnlyWidgetBuilder", function() {

		it( "returns read-only widgets", function() {

			var widgetBuilder = new metawidget.widgetbuilder.ReadOnlyWidgetBuilder();

			expect( widgetBuilder.buildWidget( {}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( {
				readOnly: "true"
			}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				masked: "true",
				type: "string"
			}, {} ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				type: "function"
			}, {} ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				lookup: "foo,bar,baz"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				type: "string"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				type: "boolean"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				type: "number"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				type: "date"
			}, {} ).toString() ).toBe( 'output' );
			expect( widgetBuilder.buildWidget( {
				hidden: "true",
				readOnly: "true",
				type: "string"
			}, {} ).toString() ).toBe( 'stub' );
			expect( widgetBuilder.buildWidget( {
				readOnly: "true",
				dontExpand: "true"
			}, {} ).toString() ).toBe( 'output' );
		} );
	} );

	describe( "The HtmlWidgetBuilder", function() {

		it( "returns HTML widgets", function() {

			var widgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

			expect( widgetBuilder.buildWidget( {}, {} ) ).toBeUndefined();
			expect( widgetBuilder.buildWidget( {
				hidden: "true"
			}, {} ).toString() ).toBe( 'stub' );

			var select = widgetBuilder.buildWidget( {
				lookup: "foo,bar,baz",
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

			select = widgetBuilder.buildWidget( {
				lookup: "foo,bar,baz",
				lookupLabels: "Foo,Bar,Baz",
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

			select = widgetBuilder.buildWidget( {
				lookup: "foo,bar,baz",
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

			select = widgetBuilder.buildWidget( {
				lookup: "foo,bar,baz",
				lookupLabels: ""
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

			select = widgetBuilder.buildWidget( {
				lookup: "foo,bar,baz",
				lookupLabels: "",
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

			select = widgetBuilder.buildWidget( {
				lookup: "foo",
				lookupLabels: "Foo",
				type: "array"
			}, {} );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			select = widgetBuilder.buildWidget( {
				lookup: "foo",
				lookupLabels: "Foo",
				componentType: "radio"
			}, {} );

			expect( select.toString() ).toBe( 'div' );
			expect( select.childNodes[0].toString() ).toBe( 'label' );
			expect( select.childNodes[0].childNodes[0].toString() ).toBe( 'input type="radio" value="foo"' );
			expect( select.childNodes[0].childNodes[1].toString() ).toBe( 'Foo' );
			expect( select.childNodes.length ).toBe( 1 );

			var button = widgetBuilder.buildWidget( {
				name: "clickMe",
				type: "function"
			}, {} );

			expect( button.toString() ).toBe( 'button' );
			expect( button.innerHTML ).toBe( 'Click Me' );

			expect( widgetBuilder.buildWidget( {
				type: "number"
			}, {} ).toString() ).toBe( 'input type="number"' );

			expect( widgetBuilder.buildWidget( {
				type: "number",
				min: "2"
			}, {} ).toString() ).toBe( 'input type="number"' );

			expect( widgetBuilder.buildWidget( {
				type: "number",
				minimumValue: "2",
				maximumValue: "4"
			}, {} ).toString() ).toBe( 'input type="range" min="2" max="4"' );

			expect( widgetBuilder.buildWidget( {
				type: "boolean"
			}, {} ).toString() ).toBe( 'input type="checkbox"' );

			expect( widgetBuilder.buildWidget( {
				type: "date"
			}, {} ).toString() ).toBe( 'input type="date"' );

			expect( widgetBuilder.buildWidget( {
				type: "string",
				large: "true"
			}, {} ).toString() ).toBe( 'textarea' );

			expect( widgetBuilder.buildWidget( {
				type: "string",
				masked: "true",
			}, {} ).toString() ).toBe( 'input type="password"' );

			expect( widgetBuilder.buildWidget( {
				type: "string",
				masked: "true",
				maximumLength: "30"
			}, {} ).toString() ).toBe( 'input type="password" maxlength="30"' );

			expect( widgetBuilder.buildWidget( {
				type: "string",
				lookup: "",
				masked: "",
				large: ""
			}, {} ).toString() ).toBe( 'input type="text"' );

			expect( widgetBuilder.buildWidget( {
				type: "string"
			}, {} ).toString() ).toBe( 'input type="text"' );

			expect( widgetBuilder.buildWidget( {
				type: "string",
				maximumLength: "32"
			}, {} ).toString() ).toBe( 'input type="text" maxlength="32"' );

			expect( widgetBuilder.buildWidget( {
				dontExpand: "true"
			}, {} ).toString() ).toBe( 'input type="text"' );
		} );
	} );
} )();