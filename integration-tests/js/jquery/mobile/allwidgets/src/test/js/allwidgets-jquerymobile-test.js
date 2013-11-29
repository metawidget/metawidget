describe( 'The JQuery Mobile All Widgets Test', function() {

	it( 'tests every sort of widget', function() {
		
		var _done = false;

		$( document ).one( 'pageshow', '#allwidgets-page', function( event ) {

			expect( $( '#allWidgetsTextbox-label' ).prop( 'for' ) ).toBe( 'allWidgetsTextbox' );
			expect( $( '#allWidgetsTextbox-label' ).text() ).toBe( 'Textbox:' );
			expect( $( '#allWidgetsTextbox' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsTextbox' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsTextbox' ).attr( 'required' ) ).toBe( 'required' );
			expect( $( '#allWidgetsTextbox' ).attr( 'placeholder' ) ).toBe( 'A Placeholder' );
			$( '#allWidgetsTextbox' ).val( 'Textbox1' );

			expect( $( '#allWidgetsLimitedTextbox-label' ).prop( 'for' ) ).toBe( 'allWidgetsLimitedTextbox' );
			expect( $( '#allWidgetsLimitedTextbox-label' ).text() ).toBe( 'Limited Textbox:' );
			expect( $( '#allWidgetsLimitedTextbox' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsLimitedTextbox' )[0].type ).toBe( 'text' );
			expect( $( '#allWidgetsLimitedTextbox' ).attr( 'maxlength' ) ).toBe( '20' );
			expect( $( '#allWidgetsLimitedTextbox' ).attr( 'required' ) ).toBeUndefined();
			$( '#allWidgetsLimitedTextbox' ).val( 'Limited Textbox1' );

			expect( $( '#allWidgetsTextarea-label' ).prop( 'for' ) ).toBe( 'allWidgetsTextarea' );
			expect( $( '#allWidgetsTextarea-label' ).text() ).toBe( 'Textarea:' );
			expect( $( '#allWidgetsTextarea' )[0].tagName ).toBe( 'TEXTAREA' );
			$( '#allWidgetsTextarea' ).val( 'Textarea1' );

			expect( $( '#allWidgetsPassword-label' ).prop( 'for' ) ).toBe( 'allWidgetsPassword' );
			expect( $( '#allWidgetsPassword-label' ).text() ).toBe( 'Password:' );
			expect( $( '#allWidgetsPassword' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsPassword' )[0].type ).toBe( 'password' );
			$( '#allWidgetsPassword' ).val( 'Password1' );

			expect( $( '#allWidgetsNumber-label' ).prop( 'for' ) ).toBe( 'allWidgetsNumber' );
			expect( $( '#allWidgetsNumber-label' ).text() ).toBe( 'Number:' );
			expect( $( '#allWidgetsNumber' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNumber' ).attr( 'type' ) ).toBe( 'number' );
			$( '#allWidgetsNumber' ).val( '311' );

			expect( $( '#allWidgetsRangedNumber-label' ).prop( 'for' ) ).toBe( 'allWidgetsRangedNumber' );
			expect( $( '#allWidgetsRangedNumber-label' ).text() ).toBe( 'Ranged Number:' );
			expect( $( '#allWidgetsRangedNumber' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsRangedNumber' ).data( 'type' ) ).toBe( 'range' );
			// TODO: expect( $( '#allWidgetsRangedNumber .ui-slider-handle' ).attr( 'style' ) ).toBe( 'left: 32%;' );
			// TODO: $( '#allWidgetsRangedNumber' ).slider( 'value', '33' );

			expect( $( '#allWidgetsBoolean-label' ).prop( 'for' ) ).toBe( 'allWidgetsBoolean' );
			expect( $( '#allWidgetsBoolean-label' ).text().trim() ).toBe( 'Boolean' );
			expect( $( '#allWidgetsBoolean' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsBoolean' )[0].type ).toBe( 'checkbox' );

			expect( $( '#allWidgetsDropdown-label' ).prop( 'for' ) ).toBe( 'allWidgetsDropdown' );
			expect( $( '#allWidgetsDropdown-label' ).text() ).toBe( 'Dropdown:' );
			expect( $( '#allWidgetsDropdown' )[0].tagName ).toBe( 'SELECT' );
			expect( $( '#allWidgetsDropdown option' )[0].value ).toBe( '' );
			expect( $( '#allWidgetsDropdown option' )[1].value ).toBe( 'foo1' );
			expect( $( '#allWidgetsDropdown option' )[2].value ).toBe( 'dropdown1' );
			expect( $( '#allWidgetsDropdown option' )[3].value ).toBe( 'bar1' );
			expect( $( '#allWidgetsDropdown option' ).length ).toBe( 4 );
			$( '#allWidgetsDropdown' ).val( 'foo1' );

			expect( $( '#allWidgetsDropdownWithLabels-label' ).prop( 'for' ) ).toBe( 'allWidgetsDropdownWithLabels' );
			expect( $( '#allWidgetsDropdownWithLabels-label' ).text() ).toBe( 'Dropdown With Labels:' );
			expect( $( '#allWidgetsDropdownWithLabels' )[0].tagName ).toBe( 'SELECT' );
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
			$( '#allWidgetsDropdownWithLabels' ).val( 'bar2' );

			expect( $( '#allWidgetsNotNullDropdown-label' ).prop( 'for' ) ).toBe( 'allWidgetsNotNullDropdown' );
			expect( $( '#allWidgetsNotNullDropdown-label' ).text() ).toBe( 'Not Null Dropdown:' );
			expect( $( '#allWidgetsNotNullDropdown' )[0].tagName ).toBe( 'SELECT' );
			expect( $( '#allWidgetsNotNullDropdown option' )[0].value ).toBe( '-1' );
			expect( $( '#allWidgetsNotNullDropdown option' )[1].value ).toBe( '0' );
			expect( $( '#allWidgetsNotNullDropdown option' )[2].value ).toBe( '1' );
			expect( $( '#allWidgetsNotNullDropdown option' ).length ).toBe( 3 );
			$( '#allWidgetsNotNullDropdown' ).val( '1' );

			expect( $( '#allWidgetsNestedWidgets-label' ).prop( 'for' ) ).toBe( 'allWidgetsNestedWidgets' );
			expect( $( '#allWidgetsNestedWidgets-label' ).text() ).toBe( 'Nested Widgets:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgets-label' ).text() ).toBe( 'Further Nested Widgets:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgets' )[0].tagName ).toBe( 'DIV' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1-label' ).text() ).toBe( 'Nested Textbox 1:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' )[0].type ).toBe( 'text' );
			$( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox1' ).val( 'Textbox 1.1 (further)' );

			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2-label' ).text() ).toBe( 'Nested Textbox 2:' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' )[0].type ).toBe( 'text' );
			$( '#allWidgetsNestedWidgetsFurtherNestedWidgetsNestedTextbox2' ).val( 'Textbox 2.2 (further)' );

			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1-label' ).text() ).toBe( 'Nested Textbox 1:' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox1' )[0].type ).toBe( 'text' );
			$( '#allWidgetsNestedWidgetsNestedTextbox1' ).val( 'Textbox 1.1' );

			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2-label' ).text() ).toBe( 'Nested Textbox 2:' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsNestedTextbox2' )[0].type ).toBe( 'text' );
			$( '#allWidgetsNestedWidgetsNestedTextbox2' ).val( 'Textbox 2.2' );

			expect( $( '#allWidgetsReadOnlyNestedWidgets-label' ).prop( 'for' ) ).toBe( 'allWidgetsReadOnlyNestedWidgets' );
			expect( $( '#allWidgetsReadOnlyNestedWidgets-label' ).text() ).toBe( 'Read Only Nested Widgets:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgets-label' ).text() ).toBe( 'Further Nested Widgets:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsFurtherNestedWidgets' )[0].tagName ).toBe( 'DIV' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1-label' ).text() ).toBe( 'Nested Textbox 1:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox1' ).text() ).toBe( 'Nested Textbox 1' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2-label' ).text() ).toBe( 'Nested Textbox 2:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsNestedTextbox2' ).text() ).toBe( 'Nested Textbox 2' );

			expect( $( '#allWidgetsNestedWidgetsDontExpand-label' ).prop( 'for' ) ).toBe( 'allWidgetsNestedWidgetsDontExpand' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand-label' ).text() ).toBe( 'Nested Widgets Dont Expand:' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand' )[0].tagName ).toBe( 'INPUT' );
			expect( $( '#allWidgetsNestedWidgetsDontExpand' )[0].type ).toBe( 'text' );

			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand-label' ).prop( 'for' ) ).toBe( 'allWidgetsReadOnlyNestedWidgetsDontExpand' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand-label' ).text() ).toBe( 'Read Only Nested Widgets Dont Expand:' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnlyNestedWidgetsDontExpand' ).text() ).toBe( '[object Object]' );

			expect( $( '#allWidgetsDate' ).attr( 'type' ) ).toBe( 'date' );

			expect( $( '#hidden' ).length ).toBe( 0 );

			expect( $( 'h1' )[0].innerHTML ).toBe( 'Section Break' );

			expect( $( '#allWidgetsReadOnly-label' ).prop( 'for' ) ).toBe( 'allWidgetsReadOnly' );
			expect( $( '#allWidgetsReadOnly-label' ).text() ).toBe( 'Read Only:' );
			expect( $( '#allWidgetsReadOnly' )[0].tagName ).toBe( 'OUTPUT' );
			expect( $( '#allWidgetsReadOnly' ).text() ).toBe( 'Read Only Value' );

			expect( $( '#allWidgetsCollection-label' ).prop( 'for' ) ).toBe( 'allWidgetsCollection' );
			expect( $( '#allWidgetsCollection-label' ).text() ).toBe( 'Collection:' );
			expect( $( '#allWidgetsCollection' )[0].tagName ).toBe( 'TABLE' );
			expect( $( '#allWidgetsCollection tr td' )[0].innerHTML ).toBe( 'element1' );
			expect( $( '#allWidgetsCollection tr td' )[1].innerHTML ).toBe( 'element2' );			

			expect( $( '#allWidgetsObjectCollection-label' ).prop( 'for' ) ).toBe( 'allWidgetsObjectCollection' );
			expect( $( '#allWidgetsObjectCollection-label' ).text() ).toBe( 'Object Collection:' );
			expect( $( '#allWidgetsObjectCollection' )[0].tagName ).toBe( 'TABLE' );
			expect( $( '#allWidgetsObjectCollection tr th' )[0].innerHTML ).toBe( 'Name' );
			expect( $( '#allWidgetsObjectCollection tr th' )[1].innerHTML ).toBe( 'Description' );
			expect( $( '#allWidgetsObjectCollection tr td' )[0].innerHTML ).toBe( 'element1' );
			expect( $( '#allWidgetsObjectCollection tr td' )[1].innerHTML ).toBe( 'First' );
			expect( $( '#allWidgetsObjectCollection tr td' )[2].innerHTML ).toBe( 'element2' );
			expect( $( '#allWidgetsObjectCollection tr td' )[3].innerHTML ).toBe( 'Second' );
			
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
			// TODO: expect( $( '#allWidgetsRangedNumber' ).text() ).toBe( '33' );			
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

			expect( $( 'h1' )[0].innerHTML ).toBe( 'Section Break' );
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
			
			_done = true;
		} );
		
		waitsFor( function() {

			return _done;
		});
	} );
} );