// Metawidget
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

'use strict';

describe( "The AngularInspectionResultProcessor", function() {

	it( "executes Angular expressions inside inspection results", function() {

		var injector = angular.bootstrap();

		injector.invoke( function( $rootScope ) {

			var processor = new metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor( {}, $rootScope.$new() );
			var inspectionResult = [ {
				"name": "foo",
				"value": "{{1+2}}"
			} ];

			processor.processInspectionResult( inspectionResult );

			expect( inspectionResult[0].name ).toBe( 'foo' );
			expect( inspectionResult[0].value ).toBe( '3' );
		} );
	} );
} );

describe( "The AngularWidgetProcessor", function() {

	it( "processes widgets and binds Angular models", function() {

		var injector = angular.bootstrap();

		injector.invoke( function( $compile, $rootScope ) {

			var processor = new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $compile, $rootScope.$new() );
			var attributes = {
				"name": "foo",
				"required": "true",
				"minimumLength": "3",
				"maximumLength": "97"
			};
			var mw = {
				"toInspect": {},
				"path": "testPath"
			};

			// Inputs

			var widget = document.createElement( 'input' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-model' ) ).toBe( 'testPath.foo' );
			expect( widget.getAttribute( 'ng-required' ) ).toBe( 'true' );
			expect( widget.getAttribute( 'ng-minlength' ) ).toBe( '3' );
			expect( widget.getAttribute( 'ng-maxlength' ) ).toBe( '97' );
			
			// Textareas (same as inputs, not same as outputs)

			widget = document.createElement( 'textarea' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-model' ) ).toBe( 'testPath.foo' );

			// Buttons

			attributes = {
				"name": "bar"
			};
			widget = document.createElement( 'button' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-click' ) ).toBe( 'testPath.bar()' );
			expect( widget.getAttribute( 'ng-required' ) ).toBe( null );
			expect( widget.getAttribute( 'ng-minlength' ) ).toBe( null );
			expect( widget.getAttribute( 'ng-maxlength' ) ).toBe( null );

			// Outputs

			widget = document.createElement( 'output' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.innerHTML ).toBe( '{{testPath.bar}}' );

			// Root-level

			attributes = {
				"name": "$root"
			};
			widget = document.createElement( 'output' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.innerHTML ).toBe( '{{testPath}}' );
		} );
	} );
	
	it( "ignores overridden widgets", function() {

		var injector = angular.bootstrap();

		injector.invoke( function( $compile, $rootScope ) {

			var processor = new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $compile, $rootScope.$new() );
			var attributes = {
				"name": "foo",
			};
			var mw = {
				"toInspect": {}
			};

			var widget = document.createElement( 'input' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-model' ) ).toBeDefined();

			widget = document.createElement( 'input' );
			widget.overridden = true;
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-model' ) ).toBe( null );
		} );
	} );	
} );
