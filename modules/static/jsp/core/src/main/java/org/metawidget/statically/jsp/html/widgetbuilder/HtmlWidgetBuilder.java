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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Richard Kennard
 * @author Ryan Bradley
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private statics
	//

	private final static String	MAX_LENGTH	= "maxLength";
	
    private final int mMaximumColumnsInDataTable;
	
	//
	// Constructor
	//
	
	public HtmlWidgetBuilder() {
	    
	    this( new HtmlWidgetBuilderConfig() );
	}
	
	public HtmlWidgetBuilder( HtmlWidgetBuilderConfig config ) {
	    
	    mMaximumColumnsInDataTable = config.getMaximumColumnsInDataTable();
	}

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

		// JSP Lookup
		
        String jspLookup = attributes.get( JSP_LOOKUP );
        
        if ( jspLookup != null && !"".equals( jspLookup ) ) {
            HtmlSelect select = new HtmlSelect();
            // Not sure if this is a legitimate replacement of HtmlWidgetBuilderUtils.evaluate(jspLookup, metawidget);
            addSelectItems( select, CollectionUtils.fromString(jspLookup), null, attributes);
            return select;
        }		
		
		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, fail gracefully with a text box

		if ( type == null ) {
			return createHtmlInputText( attributes );
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );
		
		// Support mandatory Booleans.
		
		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
		    return createHtmlCheckbox();
		}
		
		// Lookups
		
		String lookup = attributes.get( LOOKUP );
		
		if ( lookup != null && !"".equals( lookup ) ) {
		    HtmlSelect select = new HtmlSelect();
		    addSelectItems(select, CollectionUtils.fromString(lookup), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS) ), attributes);
		    return select;
		}

		if ( clazz != null ) {

		    // Primitives

			if ( clazz.isPrimitive() ) {
			    
			    if ( boolean.class.equals( clazz ) ) {
			        return createHtmlCheckbox();
			    }
			    
			    if ( char.class.equals( clazz ) ) {
			        HtmlTag inputTag = new HtmlTag( "input" );
			        inputTag.putAttribute( MAX_LENGTH, "1" );
			        return inputTag;
			    }
			    
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
    		
    		// Support List and Array Collections
    		
    		if ( List.class.isAssignableFrom( clazz ) || clazz.isArray() ) {
    		    return createDataTableComponent( elementName, attributes, metawidget );
    		}
    		
    		// Unsupported Collections
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
	// Protected methods
	//

    /**
     * @param elementName
     *            such as ENTITY or PROPERTY. Can be useful in determining how to construct the EL
     *            for the table.
     */	
      
    protected StaticXmlWidget createDataTableComponent( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

        HtmlTable table = new HtmlTable();
        ForEachTag forEach = new ForEachTag();
        
        String items = attributes.get( NAME );
        
        if ( items != null ) {
            
            items = StaticJspUtils.wrapExpression( items );
        }
        
        forEach.putAttribute( "items", items );
        String var = "item";
        forEach.putAttribute( "var", var );
        
        // Add an empty row to the table to be used for column headers.
        
        table.getChildren().add( new HtmlTableRow() );
        
        table.getChildren().add( forEach );
        
        // Inspect the component type.
        
        String componentType = WidgetBuilderUtils.getComponentType( attributes );
        String inspectedType = null;
        
        if ( componentType != null ) {
            inspectedType = metawidget.inspect( null, componentType, (String[]) null );
        }
        
        // If there is no type...
        
        if ( inspectedType == null ) {
            // ...resort to a single column table...
            
            HtmlTableRow row = new HtmlTableRow();
            forEach.getChildren().add( row );
            
            Map<String, String> columnAttributes = CollectionUtils.newHashMap();
            columnAttributes.put( NAME, attributes.get( NAME ) );
            addColumnComponent( row, forEach, attributes, ENTITY, columnAttributes, metawidget );
        }
        
        // ...otherwise, iterate over the component type and add multiple columns.
        
        else {
            Element root = XmlUtils.documentFromString( inspectedType ).getDocumentElement();
            NodeList elements = root.getFirstChild().getChildNodes();
            addColumnComponents( table, forEach, attributes, elements, metawidget );
        }
        
        return table;
    }

    /**
     * Adds column components to the given table.
     * <p>
     * Clients can override this method to add additional columns, such as a 'Delete' button.
     */
    
    private void addColumnComponents(HtmlTable table, ForEachTag forEach, Map<String, String> attributes, NodeList elements, StaticXmlMetawidget metawidget ) {

        // At first, only add columns for the 'required' fields
        
        boolean onlyRequired = true;
        
        while ( true ) {
            
            // Create a new row for the forEach tag to iterate upon.
            
            HtmlTableRow row = new HtmlTableRow();
            
            // For each property...
            
            for ( int i = 0; i < elements.getLength(); i++ ) {
                
                Node node = elements.item( i );
                
                if ( !(node instanceof Element) ) {
                    continue;
                }
                
                Element element = (Element) node;
                
                // ...(not action)...
                
                if ( ACTION.equals( element.getNodeName() ) ) {
                    continue;
                }
                
                // ...that is visible...
                
                if ( TRUE.equals( element.getAttribute( HIDDEN ) ) ) {
                    continue;
                }

                // ...and is required...
                //
                // Note: this is a controversial choice. Our logic is that a) we need to limit
                // the number of columns somehow, and b) displaying all the required fields should
                // be enough to uniquely identify the row to the user. However, users may wish
                // to override this default behaviour
                
                if ( onlyRequired && !TRUE.equals( element.getAttribute( REQUIRED ) ) ) {
                    continue;
                }
                
                // ...add a column...
                
                addColumnComponent( row, forEach, attributes, PROPERTY, XmlUtils.getAttributesAsMap( element ), metawidget);
                
                // ...and a header for that column...
                
                addColumnHeader( table, XmlUtils.getAttributesAsMap( element ), metawidget );
                
                // ...up to a sensible maximum.
                
                if ( row.getChildren().size() == mMaximumColumnsInDataTable ) {
                    break;
                }
                
            }

            if ( !row.getChildren().isEmpty() ) {
                
                forEach.getChildren().add( row );
            }
            
            // If we couldn't add any 'required' columns, try again for every field.
            
            if ( !forEach.getChildren().isEmpty() || !onlyRequired ) {
                break;
            }            
            
            onlyRequired = false;
        }       
    }

    /**
     * Add an HtmlColumn component for the given attributes, to the given HtmlDataTable.
     * <p>
     * Clients can override this method to modify the column contents. For example, to place a link
     * around the text.
     *
     * @param tableAttributes
     *            the metadata attributes used to render the parent table. May be useful for
     *            determining the overall type of the row
     */
    
    protected void addColumnComponent( HtmlTableRow row, ForEachTag forEach, Map<String, String> tableAttributes, String elementName, Map<String, String> columnAttributes, StaticXmlMetawidget metawidget ) {

        // Add a new column to the current row of the table.
        
        HtmlTableCell cell = new HtmlTableCell();
        row.getChildren().add( cell );
        
        String valueExpression = forEach.getAttribute( "var" );
        if ( !ENTITY.equals( elementName ) ) {
            valueExpression += StringUtils.SEPARATOR_DOT_CHAR + columnAttributes.get( NAME );
        }
        
        OutTag out = new OutTag();
        out.putAttribute( "value", StaticJspUtils.wrapExpression( valueExpression ) );
        cell.getChildren().add( out );
        
    }
    
    private void addColumnHeader( HtmlTable table, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

        HtmlTableCell cell = new HtmlTableCell();
        HeaderTag header = new HeaderTag();
        header.setTextContent( metawidget.getLabelString( attributes ) );
        cell.getChildren().add( header );
        
        table.getChildren().get( 0 ).getChildren().add( cell );
        
    }    

    protected void addSelectItems( HtmlSelect select, List<String> values, List<String> labels, Map<String, String> attributes ) {
        
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
    
    private void addSelectItem( HtmlSelect select, String value, String label ) {
        SelectItem selectItem = new SelectItem();
        selectItem.putAttribute( "value" , value );
        
        if( label != null ) {
            selectItem.setTextContent( label );
        }
        
        select.getChildren().add(selectItem);
        return;
    }
    
}
