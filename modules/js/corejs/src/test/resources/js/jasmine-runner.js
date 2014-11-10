// Metawidget
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

	/**
	 * Simple script to run Jasmine.
	 */

	//
	// Default Jasmine initialization
	//
	var reporter = new jasmine.JsApiReporter();
	reporter.log = print;

	reporter.reportSuiteStarting = function( suite ) {

		reporter.log( suite.getFullName() );
	};

	reporter.reportSpecResults = function( spec ) {

		var results = spec.results();

		reporter.log( '\t' + spec.description + ' (' + results.passedCount + '/' + results.totalCount + ' passed)' );

		for ( var loop = 0, length = results.getItems().length; loop < length; loop++ ) {

			var item = results.getItems()[loop];
			if ( !item.passed() ) {
				var toLog = item.matcherName + ': ' + item.message;
				reporter.log( '\t\t' + toLog );
				throw new Error( spec.description + ': ' + toLog );
			}
		}
	};

	var jasmineEnv = jasmine.getEnv();
	jasmineEnv.updateInterval = 0;
	jasmineEnv.addReporter( reporter );

	this.runJasmine = function() {

		jasmineEnv.execute();
	};

	/**
	 * Simple document implementation (can be replaced by EnvJS).
	 * <p>
	 * Note: this variable is named <tt>simpleDocument</tt>, not
	 * <tt>document</tt>, because we want to ensure we always go via
	 * <tt>element.ownerDocument</tt> and not rely on a global variable
	 * directly.
	 */

	this.simpleDocument = {
		createElement: function( elementName ) {

			var _eventListeners = [];

			return {
				nodeType: 1,
				tagName: elementName.toUpperCase(),
				attributes: [],
				childNodes: [],
				children: function() {

					throw new Error( "children is not ECMAScript" );
				},
				setAttribute: function( name, value ) {

					for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
						if ( this.attributes[loop].nodeName === name ) {
							this.attributes[loop].nodeValue = value;
							return;
						}
					}

					this.attributes.push( {
						nodeName: name,
						nodeValue: value
					} );
				},
				hasAttribute: function( name ) {

					return ( this.getAttribute( name ) !== null );
				},
				getAttribute: function( name ) {

					for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
						if ( this.attributes[loop].nodeName === name ) {
							return this.attributes[loop].nodeValue;
						}
					}

					// This method should return an empty string, however most
					// browsers return null

					return null;
				},
				insertBefore: function( childNode, beforeNode ) {

					childNode.parentNode = this;
					this.childNodes.splice( this.childNodes.indexOf( beforeNode ), 0, childNode );					
				},
				firstChild: function() {

					return this.childNodes[0];
				},
				appendChild: function( childNode, beforeNode ) {

					childNode.parentNode = this;
					this.childNodes.push( childNode );
				},
				cloneNode: function() {

					var clone = simpleDocument.createElement( elementName );

					for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
						var attribute = this.attributes[loop];
						clone.setAttribute( attribute.nodeName, attribute.nodeValue );
					}
					for ( var loop = 0, length = this.childNodes.length; loop < length; loop++ ) {
						clone.appendChild( this.childNodes[loop].cloneNode() );
					}
					return clone;
				},
				removeChild: function( childNode ) {

					for ( var loop = 0, length = this.childNodes.length; loop < length; loop++ ) {
						if ( this.childNodes[loop] === childNode ) {
							this.childNodes.splice( loop, 1 );
							return childNode;
						}
					}

					throw new Error( "childNode not found: " + childNode );
				},
				addEventListener: function( name, callback ) {

					_eventListeners.push( {
						name: name,
						callback: callback
					} );
				},
				dispatchEvent: function( event ) {

					for ( var loop = 0, length = _eventListeners.length; loop < length; loop++ ) {

						var eventListener = _eventListeners[loop];

						if ( eventListener.name === event.name ) {
							eventListener.callback( event );
						}
					}
				},
				ownerDocument: this,
				toString: function() {

					var toString = elementName.toLowerCase();

					for ( var loop = 0, length = this.attributes.length; loop < length; loop++ ) {
						var attribute = this.attributes[loop];
						toString += ' ' + attribute.nodeName + '="' + attribute.nodeValue + '"';
					}

					return toString;
				}
			};
		},
		createTextNode: function( data ) {

			return {
				nodeType: 3,
				toString: function() {

					return data;
				}
			};
		},
		createEvent: function() {

			return {

				initEvent: function( name ) {

					this.name = name;
				}
			};
		}
	};
} )();