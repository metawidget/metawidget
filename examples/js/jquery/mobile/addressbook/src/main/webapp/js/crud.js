var crud = crud || {};

( function() {

	'use strict';

	/**
	 * Model
	 */

	var _nextId = 10;
	var _model = undefined;

	function _loadById( id ) {

		for( var loop = 0, length = _model.length; loop < length; loop++ ) {

			if ( _model[loop].id === id ) {
				return _model[loop];
			}
		}
	}

	function _save( entity ) {

		if ( entity.id === undefined ) {
			entity.id = _nextId++;
			_model.push( entity );
		}
	}

	function _deleteById( id ) {

		for( var loop = 0, length = _model.length; loop < length; loop++ ) {

			if ( _model[loop].id === id ) {
				_model.splice( loop, 1 );
				break;
			}
		}
	}

	/**
	 * Summary page
	 */

	$( document ).on( 'pagebeforeshow', '#summary-page', function( event ) {

		if ( _model === undefined ) {
			$.getJSON( 'js/contacts.json', {}, function( data ) {
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
		var listview = $( '<ul>' ).attr( 'data-role', 'listview' );

		$.each( _model, function( key, value ) {

			var anchor = $( '<a>' ).on( 'click', function() {

				crud.id = value.id;

				$.mobile.changePage( page.data( 'detail-page' ) + '.html', {
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

	crud.createPersonal = function( event ) {

		crud.type = 'personal';
		_create( event );
	};

	crud.createBusiness = function( event ) {

		crud.type = 'business';
		_create( event );
	};

	function _create( event ) {

		delete crud.id;
		var page = $( event ).parents( 'article' );

		$.mobile.changePage( page.data( 'detail-page' ) + '.html', {
			transition: 'slide'
		} );
	}

	/**
	 * Retrieve
	 */

	$( document ).on( 'pageinit', '#detail-page', function( event ) {

		var page = $( event.target );

		// This should autoinit in JQuery Mobile 1.4.0

		page.find( '#metawidget' ).metawidget();
		page.find( '#metawidget' ).metawidget( "option", "inspector", new metawidget.inspector.CompositeInspector( [ new metawidget.inspector.PropertyTypeInspector(), function( toInspect, type, names ) {

			if ( names !== undefined && names.length === 1 && names[0] === 'address' ) {

				// Example of client-side schema

				return {
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
				};
			}
		} ] ));
		page.find( '#metawidget' ).metawidget( "option", "inspectionResultProcessors", [ function( inspectionResult, mw, toInspect, type, names ) {

			// Example of server-side, asynchronous schema

			if ( names === undefined && toInspect !== undefined && toInspect.type !== undefined ) {
				$.getJSON( 'js/' + toInspect.type + '-contact-schema.json', {}, function( data ) {

					metawidget.util.combineInspectionResults( inspectionResult, data );
					mw._refresh( inspectionResult );
				} );
			} else {
				return inspectionResult;
			}

		} ] );
	} );

	$( document ).on( 'pagebeforeshow', '#detail-page', function( event ) {

		var page = $( event.target );

		if ( _model === undefined ) {
			$.mobile.changePage( page.data( 'summary-page' ) + '.html' );
			return;
		}

		var mw = page.find( '#metawidget' );

		if ( crud.id === undefined ) {
			mw.metawidget( 'setReadOnly', false );
			mw.metawidget( 'buildWidgets', {
				type: crud.type
			} );

			page.find( 'h1' ).text( 'New Contact' );
			page.find( '#nav-create' ).show();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).hide();
		} else {
			mw.metawidget( 'setReadOnly', true );
			var person = _loadById( crud.id );
			mw.metawidget( 'buildWidgets', person );

			page.find( 'h1' ).text( person.firstname + ' ' + person.surname );
			page.find( '#nav-create' ).hide();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).show();
		}
	} );

	crud.cancel = function( event ) {

		var page = $( event ).parents( 'article' );

		$.mobile.changePage( page.data( 'summary-page' ) + '.html', {
			transition: 'slide',
			reverse: true
		} );
	};

	crud.edit = function( event ) {

		var page = $( event ).parents( 'article' );
		page.find( '#metawidget' ).metawidget( 'setReadOnly', false );
		page.find( '#metawidget' ).metawidget( 'buildWidgets' );

		page.find( '#nav-view' ).hide( 400 );
		page.find( '#nav-edit' ).show( 400 );
	};

	crud.view = function( event ) {

		var page = $( event ).parents( 'article' );
		page.find( '#metawidget' ).metawidget( 'setReadOnly', true );
		page.find( '#metawidget' ).metawidget( 'buildWidgets' );

		page.find( '#nav-edit' ).hide( 400 );
		page.find( '#nav-view' ).show( 400 );
	};

	/**
	 * Update
	 */

	crud.update = function( event ) {

		var page = $( event ).parents( 'article' );

		var mw = page.find( '#metawidget' );
		var mwData = mw.data( 'metawidget' );
		mw.metawidget( 'getWidgetProcessor', function( widgetProcessor ) {

			return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
		} ).save( mwData );

		_save( mwData.toInspect );

		$.mobile.changePage( page.data( 'summary-page' ) + '.html', {
			transition: 'slidedown'
		} );
	};

	/**
	 * Delete
	 */

	crud["delete"] = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' );
		var mwData = mw.data( 'metawidget' );
		_deleteById( mwData.toInspect.id );

		$.mobile.changePage( page.data( 'summary-page' ) + '.html', {
			transition: 'slide',
			reverse: true
		} );
	};

} )();