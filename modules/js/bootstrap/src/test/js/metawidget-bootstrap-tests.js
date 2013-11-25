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

( function() {

	'use strict';

	describe( "Bootstrap support", function() {

		it( "has a WidgetProcessor that supports Bootstrap styles", function() {

			var element = simpleDocument.createElement( 'div' );

			var widget = simpleDocument.createElement( 'input' );
			widget.setAttribute( 'type', 'button' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn' );

			widget = simpleDocument.createElement( 'input' );
			widget.setAttribute( 'type', 'submit' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn-primary' );

			widget.setAttribute( 'class', 'other' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'other btn btn-primary' );

			widget = simpleDocument.createElement( 'table' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'table table-striped table-bordered table-hover' );
		} );

		it( "has a WidgetProcessor that supports input-prepend and input-append", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return element;
				}
			}
			var widget = simpleDocument.createElement( 'input' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			// Prepend
			
			var widget = processor.processWidget( widget, 'property', {
				inputPrepend: '$'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-prepend"' );
			expect( widget.childNodes[0].toString() ).toBe( 'span class="add-on"' );
			expect( widget.childNodes[0].innerHTML ).toBe( '$' );
			expect( widget.childNodes[1].toString() ).toBe( 'input' );
			expect( widget.childNodes.length ).toBe( 2 );

			// Append
			
			widget = simpleDocument.createElement( 'input' );
			widget = processor.processWidget( widget, 'property', {
				inputAppend: '%'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-append"' );
			expect( widget.childNodes[0].toString() ).toBe( 'input' );
			expect( widget.childNodes[1].toString() ).toBe( 'span class="add-on"' );
			expect( widget.childNodes[1].innerHTML ).toBe( '%' );
			expect( widget.childNodes.length ).toBe( 2 );

			// Both
			
			widget = simpleDocument.createElement( 'input' );
			widget = processor.processWidget( widget, 'property', {
				inputPrepend: '$',
				inputAppend: '.00'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-prepend input-append"' );
			expect( widget.childNodes[0].toString() ).toBe( 'span class="add-on"' );
			expect( widget.childNodes[0].innerHTML ).toBe( '$' );
			expect( widget.childNodes[1].toString() ).toBe( 'input' );
			expect( widget.childNodes[2].toString() ).toBe( 'span class="add-on"' );
			expect( widget.childNodes[2].innerHTML ).toBe( '.00' );
			expect( widget.childNodes.length ).toBe( 3 );
		} );

		it( "has a Layout that supports Bootstrap styles", function() {

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							foo: {
								type: "string"
							},
							bar: {
								type: "string",
								required: true
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout()
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label" class="control-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label" class="control-label"' );
			expect( element.childNodes[1].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 2 );
		} );

		it( "has a Layout that supports configurable Bootstrap styles", function() {

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							foo: {
								type: "string"
							},
							bar: {
								type: "string",
								required: true
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout( {
					appendRequiredClassOnLabelDiv: 'label-required',
					appendRequiredClassOnWidgetDiv: 'widget-required'
				} )
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label" class="control-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div class="label-required"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label" class="control-label"' );
			expect( element.childNodes[1].childNodes[1].toString() ).toBe( 'div class="controls widget-required"' );
			expect( element.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 2 );
		} );

		it( "has a Layout that supports Bootstrap tabs", function() {

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							foo: {
								type: "string"
							},
							bar: {
								type: "string",
								section: "Tab 1"
							},
							baz: {
								type: "string",
								section: "Tab 2"
							},
							abc: {
								type: "string",
								section: ""
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.TabLayoutDecorator( new metawidget.bootstrap.layout.BootstrapDivLayout() )
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label" class="control-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div id="bar-tabs" class="tabs"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'ul class="nav nav-tabs"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'li class="active"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'a data-toggle="tab" href="#bar-tabs1"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Tab 1' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'a data-toggle="tab" href="#bar-tabs2"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].innerHTML ).toBe( 'Tab 2' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			var tabContent = element.childNodes[1].childNodes[0].childNodes[0].childNodes[1]; 
			expect( tabContent.toString() ).toBe( 'div class="tab-content"' );
			expect( tabContent.childNodes[0].toString() ).toBe( 'div class="tab-pane active" id="bar-tabs1"' );
			expect( tabContent.childNodes[0].childNodes[0].toString() ).toBe( 'div class="control-group"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label" class="control-label"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" name="bar"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( tabContent.childNodes[1].toString() ).toBe( 'div class="tab-pane" id="bar-tabs2"' );
			expect( tabContent.childNodes[1].childNodes[0].toString() ).toBe( 'div class="control-group"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="baz" id="baz-label" class="control-label"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( tabContent.childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[2].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label for="abc" id="abc-label" class="control-label"' );
			expect( element.childNodes[2].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[2].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="abc" name="abc"' );
			expect( element.childNodes[2].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 3 );
		} );
	} );
} )();