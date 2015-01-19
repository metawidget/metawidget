describe( 'The JQuery Mobile Overridden Test', function() {

	it( 'tests overrides', function() {

		var _done = false;

		$( document ).one( 'pageshow', '#overridden-page', function( event ) {

			expect( $( '#overriddenFoo-label' ).prop( 'for' ) ).toBe( 'overriddenFoo' );
			expect( $( '#overriddenFoo-label' ).text() ).toBe( 'Foo:' );
			expect( $( '#overriddenFoo' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#overriddenFoo' )[0].type ).toBe( 'password' );
			expect( $( '#overriddenFoo' ).val() ).toBe( 'Foo' );
			expect( $( '#overriddenBar' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#overriddenBar' )[0].type ).toBe( 'text' );
			expect( $( '#overriddenBar' ).val() ).toBe( 'Bar' );
			$( '#overriddenFoo' ).val( 'NewFoo' );

			$( '#actionsSave' ).click();

			expect( $( '#overriddenFoo' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#overriddenFoo' )[0].type ).toBe( 'password' );
			expect( $( '#overriddenFoo' ).val() ).toBe( 'NewFoo' );
			expect( $( '#overriddenBar' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#overriddenBar' ).text() ).toBe( 'Bar' );

			_done = true;
		} );

		waitsFor( function() {

			return _done;
		} );
	} );
} );