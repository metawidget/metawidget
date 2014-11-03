// Metawidget ${project.version}
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

	describe( "The Web Component", function() {

		it( "creates nested Metawidgets", function() {

			var metawidgetPrototype = document.getRegisteredElement( 'x-metawidget' ).prototype;
			metawidgetPrototype._pipeline = {};
			var nestedMetawidget = metawidgetPrototype.buildNestedMetawidget( {
				name: 'foo'
			} );

			expect( nestedMetawidget.tagName ).toBe( 'X-METAWIDGET' );
			expect( nestedMetawidget.getMetawidget() ).toBe( nestedMetawidget );
			expect( nestedMetawidget.getAttribute( 'path' ) ).toBe( 'object.foo' );
			expect( nestedMetawidget.getAttribute( 'readonly' ) ).toBe( 'false' );
			expect( nestedMetawidget.config ).toBe( metawidgetPrototype._pipeline );
		} );
	} );
} )();