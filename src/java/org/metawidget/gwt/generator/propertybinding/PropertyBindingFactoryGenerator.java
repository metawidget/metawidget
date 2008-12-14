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

package org.metawidget.gwt.generator.propertybinding;

import java.io.PrintWriter;

import org.metawidget.gwt.client.propertybinding.PropertyBinding;
import org.metawidget.gwt.client.propertybinding.PropertyBindingFactory;
import org.metawidget.util.simple.StringUtils;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generator for PropertyBindingFactory.
 *
 * @author Richard Kennard
 */

public class PropertyBindingFactoryGenerator
	extends Generator
{
	//
	// Public methods
	//

	@Override
	public String generate( TreeLogger logger, GeneratorContext context, String typeName )
	{
		// Lookup the type

		TypeOracle typeOracle = context.getTypeOracle();
		JClassType classType;

		try
		{
			classType = typeOracle.getType( typeName );
		}
		catch ( NotFoundException e )
		{
			throw new RuntimeException( e );
		}

		String packageName = classType.getPackage().getName();
		String sourceClassName = classType.getSimpleSourceName();
		String bindingClassName = sourceClassName + "Impl";
		PrintWriter printWriter = context.tryCreate( logger, packageName, bindingClassName );

		// Already generated?

		String qualifiedBindingClassName = packageName + StringUtils.SEPARATOR_DOT_CHAR + bindingClassName;

		if ( printWriter == null )
			return qualifiedBindingClassName;

		// Start the PropertyBindingFactoryGenerator class

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory( packageName, bindingClassName );
		composer.addImplementedInterface( PropertyBindingFactory.class.getName() );
		composer.addImport( "org.metawidget.gwt.client.ui.GwtMetawidget" );
		composer.addImport( PropertyBinding.class.getName() );

		SourceWriter sourceWriter = composer.createSourceWriter( context, printWriter );

		if ( sourceWriter != null )
		{
			// Write the method

			sourceWriter.println();
			sourceWriter.println( "// Public methods" );
			sourceWriter.println();
			sourceWriter.println( "public PropertyBinding newBinding( Class<? extends PropertyBinding> bindingClass, GwtMetawidget metawidget ) {" );
			sourceWriter.indent();

			// Write PropertyBinding subtypes

			try
			{
				JClassType bindingClass = typeOracle.getType( PropertyBinding.class.getName() );

				for( JClassType subtype : bindingClass.getSubtypes() )
				{
					sourceWriter.println( "if ( " + subtype.getQualifiedSourceName() + ".class.equals( bindingClass )) return new " + subtype.getQualifiedSourceName() + "( metawidget );" );
				}
			}
			catch( NotFoundException e )
			{
				// Fail gracefully
			}

			sourceWriter.println( "throw new RuntimeException( \"Unknown binding \" + bindingClass );" );

			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// End the PropertyBindingFactoryGenerator class

			sourceWriter.commit( logger );
		}

		return qualifiedBindingClassName;
	}
}
