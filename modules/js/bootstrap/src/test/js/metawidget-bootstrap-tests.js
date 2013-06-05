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

	describe( "Bootstrap support", function() {

		it( "has a WidgetProcessor that supports Bootstrap styles", function() {

			var element = simpleDocument.createElement( 'div' );

			var widget = simpleDocument.createElement( 'button' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn' );

			widget.setAttribute( 'class', 'other' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'other btn' );

			widget = simpleDocument.createElement( 'table' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'table table-striped table-bordered table-hover' );
		} );

		it( "has a WidgetProcessor that supports input-prepend", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return element;
				}
			}
			var widget = simpleDocument.createElement( 'input' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			var widget = processor.processWidget( widget, 'property', {
				inputPrepend: '$'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-prepend"' );
			expect( widget.childNodes[0].toString() ).toBe( 'span class="add-on"' );
			expect( widget.childNodes[0].innerHTML ).toBe( '$' );
			expect( widget.childNodes[1].toString() ).toBe( 'input' );
			expect( widget.childNodes.length ).toBe( 2 );
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
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" class="control-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" class="control-label required"' );
			expect( element.childNodes[1].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 2 );
		} );
	} );
} )();