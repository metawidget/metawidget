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

package org.metawidget.gwt.server.binding;

import java.io.PrintWriter;

import org.metawidget.gwt.client.binding.BindingAdapter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.StringUtils;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generator for BindingAdapters.
 * <p>
 * GwtMetawidget requires clients to supply an explicit interface through which to execute binding calls. In most cases, clients
 * can use <code>BindingAdapterGenerator</code> to automatically generate this as a secondary class. First, they modify their
 * <code>.gwt.xml</code> file to include...
 * <p>
 * <code>
 * &lt;generate-with class="org.metawidget.gwt.server.binding.BindingAdapterGenerator"&gt;
 * 		&lt;when-type-assignable class="com.foo.BusinessClass"/&gt;
 * &lt;/generate-with&gt;
 * </code>
 * <p>
 * ...then they call...
 * <p>
 * <code>
 * BindingAdapter&lt;BusinessClass&gt; bindingAdapter = (BindingAdapter&lt;BusinessClass&gt;) GWT.create( BusinessClass.class );
 * bindingAdapter.setAdaptee( (BusinessClass) businessClass );
 * metawidget.setBinding( bindingAdapter );
 * </code>
 *
 * @author Richard Kennard
 */

public class BindingAdapterGenerator
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
		String bindingClassName = sourceClassName + "BindingAdapter";
		PrintWriter printWriter = context.tryCreate( logger, packageName, bindingClassName );

		// Already generated?

		String qualifiedBindingClassName = packageName + StringUtils.SEPARATOR_DOT_CHAR + bindingClassName;

		if ( printWriter == null )
			return qualifiedBindingClassName;

		// Start the BindingAdapter subclass

		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory( packageName, bindingClassName );
		composer.setSuperclass( BindingAdapter.class.getName() + "<" + sourceClassName + ">" );
		SourceWriter sourceWriter = composer.createSourceWriter( context, printWriter );

		if ( sourceWriter != null )
		{
			// Write the methods

			sourceWriter.println();
			sourceWriter.println( "// Public methods" );
			sourceWriter.println();
			sourceWriter.println( "public Object getProperty( String property ) {" );
			sourceWriter.indent();

			// Sanity check

			sourceWriter.println();
			sourceWriter.println( sourceClassName + " adaptee = getAdaptee();" );

			// For each superclass (not including java.lang.Object)...

			JClassType typeTraversal = classType;

			while ( !Object.class.getName().equals( typeTraversal.getQualifiedSourceName() ) )
			{
				boolean writtenAProperty = false;

				for ( JMethod method : typeTraversal.getMethods() )
				{
					// ...if the method is a public property...

					if ( !method.isPublic() )
						continue;

					if ( method.getReturnType() == null )
						continue;

					String methodName = method.getName();
					String propertyName;

					if ( methodName.startsWith( ClassUtils.JAVABEAN_GET_PREFIX ) )
					{
						propertyName = StringUtils.lowercaseFirstLetter( methodName.substring( ClassUtils.JAVABEAN_GET_PREFIX.length() ) );
					}
					else if ( methodName.startsWith( ClassUtils.JAVABEAN_IS_PREFIX ) )
					{
						propertyName = StringUtils.lowercaseFirstLetter( methodName.substring( ClassUtils.JAVABEAN_IS_PREFIX.length() ) );
					}
					else
					{
						continue;
					}

					if ( !writtenAProperty )
					{
						sourceWriter.println();
						sourceWriter.println( "// " + typeTraversal.getSimpleSourceName() + " properties" );
						sourceWriter.println();

						writtenAProperty = true;
					}

					// ...call our adaptee...

					sourceWriter.println( "if ( \"" + propertyName + "\".equals( property )) return adaptee." + methodName + "();" );
				}

				typeTraversal = typeTraversal.getSuperclass();
			}

			// ...or error for an unknown property

			sourceWriter.println();
			sourceWriter.println( "// Everything else" );
			sourceWriter.println();
			sourceWriter.println( "throw new RuntimeException( \"Unknown property '\" + property + \"'\" );" );
			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// End the BindingAdapter subclass

			sourceWriter.commit( logger );
		}

		return qualifiedBindingClassName;
	}
}
