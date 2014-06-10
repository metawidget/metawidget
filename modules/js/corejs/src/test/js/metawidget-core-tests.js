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

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

( function() {

	'use strict';

	describe( "The core Metawidget", function() {

		it( "populates itself with widgets to match the properties of domain objects", function() {

			var firedBuildEndEvent = 0;
			
			// Defaults

			var element = simpleDocument.createElement( 'div' );
			
			element.addEventListener( 'buildEnd', function() {
				firedBuildEndEvent++;
			} );
			
			var mw = new metawidget.Metawidget( element );

			expect( mw.getElement() ).toBe( element );
			expect( element.getMetawidget() ).toBe( mw );
			
			mw.toInspect = {
				foo: "Foo"
			};
			mw.buildWidgets();
			
			expect( firedBuildEndEvent ).toBe( 1 );
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

			mw.buildWidgets();
			expect( firedBuildEndEvent ).toBe( 2 );

			// Configured

			var element = simpleDocument.createElement( 'div' );
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

		it( "supports collections", function() {

			// Direct collection

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );

			mw.toInspect = [ {
				name: "Foo",
				description: "A Foo"
			}, {
				name: "Bar",
				description: "A Bar"
			} ];
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			var table = element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0];

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

			// Collection as property

			element = simpleDocument.createElement( 'div' );
			mw = new metawidget.Metawidget( element );
			mw.toInspect = {
				collection: [ {
					name: "Foo",
					description: "A Foo"
				}, {
					name: "Bar",
					description: "A Bar"
				} ]
			};
			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'table' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-collection-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-collection-label-cell"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="collection" id="table-collection-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Collection:' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-collection-cell"' );
			table = element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0];

			expect( table.toString() ).toBe( 'table id="collection"' );
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

		it( "defensively copies overridden widgets", function() {

			var element = simpleDocument.createElement( 'div' );
			var bar = simpleDocument.createElement( 'span' );
			bar.setAttribute( 'id', 'bar' );
			element.appendChild( bar );
			var baz = simpleDocument.createElement( 'span' );
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
			mw.overriddenNodes.push( simpleDocument.createElement( 'defensive' ) );
			expect( mw.overriddenNodes.length ).toBe( 1 );
			mw.buildWidgets();
			expect( mw.overriddenNodes.length ).toBe( 0 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
		} );

		it( "can be used purely for layout", function() {

			var element = simpleDocument.createElement( 'div' );
			var bar = simpleDocument.createElement( 'span' );
			bar.setAttribute( 'id', 'bar' );
			element.appendChild( bar );
			var baz = simpleDocument.createElement( 'span' );
			baz.setAttribute( 'id', 'baz' );
			element.appendChild( baz );
			var ignore = simpleDocument.createTextNode( 'ignore' );
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

			var element = simpleDocument.createElement( 'div' );
			element.appendChild( simpleDocument.createTextNode( 'text1' ) );
			element.appendChild( simpleDocument.createElement( 'span' ) );
			element.appendChild( simpleDocument.createTextNode( 'text2' ) );
			var mw = new metawidget.Metawidget( element );
			mw.onEndBuild = function() {

				// Do not clean up overriddenNodes
			};
			mw.buildWidgets();

			expect( mw.overriddenNodes[0].toString() ).toBe( 'span' );
			expect( mw.overriddenNodes.length ).toBe( 1 );
		} );

		it( "builds nested Metawidgets", function() {

			var element = simpleDocument.createElement( 'div' );
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

			var element = simpleDocument.createElement( 'div' );
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

			var element = simpleDocument.createElement( 'div' );
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
						return simpleDocument.createElement( 'span' );
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

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element );
			mw.toInspect = {
				foo: "bar"
			};

			mw.buildWidgets();
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );

			// With null InspectionResultProcessor

			element = simpleDocument.createElement( 'div' );
			mw = new metawidget.Metawidget( element, {
				inspectionResultProcessors: [ function() {

				} ]
			} );

			mw.buildWidgets();
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );

			// With null Inspectior

			element = simpleDocument.createElement( 'div' );
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
			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				widgetBuilder: function( elementName, attributes, mw ) {

					attributes.foo = 'baz';
					widgetBuilt++;

					if ( metawidget.util.isTrueOrTrueString( attributes.readOnly ) ) {
						sawReadOnly++;
					}

					return simpleDocument.createElement( 'span' );
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

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: new metawidget.inspector.JsonSchemaInspector( {
					properties: {
						bar: {
							type: 'string',
							required: true
						}
					}
				} )
			} );
			mw.toInspect = {
				bar: "Bar"
			};
			mw.path = 'foo.bar';

			mw.buildWidgets();
			expect( element.childNodes[0].toString() ).toBe( 'table id="table-fooBar"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-fooBar-row"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td id="table-fooBar-cell" colspan="2"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="fooBar" required="required" name="fooBar"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].value ).toBe( 'Bar' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].innerHTML ).toBe( '*' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "supports stubs with their own metadata", function() {

			var element = simpleDocument.createElement( 'div' );
			var stub = simpleDocument.createElement( 'stub' );
			stub.setAttribute( 'title', 'Foo' );
			stub.appendChild( simpleDocument.createElement( 'input' ) );
			element.appendChild( stub );
			
			// (test childAttributes don't bleed into next component)
			
			var div = simpleDocument.createElement( 'div' );
			div.appendChild( simpleDocument.createElement( 'input' ) );
			element.appendChild( div );

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
			expect( element.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );

			// DivLayout

			element = simpleDocument.createElement( 'div' );
			element.appendChild( stub );
			element.appendChild( div );
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
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[1].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 2 );
		} );

		it( "handles falsy values gracefully", function() {

			// These values should produce an empty Metawidget

			testFalsy( undefined );
			testFalsy( null );
			testFalsy( {} );

			function testFalsy( falsyValue ) {

				var element = simpleDocument.createElement( 'div' );
				var mw = new metawidget.Metawidget( element );

				mw.toInspect = falsyValue;
				mw.buildWidgets();

				expect( element.childNodes[0].toString() ).toBe( 'table' );
				expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
				expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );
				expect( element.childNodes[0].childNodes.length ).toBe( 1 );
				expect( element.childNodes.length ).toBe( 1 );
			}

			// These values should not produce a primitive Metawidget

			testNotFalsy( '' );
			testNotFalsy( 0 );
			testNotFalsy( NaN );
			testNotFalsy( false );
			testNotFalsy( [] );

			function testNotFalsy( nonFalsyValue ) {

				var element = simpleDocument.createElement( 'div' );
				var mw = new metawidget.Metawidget( element );

				mw.toInspect = nonFalsyValue;
				mw.buildWidgets();

				expect( element.childNodes[0].toString() ).toBe( 'table' );
				expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
				expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
				expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2"' );

				var widget = element.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0];

				if ( widget.toString().indexOf( 'table' ) !== -1 ) {
					expect( widget.childNodes[0].toString() ).toBe( 'tbody' );
					expect( widget.childNodes.length ).toBe( 1 );
					expect( widget.childNodes[0].childNodes.length ).toBe( 0 );
				} else {
					expect( widget.toString() ).toContain( 'input type="' );

					if ( widget.toString().indexOf( 'checkbox' ) !== -1 ) {
						expect( widget.checked ).toBe( nonFalsyValue );
					} else if ( nonFalsyValue + '' !== 'NaN' ) {
						expect( widget.value ).toBe( nonFalsyValue );
					}
				}

				expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
				expect( element.childNodes[0].childNodes.length ).toBe( 1 );
				expect( element.childNodes.length ).toBe( 1 );
			}
		} );

		it( "supports arrays of configs", function() {

			var called = [];

			var element = simpleDocument.createElement( 'div' );
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

			var element = simpleDocument.createElement( 'div' );
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

			var element = simpleDocument.createElement( 'div' );
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
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="button" value="Foo Action Label" id="fooAction"' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( element.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "guards against infinite loops", function() {

			var element = simpleDocument.createElement( 'div' );
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