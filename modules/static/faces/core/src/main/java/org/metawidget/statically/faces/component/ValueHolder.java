package org.metawidget.statically.faces.component;

/**
 * Marks the widget as an editable component.
 */

public interface ValueHolder {

	//
	// Methods
	//

	void setValue( String value );

	String getValue();

	void setConverter( String converter );
}
