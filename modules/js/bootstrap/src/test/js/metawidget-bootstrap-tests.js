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

	describe( "Bootstrap support", function() {

		it( "has a WidgetProcessor that supports Bootstrap styles", function() {

			var element = simpleDocument.createElement( 'div' );

			var widget = simpleDocument.createElement( 'input' );
			widget.setAttribute( 'type', 'button' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn-default' );

			widget = simpleDocument.createElement( 'input' );
			widget.setAttribute( 'type', 'submit' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'btn btn-primary' );

			widget.setAttribute( 'class', 'other' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'other btn btn-primary' );

			widget = simpleDocument.createElement( 'table' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'table table-striped table-bordered table-hover' );

			widget = simpleDocument.createElement( 'output' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'style' ) ).toBe( 'padding:6px 12px' );

			widget = simpleDocument.createElement( 'output' );
			widget.setAttribute( 'style', 'some: style' );
			expect( processor.processWidget( widget ) ).toBe( widget );
			expect( widget.getAttribute( 'style' ) ).toBe( 'some: style;padding:6px 12px' );

			widget = simpleDocument.createElement( 'input' );
			expect( processor.processWidget( widget, 'property', {} ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( 'form-control' );

			widget = simpleDocument.createElement( 'input' );
			widget.setAttribute( 'type', 'checkbox' );
			expect( processor.processWidget( widget, 'property', {} ) ).toBe( widget );
			expect( widget.getAttribute( 'class' ) ).toBe( null );
		} );

		it( "has a WidgetProcessor that supports input-prepend and input-append", function() {

			var element = simpleDocument.createElement( 'metawidget' );
			var mw = {
				getElement: function() {

					return element;
				}
			}
			var widget = simpleDocument.createElement( 'input' );
			var processor = new metawidget.bootstrap.widgetprocessor.BootstrapWidgetProcessor();

			// Prepend

			var widget = processor.processWidget( widget, 'property', {
				inputPrepend: '$'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-prepend input-group"' );
			expect( widget.childNodes[0].toString() ).toBe( 'span class="add-on input-group-addon"' );
			expect( widget.childNodes[0].innerHTML ).toBe( '$' );
			expect( widget.childNodes[1].toString() ).toBe( 'input class="form-control"' );
			expect( widget.childNodes.length ).toBe( 2 );

			// Append

			widget = simpleDocument.createElement( 'input' );
			widget = processor.processWidget( widget, 'property', {
				inputAppend: '%'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-append input-group"' );
			expect( widget.childNodes[0].toString() ).toBe( 'input class="form-control"' );
			expect( widget.childNodes[1].toString() ).toBe( 'span class="add-on input-group-addon"' );
			expect( widget.childNodes[1].innerHTML ).toBe( '%' );
			expect( widget.childNodes.length ).toBe( 2 );

			// Both

			widget = simpleDocument.createElement( 'input' );
			widget = processor.processWidget( widget, 'property', {
				inputPrepend: '$',
				inputAppend: '.00'
			}, mw );
			expect( widget.toString() ).toBe( 'div class="input-prepend input-append input-group"' );
			expect( widget.childNodes[0].toString() ).toBe( 'span class="add-on input-group-addon"' );
			expect( widget.childNodes[0].innerHTML ).toBe( '$' );
			expect( widget.childNodes[1].toString() ).toBe( 'input class="form-control"' );
			expect( widget.childNodes[2].toString() ).toBe( 'span class="add-on input-group-addon"' );
			expect( widget.childNodes[2].innerHTML ).toBe( '.00' );
			expect( widget.childNodes.length ).toBe( 3 );
		} );

		it( "has a Layout that supports Bootstrap 3.x styles", function() {

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
							},
							baz: {
								type: "string",
								title: ''								
							},
							checkit: {
								type: "boolean"
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout()
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label"' );
			expect( element.childNodes[1].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes[2].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[2].childNodes[0].toString() ).toBe( 'div class="col-sm-10 col-sm-offset-2"' );
			expect( element.childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( element.childNodes[2].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[2].childNodes[0].toString() ).toBe( 'div class="col-sm-10 col-sm-offset-2"' );
			expect( element.childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( element.childNodes[2].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].childNodes.length ).toBe( 1 );
			expect( element.childNodes[3].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[3].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[3].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[3].childNodes[1].childNodes[0].toString() ).toBe( 'label for="checkit" id="checkit-label"' );
			expect( element.childNodes[3].childNodes[1].childNodes[0].innerHTML ).toBe( 'Checkit' );
			expect( element.childNodes[3].childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'input type="checkbox" id="checkit" name="checkit"' );
			expect( element.childNodes[3].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[3].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 4 );
		} );

		it( "supports stubs", function() {

			var element = simpleDocument.createElement( 'div' );
			var stub = simpleDocument.createElement( 'stub' );
			stub.setAttribute( 'id', 'foo' );
			element.appendChild( stub );
			
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
							},
							baz: {
								type: "string",
								title: ''								
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout()
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div class="col-sm-10 col-sm-offset-2"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 2 );
		} );

		it( "has a Layout that can span all columns", function() {

			var element = simpleDocument.createElement( 'div' );
			var mw = new metawidget.Metawidget( element, {
				inspector: function() {

					return {
						properties: {
							foo: {
								type: "string",
								title: null								
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout()
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-12"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes.length ).toBe( 1 );
		} );

		it( "has a Layout that supports Bootstrap 2.x styles", function() {

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
							},
							baz: {
								type: "string",
								title: ''								
							},
							checkit: {
								type: "boolean"
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout( {
					version: 2
				} )
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label" class="control-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label" class="control-label"' );
			expect( element.childNodes[1].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes[2].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[2].childNodes[0].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( element.childNodes[2].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].childNodes.length ).toBe( 1 );
			expect( element.childNodes[3].toString() ).toBe( 'div class="control-group"' );
			expect( element.childNodes[3].childNodes[0].toString() ).toBe( 'div' );
			expect( element.childNodes[3].childNodes[0].childNodes[0].toString() ).toBe( 'label for="checkit" id="checkit-label" class="control-label"' );
			expect( element.childNodes[3].childNodes[1].toString() ).toBe( 'div class="controls"' );
			expect( element.childNodes[3].childNodes[1].childNodes[0].toString() ).toBe( 'input type="checkbox" id="checkit" name="checkit"' );
			expect( element.childNodes[3].childNodes[1].childNodes.length ).toBe( 1 );
			expect( element.childNodes[3].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 4 );
		} );

		it( "has a Layout that supports configurable Bootstrap styles", function() {

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
				layout: new metawidget.bootstrap.layout.BootstrapDivLayout( {
					appendRequiredClassOnLabelDiv: 'label-required',
					appendRequiredClassOnWidgetDiv: 'widget-required'
				} )
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label label-required"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label"' );
			expect( element.childNodes[1].childNodes[1].toString() ).toBe( 'div class="col-sm-10 widget-required"' );
			expect( element.childNodes[1].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" required="required" name="bar"' );
			expect( element.childNodes[1].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[1].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 2 );
		} );

		it( "has a Layout that supports Bootstrap tabs", function() {

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
								section: "Tab 1"
							},
							baz: {
								type: "string",
								section: "Tab 2"
							},
							abc: {
								type: "string",
								section: ""
							}
						}
					};
				},
				layout: new metawidget.bootstrap.layout.TabLayoutDecorator( new metawidget.bootstrap.layout.BootstrapDivLayout() )
			} );

			mw.buildWidgets();

			expect( element.childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="foo" id="foo-label"' );
			expect( element.childNodes[0].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="foo" name="foo"' );
			expect( element.childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[0].childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[1].childNodes[0].toString() ).toBe( 'div class="col-sm-10 col-sm-offset-2"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div id="bar-tabs" class="tabs"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'ul class="nav nav-tabs"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'li class="active"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'a data-toggle="tab" href="#bar-tabs1" target="_self"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].innerHTML ).toBe( 'Tab 1' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'a data-toggle="tab" href="#bar-tabs2" target="_self"' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[1].childNodes[0].innerHTML ).toBe( 'Tab 2' );
			expect( element.childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 2 );
			var tabContent = element.childNodes[1].childNodes[0].childNodes[0].childNodes[1];
			expect( tabContent.toString() ).toBe( 'div class="tab-content"' );
			expect( tabContent.childNodes[0].toString() ).toBe( 'div class="tab-pane active" id="bar-tabs1"' );
			expect( tabContent.childNodes[0].childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="bar" id="bar-label"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="bar" name="bar"' );
			expect( tabContent.childNodes[0].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( tabContent.childNodes[1].toString() ).toBe( 'div class="tab-pane" id="bar-tabs2"' );
			expect( tabContent.childNodes[1].childNodes[0].toString() ).toBe( 'div class="form-group"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[0].childNodes[0].toString() ).toBe( 'label for="baz" id="baz-label"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="baz" name="baz"' );
			expect( tabContent.childNodes[1].childNodes[0].childNodes[0].childNodes.length ).toBe( 1 );
			expect( tabContent.childNodes.length ).toBe( 2 );
			expect( element.childNodes[1].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].toString() ).toBe( 'div class="form-group"' );
			expect( element.childNodes[2].childNodes[0].toString() ).toBe( 'div class="col-sm-2 control-label"' );
			expect( element.childNodes[2].childNodes[0].childNodes[0].toString() ).toBe( 'label for="abc" id="abc-label"' );
			expect( element.childNodes[2].childNodes[1].toString() ).toBe( 'div class="col-sm-10"' );
			expect( element.childNodes[2].childNodes[1].childNodes[0].toString() ).toBe( 'input type="text" id="abc" name="abc"' );
			expect( element.childNodes[2].childNodes[0].childNodes.length ).toBe( 1 );
			expect( element.childNodes[2].childNodes.length ).toBe( 2 );
			expect( element.childNodes.length ).toBe( 3 );
		} );
	} );
} )();