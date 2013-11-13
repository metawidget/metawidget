var crud = crud || {};

( function() {

	'use strict';

	/**
	 * Model
	 */

	var _id = 10;
	
	var _model = [ {
		id: 0,
		firstname: "Homer",
		surname: "Simpson"
	}, {
		id: 1,
		firstname: "Marge",
		surname: "Simpson",
		retired: true
	} ];

	function _loadById( id ) {
	
		for( var loop = 0, length = _model.length; loop < length; loop++ ) {
			
			if ( _model[loop].id === id ) {
				return _model[loop];
			}
		}
	}
	
	function _save( entity ) {
		
		if ( entity.id === undefined ) {
			entity.id = _id++;
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
	} );

	$( document ).on( 'pageinit', '#detail-page', function( event ) {

		var page = $( event.target );

		// This should autoinit in JQuery Mobile 1.4.0

		page.find( '#metawidget' ).metawidget();
		page.find( '#metawidget' ).metawidget( "option", "inspectionResultProcessors", [ function( inspectionResult, mw, toInspect, type, names ) {

			// Simulate asynchronous schema lookup (e.g. from a REST service)

			setTimeout( function() {
				var schema = {
					properties: {
						firstname: {
							type: "string"
						},
						surname: {
							type: "string"
						},
						retired: {
							type: "boolean"
						}
					}
				};
				mw._refresh( schema );
			}, 1 );

		} ] );
	} );

	/**
	 * Create
	 */
	
	crud.create = function( event ) {

		delete crud.id;
		var page = $( event ).parents( 'article' );

		$.mobile.changePage( page.data( 'detail-page' ) + '.html', {
			transition: 'slide'
		} );
	};

	/**
	 * Retrieve
	 */
	
	$( document ).on( 'pagebeforeshow', '#detail-page', function( event ) {

		var page = $( event.target );
		var mw = page.find( '#metawidget' );

		if ( crud.id === undefined ) {
			mw.metawidget( 'option', 'readOnly', false );
			mw.metawidget( 'buildWidgets', {} );

			page.find( '#nav-create' ).show();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).hide();
		} else {
			// Simulate asynchronous data lookup (e.g. from a REST service)

			setTimeout( function() {			

				mw.metawidget( 'option', 'readOnly', true );
				mw.metawidget( 'buildWidgets', _loadById( crud.id ));
			}, 1 );

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
		page.find( '#metawidget' ).metawidget( 'option', 'readOnly', false );
		page.find( '#metawidget' ).metawidget( 'buildWidgets' );

		page.find( '#nav-view' ).hide( 400 );
		page.find( '#nav-edit' ).show( 400 );
	};

	crud.view = function( event ) {

		var page = $( event ).parents( 'article' );
		page.find( '#metawidget' ).metawidget( 'option', 'readOnly', true );
		page.find( '#metawidget' ).metawidget( 'buildWidgets' );

		page.find( '#nav-edit' ).hide( 400 );
		page.find( '#nav-view' ).show( 400 );
	};

	/**
	 * Update
	 */
	
	crud.update = function( event ) {

		var page = $( event ).parents( 'article' );
		
		var mw = page.find( '#metawidget' ).data( 'metawidget' );
		page.find( '#metawidget' ).metawidget( 'getWidgetProcessor', function( widgetProcessor ) {

			return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
		} ).save( mw );

		_save( mw.toInspect );

		$.mobile.changePage( page.data( 'summary-page' ) + '.html', {
			transition: 'slidedown'
		} );
	};

	/**
	 * Delete
	 */
	
	crud["delete"] = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' ).data( 'metawidget' );		
		_deleteById( mw.toInspect.id );

		$.mobile.changePage( page.data( 'summary-page' ) + '.html', {
			transition: 'slide',
			reverse: true
		} );
	};

} )();