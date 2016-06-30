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
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div class="widgetStyle"' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[1].toString() ).toBe( 'div class="outerStyle"' );
			expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'div class="labelStyle"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="widget2-label"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'widgetLabel 2:' );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1#' );
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

		it( "supports null labels", function() {

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
				title: null
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1' );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[1].childNodes[0].toString() ).toBe( 'div class="labelStyle"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="widget2-label"' );
			expect( container.childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'widgetLabel 2:' );
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

		it( "supports label overrides", function() {

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
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Foo@' );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "can suppress label suffixes on radio buttons", function() {

			var layout = new metawidget.layout.DivLayout( {
				suppressLabelSuffixOnCheckboxes: true
			} );

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'type', 'radio' );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "can wrap checkboxes with labels", function() {

			var layout = new metawidget.layout.DivLayout( {
				wrapInsideLabels: [ 'checkbox' ],
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
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'label' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].textContent ).toBe( 'Widget 1' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "can wrap checkboxes with extra divs", function() {

			var layout = new metawidget.layout.DivLayout( {
				wrapWithExtraDiv: {
					'checkbox': 'theClass'
				},
				wrapInsideLabels: [ 'checkbox' ],
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
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );			
			expect( container.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'div class="theClass"' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[1].childNodes.length ).toBe( 1 );
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

		it( "supports suppressDivAroundLabel", function() {

			var layout = new metawidget.layout.DivLayout( {
				suppressDivAroundLabel: true
			} );

			var input = simpleDocument.createElement( 'input' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( input, "property", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( input );
			expect( container.childNodes[0].childNodes[1].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports suppressDivAroundWidget", function() {

			var layout = new metawidget.layout.DivLayout( {
				suppressDivAroundWidget: true
			} );

			var input = simpleDocument.createElement( 'input' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			};

			layout.layoutWidget( input, "property", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label' );
			expect( container.childNodes[0].childNodes[1] ).toBe( input );
			expect( container.childNodes[0].childNodes.length ).toBe( 2 );
			expect( container.childNodes.length ).toBe( 1 );
		} );
	} );

	describe( "The DefinitionListLayout", function() {

		it( "arranges widgets using dl tags", function() {

			var layout = new metawidget.layout.DefinitionListLayout( {
				labelStyleClass: 'labelStyle',
				labelSuffix: '#'
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1#' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'dd' );
			expect( container.childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[2].toString() ).toBe( 'dt' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'label for="widget2" class="labelStyle"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].textContent ).toBe( 'widgetLabel 2#' );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].textContent ).toBe( '*' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].textContent ).toBeUndefined();
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="3" class="testHeaderStyleClass"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input id="widget2"' );
			expect( container.childNodes[0].childNodes[1].toString() ).toBe( 'tfoot' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'td colspan="3" class="testFooterStyleClass"' );
			expect( container.childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input id="widget3"' );
			expect( container.childNodes[0].childNodes[2].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-widget1-label-cell"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-widget1-label"' );
			expect( container.childNodes[0].childNodes[2].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'widget1' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].textContent ).toBe( '*' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[3].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[3].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[3].childNodes[0].textContent ).toBe( 'widgetLabel 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[4].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[4].childNodes[0].toString() ).toBe( 'widget2' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[5].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[5].textContent ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 6 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-testPathWidget3-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget3-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget3-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 3:' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget3-cell" colspan="4"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'widget3' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[2].textContent ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'tr id="table-testPathWidget4-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget4-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget4-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 4:' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget4-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].childNodes[0].toString() ).toBe( 'widget4' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[2].textContent ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "support multiple columns with nesting", function() {

			var layout = new metawidget.layout.TableLayout( {
				"numberOfColumns": 2
			} );

			var container1 = simpleDocument.createElement( 'metawidget' );			
			var mw1 = {
				"path": "testPath",
				getElement: function() {

					return container1;
				}
			};
			var container2 = simpleDocument.createElement( 'metawidget' );			
			var mw2 = {
					"path": "testPath.nested",
					getElement: function() {

						return container2;
					}
				};

			layout.startContainerLayout( container1, mw1 );
			layout.layoutWidget( simpleDocument.createElement( 'widget1' ), "property", {
				"name": "widget1",
				"required": "true"
			}, container1, mw1 );
			layout.startContainerLayout( container2, mw2 );
			layout.layoutWidget( simpleDocument.createElement( 'widget2' ), "property", {
				"name": "widget2",
				"title": "widgetLabel 2"
			}, container2, mw2 );

			expect( container1.childNodes[0].toString() ).toBe( 'table id="table-testPath"' );
			expect( container1.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathWidget1-row"' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget1-label-cell"' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathWidget1-label"' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'widget1' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes[2].textContent ).toBe( '*' );
			expect( container1.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container1.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container1.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container1.childNodes.length ).toBe( 1 );
			expect( container2.childNodes[0].toString() ).toBe( 'table id="table-testPathNested"' );
			expect( container2.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-testPathNestedWidget2-row"' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-testPathNestedWidget2-label-cell"' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label id="table-testPathNestedWidget2-label"' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'widgetLabel 2:' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathNestedWidget2-cell"' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'widget2' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes[2].textContent ).toBeUndefined();
			expect( container2.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container2.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container2.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container2.childNodes.length ).toBe( 1 );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-widget1-cell" class="componentClass"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr id="table-widget2-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'th id="table-widget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-widget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'widgetLabel 2:' );
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

		it( "supports label overrides", function() {

			var layout = new metawidget.layout.TableLayout();
			layout.getLabelString = function() {

				return "abc";
			}

			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			}

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				name: "widget1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-widget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget1" id="table-widget1-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'abc' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-widget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports empty labels", function() {

			var layout = new metawidget.layout.TableLayout();
			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			}

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				name: "widget1",
				title: ""
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'th id="table-widget1-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBeUndefined();
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 0 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-widget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'td' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
		} );

		it( "supports null labels", function() {

			var layout = new metawidget.layout.TableLayout();
			var widget1 = simpleDocument.createElement( 'input' );
			widget1.setAttribute( 'id', 'widget1' );
			var container = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return container;
				}
			}

			layout.startContainerLayout( container, mw );
			layout.layoutWidget( widget1, "property", {
				name: "widget1",
				title: null
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'table' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'tbody' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'tr id="table-widget1-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'td id="table-widget1-cell" colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0] ).toBe( widget1 );
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
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 1:' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget1-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0] ).toBe( widget1 );
			expect( container.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );

			expect( container.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'tr' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'td colspan="2"' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'h1' );
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].textContent ).toBe( 'New Section' );

			expect( container.childNodes[0].childNodes[0].childNodes[2].toString() ).toBe( 'tr id="table-testPathWidget2-row"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].toString() ).toBe( 'th id="table-testPathWidget2-label-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label for="widget2" id="table-testPathWidget2-label"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[0].childNodes[0].textContent ).toBe( 'Widget 2:' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].toString() ).toBe( 'td id="table-testPathWidget2-cell"' );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes[1].childNodes[0] ).toBe( widget2 );
			expect( container.childNodes[0].childNodes[0].childNodes[2].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes[0].childNodes.length ).toBe( 3 );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
		} );

		it( "can start at arbitary heading numbers", function() {

			var layout = new metawidget.layout.HeadingTagLayoutDecorator( {
				delegate: new metawidget.layout.TableLayout(),	
				level: 3
			} );

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
			expect( container.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'h3' );
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
			expect( container.childNodes[1].textContent ).toBe( 'Section 1' );
			expect( container.childNodes[2].toString() ).toBe( 'widget2.1' );
			expect( container.childNodes[3].toString() ).toBe( 'widget2.2' );
			expect( container.childNodes[4].toString() ).toBe( 'h2' );
			expect( container.childNodes[4].textContent ).toBe( 'Section 1.1' );
			expect( container.childNodes[5].toString() ).toBe( 'widget2.3.1' );
			expect( container.childNodes[6].toString() ).toBe( 'h1' );
			expect( container.childNodes[6].textContent ).toBe( 'Section 2' );
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
			expect( container.childNodes[1].textContent ).toBe( 'Section 1' );
			expect( container.childNodes[2].toString() ).toBe( 'widget2.1' );
			expect( container.childNodes[3].toString() ).toBe( 'widget2.2' );
			expect( container.childNodes[4].toString() ).toBe( 'div title="Section 1.1"' );
			expect( container.childNodes[4].childNodes[0].toString() ).toBe( 'widget2.3.1' );
			expect( container.childNodes[5].toString() ).toBe( 'h1' );
			expect( container.childNodes[5].textContent ).toBe( 'Section 2' );
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

		it( "supports styleClass", function() {

			var layout = new metawidget.layout.DivLayoutDecorator( {
				styleClass: 'theStyleClass',
				delegate: new metawidget.layout.SimpleLayout()
			} );

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
				"section": "Section 1"
			}, container, mw );

			expect( container.childNodes[0].toString() ).toBe( 'div title="Section 1" class="theStyleClass"' );
			expect( container.childNodes[0].childNodes[0].toString() ).toBe( 'widget1' );
			expect( container.childNodes[0].childNodes.length ).toBe( 1 );
			expect( container.childNodes.length ).toBe( 1 );
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