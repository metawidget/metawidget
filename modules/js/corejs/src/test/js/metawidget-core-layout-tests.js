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

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

( function() {

	'use strict';

	describe( "The SimpleLayout", function() {

		it( "appends children to the container", function() {

			var layout = new metawidget.layout.SimpleLayout();

			var widget1 = simpleDocument.createElement( 'widget1' );
			var widget2 = simpleDocument.createElement( 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );

			layout.layoutWidget( widget1, "property", {}, container );
			layout.layoutWidget( widget2, "property", {}, container );

			expect( container.childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[1] ).toBe( widget2 );
			expect( container.childNodes.length ).toBe( 2 );
		} );

		it( "ignores empty stubs", function() {

			var layout = new metawidget.layout.SimpleLayout();

			var stub = simpleDocument.createElement( 'stub' );
			var widget1 = simpleDocument.createElement( 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );

			layout.layoutWidget( stub, "property", {}, container );
			layout.layoutWidget( widget1, "property", {}, container );

			expect( container.childNodes[0] ).toBe( widget1 );
			expect( container.childNodes.length ).toBe( 1 );

			stub.appendChild( simpleDocument.createElement( 'widget2' ) );

			layout.layoutWidget( stub, "property", {}, container );

			expect( container.childNodes[1] ).toBe( stub );
			expect( container.childNodes.length ).toBe( 2 );
		} );
	} );

	describe( "The DivLayout", function() {

		it( "arranges widgets using divs", function() {

			var layout = new metawidget.layout.DivLayout( {
				divStyleClasses: [ 'outerStyle', 'labelStyle', 'widgetStyle' ]
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				path: "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );
			layout.layoutWidget( widget2, "property", {
				name: "widget2",
				title: "widgetLabel 2"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div class="outerStyle"' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div class="labelStyle"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div class="widgetStyle"' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[1].toString() ).toBe( 'div class="outerStyle"' );
			expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'div class="labelStyle"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[1].childNodes[1].toString() ).toBe( 'div class="widgetStyle"' );
			expect( container.childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[1].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 2 );
		} );

		it( "ignores empty stubs", function() {

			var layout = new metawidget.layout.DivLayout();

			var stub = simpleDocument.createElement( 'stub' );
			var widget1 = simpleDocument.createElement( 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( stub, "property", {}, container, mw );
			layout.layoutWidget( widget1, "property", {}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );

			stub.appendChild( simpleDocument.createElement( 'widget2' ) );

			layout.layoutWidget( stub, "property", {}, container, mw );

			expect( container.childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[1].childNodes[0].childNodes[0] ).toBe( stub );
			expect( container.childNodes[1].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 2 );
		} );

		it( "supports alternate label suffixes", function() {

			var layout = new metawidget.layout.DivLayout( {
				labelSuffix: '#'
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			}

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1#' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports empty labels", function() {

			var layout = new metawidget.layout.DivLayout();
			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
				title: ''
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports empty label suffixes", function() {

			var layout = new metawidget.layout.DivLayout( {
				labelSuffix: ''
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports partial CSS classes", function() {

			var layout = new metawidget.layout.DivLayout( {
				divStyleClasses: [ undefined, 'labelStyle' ]
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				path: "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );
			layout.layoutWidget( widget2, "property", {
				name: "widget2",
				title: "widgetLabel 2"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div class="labelStyle"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'div class="labelStyle"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[1].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[1].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 2 );
		} );

		it( "suppresses labels for buttons", function() {

			var layout = new metawidget.layout.DivLayout();

			var button1 = simpleDocument.createElement( 'input' );
			button1.setAttribute( 'type', 'button' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( button1, "action", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( button1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );

			container.removeChild( container.childNodes[0] );
			var button2 = simpleDocument.createElement( 'input' );
			button2.setAttribute( 'type', 'submit' );

			layout.layoutWidget( button2, "action", {
				name: "widget2"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( button2 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "suppresses labels for entities", function() {

			var layout = new metawidget.layout.DivLayout();

			var input = simpleDocument.createElement( 'input' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( input, "entity", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( input );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "allows label overrides", function() {

			var layout = new metawidget.layout.DivLayout();
			layout.getLabelString = function() {

				return "Foo@";
			}

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			}

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Foo@' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "can suppress label suffixes on checkboxes", function() {

			var layout = new metawidget.layout.DivLayout( {
				suppressLabelSuffixOnCheckboxes: true
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'type', 'checkbox' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			}

			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "collapses buttons into the same div", function() {

			var layout = new metawidget.layout.DivLayout();

			var button1 = simpleDocument.createElement( 'input' );
			button1.setAttribute( 'type', 'button' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( button1, "action", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( button1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );

			var button2 = simpleDocument.createElement( 'input' );
			button2.setAttribute( 'type', 'submit' );

			layout.layoutWidget( button2, "action", {
				name: "widget2"
			}, container, mw );

			expect( container.childNodes[0].childNodes[0].childNodes[1] ).toBe( button2 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports appendRequiredClassOnLabelDiv", function() {

			var layout = new metawidget.layout.DivLayout( {
				appendRequiredClassOnLabelDiv: 'fooBar'
			} );

			var input = simpleDocument.createElement( 'input' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( input, "property", {
				name: "widget1",
				required: true
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div class="fooBar"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( input );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports appendRequiredClassOnWidgetDiv", function() {

			var layout = new metawidget.layout.DivLayout( {
				appendRequiredClassOnWidgetDiv: 'fooBar'
			} );

			var input = simpleDocument.createElement( 'input' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( input, "property", {
				name: "widget1",
				required: true
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div class="fooBar"' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( input );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );
	} );

	describe( "The DefinitionListLayout", function() {

		it( "arranges widgets using dl tags", function() {

			var layout = new metawidget.layout.DefinitionListLayout( {
				labelStyleClass: 'labelStyle',
				labelSuffix: '#'
			});

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				path: "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );
			layout.layoutWidget( widget2, "property", {
				name: "widget2",
				title: "widgetLabel 2"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'dl id="dl-testPath"' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'dt' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" class="labelStyle"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1#' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'dd' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[2].toString() ).toBe( 'dt' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'label for="widget2" class="labelStyle"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].innerHTML ).toBe( 'widgetLabel 2#' );
			expect( container.childNodes[0].childNodes[3].toString() ).toBe( 'dd' );
			expect( container.childNodes[0].childNodes[3].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[0].childNodes.length ).toBe( 4 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "ignores empty stubs", function() {

			var layout = new metawidget.layout.DefinitionListLayout();

			var stub = simpleDocument.createElement( 'stub' );
			var widget1 = simpleDocument.createElement( 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( stub, "property", {}, container, mw );
			layout.layoutWidget( widget1, "property", {}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'dl' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'dd' );
			expect( container.childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );

			stub.appendChild( simpleDocument.createElement( 'widget2' ) );

			layout.layoutWidget( stub, "property", {}, container, mw );

			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'dd' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( stub );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );
	} );

	describe( "The TableLayout", function() {

		it( "arranges widgets in a table", function() {

			var layout = new metawidget.layout.TableLayout();

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				"name": "widget1",
				"required": "true"
			}, container, mw );
			layout.layoutWidget( widget2, "property", {
				"name": "widget2",
				"title": "widgetLabel 2"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table id="table-testPath"' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-testPathWidget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].innerHTML ).toBe( '*' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].innerHTML ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports facets", function() {

			var layout = new metawidget.layout.TableLayout( {
				"headerStyleClass": "testHeaderStyleClass",
				"footerStyleClass": "testFooterStyleClass"
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );

			var container = simpleDocument.createElement( 'metawidget' );

			var header = simpleDocument.createElement( 'facet' );
			header.setAttribute( 'name', 'header' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			header.appendChild( widget2 );

			var footer = simpleDocument.createElement( 'facet' );
			footer.setAttribute( 'name', 'footer' );
			var widget3 = simpleDocument.createElement( 'input' );
			widget3.setAttribute( 'id', 'widget3' );
			footer.appendChild( widget3 );

			var mw = {
				overriddenNodes: [ header, footer ],
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				"name": "widget1",
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'thead' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2" class="testHeaderStyleClass"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input id="widget2"' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'tfoot' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2" class="testFooterStyleClass"' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input id="widget3"' );
			expect( container.childNodes[0].childNodes[2].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-widget1-label-cell"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-widget1-label"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-widget1-cell"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[2].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "does not assign ids to root nodes", function() {

			var layout = new metawidget.layout.TableLayout();
			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "entity", {}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "ignores empty stubs", function() {

			var layout = new metawidget.layout.TableLayout();

			var stub = simpleDocument.createElement( 'stub' );
			var widget1 = simpleDocument.createElement( 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( stub, "property", {}, container, mw );
			layout.layoutWidget( widget1, "property", {}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );

			stub.appendChild( simpleDocument.createElement( 'widget2' ) );

			layout.layoutWidget( stub, "property", {}, container, mw );

			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0] ).toBe( stub );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "support multiple columns", function() {

			var layout = new metawidget.layout.TableLayout( {
				"numberOfColumns": 2
			} );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
				"required": "true"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2' ), "property", {
				"name": "widget2",
				"title": "widgetLabel 2"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget3' ), "property", {
				"name": "widget3",
				"large": "true"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget4' ), "property", {
				"name": "widget4"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table id="table-testPath"' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'widget1' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].innerHTML ).toBe( '*' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[3].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[3].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[3].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[4].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[4].childNodes[0].toString() ).toBe( 'widget2' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[5].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[5].innerHTML ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 6 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-testPathWidget3-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget3-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget3-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 3:' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget3-cell" colspan="4"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'widget3' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].innerHTML ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'tr id="table-testPathWidget4-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget4-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget4-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 4:' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget4-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].childNodes[0].toString() ).toBe( 'widget4' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[2].innerHTML ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports partial CSS classes", function() {

			var layout = new metawidget.layout.TableLayout( {
				columnStyleClasses: [ undefined, 'componentClass' ]
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				name: "widget1",
			}, container, mw );
			layout.layoutWidget( widget2, "property", {
				name: "widget2",
				title: "widgetLabel 2"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-widget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-widget1-cell" class="componentClass"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-widget2-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-widget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-widget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-widget2-cell" class="componentClass"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "suppresses labels for buttons", function() {

			var layout = new metawidget.layout.TableLayout();

			var button = simpleDocument.createElement( 'button' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( button, "action", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-widget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-widget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( button );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "suppresses labels for entities", function() {

			var layout = new metawidget.layout.TableLayout();

			var input = simpleDocument.createElement( 'input' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( input, "entity", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td id="table-widget1-cell" colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0] ).toBe( input );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );
	} );

	describe( "The HeadingTagLayoutDecorator", function() {

		it( "decorates sections with a heading tag", function() {

			var layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.TableLayout() );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var widget2 = simpleDocument.createElement( 'input' );
			widget2.setAttribute( 'id', 'widget2' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.onStartBuild( mw );
			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				"name": "widget1",
			}, container, mw );
			layout.layoutWidget( widget2, "property", {
				"name": "widget2",
				"section": "New Section"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table id="table-testPath"' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-testPathWidget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );

			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'h1' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].innerHTML ).toBe( 'New Section' );

			expect( container.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].innerHTML ).toBe( 'Widget 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
		} );

		it( "flattens nested sections", function() {

			var layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.SimpleLayout() );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.onStartBuild( mw );
			layout.startContainerLayout( container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.1' ), "property", {
				"name": "widget2.1",
				"section": "Section 1"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.2' ), "property", {
				"name": "widget2.2",
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.3.1' ), "property", {
				"name": "widget2.3.1",
				"section": [ "Section 1", "Section 1.1" ]
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget3' ), "property", {
				"name": "widget3",
				"section": "Section 2"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget4' ), "property", {
				"name": "widget4",
				"section": ""
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget5' ), "property", {
				"name": "widget5"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'widget1' );
			expect( container.childNodes[1].toString() ).toBe( 'h1' );
			expect( container.childNodes[1].innerHTML ).toBe( 'Section 1' );
			expect( container.childNodes[2].toString() ).toBe( 'widget2.1' );
			expect( container.childNodes[3].toString() ).toBe( 'widget2.2' );
			expect( container.childNodes[4].toString() ).toBe( 'h2' );
			expect( container.childNodes[4].innerHTML ).toBe( 'Section 1.1' );
			expect( container.childNodes[5].toString() ).toBe( 'widget2.3.1' );
			expect( container.childNodes[6].toString() ).toBe( 'h1' );
			expect( container.childNodes[6].innerHTML ).toBe( 'Section 2' );
			expect( container.childNodes[7].toString() ).toBe( 'widget3' );
			expect( container.childNodes[8].toString() ).toBe( 'widget4' );
			expect( container.childNodes[9].toString() ).toBe( 'widget5' );
			expect( container.childNodes.length ).toBe( 10 );
		} );

		it( "can be mixed with a NestedSectionLayoutDecorator", function() {

			var layout = new metawidget.layout.HeadingTagLayoutDecorator( new metawidget.layout.DivLayoutDecorator( new metawidget.layout.SimpleLayout() ) );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.onStartBuild( mw );
			layout.startContainerLayout( container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.1' ), "property", {
				"name": "widget2.1",
				"section": "Section 1"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.2' ), "property", {
				"name": "widget2.2",
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.3.1' ), "property", {
				"name": "widget2.3.1",
				"section": [ "Section 1", "Section 1.1" ]
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget3' ), "property", {
				"name": "widget3",
				"section": "Section 2"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget4' ), "property", {
				"name": "widget4",
				"section": ""
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget5' ), "property", {
				"name": "widget5"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'widget1' );
			expect( container.childNodes[1].toString() ).toBe( 'h1' );
			expect( container.childNodes[1].innerHTML ).toBe( 'Section 1' );
			expect( container.childNodes[2].toString() ).toBe( 'widget2.1' );
			expect( container.childNodes[3].toString() ).toBe( 'widget2.2' );
			expect( container.childNodes[4].toString() ).toBe( 'div title="Section 1.1"' );
			expect( container.childNodes[4].childNodes[0].toString() ).toBe( 'widget2.3.1' );
			expect( container.childNodes[5].toString() ).toBe( 'h1' );
			expect( container.childNodes[5].innerHTML ).toBe( 'Section 2' );
			expect( container.childNodes[6].toString() ).toBe( 'widget3' );
			expect( container.childNodes[7].toString() ).toBe( 'widget4' );
			expect( container.childNodes[8].toString() ).toBe( 'widget5' );
			expect( container.childNodes.length ).toBe( 9 );
		} );
	} );

	describe( "The DivLayoutDecorator", function() {

		it( "decorates sections with divs", function() {

			var layout = new metawidget.layout.DivLayoutDecorator( new metawidget.layout.DivLayoutDecorator( new metawidget.layout.SimpleLayout() ) );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath",
				getElement: function() {

					return container;
				}
			};

			layout.onStartBuild( mw );
			layout.startContainerLayout( container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.1' ), "property", {
				"name": "widget2.1",
				"section": "Section 1"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.2' ), "property", {
				"name": "widget2.2",
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget2.3.1' ), "property", {
				"name": "widget2.3.1",
				"section": [ "Section 1", "Section 1.1" ]
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget3' ), "property", {
				"name": "widget3",
				"section": "Section 2"
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget4' ), "property", {
				"name": "widget4",
				"section": ""
			}, container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget5' ), "property", {
				"name": "widget5"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'widget1' );
			expect( container.childNodes[1].toString() ).toBe( 'div title="Section 1"' );
			expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'widget2.1' );
			expect( container.childNodes[1].childNodes[1].toString() ).toBe( 'widget2.2' );
			expect( container.childNodes[1].childNodes[2].toString() ).toBe( 'div title="Section 1.1"' );
			expect( container.childNodes[1].childNodes[2].childNodes[0].toString() ).toBe( 'widget2.3.1' );
			expect( container.childNodes[1].childNodes.length ).toBe( 3 );
			expect( container.childNodes[2].toString() ).toBe( 'div title="Section 2"' );
			expect( container.childNodes[2].childNodes[0].toString() ).toBe( 'widget3' );
			expect( container.childNodes[2].childNodes.length ).toBe( 1 );
			expect( container.childNodes[3].toString() ).toBe( 'widget4' );
			expect( container.childNodes[4].toString() ).toBe( 'widget5' );
			expect( container.childNodes.length ).toBe( 5 );
		} );
	} );

	describe( "The FlatSectionLayoutDecorator", function() {

		it( "delegates to its delegate", function() {

			var onStartBuildCalled = false;
			var startContainerLayoutCalled = false
			var layoutWidgetCalled = false;
			var endContainerLayoutCalled = false;
			var onEndBuildCalled = false;

			var layout = new metawidget.layout.HeadingTagLayoutDecorator( {
				delegate: {
					onStartBuild: function( mw ) {

						onStartBuildCalled = true;
					},

					startContainerLayout: function( container, mw ) {

						startContainerLayoutCalled = true;
					},

					layoutWidget: function( widget, elementName, attributes, container, mw ) {

						layoutWidgetCalled = true;
					},

					endContainerLayout: function( container, mw ) {

						endContainerLayoutCalled = true;
					},

					onEndBuild: function( mw ) {

						onEndBuildCalled = true;
					}
				}
			} );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath"
			};

			layout.onStartBuild( mw );
			layout.startContainerLayout( container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
			}, container, mw );
			layout.endContainerLayout( container, mw );
			layout.onEndBuild( mw );

			expect( onStartBuildCalled ).toBe( true );
			expect( startContainerLayoutCalled ).toBe( true );
			expect( layoutWidgetCalled ).toBe( true );
			expect( endContainerLayoutCalled ).toBe( true );
			expect( onEndBuildCalled ).toBe( true );
		} );
	} );

	describe( "The NestedSectionLayoutDecorator", function() {

		it( "delegates to its delegate", function() {

			var onStartBuildCalled = false;
			var startContainerLayoutCalled = false
			var layoutWidgetCalled = false;
			var endContainerLayoutCalled = false;
			var onEndBuildCalled = false;

			var layout = new metawidget.layout.DivLayoutDecorator( {
				delegate: {
					onStartBuild: function( mw ) {

						onStartBuildCalled = true;
					},

					startContainerLayout: function( container, mw ) {

						startContainerLayoutCalled = true;
					},

					layoutWidget: function( widget, elementName, attributes, container, mw ) {

						layoutWidgetCalled = true;
					},

					endContainerLayout: function( container, mw ) {

						endContainerLayoutCalled = true;
					},

					onEndBuild: function( mw ) {

						onEndBuildCalled = true;
					}
				}
			} );

			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				"path": "testPath"
			};

			layout.onStartBuild( mw );
			layout.startContainerLayout( container, mw );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
			}, container, mw );
			layout.endContainerLayout( container, mw );
			layout.onEndBuild( mw );

			expect( onStartBuildCalled ).toBe( true );
			expect( startContainerLayoutCalled ).toBe( true );
			expect( layoutWidgetCalled ).toBe( true );
			expect( endContainerLayoutCalled ).toBe( true );
			expect( onEndBuildCalled ).toBe( true );
		} );
	} );
} )();