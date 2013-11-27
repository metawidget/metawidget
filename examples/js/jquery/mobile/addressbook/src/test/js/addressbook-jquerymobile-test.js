describe( 'The JQuery Mobile AddressBook', function() {

	it( 'supports editing contacts', function() {
		
		var _done = false;

		$( document ).one( 'pageshow', '#contacts-page', function( event ) {

			setTimeout( function() {

				var page = $( event.target );
				expect( page.find( 'h1' ).text() ).toBe( 'Contacts' );
	
				var summary = page.find( '#summary' );
				expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Simpson' );
				expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
				summary.find( 'li:eq(0) a' ).click();
	
				$( document ).one( 'pageshow', '#contact-page', function( event ) {
					
					console.log( 'pageshow #contact-page' );
					setTimeout( function() {
						
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
						
						setTimeout( function() {
		
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
									
									setTimeout( function() {
									
										mw = page.find( '#metawidget' );
										mw.find( '#firstname' ).val( 'Homer Jay' );
										page.find( '#editUpdate' ).click();
					
										$( document ).one( 'pageshow', '#contacts-page', function( event ) {
										
											page = $( event.target );
											summary = page.find( '#summary' );
											expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
											expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
							
											_done = true;
										} );
									}, 1000 );
								} );
							} );
						}, 1000 );
					}, 1000 );
				} );			
			}, 1000 );
		} );

		waitsFor( function() {

			return _done;
		});
	} );

	it( 'supports creating contacts', function() {
		
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
			page.find( '#summaryCreate' ).click();

			$( document ).one( 'pageshow', '#contact-page', function( event ) {
				
				setTimeout( function() {
				
					page = $( event.target );
					var mw = page.find( '#metawidget' );
					
					mw.find( '#firstname' ).val( 'Business' );
					mw.find( '#surname' ).val( 'Contact' );
					
					page.find( '#createUpdate' ).click();
	
					$( document ).one( 'pageshow', '#contacts-page', function( event ) {
					
						page = $( event.target );
						summary = page.find( '#summary' );
						expect( summary.find( 'li:eq(0) a' ).text() ).toBe( 'Homer Jay Simpson' );
						expect( summary.find( 'li:eq(1) a' ).text() ).toBe( 'Marjorie Simpson' );
						expect( summary.find( 'li:eq(6) a' ).text() ).toBe( 'Business Contact' );
		
						_done = true;
					} );
				}, 1000 );
			} );			
		} );

		waitsFor( function() {

			return _done;
		});
	} );
} );