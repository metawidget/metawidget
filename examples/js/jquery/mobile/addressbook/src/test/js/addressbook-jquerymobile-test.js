describe( 'The JQuery Mobile AddressBook', function() {

	it( 'supports searching contacts', function() {
		
		var _done = false;

		$( document ).one( 'pageshow', '#contacts-page', function( event ) {

			setTimeout( function() {

				var page = $( event.target );
				expect( page.find( 'h1' ).text() ).toBe( 'Contacts' );
	
				var summary = page.find( '#summary' );
				expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Simpson' );
				expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
				expect( summary.find( 'li:eq(2) a' ).text() ).toBe( 'Nedward Flanders' );
				expect( summary.find( 'li:eq(3) a' ).text() ).toBe( 'Maude Flanders' );
				expect( summary.find( 'li:eq(4) a' ).text() ).toBe( 'Charles Montgomery Burns' );
				expect( summary.find( 'li:eq(5) a' ).text() ).toBe( 'Waylon Smithers' );
				expect( summary.find( 'li' ).size() ).toBe( 6 );
				summary.find( 'li:eq(0) a' ).click();
				
				var mw = page.find( '#metawidget' );
				expect( mw.find( '#firstname' ).data( 'type' ) ).toBe( 'search' );
				expect( mw.find( '#surname' ).data( 'type' ) ).toBe( 'search' );
				mw.find( '#surname' ).val( 'Flanders' );
				mw.find( '#search' ).click();

				expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Nedward Flanders' );
				expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Maude Flanders' );
				expect( summary.find( 'li' ).size() ).toBe( 2 );

				mw.find( '#firstname' ).val( 'ude' );
				mw.find( '#search' ).click();

				expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Maude Flanders' );
				expect( summary.find( 'li' ).size() ).toBe( 1 );

				mw.find( '#firstname' ).val( null );
				mw.find( '#surname' ).val( null );
				mw.find( '#search' ).click();

				expect( summary.find( 'li' ).size() ).toBe( 6 );

				_done = true;
			}, 1000 );
		} );

		waitsFor( function() {
		
			return _done;
		});
	} );
	
	it( 'supports editing contacts', function() {
		
		var _done = false;

		$.mobile.changePage( 'index.html',
				{
					reloadPage: true
				});

		$( document ).one( 'pageshow', '#contacts-page', function( event ) {

			setTimeout( function() {

				var page = $( event.target );
				expect( page.find( 'h1' ).text() ).toBe( 'Contacts' );
	
				var summary = page.find( '#summary' );
				expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Simpson' );
				expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
				summary.find( 'li:eq(0) a' ).click();
	
				$( document ).one( 'pageshow', '#contact-page', function( event ) {
					
					page = $( event.target );
					var mw = page.find( '#metawidget' );

					expect( mw.find( '#firstname' )[0].tagName ).toBe( 'OUTPUT' );
					expect( mw.find( '#firstname' ).text() ).toBe( 'Homer' );
					expect( mw.find( '#surname' )[0].tagName ).toBe( 'OUTPUT' );
					expect( mw.find( '#surname' ).text() ).toBe( 'Simpson' );
					expect( mw.find( '#communications tbody tr:eq(0) td:eq(0)' ).text() ).toBe( 'Telephone' );
					expect( mw.find( '#communications tbody tr:eq(0) td:eq(1)' ).text() ).toBe( '(939) 555-0113' );
					expect( mw.find( '#communications tbody tr:eq(0) td:eq(2)' )[0] ).toBeUndefined();
					
					page.find( '#viewEdit' ).click();
					
					mw.one( 'buildEnd', function() {
	
						expect( mw.find( '#firstname' )[0].tagName ).toBe( 'INPUT' );
						expect( mw.find( '#firstname' ).val() ).toBe( 'Homer' );
						expect( mw.find( '#surname' )[0].tagName ).toBe( 'INPUT' );
						expect( mw.find( '#surname' ).val() ).toBe( 'Simpson' );
						expect( mw.find( '#communications tbody tr:eq(0) td:eq(0)' ).text() ).toBe( 'Telephone' );
						expect( mw.find( '#communications tbody tr:eq(0) td:eq(1)' ).text() ).toBe( '(939) 555-0113' );
						expect( mw.find( '#communications tbody tr:eq(0) td:eq(2) button' ).val() ).toBe( 'Edit' );
						
						mw.find( '#communications tbody tr:eq(0) td:eq(2) button' ).click();
						
						$( document ).one( 'pageshow', '#communication-page', function( event ) {
							
							var communicationPage = $( event.target );
							expect( communicationPage.find( 'h1' ).text() ).toBe( 'Edit Communication' );
							
							var communicationMw = communicationPage.find( '#metawidget' );
							
							expect( communicationMw.find( '#type' )[0].tagName ).toBe( 'SELECT' );
							expect( communicationMw.find( '#type' ).val() ).toBe( 'Telephone' );
							expect( communicationMw.find( '#value' )[0].tagName ).toBe( 'INPUT' );
							expect( communicationMw.find( '#value' ).val() ).toBe( '(939) 555-0113' );

							communicationPage.find( '#editUpdate' ).click();

							$( document ).one( 'pageshow', '#contact-page', function( event ) {
							
								page = $( event.target );
								page.find( '#viewEdit' ).click();
								
								mw = page.find( '#metawidget' );
								
								mw.one( 'buildEnd', function() {
								
									mw.find( '#firstname' ).val( 'Homer Jay' );
									page.find( '#editUpdate' ).click();
				
									$( document ).one( 'pageshow', '#contacts-page', function( event ) {
									
										page = $( event.target );
										summary = page.find( '#summary' );
										expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
										expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
						
										_done = true;
									} );
								} );
							} );
						} );
					} );
				} );			
			}, 1000 );
		} );

		waitsFor( function() {

			return _done;
		});
	} );

	it( 'supports creating business contacts', function() {
		
		var _done = false;

		$.mobile.changePage( 'index.html',
				{
					reloadPage: true
				});
		
		$( document ).one( 'pageshow', '#contacts-page', function( event ) {

			var page = $( event.target );
			expect( page.find( 'h1' ).text() ).toBe( 'Contacts' );

			var summary = page.find( '#summary' );
			expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
			expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
			page.find( '#summaryCreateBusiness' ).click();

			$( document ).one( 'pageshow', '#contact-page', function( event ) {
				
				page = $( event.target );
				var mw = page.find( '#metawidget' );

				mw.find( '#firstname' ).val( 'Business' );
				mw.find( '#surname' ).val( 'Contact' );
				mw.find( '#company' ).val( 'Some Company' );
				
				page.find( '#createUpdate' ).click();

				$( document ).one( 'pageshow', '#contacts-page', function( event ) {
				
					page = $( event.target );
					summary = page.find( '#summary' );
					expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
					expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
					expect( summary.find( 'li:eq(6) a' ).text() ).toBe( 'Business Contact' );
	
					summary.find( 'li:eq(6) a' ).click();
					
					$( document ).one( 'pageshow', '#contact-page', function( event ) {
						
						setTimeout( function() {
							
							page = $( event.target );
							var mw = page.find( '#metawidget' );
							expect( mw.find( '#firstname' )[0].tagName ).toBe( 'OUTPUT' );
							expect( mw.find( '#firstname' ).text() ).toBe( 'Business' );
							expect( mw.find( '#surname' )[0].tagName ).toBe( 'OUTPUT' );
							expect( mw.find( '#surname' ).text() ).toBe( 'Contact' );
							expect( mw.find( '#company' )[0].tagName ).toBe( 'OUTPUT' );
							expect( mw.find( '#company' ).text() ).toBe( 'Some Company' );
							
							page.find( '#viewEdit' ).click();
							
							mw.one( 'buildEnd', function() {
			
								page.find( '#editDelete' ).click();

								$( document ).one( 'pageshow', '#contacts-page', function( event ) {
									
									page = $( event.target );
									summary = page.find( '#summary' );
									expect( summary.find( 'li' ).size() ).toBe( 6 );
									
									_done = true;
								} );								
							} );
						} );
					} );
				} );
			} );			
		} );

		waitsFor( function() {

			return _done;
		});
	} );

	it( 'supports creating personal contacts', function() {
		
		var _done = false;

		$.mobile.changePage( 'index.html',
				{
					reloadPage: true
				});
		
		$( document ).one( 'pageshow', '#contacts-page', function( event ) {

			var page = $( event.target );
			expect( page.find( 'h1' ).text() ).toBe( 'Contacts' );

			var summary = page.find( '#summary' );
			expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
			expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
			page.find( '#summaryCreatePersonal' ).click();

			$( document ).one( 'pageshow', '#contact-page', function( event ) {
				
				page = $( event.target );
				var mw = page.find( '#metawidget' );
				
				mw.find( '#firstname' ).val( 'Personal' );
				mw.find( '#surname' ).val( 'Contact' );
				mw.find( '#dateOfBirth' ).val( '1/1/1975' );
				
				page.find( '#createUpdate' ).click();

				$( document ).one( 'pageshow', '#contacts-page', function( event ) {
				
					page = $( event.target );
					summary = page.find( '#summary' );
					expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
					expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
					expect( summary.find( 'li:eq(6) a' ).text() ).toBe( 'Personal Contact' );
	
					summary.find( 'li:eq(6) a' ).click();
					
					$( document ).one( 'pageshow', '#contact-page', function( event ) {
						
						setTimeout( function() {
							
							page = $( event.target );
							var mw = page.find( '#metawidget' );
							expect( mw.find( '#firstname' )[0].tagName ).toBe( 'OUTPUT' );
							expect( mw.find( '#firstname' ).text() ).toBe( 'Personal' );
							expect( mw.find( '#surname' )[0].tagName ).toBe( 'OUTPUT' );
							expect( mw.find( '#surname' ).text() ).toBe( 'Contact' );
							expect( mw.find( '#dateOfBirth' )[0].tagName ).toBe( 'OUTPUT' );
							expect( mw.find( '#dateOfBirth' ).text() ).toBe( '1/1/1975' );
							
							page.find( '#viewEdit' ).click();
							
							mw.one( 'buildEnd', function() {
			
								page.find( '#editDelete' ).click();

								$( document ).one( 'pageshow', '#contacts-page', function( event ) {
									
									page = $( event.target );
									summary = page.find( '#summary' );
									expect( summary.find( 'li' ).size() ).toBe( 6 );
									
									_done = true;
								} );									
							} );
						} );
					} );
				} );
			} );			
		} );

		waitsFor( function() {

			return _done;
		});
	} );
} );