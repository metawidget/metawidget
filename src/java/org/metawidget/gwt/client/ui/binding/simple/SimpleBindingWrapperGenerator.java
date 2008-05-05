package org.metawidget.gwt.client.ui.binding.simple;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;

public class SimpleBindingWrapperGenerator
	extends Generator
{
	//
	//
	// Public methods
	//
	//

	@Override
	public String generate( TreeLogger logger, GeneratorContext context, String typeName )
	{
		return new SimpleBindingWrapperCreator( logger, context, typeName ).createWrapper();
	}
}
