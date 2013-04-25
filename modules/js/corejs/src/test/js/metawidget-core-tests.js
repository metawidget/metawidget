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

	describe( "The core Metawidget", function() {

		it( "populates itself with widgets to match the properties of business objects", function() {

			// Defaults

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );

			mw.toInspect = {
				foo: "Foo"
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].value ).toBe( 'Foo' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );

			// Configured

			var element = document.createElement( 'div' );
			mw = new metawidget.Metawidget( element, {
				layout: new metawidget.layout.SimpleLayout()
			} );

			mw.toInspect = {
				bar: "Bar"
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'input type="text" id="bar" name="bar"' );
			expect( element.childNodes[0].value ).toBe( 'Bar' );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "defensively copies overridden widgets", function() {

			var element = document.createElement( 'div' );
			var bar = document.createElement( 'span' );
			bar.setAttribute( 'id', 'bar' );
			element.appendChild( bar );
			var baz = document.createElement( 'span' );
			baz.setAttribute( 'id', 'baz' );
			element.appendChild( baz );

			var mw = new metawidget.Metawidget( element );
			mw.toInspect = {
				foo: "Foo",
				bar: "Bar"
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].value ).toBe( 'Foo' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-bar-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-bar-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="table-bar-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Bar:' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-bar-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'span id="bar"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( element.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'span id="baz"' );
			expect( element.childNodes[0].childNodes[0].childNodes[2].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[2].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );

			expect( mw.overriddenNodes.length ).toBe( 0 );
			mw.overriddenNodes.push( document.createElement( 'defensive' ) );
			expect( mw.overriddenNodes.length ).toBe( 1 );
			mw.buildWidgets();
			expect( mw.overriddenNodes.length ).toBe( 0 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		} );

		it( "can be used purely for layout", function() {

			var element = document.createElement( 'div' );
			var bar = document.createElement( 'span' );
			bar.setAttribute( 'id', 'bar' );
			element.appendChild( bar );
			var baz = document.createElement( 'span' );
			baz.setAttribute( 'id', 'baz' );
			element.appendChild( baz );
			var ignore = document.createTextNode( 'ignore' );
			element.appendChild( ignore );

			var mw = new metawidget.Metawidget( element );
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'span id="bar"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'span id="baz"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "ignores embedded text nodes", function() {

			var element = document.createElement( 'div' );
			element.appendChild( document.createTextNode( 'text1' ) );
			element.appendChild( document.createElement( 'span' ) );
			element.appendChild( document.createTextNode( 'text2' ) );
			var mw = new metawidget.Metawidget( element );
			mw.onEndBuild = function() {

				// Do not clean up overriddenNodes
			};
			mw.buildWidgets();

			expect( mw.overriddenNodes[0].toString() ).toBe( 'span' );
			expect( mw.overriddenNodes.length ).toBe( 1 );
		} );

		it( "builds nested Metawidgets", function() {

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );

			mw.toInspect = {
				foo: {
					nestedFoo: "Foo"
				}
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div id="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'table id="table-foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-fooNestedFoo-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe(
					'th id="table-fooNestedFoo-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe(
					'label for="fooNestedFoo" id="table-fooNestedFoo-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML )
					.toBe( 'Nested Foo:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe(
					'td id="table-fooNestedFoo-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe(
					'input type="text" id="fooNestedFoo" name="fooNestedFoo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].value ).toBe( 'Foo' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "guards against infinite recursion", function() {

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: function( toInspect, type, names ) {

					return {
						properties: {
							foo: {}
						}
					};
				}
			} );
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );

			var childNode = element.childNodes[0].childNodes[0];
			var idMiddle = '';

			for ( var loop = 0; loop < 10; loop++ ) {

				expect( childNode.childNodes[0].toString() ).toBe( 'tr id="table-foo' + idMiddle + '-row"' );
				expect( childNode.childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo' + idMiddle + '-label-cell"' );
				expect( childNode.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo' + idMiddle + '" id="table-foo' + idMiddle + '-label"' );
				expect( childNode.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
				expect( childNode.childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo' + idMiddle + '-cell"' );
				expect( childNode.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div id="foo' + idMiddle + '"' );
				expect( childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'table id="table-foo' + idMiddle + '"' );
				expect( childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
				expect( childNode.childNodes[0].childNodes.length ).toBe( 3 );
				expect( childNode.childNodes.length ).toBe( 1 );

				idMiddle += 'Foo';
				childNode = childNode.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0];
			}

			expect( childNode.childNodes.length ).toBe( 0 );

			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "calls events on its configured components", function() {

			var called = [];

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: {
					inspect: function() {

						called.push( 'inspector.inspect' );
						return {};
					}
				},
				inspectionResultProcessors: [ {
					processInspectionResult: function() {

						called.push( 'inspectionResultProcessor.processInspectionResult' );
						return {};
					}
				} ],
				addInspectionResultProcessors: [ {
					processInspectionResult: function() {

						called.push( 'addedInspectionResultProcessor.processInspectionResult' );
						return {
							properties: {
								foo: "string"
							}
						};
					}
				} ],
				widgetBuilder: {
					onStartBuild: function() {

						called.push( 'widgetBuilder.onStartBuild' );
					},
					buildWidget: function() {

						called.push( 'widgetBuilder.buildWidget' );
						return document.createElement( 'span' );
					},
					onEndBuild: function() {

						called.push( 'widgetBuilder.onEndBuild' );
					}
				},
				widgetProcessors: [ {
					onStartBuild: function() {

						called.push( 'widgetProcessor.onStartBuild' );
					},
					processWidget: function( widget ) {

						called.push( 'widgetProcessor.processWidget' );
						return widget;
					},
					onEndBuild: function() {

						called.push( 'widgetProcessor.onEndBuild' );
					}
				} ],
				prependWidgetProcessors: [ {
					onStartBuild: function() {

						called.push( 'prependedWidgetProcessor1.onStartBuild' );
					},
					processWidget: function( widget ) {

						called.push( 'prependedWidgetProcessor1.processWidget' );
						return widget;
					},
					onEndBuild: function() {

						called.push( 'prependedWidgetProcessor1.onEndBuild' );
					}
				}, {
					onStartBuild: function() {

						called.push( 'prependedWidgetProcessor2.onStartBuild' );
					},
					processWidget: function( widget ) {

						called.push( 'prependedWidgetProcessor2.processWidget' );
						return widget;
					},
					onEndBuild: function() {

						called.push( 'prependedWidgetProcessor2.onEndBuild' );
					}
				} ],
				addWidgetProcessors: [ {
					onStartBuild: function() {

						called.push( 'addedWidgetProcessor1.onStartBuild' );
					},
					processWidget: function( widget ) {

						called.push( 'addedWidgetProcessor1.processWidget' );
						return widget;
					},
					onEndBuild: function() {

						called.push( 'addedWidgetProcessor1.onEndBuild' );
					}
				}, {
					onStartBuild: function() {

						called.push( 'addedWidgetProcessor2.onStartBuild' );
					},
					processWidget: function( widget ) {

						called.push( 'addedWidgetProcessor2.processWidget' );
						return widget;
					},
					onEndBuild: function() {

						called.push( 'addedWidgetProcessor2.onEndBuild' );
					}
				} ],
				layout: {
					onStartBuild: function() {

						called.push( 'layout.onStartBuild' );
					},
					startContainerLayout: function() {

						called.push( 'layout.startContainerLayout' );
					},
					layoutWidget: function() {

						called.push( 'layout.layoutWidget' );
					},
					endContainerLayout: function() {

						called.push( 'layout.endContainerLayout' );
					},
					onEndBuild: function() {

						called.push( 'layout.onEndBuild' );
					}
				}
			} );

			mw.buildWidgets();

			expect( called[0] ).toBe( 'inspector.inspect' );
			expect( called[1] ).toBe( 'inspectionResultProcessor.processInspectionResult' );
			expect( called[2] ).toBe( 'addedInspectionResultProcessor.processInspectionResult' );
			expect( called[3] ).toBe( 'widgetBuilder.onStartBuild' );
			expect( called[4] ).toBe( 'prependedWidgetProcessor1.onStartBuild' );
			expect( called[5] ).toBe( 'prependedWidgetProcessor2.onStartBuild' );
			expect( called[6] ).toBe( 'widgetProcessor.onStartBuild' );
			expect( called[7] ).toBe( 'addedWidgetProcessor1.onStartBuild' );
			expect( called[8] ).toBe( 'addedWidgetProcessor2.onStartBuild' );
			expect( called[9] ).toBe( 'layout.onStartBuild' );
			expect( called[10] ).toBe( 'layout.startContainerLayout' );
			expect( called[11] ).toBe( 'widgetBuilder.buildWidget' );
			expect( called[12] ).toBe( 'prependedWidgetProcessor1.processWidget' );
			expect( called[13] ).toBe( 'prependedWidgetProcessor2.processWidget' );
			expect( called[14] ).toBe( 'widgetProcessor.processWidget' );
			expect( called[15] ).toBe( 'addedWidgetProcessor1.processWidget' );
			expect( called[16] ).toBe( 'addedWidgetProcessor2.processWidget' );
			expect( called[17] ).toBe( 'layout.layoutWidget' );
			expect( called[18] ).toBe( 'layout.endContainerLayout' );
			expect( called[19] ).toBe( 'layout.onEndBuild' );
			expect( called[20] ).toBe( 'prependedWidgetProcessor1.onEndBuild' );
			expect( called[21] ).toBe( 'prependedWidgetProcessor2.onEndBuild' );
			expect( called[22] ).toBe( 'widgetProcessor.onEndBuild' );
			expect( called[23] ).toBe( 'addedWidgetProcessor1.onEndBuild' );
			expect( called[24] ).toBe( 'addedWidgetProcessor2.onEndBuild' );
			expect( called[25] ).toBe( 'widgetBuilder.onEndBuild' );

			expect( called.length ).toBe( 26 );
		} );

		it( "will stop the build if the inspection returns null", function() {

			// Normal

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );
			mw.toInspect = {
				foo: "bar"
			};

			mw.buildWidgets();
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );

			// With null InspectionResultProcessor

			element = document.createElement( 'div' );
			mw = new metawidget.Metawidget( element, {
				inspectionResultProcessors: [ function() {

				} ]
			} );

			mw.buildWidgets();
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );

			// With null Inspectior

			element = document.createElement( 'div' );
			mw = new metawidget.Metawidget( element, {
				inspector: function() {

				},
				inspectionResultProcessors: [ function() {

					throw new Error( 'Should not reach' );
				} ]
			} );

			mw.buildWidgets();
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );
		} );

		it( "defensively copies attributes", function() {

			var inspectionResult = {
				properties: {
					prop1: {
						"foo": "bar"
					}
				}
			};

			var widgetBuilt = 0;
			var sawReadOnly = 0;
			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				widgetBuilder: function( elementName, attributes, mw ) {

					attributes.foo = 'baz';
					widgetBuilt++;

					if ( metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
						sawReadOnly++;
					}

					return document.createElement( 'span' );
				}
			} );
			mw.readOnly = true;

			mw.buildWidgets( inspectionResult );
			expect( inspectionResult.properties.prop1.name ).toBeUndefined();
			expect( inspectionResult.properties.prop1.foo ).toBe( "bar" );
			expect( widgetBuilt ).toBe( 1 );
			expect( sawReadOnly ).toBe( 1 );

			// root nodes too

			inspectionResult = {
				foo: "bar"
			};

			mw.buildWidgets( inspectionResult );
			expect( inspectionResult.foo ).toBe( "bar" );
			expect( widgetBuilt ).toBe( 2 );
			expect( sawReadOnly ).toBe( 2 );
		} );

		it( "inspects from parent", function() {

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );
			mw.toInspect = {
				bar: "Bar"
			};
			mw.path = 'foo.bar';

			mw.buildWidgets();
			expect( element.childNodes[0].toString() ).toBe( 'table id="table-fooBar"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-fooBar-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-fooBar-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="fooBar" id="table-fooBar-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Bar:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-fooBar-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="fooBar" name="fooBar"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].value ).toBe( 'Bar' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "supports stubs with their own metadata", function() {

			var element = document.createElement( 'div' );
			var stub = document.createElement( 'stub' );
			stub.setAttribute( 'title', 'Foo' );
			stub.appendChild( document.createElement( 'input' ) );
			element.appendChild( stub );

			// TableLayout

			var mw = new metawidget.Metawidget( element );
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'stub title="Foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'input' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );

			// DivLayout

			element = document.createElement( 'div' );
			element.appendChild( stub );
			mw = new metawidget.Metawidget( element, {
				layout: new metawidget.layout.DivLayout()
			} );
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo:' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'stub title="Foo"' );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "handles falsy values gracefully", function() {

			// These values should produce an empty Metawidget

			testFalsy( undefined );
			testFalsy( null );
			testFalsy( {} );
			testFalsy( [] );

			function testFalsy( falsyValue ) {

				var element = document.createElement( 'div' );
				var mw = new metawidget.Metawidget( element );

				mw.toInspect = falsyValue;
				mw.buildWidgets();

				expect( element.childNodes[0].toString() ).toBe( 'table' );
				expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
				expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );
				expect( element.childNodes[0].childNodes.length ).toBe( 1 );
				expect( element.childNodes.length ).toBe( 1 );
			}

			// These values should produce a primitive Metawidget

			testNotFalsy( '' );
			testNotFalsy( 0 );
			testNotFalsy( NaN );
			testNotFalsy( false );

			function testNotFalsy( nonFalsyValue ) {

				var element = document.createElement( 'div' );
				var mw = new metawidget.Metawidget( element );

				mw.toInspect = nonFalsyValue;
				mw.buildWidgets();

				expect( element.childNodes[0].toString() ).toBe( 'table' );
				expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
				expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
				expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2"' );

				var widget = element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0];
				expect( widget.toString() ).toContain( 'input type="' );

				if ( widget.toString().indexOf( 'checkbox' ) !== -1 ) {
					expect( widget.checked ).toBe( nonFalsyValue );
				} else if ( nonFalsyValue + '' !== 'NaN' ) {
					expect( widget.value ).toBe( nonFalsyValue );
				}

				expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
				expect( element.childNodes[0].childNodes.length ).toBe( 1 );
				expect( element.childNodes.length ).toBe( 1 );
			}
		} );

		it( "supports arrays of configs", function() {

			var called = [];

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, [ {
				inspector: {
					inspect: function() {

						called.push( 'inspector.inspect' );
						return {
							properties: {
								foo: {
									type: "string"
								}
							}
						};
					}
				}
			}, {
				layout: {
					layoutWidget: function() {

						called.push( 'layout.layoutWidget' );
					}
				}
			} ] );

			mw.buildWidgets();

			expect( called[0] ).toBe( 'inspector.inspect' );
			expect( called[1] ).toBe( 'layout.layoutWidget' );

			expect( called.length ).toBe( 2 );
		} );

		it( "sorts properties by propertyOrder", function() {

			// Test sorting in reverse

			var element = document.createElement( 'div' );
			var toInspect = {
				baz: "Baz",
				bar: "Bar",
				foo: "Foo"
			};

			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							"baz": {
								"propertyOrder": 3,
								"type": "string"
							},
							"bar": {
								"propertyOrder": 2,
								"type": "string"
							},
							"foo": {
								"propertyOrder": 1,
								"type": "string"
							}
						}
					}
				},

				layout: new metawidget.layout.SimpleLayout()
			} );
			mw.toInspect = toInspect;
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[1].toString() ).toBe( 'input type="text" id="bar" name="bar"' );
			expect( element.childNodes[2].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( element.childNodes.length ).toBe( 3 );

			// Test a different sort in case VM coincidentally sorts in reverse

			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							"baz": {
								"propertyOrder": 2,
								"type": "string"
							},
							"bar": {
								"propertyOrder": 3,
								"type": "string"
							},
							"foo": {
								"propertyOrder": 1,
								"type": "string"
							}
						}
					}
				},

				layout: new metawidget.layout.SimpleLayout()
			} );
			mw.toInspect = toInspect;
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[1].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( element.childNodes[2].toString() ).toBe( 'input type="text" id="bar" name="bar"' );
			expect( element.childNodes.length ).toBe( 3 );
		} );

		it( "supports localization", function() {

			// Defaults

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );

			mw.toInspect = {
				foo: "",
				fooAction: function() {

				}
			};
			mw.l10n = {
				foo: "Foo Label",
				fooAction: "Foo Action Label"
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-foo-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-foo-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="table-foo-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo Label:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-foo-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-fooAction-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-fooAction-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes.length ).toBe( 0 );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-fooAction-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'button id="fooAction"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].innerHTML ).toBe( 'Foo Action Label' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "guards against infinite loops", function() {

			var element = document.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, type, names ) {

					mw.buildWidgets( undefined );
				} ]
			} );
			mw.toInspect = {};

			try {
				mw.buildWidgets();
				expect( true ).toBe( false );
			} catch ( e ) {
				expect( e.message ).toBe( "Calling buildWidgets( undefined ) may cause infinite loop. Check your argument, or pass no arguments instead" );
			}
		} );
	} );
} )();