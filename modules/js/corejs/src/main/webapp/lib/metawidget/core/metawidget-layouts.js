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

var metawidget = metawidget || {};

/**
 * @namespace Layouts.
 */

metawidget.layout = metawidget.layout || {};

/**
 * @class SimpleLayout.
 * 
 * Layout to simply output components one after another, with no labels and no structure. This
 * Layout is suited to rendering single components, or for rendering components whose layout relies
 * entirely on CSS.
 */

metawidget.layout.SimpleLayout = function() {

	if ( ! ( this instanceof metawidget.layout.SimpleLayout ) ) {
		throw new Error( 'Constructor called as a function' );
	}
};

metawidget.layout.SimpleLayout.prototype.layoutWidget = function( widget, attributes, container, mw ) {

	if ( widget.tagName === 'STUB' && !metawidget.util.hasChildElements( widget ) ) {
		return;
	}

	container.appendChild( widget );
};

/**
 * @class DivLayout.
 * 
 * Layout to arrange widgets using div tags.
 */

metawidget.layout.DivLayout = function( config ) {

	if ( ! ( this instanceof metawidget.layout.DivLayout ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	var divStyleClasses = config !== undefined ? config.divStyleClasses : undefined;

	this.layoutWidget = function( widget, attributes, container, mw ) {

		if ( widget.tagName === 'STUB' && !metawidget.util.hasChildElements( widget ) ) {
			return;
		}
	
		var outerDiv = document.createElement( 'div' );
		if ( divStyleClasses !== undefined ) {
			outerDiv.setAttribute( 'class', divStyleClasses.split( ',' )[0] );
		}
		
		// Label
	
		if ( attributes.name !== undefined ) {
	
			var labelDiv = document.createElement( 'div' );
			if ( divStyleClasses !== undefined ) {
				labelDiv.setAttribute( 'class', divStyleClasses.split( ',' )[1] );
			}
			
			var label = document.createElement( 'label' );
			label.setAttribute( 'for', widget.getAttribute( 'id' ) );
	
			if ( attributes.label !== undefined ) {
				label.innerHTML = attributes.label + ':';
			} else {
				label.innerHTML = metawidget.util.uncamelCase( attributes.name ) + ':';
			}
	
			labelDiv.appendChild( label );
			outerDiv.appendChild( labelDiv );
		}
	
		// Widget
	
		var widgetDiv = document.createElement( 'div' );
		if ( divStyleClasses !== undefined ) {
			widgetDiv.setAttribute( 'class', divStyleClasses.split( ',' )[2] );
		}

		widgetDiv.appendChild( widget );
		outerDiv.appendChild( widgetDiv );
	
		container.appendChild( outerDiv );
	};
};

/**
 * @class TableLayout.
 * 
 * Layout to arrange widgets in a table, with one column for the label and another for the widget.
 */

metawidget.layout.TableLayout = function( config ) {

	if ( ! ( this instanceof metawidget.layout.TableLayout ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	var tableStyleClass = config !== undefined ? config.tableStyleClass : undefined;
	var columnStyleClasses = config !== undefined ? config.columnStyleClasses : undefined;
	var footerStyleClass = config !== undefined ? config.footerStyleClass : undefined;
	var numberOfColumns = config !== undefined && config.numberOfColumns ? config.numberOfColumns : 1;
	var currentColumn = 0;

	this.startContainerLayout = function( container, mw ) {

		var table = document.createElement( 'table' );
		if ( mw.path !== undefined ) {
			var id = metawidget.util.getId( {}, mw );
			table.setAttribute( 'id', 'table-' + id );
		}

		if ( tableStyleClass !== undefined ) {
			table.setAttribute( 'class', tableStyleClass );
		}

		container.appendChild( table );

		// tfoot

		if ( mw.overriddenNodes !== undefined ) {
			for ( var loop1 = 0, length1 = mw.overriddenNodes.length; loop1 < length1; loop1++ ) {

				var child = mw.overriddenNodes[loop1];

				if ( child.tagName === 'FACET' && child.getAttribute( 'name' ) === 'footer' ) {
					var tfoot = document.createElement( 'tfoot' );
					table.appendChild( tfoot );
					var tr = document.createElement( 'tr' );
					tfoot.appendChild( tr );
					var td = document.createElement( 'td' );
					td.setAttribute( 'colspan', numberOfColumns * 2 );

					if ( footerStyleClass !== undefined ) {
						td.setAttribute( 'class', footerStyleClass );
					}

					tr.appendChild( td );

					// Append children, so as to unwrap the 'facet' tag

					while ( child.childNodes.length > 0 ) {
						td.appendChild( child.removeChild( child.childNodes[0] ) );
					}
					break;
				}
			}
		}

		// tbody

		table.appendChild( document.createElement( 'tbody' ) );
	},

	this.layoutWidget = function( widget, attributes, container, mw ) {

		// Do not render empty stubs
		
		if ( widget.tagName === 'STUB' && !metawidget.util.hasChildElements( widget ) ) {
			return;
		}

		// Special support for large components

		var spanAllColumns = metawidget.util.isSpanAllColumns( attributes );

		if ( spanAllColumns === true && currentColumn > 0 ) {
			currentColumn = 0;
		}
		
		// Id
		
		var table = container.childNodes[container.childNodes.length - 1];
		var idPrefix = undefined;

		if ( attributes.name !== undefined ) {
			if ( table.hasAttribute( 'id' )) {
				idPrefix = table.getAttribute( 'id' );
			}
			
			if ( idPrefix !== undefined ) {
				if ( attributes._root !== 'true' ) {
					if ( idPrefix.charAt( idPrefix.length - 1 ) !== '-' ) {
						idPrefix += metawidget.util.capitalize( attributes.name );
					} else {
						idPrefix += attributes.name;
					}
				}
			} else {
				idPrefix = 'table-' + attributes.name;
			}
		}

		// Start column
		
		var tbody = table.childNodes[table.childNodes.length - 1];
		var tr;
		
		if ( currentColumn === 0 ) {
			tr = document.createElement( 'tr' );
			if ( idPrefix !== undefined ) {
				tr.setAttribute( 'id', idPrefix + '-row' );
			}
			tbody.appendChild( tr );
		} else {
			tr = tbody.childNodes[tbody.childNodes.length - 1];
		}

		if ( attributes.name !== undefined ) {
			// Label

			var th = document.createElement( 'th' );
			th.setAttribute( 'id', idPrefix + '-label-cell' );

			if ( columnStyleClasses !== undefined ) {
				th.setAttribute( 'class', columnStyleClasses.split( ',' )[0] );
			}

			var label = document.createElement( 'label' );
			
			if ( widget.hasAttribute( 'id' )) {
				label.setAttribute( 'for', widget.getAttribute( 'id' ) );
			}
			
			label.setAttribute( 'id', idPrefix + '-label' );

			if ( attributes.label !== undefined ) {
				label.innerHTML = attributes.label + ':';
			} else {
				label.innerHTML = metawidget.util.uncamelCase( attributes.name ) + ':';
			}

			th.appendChild( label );
			tr.appendChild( th );
		}

		// Widget

		var td = document.createElement( 'td' );

		if ( idPrefix !== undefined ) {
			td.setAttribute( 'id', idPrefix + '-cell' );
		}

		if ( columnStyleClasses !== undefined ) {
			td.setAttribute( 'class', columnStyleClasses.split( ',' )[1] );
		}

		if ( spanAllColumns === true ) {
			td.setAttribute( 'colspan', (( numberOfColumns * 3 ) - 1 ) - tr.childNodes.length );
		} else if ( tr.childNodes.length < 1 ) {
			td.setAttribute( 'colspan', 2 - tr.childNodes.length );
		}

		td.appendChild( widget );
		tr.appendChild( td );

		// Error

		td = document.createElement( 'td' );

		if ( columnStyleClasses !== undefined ) {
			td.setAttribute( 'class', columnStyleClasses.split( ',' )[2] );
		}

		if ( attributes.readOnly !== 'true' && attributes.required === 'true' ) {
			td.innerHTML = '*';
		}

		tr.appendChild( td );
		
		// Next column

		if ( spanAllColumns === true ) {
			currentColumn = numberOfColumns - 1;
		}
		
		currentColumn = ( currentColumn + 1 ) % numberOfColumns;
	};
};

//
// LayoutDecorator
//

metawidget.layout._createSectionLayoutDecorator = function( config, decorator, decoratorName ) {

	if ( this instanceof metawidget.layout._createSectionLayoutDecorator ) {
		throw new Error( 'Function called as a Constructor' );
	}

	var delegate;

	if ( config.delegate !== undefined ) {
		delegate = config.delegate;
	} else {
		delegate = config;
	}

	/**
	 * Read-only getter.
	 * <p>
	 * Dangerous to add a 'delegate' property, because can conflict with
	 * 'config.delegate'.
	 */

	decorator.getDelegate = function() {

		return delegate;
	};

	decorator.onStartBuild = function( mw ) {

		if ( decorator.getDelegate().onStartBuild !== undefined ) {
			decorator.getDelegate().onStartBuild( mw );
		}
	};

	decorator.startContainerLayout = function( container, mw ) {

		container[decoratorName] = {};

		if ( decorator.getDelegate().startContainerLayout !== undefined ) {
			decorator.getDelegate().startContainerLayout( container, mw );
		}
	};

	decorator.endContainerLayout = function( container, mw ) {

		if ( decorator.getDelegate().endContainerLayout !== undefined ) {
			decorator.getDelegate().endContainerLayout( container, mw );
		}

		container[decoratorName] = {};		
	};

	decorator.onEndBuild = function( mw ) {

		if ( decorator.getDelegate().onEndBuild !== undefined ) {
			decorator.getDelegate().onEndBuild( mw );
		}
	};
};

metawidget.layout.createFlatSectionLayoutDecorator = function( config, decorator, decoratorName ) {

	if ( this instanceof metawidget.layout.createFlatSectionLayoutDecorator ) {
		throw new Error( 'Function called as a Constructor' );
	}

	metawidget.layout._createSectionLayoutDecorator( config, decorator, decoratorName );
	
	decorator.layoutWidget = function( widget, attributes, container, mw ) {

		// If our delegate is itself a NestedSectionLayoutDecorator, strip the
		// section

		if ( decorator.getDelegate().nestedSectionLayoutDecorator === true ) {

			// Stay where we are?

			var section = metawidget.util.stripSection( attributes );

			if ( section === undefined || section === container[decoratorName].currentSection ) {
				return decorator.getDelegate().layoutWidget( widget, attributes, container, mw );
			}

			// End nested LayoutDecorator's current section

			if ( container[decoratorName].currentSection !== undefined ) {
				decorator.getDelegate().endContainerLayout( container, mw );
			}

			container[decoratorName].currentSection = section;

			// Add a heading

			if ( section !== '' ) {
				decorator.addSectionWidget( section, 0, attributes, container, mw );
			}
		} else {

			// Stay where we are?

			if ( attributes.section === undefined || attributes.section === container[decoratorName].currentSection ) {
				return decorator.getDelegate().layoutWidget( widget, attributes, container, mw );
			}

			// For each of the new sections...

			var sections = attributes.section.split( ',' );
			var currentSections;

			if ( container[decoratorName].currentSection !== undefined ) {
				currentSections = container[decoratorName].currentSection.split( ',' );
			} else {
				currentSections = [];
			}

			for ( var level = 0; level < sections.length; level++ ) {
				var section = sections[level];

				// ...that are different from our current...

				if ( section === '' ) {
					continue;
				}

				if ( level < currentSections.length && section === currentSections[level] ) {
					continue;
				}

				// ...add a heading
				//
				// Note: we cannot stop/start the delegate layout here. It is
				// tempting, but remember addSectionWidget needs to use the
				// delegate. If you stop/add section heading/start the delegate,
				// who is laying out the section heading?

				decorator.addSectionWidget( section, level, attributes, container, mw );
			}

			container[decoratorName].currentSection = attributes.section;
		}

		// Add component as normal

		decorator.getDelegate().layoutWidget( widget, attributes, container, mw );
	};
};

metawidget.layout.createNestedSectionLayoutDecorator = function( config, decorator, decoratorName ) {

	if ( this instanceof metawidget.layout.createNestedSectionLayoutDecorator ) {
		throw new Error( 'Function called as a Constructor' );
	}

	metawidget.layout._createSectionLayoutDecorator( config, decorator, decoratorName );

	// Tag this NestedSectionLayoutDecorator so that FlatSectionLayoutDecorator
	// can recognize it

	decorator.nestedSectionLayoutDecorator = true;

	decorator.layoutWidget = function( widget, attributes, container, mw ) {

		// Stay where we are?

		var section = metawidget.util.stripSection( attributes );

		if ( section === undefined || section === container[decoratorName].currentSection ) {
			if ( container[decoratorName].currentSectionWidget ) {
				return decorator.getDelegate().layoutWidget( widget, attributes, container[decoratorName].currentSectionWidget, mw );
			}
			return decorator.getDelegate().layoutWidget( widget, attributes, container, mw );
		}

		// End current section

		if ( container[decoratorName].currentSectionWidget !== undefined ) {
			decorator.endContainerLayout( container[decoratorName].currentSectionWidget, mw );
		}

		container[decoratorName].currentSection = section;
		var previousSectionWidget = container[decoratorName].currentSectionWidget; 
		delete container[decoratorName].currentSectionWidget;

		// No new section?

		if ( section === '' ) {
			decorator.getDelegate().layoutWidget( widget, attributes, container, mw );
			return;
		}

		// Start new section

		container[decoratorName].currentSectionWidget = decorator.createSectionWidget( previousSectionWidget, section, attributes, container, mw );
		decorator.startContainerLayout( container[decoratorName].currentSectionWidget, mw );

		// Add component to new section

		decorator.getDelegate().layoutWidget( widget, attributes, container[decoratorName].currentSectionWidget, mw );
	};

	var _superEndContainerLayout = decorator.endContainerLayout;
 
	decorator.endContainerLayout = function( container, mw ) {

		// End hanging layouts

		if ( container[decoratorName].currentSectionWidget !== undefined ) {
			decorator.endContainerLayout( container[decoratorName].currentSectionWidget, mw );
		}

		_superEndContainerLayout( container, mw );
	};
};

/**
 * @class HeadingTagLayoutDecorator.
 * 
 * LayoutDecorator to decorate widgets from different sections using an HTML heading
 * tag (i.e. h1, h2 etc).
 */

metawidget.layout.HeadingTagLayoutDecorator = function( config ) {

	if ( ! ( this instanceof metawidget.layout.HeadingTagLayoutDecorator ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	metawidget.layout.createFlatSectionLayoutDecorator( config, this, 'headingTagLayoutDecorator' );

	this.addSectionWidget = function( section, level, attributes, container, mw ) {

		var h1 = document.createElement( 'h' + ( level + 1 ) );
		h1.innerHTML = section;

		this.getDelegate().layoutWidget( h1, {
			wide: 'true'
		}, container, mw );
	};
};

/**
 * @class DivLayoutDecorator.
 */

metawidget.layout.DivLayoutDecorator = function( config ) {

	if ( ! ( this instanceof metawidget.layout.DivLayoutDecorator ) ) {
		throw new Error( 'Constructor called as a function' );
	}

	metawidget.layout.createNestedSectionLayoutDecorator( config, this, 'divLayoutDecorator' );

	this.createSectionWidget = function( previousSectionWidget, section, attributes, container, mw ) {

		var div = document.createElement( 'div' );
		div.setAttribute( 'title', section );
		this.getDelegate().layoutWidget( div, {
			wide: 'true'
		}, container, mw );

		return div;
	};
};
