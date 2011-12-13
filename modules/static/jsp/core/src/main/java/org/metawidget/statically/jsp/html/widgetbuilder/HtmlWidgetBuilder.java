// Metawidget
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

package org.metawidget.statically.jsp.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private statics
	//

	private final static String	MAX_LENGTH	= "maxLength";

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new StaticXmlStub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new StaticXmlStub();
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, fail gracefully with a text box

		if ( type == null ) {
			return createHtmlInputText( attributes );
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );
		
		// Support mandatory Booleans.
		
		if ( Boolean.class.equals( clazz ) && TRUE.equals( REQUIRED ) ) {
		    return createHtmlCheckbox();
		}
		
		// Lookups
		
		String jspLookup = attributes.get( JSP_LOOKUP );
		
		if ( jspLookup != null && !"".equals( jspLookup ) ) {
		    HtmlSelect select = new HtmlSelect();
		    // Not sure if this is a legitimate replacement of HtmlWidgetBuilderUtils.evaluate(jspLookup, metawidget);
		    addSelectItems( select, CollectionUtils.fromString(jspLookup), null, attributes);
		    return select;
		}
		
		String lookup = attributes.get( LOOKUP );
		
		if ( lookup != null && !"".equals( lookup ) ) {
		    HtmlSelect select = new HtmlSelect();
		    addSelectItems(select, CollectionUtils.fromString(lookup), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS) ), attributes);
		    return select;
		}

		if ( clazz != null ) {
		    
		    // Booleans
		    
		    if ( Boolean.class.equals( clazz ) ) {
		        return createHtmlCheckbox();
		    }

			// Primitives

			if ( clazz.isPrimitive() ) {
				return createHtmlInputText( attributes );
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new HtmlTag( "textarea" );
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					HtmlTag inputSecret = new HtmlTag( "input" );
					inputSecret.putAttribute( "type", "secret" );
					inputSecret.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );
					return inputSecret;
				}

				return createHtmlInputText( attributes );
			}
    		
    		// Character
    		
    		if ( Character.class.equals( clazz ) ) {
    		    HtmlTag characterInput = createHtmlInputText( attributes );
    		    characterInput.putAttribute( MAX_LENGTH, "1" );
    		    return characterInput;
    		}
    		
    		// Date
    		
    		if ( Date.class.equals( clazz ) ) {
    		    return createHtmlInputText( attributes );
    		}
    		
    		// Numbers
    		
    		if ( Number.class.isAssignableFrom( clazz ) ) {
    		    return createHtmlInputText( attributes );
    		}
    		
    		// Collections
            if ( Collection.class.isAssignableFrom( clazz ) ) {
                return new StaticXmlStub();
            }
            
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createHtmlInputText( attributes );
		}

		// Not simple

		return null;
	}

	//
	// Private methods
	//

    private HtmlTag createHtmlCheckbox() {
        HtmlTag checkbox = new HtmlTag( "input" );
        checkbox.putAttribute( "type" , "checkbox" );
        
        return checkbox;
    }

    private HtmlTag createHtmlInputText( Map<String, String> attributes ) {
		HtmlTag inputText = new HtmlTag( "input" );
		inputText.putAttribute( "type", "text" );
		inputText.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );

		return inputText;
	}
    
    private void addSelectItems(HtmlSelect select, List<String> values, List<String> labels, Map<String, String> attributes) {
        if ( values == null ) {
            return;
        }
        
        // Empty option.
        
        if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
            addSelectItem(select, "", null);
        }
        
        // Add the rest of the select items.
        
        for ( int i = 0, length = values.size(); i < length; i++ ) {
            String value = values.get(i);
            String label = null;
            
            if( labels != null && !labels.isEmpty() ) {
                label = labels.get(i);
            }
            
            addSelectItem(select, value, label);
        }
        
        return;
    }

    private void addSelectItem(HtmlSelect select, String value, String label) {
        SelectItem selectItem = new SelectItem();
        selectItem.setValue( value );
        
        if( label != null ) {
            selectItem.setTextContent( label );
        }
        
        select.getChildren().add(selectItem);
        return;
    }
}
