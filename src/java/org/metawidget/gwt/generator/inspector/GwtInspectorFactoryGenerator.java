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

package org.metawidget.gwt.generator.inspector;

import java.io.PrintWriter;

import org.metawidget.gwt.client.inspector.GwtInspectorFactory;
import org.metawidget.inspector.iface.Inspector;
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
 * Generator for GwtInspectorFactory.
 *
 * @author Richard Kennard
 */

public class GwtInspectorFactoryGenerator
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

		// Start the GwtInspectorFactoryGenerator class

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory( packageName, bindingClassName );
		composer.addImplementedInterface( GwtInspectorFactory.class.getName() );
		composer.addImport( Inspector.class.getName() );

		SourceWriter sourceWriter = composer.createSourceWriter( context, printWriter );

		if ( sourceWriter != null )
		{
			// Write the method

			sourceWriter.println();
			sourceWriter.println( "// Public methods" );
			sourceWriter.println();
			sourceWriter.println( "public Inspector newInspector( Class<? extends Inspector> inspectorClass ) {" );
			sourceWriter.indent();

			// Write GwtInspector types

			try
			{
				JClassType inspectorClass = typeOracle.getType( Inspector.class.getName() );

				for( JClassType type : inspectorClass.getSubtypes() )
				{
					if ( type.isClass() == null )
						continue;

					sourceWriter.println( "if ( " + type.getQualifiedSourceName() + ".class.equals( inspectorClass )) return new " + type.getQualifiedSourceName() + "();" );
				}
			}
			catch( NotFoundException e )
			{
				// Fail gracefully
			}

			sourceWriter.println( "throw new RuntimeException( \"Unknown GwtInspector \" + inspectorClass );" );

			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// End the GwtInspectorFactoryGenerator class

			sourceWriter.commit( logger );
		}

		return qualifiedBindingClassName;
	}
}
