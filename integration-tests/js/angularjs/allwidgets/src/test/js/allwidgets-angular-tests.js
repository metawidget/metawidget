// Metawidget (licensed under LGPL)
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

( function() {

	'use strict';

	describe( "AngularJS AllWidgets", function() {

		it( "tests every sort of widget", function() {

			angular.bootstrap( document, [ 'allWidgets' ] );

			expect( $( '#table-allWidgetsTextbox-label' ).prop( 'for' ) ).toBe( 'allWidgetsTextbox' );
			expect( $( '#table-allWidgetsTextbox-label' ).text() ).toBe( 'Textbox:' );
			expect( $( '#allWidgetsTextbox' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsTextbox' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsTextbox' ).attr( 'name' ) ).toBeUndefined();
			expect( $( '#allWidgetsTextbox' ).attr( 'ng-model' ) ).toBe( 'allWidgets.textbox' );
			expect( $( '#allWidgetsTextbox' ).attr( 'ng-required' ) ).toBe( 'true' );
			expect( $( '#allWidgetsTextbox' ).attr( 'placeholder' ) ).toBe( 'A Placeholder' );
			expect( $( '#table-allWidgetsTextbox-row td' )[1].innerHTML ).toBe( '*' );
			$( '#allWidgetsTextbox' ).val( 'Textbox1' ).change();

			expect( $( '#table-allWidgetsLimitedTextbox-label' ).prop( 'for' ) ).toBe( 'allWidgetsLimitedTextbox' );
			expect( $( '#table-allWidgetsLimitedTextbox-label' ).text() ).toBe( 'Limited Textbox:' );
			expect( $( '#allWidgetsLimitedTextbox' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsLimitedTextbox' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsLimitedTextbox' ).attr( 'ng-model' ) ).toBe( 'allWidgets.limitedTextbox' );
			expect( $( '#allWidgetsLimitedTextbox' ).attr( 'maxlength' ) ).toBeUndefined();
			expect( $( '#allWidgetsLimitedTextbox' ).attr( 'ng-maxlength' ) ).toBe( '20' );
			expect( $( '#allWidgetsLimitedTextbox' ).attr( 'ng-required' ) ).toBeUndefined();
			expect( $( '#table-allWidgetsLimitedTextbox-row td' )[1].innerHTML ).toBe( '' );
			$( '#allWidgetsLimitedTextbox' ).val( 'Limited Textbox1' ).change();

			expect( $( '#table-allWidgetsTextarea-label' ).prop( 'for' ) ).toBe( 'allWidgetsTextarea' );
			expect( $( '#table-allWidgetsTextarea-label' ).text() ).toBe( 'Textarea:' );
			expect( $( '#allWidgetsTextarea' )[0].tagName ).toBe( 'TEXTAREA' );
			expect( $( '#allWidgetsTextarea' ).attr( 'ng-model' ) ).toBe( 'allWidgets.textarea' );
			$( '#allWidgetsTextarea' ).val( 'Textarea1' ).change();

			expect( $( '#table-allWidgetsPassword-label' ).prop( 'for' ) ).toBe( 'allWidgetsPassword' );
			expect( $( '#table-allWidgetsPassword-label' ).text() ).toBe( 'Password:' );
			expect( $( '#allWidgetsPassword' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsPassword' )[0].type ).toBe( 'password' );
			expect( $( '#allWidgetsPassword' ).attr( 'ng-model' ) ).toBe( 'allWidgets.password' );
			$( '#allWidgetsPassword' ).val( 'Password1' ).change();

			expect( $( '#table-allWidgetsNumber-label' ).prop( 'for' ) ).toBe( 'allWidgetsNumber' );
			expect( $( '#table-allWidgetsNumber-label' ).text() ).toBe( 'Number:' );
			expect( $( '#allWidgetsNumber' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNumber' )[0].type ).toBe( 'number' );
			expect( $( '#allWidgetsNumber' ).attr( 'ng-model' ) ).toBe( 'allWidgets.number' );
			$( '#allWidgetsNumber' ).val( '311' ).change();

			expect( $( '#table-allWidgetsRangedNumber-label' ).prop( 'for' ) ).toBe( 'allWidgetsRangedNumber' );
			expect( $( '#table-allWidgetsRangedNumber-label' ).text() ).toBe( 'Ranged Number:' );
			expect( $( '#allWidgetsRangedNumber' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsRangedNumber' )[0].type ).toBe( 'range' );
			expect( $( '#allWidgetsRangedNumber' ).attr( 'ng-model' ) ).toBe( 'allWidgets.rangedNumber' );
			expect( $( '#allWidgetsRangedNumber' ).attr( 'min' ) ).toBe( '1' );
			expect( $( '#allWidgetsRangedNumber' ).attr( 'max' ) ).toBe( '100' );
			$( '#allWidgetsRangedNumber' ).val( '33' ).change();

			expect( $( '#table-allWidgetsBoolean-label' ).prop( 'for' ) ).toBe( 'allWidgetsBoolean' );
			expect( $( '#table-allWidgetsBoolean-label' ).text() ).toBe( 'Boolean:' );
			expect( $( '#allWidgetsBoolean' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsBoolean' )[0].type ).toBe( 'checkbox' );
			expect( $( '#allWidgetsBoolean' ).attr( 'ng-model' ) ).toBe( 'allWidgets.boolean' );

			expect( $( '#table-allWidgetsDropdown-label' ).prop( 'for' ) ).toBe( 'allWidgetsDropdown' );
			expect( $( '#table-allWidgetsDropdown-label' ).text() ).toBe( 'Dropdown:' );
			expect( $( '#allWidgetsDropdown' )[0].tagName ).toBe( 'SELECT' );
			expect( $( '#allWidgetsDropdown' ).attr( 'ng-model' ) ).toBe( 'allWidgets.dropdown' );
			expect( $( '#allWidgetsDropdown option' )[0].value ).toBe( '' );
			expect( $( '#allWidgetsDropdown option' )[1].value ).toBe( 'foo1' );
			expect( $( '#allWidgetsDropdown option' )[2].value ).toBe( 'dropdown1' );
			expect( $( '#allWidgetsDropdown option' )[3].value ).toBe( 'bar1' );
			expect( $( '#allWidgetsDropdown option' ).length ).toBe( 4 );
			$( '#allWidgetsDropdown' ).val( 'foo1' ).change();

			expect( $( '#table-allWidgetsDropdownWithLabels-label' ).prop( 'for' ) ).toBe( 'allWidgetsDropdownWithLabels' );
			expect( $( '#table-allWidgetsDropdownWithLabels-label' ).text() ).toBe( 'Dropdown With Labels:' );
			expect( $( '#allWidgetsDropdownWithLabels' )[0].tagName ).toBe( 'SELECT' );
			expect( $( '#allWidgetsDropdownWithLabels' ).attr( 'ng-model' ) ).toBe( 'allWidgets.dropdownWithLabels' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[0].value ).toBe( '' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[1].value ).toBe( 'foo2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[1].text ).toBe( 'Foo #2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[2].value ).toBe( 'dropdown2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[2].text ).toBe( 'Dropdown #2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[3].value ).toBe( 'bar2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[3].text ).toBe( 'Bar #2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[4].value ).toBe( 'baz2' );
			expect( $( '#allWidgetsDropdownWithLabels option' )[4].text ).toBe( 'Baz #2' );
			expect( $( '#allWidgetsDropdownWithLabels option' ).length ).toBe( 5 );
			$( '#allWidgetsDropdownWithLabels' ).val( 'bar2' ).change();

			expect( $( '#table-allWidgetsNotNullDropdown-label' ).prop( 'for' ) ).toBe( 'allWidgetsNotNullDropdown' );
			expect( $( '#table-allWidgetsNotNullDropdown-label' ).text() ).toBe( 'Not Null Dropdown:' );
			expect( $( '#allWidgetsNotNullDropdown' )[0].tagName ).toBe( 'SELECT' );
			expect( $( '#allWidgetsNotNullDropdown' ).attr( 'ng-model' ) ).toBe( 'allWidgets.notNullDropdown' );
			expect( $( '#allWidgetsNotNullDropdown option' )[0].value ).toBe( '-1' );
			expect( $( '#allWidgetsNotNullDropdown option' )[1].value ).toBe( '0' );
			expect( $( '#allWidgetsNotNullDropdown option' )[2].value ).toBe( '1' );
			expect( $( '#allWidgetsNotNullDropdown option' ).length ).toBe( 3 );
			expect( $( '#table-allWidgetsNotNullDropdown-row td' )[1].innerHTML ).toBe( '*' );
			$( '#allWidgetsNotNullDropdown' ).val( '1' ).change();

			expect( $( '#table-allWidgetsNestedWidgets-label' ).prop( 'for' ) ).toBe( 'allWidgetsNestedWidgets' );
			expect( $( '#table-allWidgetsNestedWidgets-label' ).text() ).toBe( 'Nested Widgets:' );
			expect( $( '#table-allWidgetsNestedWidgetsFurtherNestedWidgets-label' ).text() ).toBe( 'Further Nested Widgets:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgets' )[0].tagName ).toBe( 'METAWIDGET' );
			expect( $( '#table-allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1-label' ).text() ).toBe( 'Nested Textbox 1:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' ).attr( 'ng-model' ) ).toBe( 'allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox1' );
			$( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' ).val( 'Textbox 1.1 (further)' ).change();

			expect( $( '#table-allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2-label' ).text() ).toBe( 'Nested Textbox 2:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' ).attr( 'ng-model' ) ).toBe( 'allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox2' );
			$( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' ).val( 'Textbox 2.2 (further)' ).change();

			expect( $( '#table-allWidgetsNestedWidgetsNestedTextbox1-label' ).text() ).toBe( 'Nested Textbox 1:' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1' ).attr( 'ng-model' ) ).toBe( 'allWidgets.nestedWidgets.nestedTextbox1' );
			$( '#allWidgetsNestedWidgetsNestedTextbox1' ).val( 'Textbox 1.1' ).change();

			expect( $( '#table-allWidgetsNestedWidgetsNestedTextbox2-label' ).text() ).toBe( 'Nested Textbox 2:' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2' ).attr( 'ng-model' ) ).toBe( 'allWidgets.nestedWidgets.nestedTextbox2' );
			$( '#allWidgetsNestedWidgetsNestedTextbox2' ).val( 'Textbox 2.2' ).change();

			expect( $( '#table-allWidgetsReadOnlyNestedWidgets-label' ).prop( 'for' ) ).toBe( 'allWidgetsReadOnlyNestedWidgets' );
			expect( $( '#table-allWidgetsReadOnlyNestedWidgets-label' ).text() ).toBe( 'Read Only Nested Widgets:' );
			expect( $( '#table-allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgets-label' ).text() ).toBe( 'Further Nested Widgets:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgets' )[0].tagName ).toBe( 'METAWIDGET' );
			expect( $( '#table-allWidgetsReadOnlyNestedWidgetsNestedTextbox1-label' ).text() ).toBe( 'Nested Textbox 1:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1' ).text() ).toBe( 'Nested Textbox 1' );
			expect( $( '#table-allWidgetsReadOnlyNestedWidgetsNestedTextbox2-label' ).text() ).toBe( 'Nested Textbox 2:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2' ).text() ).toBe( 'Nested Textbox 2' );

			expect( $( '#table-allWidgetsNestedWidgetsDontExpand-label' ).prop( 'for' ) ).toBe( 'allWidgetsNestedWidgetsDontExpand' );
			expect( $( '#table-allWidgetsNestedWidgetsDontExpand-label' ).text() ).toBe( 'Nested Widgets Dont Expand:' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand' ).attr( 'ng-model' ) ).toBe( 'allWidgets.nestedWidgetsDontExpand' );

			expect( $( '#table-allWidgetsReadOnlyNestedWidgetsDontExpand-label' ).prop( 'for' ) ).toBe( 'allWidgetsReadOnlyNestedWidgetsDontExpand' );
			expect( $( '#table-allWidgetsReadOnlyNestedWidgetsDontExpand-label' ).text() ).toBe( 'Read Only Nested Widgets Dont Expand:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand' ).attr( 'ng-bind' ) ).toBe( 'allWidgets.readOnlyNestedWidgetsDontExpand' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand' ).text() ).toBe( '[object Object]' );

			expect( $( '#table-allWidgetsDate-label' ).prop( 'for' ) ).toBe( 'allWidgetsDate' );
			expect( $( '#table-allWidgetsDate-label' ).text() ).toBe( 'Date:' );
			expect( $( '#allWidgetsDate' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsDate' )[0].type ).toBe( 'date' );
			expect( $( '#allWidgetsDate' ).attr( 'ng-model' ) ).toBe( 'allWidgets.date' );

			expect( $( '#hidden' ).length ).toBe( 0 );

			expect( $( 'h1' ).text() ).toBe( 'Section Break' );

			expect( $( '#table-allWidgetsReadOnly-label' ).prop( 'for' ) ).toBe( 'allWidgetsReadOnly' );
			expect( $( '#table-allWidgetsReadOnly-label' ).text() ).toBe( 'Read Only:' );
			expect( $( '#allWidgetsReadOnly' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnly' ).text() ).toBe( 'Read Only Value' );

			expect( $( '#table-allWidgetsCollection-label' ).prop( 'for' ) ).toBe( 'allWidgetsCollection' );
			expect( $( '#table-allWidgetsCollection-label' ).text() ).toBe( 'Collection:' );
			expect( $( '#allWidgetsCollection' )[0].tagName ).toBe( 'TABLE' );
			expect( $( '#allWidgetsCollection tr td' )[0].innerHTML ).toBe( 'element1' );
			expect( $( '#allWidgetsCollection tr td' )[1].innerHTML ).toBe( 'element2' );

			expect( $( '#table-allWidgetsObjectCollection-label' ).prop( 'for' ) ).toBe( 'allWidgetsObjectCollection' );
			expect( $( '#table-allWidgetsObjectCollection-label' ).text() ).toBe( 'Object Collection:' );
			expect( $( '#allWidgetsObjectCollection' )[0].tagName ).toBe( 'TABLE' );
			expect( $( '#allWidgetsObjectCollection tr th' )[0].innerHTML ).toBe( 'Name' );
			expect( $( '#allWidgetsObjectCollection tr th' )[1].innerHTML ).toBe( 'Description' );
			expect( $( '#allWidgetsObjectCollection tr td' )[0].innerHTML ).toBe( 'element1' );
			expect( $( '#allWidgetsObjectCollection tr td' )[1].innerHTML ).toBe( 'First' );
			expect( $( '#allWidgetsObjectCollection tr td' )[2].innerHTML ).toBe( 'element2' );
			expect( $( '#allWidgetsObjectCollection tr td' )[3].innerHTML ).toBe( 'Second' );

			expect( $( '#actionsSave' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#actionsSave' ).attr( 'type' ) ).toBe( 'button' );
			expect( $( '#actionsSave' ).attr( 'ng-click' ) ).toBe( 'actions.save()' );
			expect( $( '#actionsSave' ).text() ).toBe( 'Save' );

			$( '#actionsSave' ).click();

			expect( $( '#allWidgetsTextbox' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsTextbox' ).text() ).toBe( 'Textbox1' );
			expect( $( '#allWidgetsLimitedTextbox' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsLimitedTextbox' ).text() ).toBe( 'Limited Textbox1' );
			expect( $( '#allWidgetsTextarea' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsTextarea' ).text() ).toBe( 'Textarea1' );
			expect( $( '#allWidgetsPassword' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsPassword' ).text() ).toBe( '*********' );
			expect( $( '#allWidgetsNumber' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsNumber' ).text() ).toBe( '311' );
			expect( $( '#allWidgetsRangedNumber' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsRangedNumber' ).text() ).toBe( '33' );
			expect( $( '#allWidgetsDropdown' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsDropdown' ).text() ).toBe( 'foo1' );
			expect( $( '#allWidgetsDropdownWithLabels' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsDropdownWithLabels' ).text() ).toBe( 'Bar #2' );
			expect( $( '#allWidgetsNotNullDropdown' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsNotNullDropdown' ).text() ).toBe( '1' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' ).text() ).toBe( 'Textbox 1.1 (further)' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' ).text() ).toBe( 'Textbox 2.2 (further)' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1' ).text() ).toBe( 'Textbox 1.1' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2' ).text() ).toBe( 'Textbox 2.2' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1' ).text() ).toBe( 'Nested Textbox 1' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2' ).text() ).toBe( 'Nested Textbox 2' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsDate' )[0].tagName ).toBe( 'OUTPUT' );

			expect( $( 'h1' ).text() ).toBe( 'Section Break' );
			expect( $( '#allWidgetsReadOnly' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnly' ).text() ).toBe( 'Read Only Value' );
			expect( $( '#allWidgetsCollection' )[0].tagName ).toBe( 'TABLE' );
			expect( $( '#allWidgetsCollection tr td' )[0].innerHTML ).toBe( 'element1' );
			expect( $( '#allWidgetsCollection tr td' )[1].innerHTML ).toBe( 'element2' );
			
			expect( $( '#allWidgetsObjectCollection' )[0].tagName ).toBe( 'TABLE' );
			expect( $( '#allWidgetsObjectCollection tr th' )[0].innerHTML ).toBe( 'Name' );
			expect( $( '#allWidgetsObjectCollection tr th' )[1].innerHTML ).toBe( 'Description' );
			expect( $( '#allWidgetsObjectCollection tr td' )[0].innerHTML ).toBe( 'element1' );
			expect( $( '#allWidgetsObjectCollection tr td' )[1].innerHTML ).toBe( 'First' );
			expect( $( '#allWidgetsObjectCollection tr td' )[2].innerHTML ).toBe( 'element2' );
			expect( $( '#allWidgetsObjectCollection tr td' )[3].innerHTML ).toBe( 'Second' );
		} );
	} );
} )();