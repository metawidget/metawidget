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
		var mw = new metawidget.Metawidget( element );

		var bar = document.createElement( 'span' );
		bar.setAttribute( 'id', 'bar' );
		mw._overriddenNodes = [ bar ];

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
		expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
		expect( element.childNodes[0].childNodes.length ).toBe( 1 );
		expect( element.childNodes.length ).toBe( 1 );

		expect( mw._overriddenNodes.length ).toBe( 1 );
		expect( mw.overriddenNodes.length ).toBe( 1 );
		mw.overriddenNodes.push( "defensive" );
		expect( mw.overriddenNodes.length ).toBe( 2 );
		expect( mw._overriddenNodes.length ).toBe( 1 );
	} );
	
	it( "ignores embedded text nodes", function() {

		var element = document.createElement( 'div' );
		element.appendChild( document.createTextNode( 'text1' ) );
		element.appendChild( document.createElement( 'span' ) );
		element.appendChild( document.createTextNode( 'text2' ) );
		var mw = new metawidget.Metawidget( element );

		expect( mw._overriddenNodes[0].toString() ).toBe( 'span' );
		expect( mw._overriddenNodes.length ).toBe( 1 );
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
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Nested Foo:' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe(
				'td id="table-fooNestedFoo-cell"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe(
				'input type="text" id="fooNestedFoo" name="fooNestedFoo"' );
		expect( element.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].value ).toBe(
		'Foo' );
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

				return [ {
					name: "foo"
				} ];
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

	it( "calls onStartBuild and onEndBuild", function() {

		var called = [];

		var element = document.createElement( 'div' );
		var mw = new metawidget.Metawidget( element, {
			widgetBuilder: {
				onStartBuild: function( mw ) {

					called.push( 'widgetBuilder.onStartBuild( ' + mw + ' )' );
				},
				onEndBuild: function( mw ) {

					called.push( 'widgetBuilder.onEndBuild( ' + mw + ' )' );
				}
			},
			widgetProcessors: [ {
				onStartBuild: function( mw ) {

					called.push( 'widgetProcessor.onStartBuild( ' + mw + ' )' );
				},
				onEndBuild: function( mw ) {

					called.push( 'widgetProcessor.onEndBuild( ' + mw + ' )' );
				}
			} ],
			layout: {
				onStartBuild: function( mw ) {

					called.push( 'layout.onStartBuild( ' + mw + ' )' );
				},
				startContainerLayout: function( element, mw ) {

					called.push( 'layout.startContainerLayout( ' + element + ', ' + mw + ' )' );
				},
				endContainerLayout: function( element, mw ) {

					called.push( 'layout.endContainerLayout( ' + element + ', ' + mw + ' )' );
				},
				onEndBuild: function( mw ) {

					called.push( 'layout.onEndBuild( ' + mw + ' )' );
				}
			}
		} );

		mw.buildWidgets();

		expect( called[0] ).toBe( 'widgetBuilder.onStartBuild( [object Object] )' );
		expect( called[1] ).toBe( 'widgetProcessor.onStartBuild( [object Object] )' );
		expect( called[2] ).toBe( 'layout.onStartBuild( [object Object] )' );
		expect( called[3] ).toBe( 'layout.startContainerLayout( div, [object Object] )' );
		expect( called[4] ).toBe( 'layout.endContainerLayout( div, [object Object] )' );
		expect( called[5] ).toBe( 'layout.onEndBuild( [object Object] )' );
		expect( called[6] ).toBe( 'widgetProcessor.onEndBuild( [object Object] )' );
		expect( called[7] ).toBe( 'widgetBuilder.onEndBuild( [object Object] )' );
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
} );
