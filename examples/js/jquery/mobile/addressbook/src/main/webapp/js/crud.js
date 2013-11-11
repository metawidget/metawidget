var crud = crud || {};

( function() {

	'use strict';

	/**
	 * Model
	 */

	var _model = [ {
		id: 0,
		firstname: "Homer",
		surname: "Simpson"
	}, {
		id: 1,
		firstname: "Marge",
		surname: "Simpson"
	} ];

	/**
	 * Page init
	 */

	$( document ).on( 'pagebeforeshow', '#summary-page', function( event ) {

		var page = $( event.target );
		var entity = page.data( 'crud' );
		var summary = page.find( '#summary' );
		var listview = $( '<ul>' ).attr( 'data-role', 'listview' );

		$.each( _model, function( key, value ) {

			var anchor = $( '<a>' ).on( 'click', function() {

				crud.id = value.id;

				$.mobile.changePage( entity + '.html', {
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
		var mw = page.find( '#metawidget' );
		
		// TODO: this should autoinit in JQuery Mobile 1.4.0
		
		mw.metawidget();
		mw.metawidget( "option", "inspector", new metawidget.inspector.JsonSchemaInspector( {
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
		} ));
		//mw.path = page.data( 'crud' );
	} );

	$( document ).on( 'pagebeforeshow', '#detail-page', function( event ) {

		var page = $( event.target );
		var mw = page.find( '#metawidget' )[0].getMetawidget();

		if ( crud.id === undefined ) {
			mw.toInspect = {};
			mw.readOnly = false;
			mw.buildWidgets();

			page.find( '#nav-create' ).show();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).hide();
		} else {
			$.ajax( {
				url: 'rest/' + mw.path + '/' + crud.id,
				dataType: 'json'
			} ).done( function( data ) {

				mw.toInspect = data;
				mw.readOnly = true;
				mw.buildWidgets();
			} );

			page.find( '#nav-create' ).hide();
			page.find( '#nav-edit' ).hide();
			page.find( '#nav-view' ).show();
		}
	} );

	// Create, Retrieve, Update, Delete

	crud.create = function( event ) {

		delete crud.id;

		var page = $( event ).parents( 'article' );
		var entity = page.data( 'crud' );

		$.mobile.changePage( entity + '.html', {
			transition: 'slide'
		} );
	};

	crud.cancel = function( event ) {

		$.mobile.changePage( 'index.html', {
			transition: 'slide',
			reverse: true
		} );
	};

	crud.edit = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' )[0].getMetawidget();
		mw.readOnly = false;
		mw.buildWidgets();

		page.find( '#nav-view' ).hide( 400 );
		page.find( '#nav-edit' ).show( 400 );
	};

	crud.view = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' )[0].getMetawidget();
		mw.readOnly = true;
		mw.buildWidgets();

		page.find( '#nav-edit' ).hide( 400 );
		page.find( '#nav-view' ).show( 400 );
	};

	crud.update = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' )[0].getMetawidget();
		mw.getWidgetProcessor( function( widgetProcessor ) {

			return widgetProcessor instanceof metawidget.widgetprocessor.SimpleBindingProcessor;
		} ).save( mw );

		$.ajax( {
			url: 'rest/' + mw.path,
			type: 'POST',
			data: JSON.stringify( mw.toInspect ),
			contentType: 'application/json',
			dataType: 'json',
			success: function() {

				$.mobile.changePage( mw.crudSummaryUrl, {
					transition: 'slidedown'
				} );
			},
			error: function( jqXHR ) {

				console.log( jqXHR.responseText );
			}
		} );
	};

	crud["delete"] = function( event ) {

		var page = $( event ).parents( 'article' );
		var mw = page.find( '#metawidget' )[0].getMetawidget();

		$.ajax( {
			url: mw.crudRestUrl + mw.path + '/' + mw.toInspect.id,
			type: 'DELETE',
			success: function() {

				$.mobile.changePage( mw.crudSummaryUrl, {
					transition: 'slidedown'
				} );
			},
			error: function( jqXHR, textStatus, errorThrown ) {

				console.log( jqXHR.responseText );
			}
		} );
	};

} )();