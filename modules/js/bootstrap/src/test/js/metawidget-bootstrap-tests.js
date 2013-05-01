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

			var element = document.createElement( 'div' );

			var widget = document.createElement( 'button' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			expect( processor.processWidget( widget )).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn' );

			widget.setAttribute( 'class', 'other' );
			expect( processor.processWidget( widget )).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'other btn' );
		} );

		it( "has a Layout that supports Bootstrap styles", function() {

			var element = document.createElement( 'div' );
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