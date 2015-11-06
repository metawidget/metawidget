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

( function() {

	'use strict';

	describe(
			"The AngularMetawidget",
			function() {

				it(
						"populates itself with widgets to match the properties of domain objects",
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
							mw.setAttribute( 'configs', '[ metawidgetConfigInArray ]' );

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

										// Test proper cleanup

										var destroyCalled = 0;

										angular.element( body ).find( '#fooBar' ).on( '$destroy', function() {

											destroyCalled++;
										} );

										// Test watching ngModel

										var scope = angular.element( body ).scope();
										scope.foo = {
											baz: "Baz"
										};
										scope.$digest();

										expect( destroyCalled ).toBe( 1 );
										expect( mw.innerHTML ).toContain( '<input type="text" id="fooBaz" ng-model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
										expect( mw.innerHTML ).toNotContain( '<input type="text" id="fooBar" ng-model="foo.bar"' );

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

										// Test updating config *and* model at
										// the same time

										var regenerations = 0;
										scope.metawidgetConfig = {
											inspector: function() {

												regenerations++;
												return {
													properties: {
														fromInspector1: {
															type: 'string'
														}
													}
												}
											}
										};
										scope.$digest();
										expect( mw.innerHTML ).toBe( '<output id="fooFromInspector1" ng-bind="foo.fromInspector1" class="ng-scope ng-binding"></output>' );
										expect( regenerations ).toBe( 1 );

										// watchConfig should fire before
										// watchModel, so should only regenerate
										// the once

										scope.foo = {
											abc: "Abc"
										};
										scope.metawidgetConfig = {
											inspector: function() {

												regenerations++;
												return {
													properties: {
														fromInspector2: {
															type: 'string'
														}
													}
												}
											}
										};
										scope.$digest();

										expect( mw.innerHTML ).toBe( '<output id="fooFromInspector2" ng-bind="foo.fromInspector2" class="ng-scope ng-binding"></output>' );
										expect( regenerations ).toBe( 2 );

										// Test *not* watching config array

										scope.metawidgetConfigInArray = {
											layout: new metawidget.layout.DivLayout()
										};
										scope.$digest();

										expect( mw.innerHTML ).toBe( '<output id="fooFromInspector2" ng-bind="foo.fromInspector2" class="ng-scope ng-binding"></output>' );
										expect( mw.innerHTML ).toNotContain( '<div' );
										expect( regenerations ).toBe( 2 );
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
									appendWidgetProcessors: [ function( widget, attributes, mw ) {

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
									appendWidgetProcessors: [ function( widget, attributes, mw ) {

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

										// Test changing configs is *not*
										// watched

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
						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									properties: {
										bar: {
											type: 'string',
											required: true
										}
									}
								};
							}
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'foo.bar' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toNotContain( 'label' );
						expect( mw.innerHTML ).toNotContain( '<input type="text" id="fooBar" required="true" ng-model="foo.bar"' );
						expect( mw.childNodes.length ).toBe( 1 );
					} );
				} );

				it(
						"supports stubs with their own metadata",
						function() {

							var mw = document.createElement( 'metawidget' );
							var stub = document.createElement( 'stub' );
							stub.setAttribute( 'title', 'Foo' );
							stub.appendChild( document.createElement( 'input' ) );
							mw.appendChild( stub );

							// (test childAttributes don't bleed into next
							// component)

							var div = document.createElement( 'div' );
							div.appendChild( document.createElement( 'input' ) );
							mw.appendChild( div );

							var body = document.createElement( 'body' );
							body.appendChild( mw );

							var injector = angular.bootstrap( body, [ 'metawidget' ] );

							injector
									.invoke( function() {

										expect( mw.innerHTML )
												.toBe(
														'<table><tbody><tr><th><label>Foo:</label></th><td><stub title="Foo" class="ng-scope"><input/></stub></td><td/></tr><tr><td colspan="2"><div class="ng-scope"><input/></div></td><td/></tr></tbody></table>' );
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

				it( "supports normalized attribute names", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: "Bar",
							baz: "Baz"
						};
					} );

					// x-ng-bind

					var mw = document.createElement( 'metawidget' );
					var bar = document.createElement( 'span' );
					bar.setAttribute( 'x-ng-bind', 'foo.bar' );
					mw.appendChild( bar );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<label id="table-bar-label">Bar:</label>' );
						expect( mw.innerHTML ).toContain( '<span x-ng-bind="foo.bar" class="ng-scope ng-binding">Bar</span>' );
						expect( mw.childNodes.length ).toBe( 1 );
					} );

					// ng:model

					var baz = document.createElement( 'span' );
					baz.setAttribute( 'ng:model', 'foo.baz' );
					mw.appendChild( baz );

					injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<label id="table-baz-label">Baz:</label>' );
						expect( mw.innerHTML ).toContain( '<span ng:model="foo.baz" class="ng-scope ng-pristine ng-valid"/>' );
						expect( mw.childNodes.length ).toBe( 1 );
					} );

					// ngmodel (Angular's template mechanism lowercases
					// attribute names)

					var baz = document.createElement( 'span' );
					baz.setAttribute( 'ngmodel', 'foo.baz' );
					mw.appendChild( baz );

					injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<label id="table-baz-label">Baz:</label>' );
						expect( mw.innerHTML ).toContain( '<span ngmodel="foo.baz" class="ng-scope"/>' );
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

										return {
											"properties": {
												"bar": {
													type: "array",
													enum: [ "Abc", "Def", "Ghi" ]
												}
											}
										};
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
														'<div id="fooBar" class="ng-scope"><label class="checkbox"><input type="checkbox" ng-checked="foo.bar.indexOf(&apos;Abc&apos;)&gt;=0" ng-click="mwUpdateSelection($event,&apos;foo.bar&apos;)" checked="checked"/>Abc</label>' );
										expect( mw.innerHTML )
												.toContain(
														'<label class="checkbox"><input type="checkbox" ng-checked="foo.bar.indexOf(&apos;Def&apos;)&gt;=0" ng-click="mwUpdateSelection($event,&apos;foo.bar&apos;)"/>Def</label>' );
										expect( mw.innerHTML )
												.toContain(
														'<label class="checkbox"><input type="checkbox" ng-checked="foo.bar.indexOf(&apos;Ghi&apos;)&gt;=0" ng-click="mwUpdateSelection($event,&apos;foo.bar&apos;)"/>Ghi</label>' );
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

								return {
									"properties": {
										"bar": {
											componentType: "radio",
											enum: [ "Abc", "Def", "Ghi" ]
										}
									}
								};
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
						expect( mw.innerHTML ).toContain( '<div id="fooBar" class="ng-scope"><label class="radio"><input type="radio" ng-model="foo.bar" class="ng-pristine ng-valid" name="' );
						expect( mw.innerHTML ).toContain( '"/>Abc</label><label class="radio"><input type="radio" ng-model="foo.bar" class="ng-pristine ng-valid" name="' );
						expect( mw.innerHTML ).toContain( '"/>Def</label><label class="radio"><input type="radio" ng-model="foo.bar" class="ng-pristine ng-valid" name="' );
						expect( mw.innerHTML ).toContain( '"/>Ghi</label></div></td><td/></tr></tbody></table>' );
					} );
				} );

				it( "supports boolean radio buttons", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: true
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									"properties": {
										"bar": {
											type: "boolean",
											componentType: "radio"
										}
									}
								};
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
						expect( mw.innerHTML ).toContain(
								'<div id="fooBar" class="ng-scope"><label class="radio"><input type="radio" ng-model="foo.bar" ng-value="true" class="ng-pristine ng-valid" value="true" name="' );
						expect( mw.innerHTML ).toContain(
								'"/>Yes</label><label class="radio"><input type="radio" ng-model="foo.bar" ng-value="false" class="ng-pristine ng-valid" value="false" name="' );
						expect( mw.innerHTML ).toContain( '"/>No</label></div></td><td/></tr></tbody></table>' );
					} );

					// Some browsers convert values to strings
					// (e.g. child.value = true becomes 'true')

					myApp = angular.module( 'test-app', [ 'metawidget' ] );
					controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: true
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									"properties": {
										"bar": {
											type: "boolean",
											enum: [ "true", "false" ],
											enumTitles: [ "Yes", "No" ],
											componentType: "radio"
										}
									}
								};
							}
						};
					} );

					mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'foo' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th>' );
						expect( mw.innerHTML ).toContain(
								'<div id="fooBar" class="ng-scope"><label class="radio"><input type="radio" ng-model="foo.bar" ng-value="true" class="ng-pristine ng-valid" value="true" name="' );
						expect( mw.innerHTML ).toContain(
								'"/>Yes</label><label class="radio"><input type="radio" ng-model="foo.bar" ng-value="false" class="ng-pristine ng-valid" value="false" name="' );
						expect( mw.innerHTML ).toContain( '"/>No</label></div></td><td/></tr></tbody></table>' );
					} );

				} );

				it( "supports boolean selects", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: true
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									"properties": {
										"bar": {
											type: "boolean",
											'enum': [ false, true ],
											enumTitles: [ 'Pending', 'Approved' ]
										}
									}
								};
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
						expect( mw.innerHTML ).toContain( '<select id="fooBar" ng-model="foo.bar" ng-change="mwChangeAsType(&apos;boolean&apos;,&apos;foo.bar&apos;)" class="ng-scope ng-pristine ng-valid"><option value=""/>' );
						expect( mw.innerHTML ).toContain( '<option value="false" ng-selected="foo.bar==false">Pending</option>' );
						expect( mw.innerHTML ).toContain( '<option value="true" ng-selected="foo.bar==true" selected="selected">Approved</option></select>' );
					} );
				} );

				it( "supports numeric selects", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: 2
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									"properties": {
										"bar": {
											type: "number",
											'enum': [ 1, 2, 3 ],
											enumTitles: [ 'One', 'Two', 'Three' ],
											required: true
										}
									}
								};
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
						expect( mw.innerHTML ).toContain(
								'<select id="fooBar" ng-model="foo.bar" ng-change="mwChangeAsType(&apos;number&apos;,&apos;foo.bar&apos;)" ng-required="true" class="ng-scope ng-pristine ng-valid ng-valid-required" required="required"><option value="1"' );
						expect( mw.innerHTML ).toContain( '<option value="1" ng-selected="foo.bar==1">One</option>' );
						expect( mw.innerHTML ).toContain( '<option value="2" ng-selected="foo.bar==2" selected="selected">Two</option>' );
						expect( mw.innerHTML ).toContain( '<option value="3" ng-selected="foo.bar==3">Three</option>' );
					} );
				} );

				it( "guards against infinite recursion", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									"properties": {
										"foo": {}
									}
								};
							}
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'root' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.childNodes[0].tagName ).toBe( 'TABLE' );
						expect( mw.childNodes[0].childNodes[0].tagName ).toBe( 'TBODY' );

						var childNode = mw.childNodes[0].childNodes[0];
						var idMiddle = 'Foo';

						for ( var loop = 0; loop < 10; loop++ ) {

							expect( childNode.childNodes[0].tagName ).toBe( 'TR' );
							expect( childNode.childNodes[0].id ).toBe( 'table-root' + idMiddle + '-row' );
							expect( childNode.childNodes[0].childNodes[0].tagName ).toBe( 'TH' );
							expect( childNode.childNodes[0].childNodes[0].getAttribute( 'id' ) ).toBe( 'table-root' + idMiddle + '-label-cell' );
							expect( childNode.childNodes[0].childNodes[0].childNodes[0].tagName ).toBe( 'LABEL' );
							expect( childNode.childNodes[0].childNodes[0].childNodes[0].getAttribute( 'for' ) ).toBe( 'root' + idMiddle );
							expect( childNode.childNodes[0].childNodes[0].childNodes[0].getAttribute( 'id' ) ).toBe( 'table-root' + idMiddle + '-label' );
							expect( childNode.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
							expect( childNode.childNodes[0].childNodes[1].tagName ).toBe( 'TD' );
							expect( childNode.childNodes[0].childNodes[1].getAttribute( 'id' ) ).toBe( 'table-root' + idMiddle + '-cell' );
							expect( childNode.childNodes[0].childNodes[1].childNodes[0].tagName ).toBe( 'METAWIDGET' );
							expect( childNode.childNodes[0].childNodes[1].childNodes[0].getAttribute( 'id' ) ).toBe( 'root' + idMiddle );
							expect( childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].tagName ).toBe( 'TABLE' );
							expect( childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].getAttribute( 'id' ) ).toBe( 'table-root' + idMiddle );
							expect( childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].tagName ).toBe( 'TBODY' );
							expect( childNode.childNodes[0].childNodes.length ).toBe( 3 );
							expect( childNode.childNodes.length ).toBe( 1 );

							idMiddle += 'Foo';
							childNode = childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0];
						}

						expect( childNode.childNodes.length ).toBe( 0 );

						expect( mw.childNodes[0].childNodes.length ).toBe( 1 );
						expect( mw.childNodes.length ).toBe( 1 );
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

						expect( mw.innerHTML ).toBe(
								'<table id="table-foo"><tbody><tr><td colspan="2"><input type="text" id="foo" ng-model="foo" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

						expect( inspectionCount ).toBe( 1 );

						var scope = angular.element( body ).scope();
						scope.foo = 'goodbye';
						scope.$digest();

						expect( inspectionCount ).toBe( 1 );
					} );
				} );

				it( "provides access to attached element", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var attachedElement = [];
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.metawidgetConfig = {
							inspector: function() {

								return {};
							},
							inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

								attachedElement.push( mw.getElement() );
								return inspectionResult;
							} ]
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toBe( '<table><tbody/></table>' );
						expect( attachedElement[0] ).toBe( mw );
						expect( attachedElement.length ).toBe( 1 );
					} );
				} );

				it(
						"supports directives that add a sibling element",
						function() {

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var attachedElement = [];
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.model = {
									"foo": "fooValue",
									"bar": "barValue"
								};
							} );

							myApp.directive( 'input', function() {

								return {
									restrict: 'E',
									scope: {
										ngModel: '=',
									},
									compile: function compile( element, attrs, transclude ) {

										if ( attrs.ngModel === 'model.foo' ) {
											element.after( '<div class="input-sibling"></div>' );
										}
									}
								};
							} );

							var body = document.createElement( 'body' );
							body.setAttribute( 'ng-controller', 'TestController' );

							var mw = document.createElement( 'metawidget' );
							mw.setAttribute( 'ng-model', 'model' );
							body.appendChild( mw );

							var injector = angular.bootstrap( body, [ 'test-app' ] );

							injector
									.invoke( function() {

										expect( mw.innerHTML )
												.toBe(
														'<table id="table-model"><tbody><tr id="table-modelFoo-row"><th id="table-modelFoo-label-cell"><label for="modelFoo" id="table-modelFoo-label">Foo:</label></th><td id="table-modelFoo-cell"><input type="text" id="modelFoo" ng-model="model.foo" class="ng-isolate-scope ng-scope ng-pristine ng-valid"/><div class="input-sibling"/></td><td/></tr><tr id="table-modelBar-row"><th id="table-modelBar-label-cell"><label for="modelBar" id="table-modelBar-label">Bar:</label></th><td id="table-modelBar-cell"><input type="text" id="modelBar" ng-model="model.bar" class="ng-isolate-scope ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );
										expect( mw.innerHTML )
												.toContain(
														'<td id="table-modelFoo-cell"><input type="text" id="modelFoo" ng-model="model.foo" class="ng-isolate-scope ng-scope ng-pristine ng-valid"/><div class="input-sibling"/></td>' );
									} );
						} );

				it(
						"supports directives that use templates",
						function() {

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var attachedElement = [];
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.model = {
									"foo": "fooValue",
									"bar": "barValue"
								};

								$scope.config = {

									widgetBuilder: function( elementName ) {

										if ( elementName !== 'property' ) {
											return;
										}

										return document.createElement( 'field' );
									}
								};
							} );

							myApp.directive( 'field', function() {

								return {
									restrict: 'E',
									replace: true,
									template: '<div class="input-append"><input type="text" data-type="date" mask="date" class="input-small"/><span class="add-on"></span></div>'
								};
							} );

							var body = document.createElement( 'body' );
							body.setAttribute( 'ng-controller', 'TestController' );

							var mw = document.createElement( 'metawidget' );
							mw.setAttribute( 'ng-model', 'model' );
							mw.setAttribute( 'config', 'config' );
							body.appendChild( mw );

							var injector = angular.bootstrap( body, [ 'test-app' ] );

							injector
									.invoke( function() {

										expect( mw.innerHTML )
												.toBe(
														'<table id="table-model"><tbody><tr id="table-modelFoo-row"><th id="table-modelFoo-label-cell"><label for="modelFoo" id="table-modelFoo-label">Foo:</label></th><td id="table-modelFoo-cell"><div class="input-append ng-scope" id="modelFoo"><input type="text" data-type="date" mask="date" class="input-small" value=""/><span class="add-on"/></div></td><td/></tr><tr id="table-modelBar-row"><th id="table-modelBar-label-cell"><label for="modelBar" id="table-modelBar-label">Bar:</label></th><td id="table-modelBar-cell"><div class="input-append ng-scope" id="modelBar"><input type="text" data-type="date" mask="date" class="input-small" value=""/><span class="add-on"/></div></td><td/></tr></tbody></table>' );
										expect( mw.innerHTML )
												.toContain(
														'<div class="input-append ng-scope" id="modelFoo"><input type="text" data-type="date" mask="date" class="input-small" value=""/><span class="add-on"/></div>' );
									} );
						} );

				it( "guards against infinite loops", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.model = {};

						$scope.metawidgetConfig = {
							appendInspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

								mw.buildWidgets( undefined );
							} ]
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'model' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toBe( '' );
					} );
				} );

				it( "supports read-only enumTitles", function() {

					// Normal

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: 2
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									properties: {
										"bar": {
											enum: [ 1, 2, 3 ],
											enumTitles: [ "One", "Two", "Three" ]
										}
									}
								};
							}
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'foo' );
					mw.setAttribute( 'read-only', 'true' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<output id="fooBar" ng-bind="mwLookupEnumTitle[&quot;foo.bar&quot;](foo.bar)" class="ng-scope ng-binding">Two</output>' );
					} );

					// Mismatched

					controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: 2
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									properties: {
										"bar": {
											enum: [ 1, 2, 3 ],
											enumTitles: []
										}
									}
								};
							}
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'foo' );
					mw.setAttribute( 'read-only', 'true' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<output id="fooBar" ng-bind="mwLookupEnumTitle[&quot;foo.bar&quot;](foo.bar)" class="ng-scope ng-binding">2</output>' );
					} );
				} );

				it( "supports masked outputs", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							password: 'fooBar'
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									properties: {
										"password": {
											readOnly: true,
											masked: true,
											type: 'string'
										}
									}
								};
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

						expect( mw.innerHTML ).toContain( '<label for="fooPassword" id="table-fooPassword-label">Password:</label>' );
						expect( mw.innerHTML ).toContain( '<output id="fooPassword" ng-bind="mwMaskedOutput(foo.password)" class="ng-scope ng-binding">******</output>' );
					} );
				} );

				it( "supports collections", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
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
						};
					} );

					var mw = document.createElement( 'metawidget' );
					mw.setAttribute( 'ng-model', 'foo' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( '<label for="fooBar" id="table-fooBar-label">Bar:</label>' );
						expect( mw.innerHTML ).toContain( '<table id="fooBar" class="ng-scope">' );
						expect( mw.innerHTML ).toContain( '<thead><tr><th>Firstname</th><th>Surname</th></tr></thead>' );
						expect( mw.innerHTML ).toContain(
								'<tbody><tr><td>firstname1</td><td>surname1</td></tr><tr><td>firstname2</td><td>surname2</td></tr><tr><td>firstname3</td><td>surname3</td></tr></tbody>' );
					} );
				} );

				it( "supports collections with alwaysUseNestedMetawidgetInTables", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
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
						};

						$scope.metawidgetConfig = {
							widgetBuilder: new metawidget.widgetbuilder.HtmlWidgetBuilder( {
								alwaysUseNestedMetawidgetInTables: true
							} )
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

						expect( mw.innerHTML ).toContain( '<label for="fooBar" id="table-fooBar-label">Bar:</label>' );
						expect( mw.innerHTML ).toContain( '<table id="fooBar" class="ng-scope">' );
						expect( mw.innerHTML ).toContain( '<thead><tr><th>Firstname</th><th>Surname</th></tr></thead>' );
						expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar[0].firstname"' );
						expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar[0].surname"' );
						expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar[1].firstname"' );
						expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar[1].surname"' );
						expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar[2].firstname"' );
						expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar[2].surname"' );
					} );
				} );

				it(
						"supports nested Metawidgets",
						function() {

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.foo = {
									bar: {
										baz: {
											firstname: 'firstname1',
											surname: 'surname1'
										}
									}
								};
							} );

							var mw = document.createElement( 'metawidget' );
							mw.setAttribute( 'ng-model', 'foo' );

							var body = document.createElement( 'body' );
							body.setAttribute( 'ng-controller', 'TestController' );
							body.appendChild( mw );

							var injector = angular.bootstrap( body, [ 'test-app' ] );

							injector
									.invoke( function() {

										expect( mw.innerHTML )
												.toContain(
														'<table id="table-fooBarBaz"><tbody><tr id="table-fooBarBazFirstname-row"><th id="table-fooBarBazFirstname-label-cell"><label for="fooBarBazFirstname" id="table-fooBarBazFirstname-label">Firstname:</label></th><td id="table-fooBarBazFirstname-cell"><input type="text" id="fooBarBazFirstname" ng-model="foo.bar.baz.firstname" class="ng-scope ng-pristine ng-valid"/></td><td/></tr><tr id="table-fooBarBazSurname-row"><th id="table-fooBarBazSurname-label-cell"><label for="fooBarBazSurname" id="table-fooBarBazSurname-label">Surname:</label></th><td id="table-fooBarBazSurname-cell"><input type="text" id="fooBarBazSurname" ng-model="foo.bar.baz.surname" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );
										expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar"' );
										expect( mw.innerHTML ).toContain( '<metawidget ng-model="foo.bar.baz"' );
										expect( mw.innerHTML ).toContain( '</metawidget>' );
									} );
						} );

				it( "supports non-string enums", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: 3,
							baz: true
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									"properties": {
										"bar": {
											enum: [ 1, 2, 3 ]
										},
										"baz": {
											enum: [ true, false ]
										}
									}
								};
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

					injector.invoke( function( $rootScope ) {

						expect( mw.innerHTML ).toContain( '<option value="3">3</option>' );
						expect( angular.element( mw ).find( '#fooBar' ).val() ).toBe( '3' );
						expect( mw.innerHTML ).toContain( '<option value="true">true</option>' );

						var scope = angular.element( body ).scope();
						expect( scope.foo.bar ).toBe( 3 );
						expect( scope.foo.baz ).toBe( true );

						angular.element( mw ).find( '#fooBar' ).val( 2 ).change();
						angular.element( mw ).find( '#fooBaz' ).val( 'false' ).change();

						// Note: it's debatable if this is the correct
						// behaviour. SELECT can only handle strings, but we
						// could try and be smarter about the reverse binding

						expect( scope.foo.bar ).toBe( '2' );
						expect( scope.foo.baz ).toBe( 'false' );
					} );
				} );

				it( "supports enums that contain null", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							bar: 2
						};

						$scope.metawidgetConfig = {
							inspector: function() {

								return {
									properties: {
										"bar": {
											enum: [ null, 2, 3 ],
											enumTitles: [ "Null", "Two", "Three" ]
										}
									}
								};
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

						expect( mw.innerHTML ).toContain( '<option value="null">Null</option><option value="2">Two</option>' );
					} );
				} );

				it(
						"supports namespaces (for IE8)",
						function() {

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.foo = {
									bar: "Bar"
								};
							} );

							var mw = document.createElement( 'mw:metawidget' );
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
									} );
						} );

				it( "escapes property names", function() {

					var myApp = angular.module( 'test-app', [ 'metawidget' ] );
					var controller = myApp.controller( 'TestController', function( $scope ) {

						$scope.foo = {
							'with.dot': "With Dot",
							"with'apostrophe": "With Apostrophe",
							'with"quote': "With Quote",
							'with space': "With Space"
						};
					} );

					var mw = document.createElement( 'mw:metawidget' );
					mw.setAttribute( 'ng-model', 'foo' );
					mw.setAttribute( 'read-only', 'readOnly' );
					mw.setAttribute( 'config', 'metawidgetConfig' );

					var body = document.createElement( 'body' );
					body.setAttribute( 'ng-controller', 'TestController' );
					body.appendChild( mw );

					var injector = angular.bootstrap( body, [ 'test-app' ] );

					injector.invoke( function() {

						expect( mw.innerHTML ).toContain( 'ng-model="foo[&apos;with.dot&apos;]"' );
						expect( mw.innerHTML ).toContain( 'ng-model="foo[&apos;with\\&apos;apostrophe&apos;]"' );
						expect( mw.innerHTML ).toContain( 'ng-model="foo[&apos;with&quot;quote&apos;]"' );
						expect( mw.innerHTML ).toContain( 'ng-model="foo[&apos;with space&apos;]"' );
					} );
				} );

				it(
						"supports ngShow",
						function() {

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var inspectionCount = 0;
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.foo = {
									bar: "Bar"
								};

								$scope.metawidgetConfig = {
									inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

										inspectionCount++;
										return inspectionResult;
									} ],
								};

								$scope.show = false;
							} );

							var mw = document.createElement( 'metawidget' );
							mw.setAttribute( 'ng-model', 'foo' );
							mw.setAttribute( 'config', 'metawidgetConfig' );
							mw.setAttribute( 'ng-show', 'show' );

							var body = document.createElement( 'body' );
							body.setAttribute( 'ng-controller', 'TestController' );
							body.appendChild( mw );

							var injector = angular.bootstrap( body, [ 'test-app' ] );

							injector
									.invoke( function() {

										expect( mw.innerHTML ).toBe( '' );
										expect( inspectionCount ).toBe( 0 );

										var scope = angular.element( body ).scope();
										scope.show = true;
										scope.$digest();

										expect( mw.innerHTML )
												.toBe(
														'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

										expect( mw.innerHTML ).toContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );
										expect( inspectionCount ).toBe( 1 );
									} );
						} );

				it(
						"supports ngHide",
						function() {

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var inspectionCount = 0;
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.foo = {
									bar: "Bar"
								};

								$scope.metawidgetConfig = {
									inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, path, names ) {

										inspectionCount++;
										return inspectionResult;
									} ],
								};

								$scope.hide = true;
							} );

							var mw = document.createElement( 'metawidget' );
							mw.setAttribute( 'ng-model', 'foo' );
							mw.setAttribute( 'config', 'metawidgetConfig' );
							mw.setAttribute( 'ng-hide', 'hide' );

							var body = document.createElement( 'body' );
							body.setAttribute( 'ng-controller', 'TestController' );
							body.appendChild( mw );

							var injector = angular.bootstrap( body, [ 'test-app' ] );

							injector
									.invoke( function() {

										expect( mw.innerHTML ).toBe( '' );
										expect( inspectionCount ).toBe( 0 );

										var scope = angular.element( body ).scope();
										scope.hide = false;
										scope.$digest();

										expect( mw.innerHTML )
												.toBe(
														'<table id="table-foo"><tbody><tr id="table-fooBar-row"><th id="table-fooBar-label-cell"><label for="fooBar" id="table-fooBar-label">Bar:</label></th><td id="table-fooBar-cell"><input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/></td><td/></tr></tbody></table>' );

										expect( mw.innerHTML ).toContain( '<input type="text" id="fooBar" ng-model="foo.bar" class="ng-scope ng-pristine ng-valid"/>' );
										expect( inspectionCount ).toBe( 1 );
									} );
						} );
			} );

	describe(
			"The AngularInspectionResultProcessor",
			function() {

				it( "executes Angular expressions inside inspection results", function() {

					var injector = angular.bootstrap();

					injector.invoke( function( $rootScope ) {

						var processor = new metawidget.angular.inspectionresultprocessor.AngularInspectionResultProcessor( $rootScope.$new() );
						var inspectionResult = {
							properties: {
								"foo": {
									value: "{{1+2}}",
									ignore: 3,
									missing: undefined
								}
							}
						};

						inspectionResult = processor.processInspectionResult( inspectionResult, {} );

						expect( inspectionResult.properties.foo.value ).toBe( '3' );
					} );
				} );

				it(
						"watches expressions and invalidates inspection results",
						function() {

							var inspectionCount = 0;
							var buildingCount = 0;

							var myApp = angular.module( 'test-app', [ 'metawidget' ] );
							var controller = myApp.controller( 'TestController', function( $scope ) {

								$scope.readOnlyz = true;

								$scope.foo = {
									edit: function() {

									},
									save: function() {

									}
								};

								$scope.metawidgetConfig = {
									inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

										inspectionCount++;

										return {
											properties: {
												edit: {
													"hidden": "{{!readOnlyz}}"
												},
												save: {
													"hidden": "{{readOnlyz}}"
												}
											}
										};
									} ] ),
									appendWidgetProcessors: [ function( widget, attributes, mw ) {

										buildingCount++;
										return widget;
									} ]
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

										expect( mw.innerHTML )
												.toBe(
														'<table id="table-foo"><tbody><tr id="table-fooEdit-row"><th id="table-fooEdit-label-cell"/><td id="table-fooEdit-cell"><input type="button" value="Edit" id="fooEdit" ng-click="foo.edit()" class="ng-scope"/></td><td/></tr></tbody></table>' );

										expect( mw.innerHTML ).toContain( '<input type="button" value="Edit" id="fooEdit" ng-click="foo.edit()" class="ng-scope"/>' );
										expect( inspectionCount ).toBe( 1 );
										expect( buildingCount ).toBe( 2 );

										var scope = angular.element( body ).scope();
										scope.readOnlyz = false;
										scope.$digest();

										expect( mw.innerHTML ).toNotContain( 'fooEdit' );
										expect( mw.innerHTML ).toContain( '<input type="button" value="Save" id="fooSave" ng-click="foo.save()" class="ng-scope"/>' );
										expect( inspectionCount ).toBe( 2 );
										expect( buildingCount ).toBe( 4 );

										scope.readOnlyz = true;
										scope.$digest();

										expect( mw.innerHTML ).toNotContain( 'fooSave' );
										expect( mw.innerHTML ).toContain( '<input type="button" value="Edit" id="fooEdit" ng-click="foo.edit()" class="ng-scope"/>' );
										expect( inspectionCount ).toBe( 3 );
										expect( buildingCount ).toBe( 6 );
									} );
						} );
			} );

	describe( "The AngularWidgetProcessor", function() {

		it( "processes widgets and binds Angular models", function() {

			var injector = angular.bootstrap();

			injector.invoke( function( $compile, $parse, $rootScope ) {

				var scope = $rootScope.$new();
				var processor = new metawidget.angular.widgetprocessor.AngularWidgetProcessor( $parse, scope );
				var attributes = {
					name: "foo",
					required: "true",
					minLength: "3",
					maxLength: "97"
				};
				var readOnlyAttributes = {
					name: "foo",
					required: "true",
					minLength: "3",
					maxLength: "97",
					readOnly: "true"
				};
				var mw = {
					path: "testPath"
				};

				// Inputs

				var widget = document.createElement( 'input' );
				widget.setAttribute( 'maxlength', 97 );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-model' ) ).toBe( 'testPath.foo' );
				expect( widget.getAttribute( 'ng-required' ) ).toBe( 'true' );
				expect( widget.getAttribute( 'ng-minlength' ) ).toBe( '3' );
				expect( widget.getAttribute( 'ng-maxlength' ) ).toBe( '97' );
				expect( widget.getAttribute( 'maxlength' ) ).toBe( '97' );

				// Outputs

				var widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', readOnlyAttributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'testPath.foo' );
				expect( widget.getAttribute( 'ng-required' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-minlength' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-maxlength' ) ).toBe( null );

				// Textareas (same as inputs, not same as outputs)

				widget = document.createElement( 'textarea' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-model' ) ).toBe( 'testPath.foo' );

				// Button inputs

				attributes = {
					name: "bar"
				};
				widget = document.createElement( 'input' );
				widget.setAttribute( 'type', 'button' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-click' ) ).toBe( 'testPath.bar()' );
				expect( widget.getAttribute( 'ng-required' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-minlength' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-maxlength' ) ).toBe( null );

				// Submit input

				attributes = {
					name: "bar"
				};
				widget = document.createElement( 'input' );
				widget.setAttribute( 'type', 'submit' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-click' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-model' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-required' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-minlength' ) ).toBe( null );
				expect( widget.getAttribute( 'ng-maxlength' ) ).toBe( null );

				// Outputs

				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'testPath.bar' );

				attributes = {
					name: "bar",
					type: "array"
				};
				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( "testPath.bar.join(', ')" );

				// Manually override

				widget = document.createElement( 'output' );
				widget.setAttribute( 'ng-bind', 'foo' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( "foo" );

				// Date outputs

				attributes = {
					name: "bar",
					type: "date"
				};
				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( "testPath.bar|date" );

				// Masked

				attributes = {
					name: "bar",
					masked: true,
					type: "array"
				};
				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( "mwMaskedOutput(testPath.bar)" );
				expect( typeof ( scope.mwMaskedOutput ) ).toBe( 'function' );
				expect( scope.mwMaskedOutput() ).toBeUndefined();
				expect( scope.mwMaskedOutput( '' ) ).toBe( '' );
				expect( scope.mwMaskedOutput( 'Foo1' ) ).toBe( '****' );

				// Enums

				attributes = {
					name: "enumBaz",
					enum: [ "1", "2", "3" ]
				};
				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'testPath.enumBaz' );

				attributes = {
					name: "enumTitleBaz",
					enum: [ "1", "2", 3 ],
					enumTitles: [ "One", "Two", "Three" ]
				};
				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'property', attributes, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'mwLookupEnumTitle["testPath.enumTitleBaz"](testPath.enumTitleBaz)' );
				expect( typeof ( scope.mwLookupEnumTitle['testPath.enumTitleBaz'] ) ).toBe( 'function' );
				scope.$parent.testPath = {
					enumTitleBaz: '2'
				};
				expect( scope.mwLookupEnumTitle['testPath.enumTitleBaz']( scope.$parent.testPath.enumTitleBaz ) ).toBe( 'Two' );
				scope.$parent.testPath = {
					enumTitleBaz: 3
				};
				expect( scope.mwLookupEnumTitle['testPath.enumTitleBaz']( scope.$parent.testPath.enumTitleBaz ) ).toBe( 'Three' );
				scope.$parent.testPath = {
					enumTitleBaz: '3'
				};
				expect( scope.mwLookupEnumTitle['testPath.enumTitleBaz']( scope.$parent.testPath.enumTitleBaz ) ).toBe( '3' );

				// Root-level

				widget = document.createElement( 'output' );
				processor.processWidget( widget, 'entity', {}, mw );
				expect( widget.getAttribute( 'ng-bind' ) ).toBe( 'testPath' );
			} );
		} );
	} );
} )();