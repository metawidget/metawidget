package org.metawidget.gwt.client.ui.binding.simple;

import java.io.Serializable;

public interface SimpleBindingWrapper
{
	//
	//
	// Methods
	//
	//

	Serializable getToBind();
	void setToBind( Serializable toBind );

	Object getAttribute( String... attribute );
	void setAttribute( Object value, String... attribute );
}
