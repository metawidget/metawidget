package org.metawidget.gwt.client.ui.binding.simple;

import java.io.Serializable;
import java.util.Set;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.binding.Binding;

import com.google.gwt.core.client.GWT;

public class SimpleBinding
	extends Binding
{
	//
	//
	// Private members
	//
	//

	private SimpleBindingWrapper	mBindingWrapper;

	private Set<String[]>			mBindings;

	//
	//
	// Constructor
	//
	//

	public SimpleBinding( GwtMetawidget metawidget )
	{
		super( metawidget );

		Serializable toInspect = metawidget.getToInspect();

		mBindingWrapper = (SimpleBindingWrapper) GWT.create( toInspect.getClass() );
		mBindingWrapper.setToBind( toInspect );
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void bind( String... names )
	{
		getMetawidget().setValue( mBindingWrapper.getAttribute( names ), names );
		mBindings.add( names );
	}

	@Override
	public void save()
	{
		if ( mBindings == null )
			return;

		for( String[] names : mBindings )
		{
			mBindingWrapper.setAttribute( getMetawidget().getValue( names ), names );
		}
	}
}
