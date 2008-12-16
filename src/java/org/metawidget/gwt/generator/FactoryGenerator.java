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

package org.metawidget.gwt.generator;

import java.io.PrintWriter;

import org.metawidget.util.simple.StringUtils;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generator for generically generating factories, such as <code>InspectorFactory</code>,
 * <code>LayoutFactory</code> etc. Hooked up in <code>GwtMetawidget.gwt.xml</code>.
 *
 * @author Richard Kennard
 */

public class FactoryGenerator<F>
	extends Generator
{
	//
	// Protected methods
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

		// Work out what we're generating

		JMethod[] methods = classType.getMethods();

		if ( methods.length != 1 )
			throw new RuntimeException( "Factory interfaces should have one, and only one, method" );

		JMethod newMethod = methods[0];
		JParameter[] parameters = newMethod.getParameters();

		if ( parameters.length != 1 && parameters.length != 2 )
			throw new RuntimeException( "Factory interface methods should have only one or two parameters" );

		if ( parameters.length == 2 && !"org.metawidget.gwt.client.ui.GwtMetawidget".equals( parameters[1].getType().getQualifiedSourceName() ) )
			throw new RuntimeException( "If a factory interface method has two parameters, the second must be a GwtMetawidget" );

		// Start the FactoryGenerator

		JType returnType = newMethod.getReturnType();

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory( packageName, bindingClassName );
		composer.addImplementedInterface( classType.getQualifiedSourceName() );
		composer.addImport( "org.metawidget.gwt.client.ui.GwtMetawidget" );

		String qualifiedName = returnType.getQualifiedSourceName();
		composer.addImport( qualifiedName );

		SourceWriter sourceWriter = composer.createSourceWriter( context, printWriter );

		if ( sourceWriter != null )
		{
			// Write the method

			sourceWriter.println();
			sourceWriter.println( "// Public methods" );
			sourceWriter.println();

			String simpleName = returnType.getSimpleSourceName();
			sourceWriter.print( "public " + simpleName + " " + newMethod.getName() + "( Class<? extends " + simpleName + "> implementingClass" );
			if ( parameters.length == 2 )
				sourceWriter.println( ", GwtMetawidget metawidget" );
			sourceWriter.println( " ) {" );
			sourceWriter.indent();

			// Write subtypes

			try
			{
				JClassType bindingClass = typeOracle.getType( qualifiedName );

				for ( JClassType subtype : bindingClass.getSubtypes() )
				{
					// (ignore abstract xxxImpl types)

					if ( subtype.isAbstract() )
						continue;

					sourceWriter.print( "if ( " + subtype.getQualifiedSourceName() + ".class.equals( implementingClass )) return new " + subtype.getQualifiedSourceName() + "(" );

					if ( parameters.length == 2 )
						sourceWriter.println( " metawidget " );

					sourceWriter.println( ");" );
				}
			}
			catch ( NotFoundException e )
			{
				// Fail gracefully
			}

			sourceWriter.println( "throw new RuntimeException( \"Unknown type \" + implementingClass );" );

			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// End the FactoryGenerator

			sourceWriter.commit( logger );
		}

		return qualifiedBindingClassName;
	}
}
