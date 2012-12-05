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

/**
 * Metawidget for pure JavaScript environments.
 */

var metawidget = {

	/**
	 * Metawidget
	 */

	Metawidget : function( config ) {

		this.toInspect = {};
		this.path = '';
		this.readOnly = false;

		// Configure

		if ( config && config.inspector != null ) {
			this.inspector = config.inspector;
		} else {
			this.inspector = metawidget.PropertyInspector;
		}
		if ( config && config.inspectionResultProcessors ) {
			this.inspectionResultProcessors = config.inspectionResultProcessors;
		} else {
			this.inspectionResultProcessors = [];
		}
		this.widgetBuilder = new metawidget.CompositeWidgetBuilder( [ metawidget.ReadOnlyWidgetBuilder,
		                                                              metawidget.HtmlWidgetBuilder ] );
		this.widgetProcessors = [ metawidget.IdWidgetProcessor ];
		this.layout = metawidget.TableLayout;

		this.buildWidgets = function() {

			// Inspector

			var inspectionResult;

			if ( this.inspector.inspect ) {
				inspectionResult = this.inspector.inspect( this.toInspect, this.path );
			} else {
				inspectionResult = this.inspector( this.toInspect, this.path );
			}

			// InspectionResultProcessors

			for ( var irp = 0, irpLength = this.inspectionResultProcessors.length; irp < irpLength; irp++ ) {

				var inspectionResultProcessor = this.inspectionResultProcessors[irp];

				if ( inspectionResultProcessor.processInspectionResult ) {
					inspectionResultProcessor.processInspectionResult( inspectionResult );
				} else {
					inspectionResultProcessor( inspectionResult );
				}
			}

			console.log( inspectionResult );
			var element = document.createElement( 'metawidget' );

			if ( this.layout.startContainerLayout ) {
				this.layout.startContainerLayout( element );
			}
			
			for ( var loop = 0, length = inspectionResult.length; loop < length; loop++ ) {

				var attributes = inspectionResult[loop];

				// WidgetBuilder

				var widget;

				if ( this.widgetBuilder.buildWidget ) {
					widget = this.widgetBuilder.buildWidget( attributes, this );
				} else {
					widget = this.widgetBuilder( attributes, this );
				}

				// WidgetProcessors

				for ( var wp = 0, wpLength = this.widgetProcessors.length; wp < wpLength; wp++ ) {

					var widgetProcessor = this.widgetProcessors[wp];

					if ( widgetProcessor.processWidget ) {
						widgetProcessor = widgetProcessor.processWidget( widget, attributes );
					} else {
						widgetProcessor = widgetProcessor( widget, attributes );
					}
				}

				// Layout

				if ( this.layout.layoutWidget ) {
					this.layout.layoutWidget( widget, attributes, element );
				} else {
					this.layout( widget, attributes, element );
				}
			}

			return element;
		};
	}
};

/**
 * Inspectors.
 */

metawidget.CompositeInspector = function( inspectors ) {

	this.inspect = function( toInspect, type ) {

		var compositeInspectionResult = [];

		outer: for ( var ins = 0, insLength = inspectors.length; ins < insLength; ins++ ) {

			var inspectionResult;
			var inspector = inspectors[ins];
			
			if ( inspector.inspect ) {
				inspectionResult = inspector.inspect( toInspect, type );
			} else {
				inspectionResult = inspector( toInspect, type );
			}			

			// Inspector may return null
			
			if ( !inspectionResult ) {
				continue;
			}
			
			// If this is the first result...
			
			if ( compositeInspectionResult.length == 0 ) {
				
				// ...use it
				
				compositeInspectionResult = inspectionResult;
			} else {
				
				// ...otherwise merge it
				
				for ( var loop1 = 0, length1 = inspectionResult.length; loop1 < length1; loop1++ ) {
					
					var newAttributes = inspectionResult[loop1];
					
					for ( var loop2 = 0, length2 = compositeInspectionResult.length; loop2 < length2; loop2++ ) {
						var existingAttributes = compositeInspectionResult[loop2];
						
						if ( existingAttributes.name == newAttributes.name ) {
							
							for( var attribute in newAttributes ) {
								existingAttributes[attribute] = newAttributes[attribute];
							}
							
							continue outer;
						}
					}
					
					compositeInspectionResult.push( newAttributes );
				}
			}
		}

		return compositeInspectionResult;
	};
};

metawidget.PropertyInspector = {

	inspect : function( toInspect, type ) {

		var inspectionResult = [];

		for ( var property in toInspect ) {

			var inspectedProperty = {};
			inspectedProperty.name = property;
			inspectedProperty.label = property.toUpperCase();
			inspectedProperty.type = ( typeof property );

			inspectionResult.push( inspectedProperty );
		}

		return inspectionResult;
	}
};

/**
 * InspectionResultProcessors.
 */

/**
 * WidgetBuilders.
 */

metawidget.CompositeWidgetBuilder = function( widgetBuilders ) {

	this.buildWidget = function( attributes, metawidget ) {

		for ( var wb = 0, wbLength = widgetBuilders.length; wb < wbLength; wb++ ) {

			var widget = widgetBuilders[wb].buildWidget( attributes, metawidget );
			
			if ( widget ) {
				return widget;
			}
		}
	};
};

metawidget.ReadOnlyWidgetBuilder = {

	buildWidget : function( attributes, metawidget ) {

		if ( attributes.hidden == 'true' ) {
			return document.createElement( 'stub' );
		}
		
		if ( metawidget.readOnly == 'true' || attributes.readOnly ) {
			return document.createElement( 'span' );
		}
	}
};

metawidget.HtmlWidgetBuilder = {

	buildWidget : function( attributes, metawidget ) {

		if ( attributes.lookup ) {
			var select = document.createElement( 'select' );
			select.appendChild( document.createElement( 'option' ));
			var lookupSplit = attributes.lookup.split( ',' );
			
			for( var loop = 0, length = lookupSplit.length; loop < length; loop++ ) {
				var option = document.createElement( 'option' );
				option.innerHTML = lookupSplit[loop];
				select.appendChild( option );
			}
			return select;			
		}
		
		var text = document.createElement( 'input' );
		text.setAttribute( 'type', 'text' );
		return text;
	}
};

/**
 * WidgetProcessors.
 */

metawidget.IdWidgetProcessor = {

	processWidget : function( widget, attributes ) {

		widget.setAttribute( 'id', attributes.name );
	}
};

/**
 * Layouts.
 */

metawidget.DivLayout = {

	layoutWidget : function( widget, attributes, container ) {

		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		label.innerHTML = attributes.label + ':';

		var div = document.createElement( 'div' );
		div.appendChild( label );
		div.appendChild( widget );

		container.appendChild( div );
	}
};

metawidget.TableLayout = {

	startContainerLayout : function( container ) {
		container.appendChild( document.createElement( 'table' ) );
	},

	layoutWidget : function( widget, attributes, container ) {

		if ( widget.tagName == 'STUB' ) {
			return;
		}
		
		var th = document.createElement( 'th' );
		var label = document.createElement( 'label' );
		label.setAttribute( 'for', attributes.name );
		label.innerHTML = attributes.label + ':';
		th.appendChild( label );

		var td = document.createElement( 'td' );
		td.appendChild( widget );

		var tr = document.createElement( 'tr' );
		tr.appendChild( th );
		tr.appendChild( td );

		container.firstChild.appendChild( tr );
	}
};
