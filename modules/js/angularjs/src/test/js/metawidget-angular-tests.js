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

'use strict';

describe(
		"The AngularMetawidget",
		function() {

			it(
					"populates itself with widgets to match the properties of business objects",
					function() {

						var myApp = angular.module( 'test-app', [ 'metawidget' ] );
						var controller = myApp.controller( 'TestController', function( $scope ) {

							$scope.foo = {
								bar: "Bar"
							};
						} );

						var mw = document.createElement( 'metawidget' );
						mw.setAttribute( 'ng-model', 'foo' );
						mw.setAttribute( 'read-only', 'readOnly' );
						mw.setAttribute( 'config', 'metawidgetConfig' );

						var body = document.createElement( 'body' );
						body.setAttribute( 'ng-controller', 'TestController' );
						body.appendChild( mw );

						var injector = angular.bootstrap( body, [ 'test-app' ] );

						injector
								.invoke( function() {

									expect( mw.innerHTML )
											.toBe(
													'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );

									// Test watching ngModel

									var scope = angular.element( body ).scope();
									scope.foo = {
										baz: "Baz"
									};
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
									expect( mw.innerHTML ).toNotContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );

									// Test watching readOnly

									scope.readOnly = true;
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<output id="fooBaz" ng-bind="foo.baz" class="ng-scope ng-binding">Baz</output>' );

									// Test watching config

									scope.metawidgetConfig = {
										layout: new metawidget.layout.SimpleLayout()
									};
									scope.$digest();

									expect( mw.innerHTML ).toBe( '<output id="fooBaz" ng-bind="foo.baz" class="ng-scope ng-binding">Baz</output>' );
									expect( mw.innerHTML ).toNotContain( '<table' );
								} );
					} );

			it(
					"watches toInspects that are 'undefined'",
					function() {

						var myApp = angular.module( 'test-app', [ 'metawidget' ] );

						var mw = document.createElement( 'metawidget' );
						mw.setAttribute( 'ng-model', 'foo' );

						var body = document.createElement( 'body' );
						body.appendChild( mw );

						var injector = angular.bootstrap( body, [ 'test-app' ] );

						injector
								.invoke( function() {

									expect( mw.innerHTML ).toBe( '<table id="table-foo"><tbody/></table>' );

									var scope = angular.element( body ).scope();
									scope.foo = {
										bar: "Bar"
									};
									scope.$digest();

									expect( mw.innerHTML )
											.toBe(
													'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );
								} );
					} );
			it(
					"minimizes reinspection",
					function() {

						var myApp = angular.module( 'test-app', [ 'metawidget' ] );
						var inspectionCount = 0;
						var buildingCount = 0;
						var controller = myApp.controller( 'TestController', function( $scope ) {

							$scope.foo = {
								bar: "Bar"
							};

							$scope.metawidgetConfig = {
								inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

									inspectionCount++;
									return inspectionResult;
								} ],
								addWidgetProcessors: [ function( widget, attributes, mw ) {

									buildingCount++;
									return widget;
								} ]
							};
						} );

						var mw = document.createElement( 'metawidget' );
						mw.setAttribute( 'ng-model', 'foo' );
						mw.setAttribute( 'read-only', 'readOnly' );
						mw.setAttribute( 'config', 'metawidgetConfig' );

						var body = document.createElement( 'body' );
						body.setAttribute( 'ng-controller', 'TestController' );
						body.appendChild( mw );

						var injector = angular.bootstrap( body, [ 'test-app' ] );

						injector
								.invoke( function() {

									expect( mw.innerHTML )
											.toBe(
													'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );

									expect( inspectionCount ).toBe( 1 );
									expect( buildingCount ).toBe( 1 );

									// Test changing two things at once

									var scope = angular.element( body ).scope();
									scope.foo = {
										baz: "Baz"
									};
									scope.readOnly = true;
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<output id="fooBaz" ng-bind="foo.baz" class="ng-scope ng-binding">Baz</output>' );
									expect( inspectionCount ).toBe( 2 );
									expect( buildingCount ).toBe( 2 );

									// Test changing to the same value

									scope.toInspect = scope.toInspect;
									scope.readOnly = scope.readOnly;
									scope.$digest();

									// Test rebuilding but not reinspecting

									scope.readOnly = false;
									scope.$digest();
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
									expect( inspectionCount ).toBe( 2 );
									expect( buildingCount ).toBe( 3 );

									// Test changing toInspect to a similar
									// value

									scope.foo = {
										baz: "Baz"
									};
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
									expect( inspectionCount ).toBe( 3 );
									expect( buildingCount ).toBe( 4 );
									
									// Test changing config
									
									scope.metawidgetConfig = {
										layout: new metawidget.layout.SimpleLayout()
									};
									
									scope.$digest();

									expect( mw.innerHTML ).toBe( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
									expect( mw.innerHTML ).toNotContain( '<table' );
									expect( inspectionCount ).toBe( 4 );
									expect( buildingCount ).toBe( 5 );
								} );
					} );

			it(
					"supports arrays of configs",
					function() {

						var myApp = angular.module( 'test-app', [ 'metawidget' ] );
						var inspectionCount = 0;
						var buildingCount = 0;
						var controller = myApp.controller( 'TestController', function( $scope ) {

							$scope.foo = {
								bar: "Bar"
							};

							$scope.metawidgetConfig1 = {
								inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

									inspectionCount++;
									return inspectionResult;
								} ]
							};
							$scope.metawidgetConfig2 = {
								addWidgetProcessors: [ function( widget, attributes, mw ) {

									buildingCount++;
									return widget;
								} ]
							};
						} );

						var mw = document.createElement( 'metawidget' );
						mw.setAttribute( 'ng-model', 'foo' );
						mw.setAttribute( 'read-only', 'readOnly' );
						mw.setAttribute( 'configs', '[metawidgetConfig1,metawidgetConfig2]' );

						var body = document.createElement( 'body' );
						body.setAttribute( 'ng-controller', 'TestController' );
						body.appendChild( mw );

						var injector = angular.bootstrap( body, [ 'test-app' ] );

						injector
								.invoke( function() {

									expect( mw.innerHTML )
											.toBe(
													'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );

									expect( inspectionCount ).toBe( 1 );
									expect( buildingCount ).toBe( 1 );

									// Test changing two things at once

									var scope = angular.element( body ).scope();
									scope.foo = {
										baz: "Baz"
									};
									scope.readOnly = true;
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<output id="fooBaz" ng-bind="foo.baz" class="ng-scope ng-binding">Baz</output>' );
									expect( inspectionCount ).toBe( 2 );
									expect( buildingCount ).toBe( 2 );

									// Test changing to the same value

									scope.toInspect = scope.toInspect;
									scope.readOnly = scope.readOnly;
									scope.$digest();

									// Test rebuilding but not reinspecting

									scope.readOnly = false;
									scope.$digest();
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
									expect( inspectionCount ).toBe( 2 );
									expect( buildingCount ).toBe( 3 );

									// Test changing toInspect to a similar
									// value

									scope.foo = {
										baz: "Baz"
									};
									scope.$digest();

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
									expect( inspectionCount ).toBe( 3 );
									expect( buildingCount ).toBe( 4 );

									// Test changing configs is *not* watched
									
									scope.metawidgetConfig1 = {
										layout: new metawidget.layout.SimpleLayout()
									};
									
									scope.$digest();

									expect( inspectionCount ).toBe( 3 );
									expect( buildingCount ).toBe( 4 );
								} );
					} );

			it( "defensively copies overridden widgets", function() {

				var myApp = angular.module( 'test-app', [ 'metawidget' ] );
				var controller = myApp.controller( 'TestController', function( $scope ) {

					$scope.foo = {
						foo: "Foo",
						bar: "Bar"
					};
				} );

				var mw = document.createElement( 'metawidget' );
				mw.setAttribute( 'ng-model', 'foo' );
				var bar = document.createElement( 'span' );
				bar.setAttribute( 'id', 'fooBar' );
				mw.appendChild( bar );
				var baz = document.createElement( 'span' );
				baz.setAttribute( 'id', 'fooBaz' );
				mw.appendChild( baz );

				var body = document.createElement( 'body' );
				body.setAttribute( 'ng-controller', 'TestController' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'test-app' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toContain( '<input type="text" id="fooFoo" ng-model="foo.foo"' );
					expect( mw.innerHTML ).toContain( '<td id="table-fooBar-cell"><span id="fooBar"' );
					expect( mw.innerHTML ).toContain( '<td colspan="2"><span id="fooBaz"' );
					expect( mw.childNodes.length ).toBe( 1 );
				} );
			} );

			it( "can be used purely for layout", function() {

				var mw = document.createElement( 'metawidget' );
				var bar = document.createElement( 'span' );
				bar.setAttribute( 'id', 'fooBar' );
				mw.appendChild( bar );
				var baz = document.createElement( 'span' );
				baz.setAttribute( 'id', 'fooBaz' );
				mw.appendChild( baz );
				var ignore = document.createTextNode( 'ignore' );
				mw.appendChild( ignore );

				var body = document.createElement( 'body' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'metawidget' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toContain( '<td colspan="2"><span id="fooBar"' );
					expect( mw.innerHTML ).toContain( '<td colspan="2"><span id="fooBaz"' );
					expect( mw.innerHTML ).toNotContain( 'ignore' );
					expect( mw.childNodes.length ).toBe( 1 );
				} );
			} );

			it( "inspects from parent", function() {

				var myApp = angular.module( 'test-app', [ 'metawidget' ] );
				var controller = myApp.controller( 'TestController', function( $scope ) {

					$scope.foo = {
						bar: "Bar"
					};
				} );

				var mw = document.createElement( 'metawidget' );
				mw.setAttribute( 'ng-model', 'foo.bar' );

				var body = document.createElement( 'body' );
				body.setAttribute( 'ng-controller', 'TestController' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'test-app' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toContain( '<label for="fooBar" id="table-fooBar-label">Bar:</label>' );
					expect( mw.childNodes.length ).toBe( 1 );
				} );
			} );

			it( "supports stubs with their own metadata", function() {

				var mw = document.createElement( 'metawidget' );
				var stub = document.createElement( 'stub' );
				stub.setAttribute( 'label', 'Foo' );
				stub.appendChild( document.createElement( 'input' ) );
				mw.appendChild( stub );

				var body = document.createElement( 'body' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'metawidget' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toBe( '<table><tbody><tr><th><label>Foo:</label></th><td><stub label="Foo" class="ng-scope"><input/></stub></td><td/></tr></tbody></table>' );
				} );
			} );

			it( "defensively copies overridden widgets", function() {

				var myApp = angular.module( 'test-app', [ 'metawidget' ] );
				var controller = myApp.controller( 'TestController', function( $scope ) {

					$scope.foo = {
						bar: "Bar"
					};
				} );

				var mw = document.createElement( 'metawidget' );
				var bar = document.createElement( 'span' );
				bar.setAttribute( 'ng-model', 'foo.bar' );
				mw.appendChild( bar );

				var body = document.createElement( 'body' );
				body.setAttribute( 'ng-controller', 'TestController' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'test-app' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toContain( '<label id="table-bar-label">Bar:</label>' );
					expect( mw.innerHTML ).toContain( '<span ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );
					expect( mw.childNodes.length ).toBe( 1 );
				} );
			} );

			it( "does not suppress undefined child inspection results", function() {

				var mw = document.createElement( 'metawidget' );
				var bar = document.createElement( 'span' );
				bar.setAttribute( 'ng-model', 'fooBar' );
				mw.appendChild( bar );

				var body = document.createElement( 'body' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'metawidget' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toContain( '<td colspan="2"><span ng-model="fooBar"' );
					expect( mw.childNodes.length ).toBe( 1 );
				} );
			} );

			it(
					"supports multiselects",
					function() {

						var myApp = angular.module( 'test-app', [ 'metawidget' ] );
						var controller = myApp.controller( 'TestController', function( $scope ) {

							$scope.foo = {
								bar: [ "Abc" ]
							};

							$scope.metawidgetConfig = {
								inspector: function() {

									return [ {
										name: "bar",
										type: "array",
										lookup: "Abc,Def,Ghi"
									} ];
								}
							};
						} );

						var mw = document.createElement( 'metawidget' );
						mw.setAttribute( 'ng-model', 'foo' );
						mw.setAttribute( 'config', 'metawidgetConfig' );

						var body = document.createElement( 'body' );
						body.setAttribute( 'ng-controller', 'TestController' );
						body.appendChild( mw );

						var injector = angular.bootstrap( body, [ 'test-app' ] );

						injector
								.invoke( function() {

									expect( mw.innerHTML ).toContain( '<th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th>' );
									expect( mw.innerHTML )
											.toContain(
													'<div id="fooBar" class="ng-scope"><label><input type="checkbox" value="Abc" ng-checked="foo.bar.indexOf(&apos;Abc&apos;)&gt;=0" ng-click="_mwUpdateSelection($event,&apos;foo.bar&apos;)" checked="checked"/>Abc</label>' );
									expect( mw.innerHTML )
											.toContain(
													'<label><input type="checkbox" value="Def" ng-checked="foo.bar.indexOf(&apos;Def&apos;)&gt;=0" ng-click="_mwUpdateSelection($event,&apos;foo.bar&apos;)"/>Def</label>' );
									expect( mw.innerHTML )
											.toContain(
													'<label><input type="checkbox" value="Ghi" ng-checked="foo.bar.indexOf(&apos;Ghi&apos;)&gt;=0" ng-click="_mwUpdateSelection($event,&apos;foo.bar&apos;)"/>Ghi</label>' );
									expect( mw.innerHTML ).toContain( '</div></td><td/></tr></tbody></table>' );
								} );
					} );

			it( "supports radio buttons", function() {

				var myApp = angular.module( 'test-app', [ 'metawidget' ] );
				var controller = myApp.controller( 'TestController', function( $scope ) {

					$scope.foo = {
						bar: "Def"
					};

					$scope.metawidgetConfig = {
						inspector: function() {

							return [ {
								name: "bar",
								componentType: "radio",
								lookup: "Abc,Def,Ghi"
							} ]
						}
					};
				} );

				var mw = document.createElement( 'metawidget' );
				mw.setAttribute( 'ng-model', 'foo' );
				mw.setAttribute( 'config', 'metawidgetConfig' );

				var body = document.createElement( 'body' );
				body.setAttribute( 'ng-controller', 'TestController' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'test-app' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toContain( '<th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th>' );
					expect( mw.innerHTML ).toContain( '<div id="fooBar" class="ng-scope"><label><input type="radio" value="Abc" ng-model="foo.bar" class="ng-pristine ng-valid" name="' );
					expect( mw.innerHTML ).toContain( '"/>Abc</label><label><input type="radio" value="Def" ng-model="foo.bar" class="ng-pristine ng-valid" name="' );
					expect( mw.innerHTML ).toContain( '"/>Def</label><label><input type="radio" value="Ghi" ng-model="foo.bar" class="ng-pristine ng-valid" name="' );
					expect( mw.innerHTML ).toContain( '"/>Ghi</label></div></td><td/></tr></tbody></table>' );
				} );
			} );

			it(
					"defensively creates parent objects",
					function() {

						var myApp = angular.module( 'test-app', [ 'metawidget' ] );
						var inspectionCount = 0;
						var controller = myApp.controller( 'TestController', function( $scope ) {

							$scope.metawidgetConfig = {
								inspector: function() {

									return [ {
										name: "bar",
										type: "string"
									} ];
								},
								inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

									inspectionCount++;
									return inspectionResult;
								} ]
							}
						} );

						var mw = document.createElement( 'metawidget' );
						mw.setAttribute( 'ng-model', 'foo' );
						mw.setAttribute( 'config', 'metawidgetConfig' );

						var body = document.createElement( 'body' );
						body.setAttribute( 'ng-controller', 'TestController' );
						body.appendChild( mw );

						var injector = angular.bootstrap( body, [ 'test-app' ] );

						injector
								.invoke( function() {

									expect( mw.innerHTML )
											.toBe(
													'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

									expect( mw.innerHTML ).toContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );

									expect( inspectionCount ).toBe( 1 );

									// Parent object should have been created

									var scope = angular.element( body ).scope();
									expect( scope.foo ).toBeDefined();
									expect( scope.foo.bar ).toBeUndefined();

									// So subsequent updates shouldn't reinspect

									scope.foo.bar = '123';
									scope.$digest();

									expect( inspectionCount ).toBe( 1 );
								} );
					} );

			it( "does not watch primitives", function() {

				var myApp = angular.module( 'test-app', [ 'metawidget' ] );
				var inspectionCount = 0;
				var controller = myApp.controller( 'TestController', function( $scope ) {

					$scope.foo = 'hello';

					$scope.metawidgetConfig = {
						inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

							inspectionCount++;
							return inspectionResult;
						} ]
					}
				} );

				var mw = document.createElement( 'metawidget' );
				mw.setAttribute( 'ng-model', 'foo' );
				mw.setAttribute( 'config', 'metawidgetConfig' );

				var body = document.createElement( 'body' );
				body.setAttribute( 'ng-controller', 'TestController' );
				body.appendChild( mw );

				var injector = angular.bootstrap( body, [ 'test-app' ] );

				injector.invoke( function() {

					expect( mw.innerHTML ).toBe(
							'<table id="table-foo"><tbody><tr><td colspan="2"><input type="text" id="foo" ng-model="foo" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

					expect( inspectionCount ).toBe( 1 );

					var scope = angular.element( body ).scope();
					scope.foo = 'goodbye';
					scope.$digest();

					expect( inspectionCount ).toBe( 1 );
				} );
			} );
		} );

describe( "The AngularInspectionResultProcessor", function() {

	it( "executes Angular expressions inside inspection results", function() {

		var injector = angular.bootstrap();

		injector.invoke( function( $rootScope ) {

			var processor = new metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor( $rootScope.$new() );
			var inspectionResult = [ {
				name: "foo",
				value: "{{1+2}}"
			} ];

			inspectionResult = processor.processInspectionResult( inspectionResult );

			expect( inspectionResult[0].name ).toBe( 'foo' );
			expect( inspectionResult[0].value ).toBe( '3' );
		} );
	} );
} );

describe( "The AngularWidgetProcessor", function() {

	it( "processes widgets and binds Angular models", function() {

		var injector = angular.bootstrap();

		injector.invoke( function( $compile, $parse, $rootScope ) {

			var processor = new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $compile, $parse, $rootScope.$new() );
			var attributes = {
				name: "foo",
				required: "true",
				minimumLength: "3",
				maximumLength: "97"
			};
			var mw = {
				toInspect: {},
				path: "testPath"
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
				name: "bar"
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
			expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'testPath.bar' );

			attributes = {
				name: "bar",
				type: "array"
			}
			widget = document.createElement( 'output' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-bind' ) ).toBe( "testPath.bar.join(', ')" );

			// Root-level

			attributes = {
				_root: 'true'
			};
			widget = document.createElement( 'output' );
			processor.processWidget( widget, attributes, mw );
			expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'testPath' );
		} );
	} );

	it( "ignores overridden widgets", function() {

		var injector = angular.bootstrap();

		injector.invoke( function( $compile, $parse, $rootScope ) {

			var processor = new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $compile, $parse, $rootScope.$new() );
			var attributes = {
				name: "foo",
			};
			var mw = {
				toInspect: {}
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
