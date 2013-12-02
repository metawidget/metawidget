var addressbook = addressbook || {};

( function() {

	'use strict';

	/**
	 * Model
	 */

	var _model = undefined;

	function _save( entity ) {

		if ( _model.indexOf( entity ) == -1 ) {
			_model.push( entity );
		}
	}

	function _delete( entity ) {

		for ( var loop = 0, length = _model.length; loop < length; loop++ ) {

			if ( _model[loop] === entity ) {
				_model.splice( loop, 1 );
				break;
			}
		}
	}

	addressbook.restUrl = addressbook.restUrl || '';

	/**
	 * Summary page
	 */

	$( document ).on( 'pageinit', '#contacts-page', function( event ) {

		var page = $( event.target );

		page.find( '#metawidget' ).metawidget( {
			inspector: new metawidget.inspector.JsonSchemaInspector( {

				properties: {
					firstname: {
						type: 'string',
						placeholder: 'Firstname',
						componentType: 'search'
					},
					surname: {
						type: 'string',
						placeholder: 'Surname',
						componentType: 'search'
					},
					search: {
						type: 'function'
					}
				}
			} ),
			layout: new metawidget.layout.SimpleLayout()
		} );
	} );

	var _search = {};

	$( document ).on( 'pagebeforeshow', '#contacts-page', function( event ) {

		var page = $( event.target );
		var mw = page.find( '#metawidget' );

		_search.search = function() {

			mw.metawidget( 'getWidgetProcessor', function( widgetProcessor ) {

				return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
			} ).save( mw.data( 'metawidget' ) );

			_populateSummaryPage( event );
		};

		mw.metawidget( "buildWidgets", _search );

		if ( _model === undefined ) {
			$.getJSON( addressbook.restUrl + 'js/contacts.json', {}, function( data ) {
				_model = data;
				_populateSummaryPage( event );
			} );
		} else {
			_populateSummaryPage( event );
		}
	} );

	function _populateSummaryPage( event ) {

		var page = $( event.target );
		var summary = page.find( '#summary' );
		summary.empty();
		var listview = $( '<ul>' ).attr( 'data-role', 'listview' );

		$.each( _model, function( key, value ) {

			// Filter

			if ( _search.firstname !== undefined && value.firstname.indexOf( _search.firstname ) === -1 ) {
				return;
			}

			if ( _search.surname !== undefined && value.surname.indexOf( _search.surname ) === -1 ) {
				return;
			}

			// List

			var anchor = $( '<a>' ).on( 'click', function() {

				addressbook.current = value;

				$.mobile.changePage( page.data( 'contact-page' ) + '.html', {
					transition: 'slide'
				} );
			} );

			anchor.text( value.firstname + ' ' + value.surname );
			listview.append( $( '<li>' ).append( anchor ) );
		} );

		summary.html( listview ).trigger( 'create' );
	}

	/**
	 * Create
	 */

	addressbook.createPersonal = function( event ) {

		addressbook.type = 'personal';
		_create( event );
	};

	addressbook.createBusiness = function( event ) {

		addressbook.type = 'business';
		_create( event );
	};

	function _create( event ) {

		delete addressbook.current;
		var page = $( event ).parents( 'article' );

		$.mobile.changePage( page.data( 'contact-page' ) + '.html', {
			transition: 'slide'
		} );
	}

	/**
	 * Retrieve
	 */

	$( document ).on(
			'pageinit',
			'#contact-page',
			function( event ) {

				// Example of adding edit buttons to a table

				var communicationsWidgetBuilder = new metawidget.widgetbuilder.HtmlWidgetBuilder();

				var superCreateTable = communicationsWidgetBuilder.createTable;
				communicationsWidgetBuilder.createTable = function( elementName, attributes, mw ) {
					var table = superCreateTable.call( communicationsWidgetBuilder, elementName, attributes, mw );

					if ( mw.readOnly === false ) {
						$( table ).append( $( '<tfoot>' ).append( $( '<tr>' ).append( $( '<td>' ).append( $( '<button>' ).val( 'Add' ).on( 'click', function() {

							delete addressbook.currentCommunication;

							$.mobile.changePage( 'communication.html', {
								transition: 'slide',
								reverse: true
							} );
						} ) ) ) ) );
					}

					return table;
				};

				var superAddRow = communicationsWidgetBuilder.addRow;
				communicationsWidgetBuilder.addRow = function( tbody, value, row, columnAttributesArray, elementName, tableAttributes, mw ) {

					var tr = superAddRow.call( communicationsWidgetBuilder, tbody, value, row, columnAttributesArray, elementName, tableAttributes, mw );

					if ( mw.readOnly === false ) {
						$( tr ).append( $( '<td>' ).append( $( '<button>' ).val( 'Edit' ).on( 'click', function() {

							addressbook.currentCommunication = value[row];

							$.mobile.changePage( 'communication.html', {
								transition: 'slide',
								reverse: true
							} );
						} ) ) );
					}

					return tr;
				};

				var page = $( event.target );
				page.find( '#metawidget' ).metawidget(
						{

							inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), new metawidget.inspector.JsonSchemaInspector( {

								// Example of client-side schema

								properties: {

									address: {
										properties: {
											street: {
												type: "string"
											},
											city: {
												type: "string"
											},
											state: {
												"enum": [ "Anytown", "Cyberton", "Lostville", "Whereverton" ]
											},
											postcode: {
												type: "string"
											}
										}
									},

									communications: {
										type: 'array',
										items: {
											properties: {
												type: {
													type: "string"
												},
												value: {
													type: "string"
												}
											}
										}
									}
								}
							} ) ] ),

							inspectionResultProcessors: [ function( inspectionResult, mw, toInspect, type, names ) {

								// Example of server-side, asynchronous schema

								if ( names === undefined && toInspect !== undefined && toInspect.type !== undefined ) {
									$.getJSON( addressbook.restUrl + 'js/' + toInspect.type + '-contact-schema.json', {}, function( data ) {

										metawidget.util.combineInspectionResults( inspectionResult, data );
										mw._refresh( inspectionResult );
									} );
								} else {
									return inspectionResult;
								}
							} ],

							widgetBuilder: new metawidget.widgetbuilder.CompositeWidgetBuilder( [ new metawidget.widgetbuilder.OverriddenWidgetBuilder(),
									new metawidget.widgetbuilder.ReadOnlyWidgetBuilder(), communicationsWidgetBuilder ] )
						} );
			} );

	$( document ).on( 'pagebeforeshow', '#contact-page', function( event ) {

		if ( _model === undefined ) {
			$.mobile.changePage( 'index.html' );
			return;
		}

		var page = $( event.target );
		var mw = page.find( '#metawidget' );

		if ( addressbook.current === undefined ) {

			addressbook.current = {
				type: addressbook.type
			};
			mw.metawidget( 'setReadOnly', false );
			mw.metawidget( 'buildWidgets', addressbook.current );

			page.find( 'h1' ).text( 'New Contact' );
			page.find( '#nav-create' ).show();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).hide();
		} else {
			mw.metawidget( 'setReadOnly', true );
			mw.metawidget( 'buildWidgets', addressbook.current );

			page.find( 'h1' ).text( addressbook.current.firstname + ' ' + addressbook.current.surname );
			page.find( '#nav-create' ).hide();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).show();
		}
	} );

	addressbook.cancel = function( event ) {

		$.mobile.changePage( 'index.html', {
			transition: 'slide',
			reverse: true
		} );
	};

	addressbook.edit = function( event ) {

		var page = $( event ).parents( 'article' );
		page.find( '#metawidget' ).metawidget( 'setReadOnly', false );
		page.find( '#metawidget' ).metawidget( 'buildWidgets' );

		page.find( '#nav-view' ).hide( 400 );
		page.find( '#nav-edit' ).show( 400 );
	};

	addressbook.view = function( event ) {

		var page = $( event ).parents( 'article' );
		page.find( '#metawidget' ).metawidget( 'setReadOnly', true );
		page.find( '#metawidget' ).metawidget( 'buildWidgets' );

		page.find( '#nav-edit' ).hide( 400 );
		page.find( '#nav-view' ).show( 400 );
	};

	/**
	 * Update
	 */

	addressbook.update = function( event ) {

		var page = $( event ).parents( 'article' );

		var mw = page.find( '#metawidget' );
		var mwData = mw.data( 'metawidget' );
		mw.metawidget( 'getWidgetProcessor', function( widgetProcessor ) {

			return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
		} ).save( mwData );

		_save( mwData.toInspect );

		$.mobile.changePage( 'index.html', {
			transition: 'slidedown'
		} );
	};

	/**
	 * Delete
	 */

	addressbook["delete"] = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' );
		var mwData = mw.data( 'metawidget' );
		_delete( mwData.toInspect );

		$.mobile.changePage( 'index.html', {
			transition: 'slide',
			reverse: true
		} );
	};

	/**
	 * Communication
	 */

	$( document ).on( 'pageinit', '#communication-page', function( event ) {

		var page = $( event.target );

		page.find( '#metawidget' ).metawidget( {
			inspector: new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), new metawidget.inspector.JsonSchemaInspector( {

				properties: {
					type: {
						"enum": [ "Telephone", "Mobile", "Fax", "E-mail" ]
					},
					value: {
						type: "string"
					}
				}
			} ) ] )
		} );
	} );

	$( document ).on( 'pagebeforeshow', '#communication-page', function( event ) {

		if ( _model === undefined ) {
			$.mobile.changePage( 'index.html' );
			return;
		}

		var page = $( event.target );
		var mw = page.find( '#metawidget' );

		if ( addressbook.currentCommunication === undefined ) {
			mw.metawidget( 'buildWidgets', {} );

			page.find( 'h1' ).text( 'New Communication' );
			page.find( '#nav-create' ).show();
			page.find( '#nav-edit' ).hide();
		} else {
			mw.metawidget( 'buildWidgets', addressbook.currentCommunication );

			page.find( 'h1' ).text( 'Edit Communication' );
			page.find( '#nav-create' ).hide();
			page.find( '#nav-edit' ).show();
		}
	} );

	addressbook.addCommunication = function( event ) {

		$.mobile.changePage( 'communication.html', {
			transition: 'slide',
			reverse: true
		} );
	};

	addressbook.communication = addressbook.communication || {};

	addressbook.communication.update = function( event ) {

		var page = $( event ).parents( 'article' );

		var mw = page.find( '#metawidget' );
		var mwData = mw.data( 'metawidget' );
		mw.metawidget( 'getWidgetProcessor', function( widgetProcessor ) {

			return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
		} ).save( mwData );

		if ( addressbook.currentCommunication === undefined ) {
			addressbook.current.communications = addressbook.current.communications || [];
			addressbook.current.communications.push( mwData.toInspect );
		}

		$.mobile.changePage( 'contact.html', {
			transition: 'slidedown'
		} );
	};

	addressbook.communication.cancel = function( event ) {

		$.mobile.changePage( 'contact.html', {
			transition: 'slide',
			reverse: true
		} );
	};

	addressbook.communication['delete'] = function( event ) {

		if ( addressbook.currentCommunication !== undefined ) {
			var indexOf = addressbook.current.communications.indexOf( addressbook.currentCommunication );
			addressbook.current.communications.splice( indexOf, 1 );
		}

		$.mobile.changePage( 'contact.html', {
			transition: 'slide',
			reverse: true
		} );
	};

} )();