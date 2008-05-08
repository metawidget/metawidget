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
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generator for BindingAdapters.
 * <p>
 * GwtMetawidget requires clients to supply an explicit interface through which to execute binding
 * calls. In most cases, clients can use <code>BindingAdapterGenerator</code> to automatically
 * generate this as a secondary class. First, they modify their <code>.gwt.xml</code> file to
 * include...
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
 * <p>
 * This generator <em>statically</em> generates code for all levels of all possible properties
 * (eg. <code>contact.address.street</code>), including subtypes of properties. Because this
 * could quickly become very large, we impose the following restrictions:
 * <ul>
 * <li>only properties whose return type is in the same package, or a subpackage, of the parent
 * type are considered</li>
 * <li>only subtypes and base types in the same package, or a subpackage, of the parent type
 * are considered</li>
 * </ul>
 * Clients needing to avoid such restrictions must write their own class that implements
 * <code>Binding</code>.
 *
 * @author Richard Kennard
 */

public class BindingAdapterGenerator
	extends Generator
{
	//
	//
	// Private statics
	//
	//

	/**
	 * Prefix to use for all variable names.
	 * <p>
	 * We prefix variable names (eg. 'theContact') rather than lowercasing them
	 * (eg. 'contact') to avoid keyword clashes (eg. 'class')
	 */

	private final static String	VARIABLE_NAME_PREFIX = "the";

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
			sourceWriter.println( "public Object getProperty( String... property ) {" );
			sourceWriter.indent();

			// Sanity check

			sourceWriter.println();
			sourceWriter.println( "// Sanity check" );
			sourceWriter.println();
			sourceWriter.println( "if ( property == null || property.length == 0 ) throw new RuntimeException( \"No property specified\" );" );

			// Write subtypes

			String variableName = VARIABLE_NAME_PREFIX + sourceClassName;
			sourceWriter.println( sourceClassName + " " + variableName + " = getAdaptee();" );

			writeSubtypes( sourceWriter, classType, variableName, 0 );

			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// End the BindingAdapter subclass

			sourceWriter.commit( logger );
		}

		return qualifiedBindingClassName;
	}

	//
	//
	// Private methods
	//
	//

	private void writeSubtypes( SourceWriter sourceWriter, JClassType classType, String variableName, int propertyIndex )
	{
		// For each subclass...

		for ( JClassType subtype : classType.getSubtypes() )
		{
			// ...write its subclass-level properties...

			writeProperties( sourceWriter, subtype, variableName, propertyIndex, true );
		}

		// ...and for the base class write every superclass within our package

		JClassType typeTraversal = classType;

		while ( typeTraversal.getPackage().getName().startsWith( classType.getPackage().getName() ) )
		{
			writeProperties( sourceWriter, typeTraversal, variableName, propertyIndex, false );

			typeTraversal = typeTraversal.getSuperclass();
		}

		// ...or error for an unknown property

		sourceWriter.println();
		sourceWriter.println( "// Unknown" );
		sourceWriter.println();
		sourceWriter.println( "throw new RuntimeException( \"Unknown property '\" + property[" + propertyIndex + "] + \"'\" );" );
	}

	private void writeProperties( SourceWriter sourceWriter, JClassType classType, String variableName, int propertyIndex, boolean writeInstanceOf )
	{
		String currentVariableName = variableName;
		boolean writtenAProperty = false;

		for ( JMethod method : classType.getMethods() )
		{
			// ...if the method is a public property...

			if ( !method.isPublic() )
				continue;

			JType returnType = method.getReturnType();

			if ( returnType == null )
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

			// Open the block

			if ( !writtenAProperty )
			{
				sourceWriter.println();
				sourceWriter.println( "// " + classType.getSimpleSourceName() + " properties" );
				sourceWriter.println();

				if ( writeInstanceOf )
				{
					sourceWriter.println( "if ( " + currentVariableName + " instanceof " + classType.getName() + " ) {" );
					sourceWriter.indent();
					String superclassVariableName = currentVariableName;
					currentVariableName = VARIABLE_NAME_PREFIX + classType.getSimpleSourceName();
					sourceWriter.println( classType.getParameterizedQualifiedSourceName() + " " + currentVariableName + " = (" + classType.getParameterizedQualifiedSourceName() + ") " + superclassVariableName + ";" );
				}

				writtenAProperty = true;
			}

			// ...call our adaptee...

			sourceWriter.println( "if ( \"" + propertyName + "\".equals( property[" + propertyIndex + "] )) {" );
			sourceWriter.indent();

			int nextPropertyIndex = propertyIndex + 1;

			// ...recursively if necessary...

			if ( returnType instanceof JClassType )
			{
				JClassType nestedClassType = (JClassType) returnType;

				if ( nestedClassType.getPackage().getName().startsWith( classType.getPackage().getName() ) )
				{
					String nestedVariableName = VARIABLE_NAME_PREFIX + nestedClassType.getSimpleSourceName();
					sourceWriter.println( nestedClassType.getParameterizedQualifiedSourceName() + " " + nestedVariableName + " = " + currentVariableName + StringUtils.SEPARATOR_DOT_CHAR + methodName + "();" );
					sourceWriter.println( "if ( property.length == " + nextPropertyIndex + " ) return " + nestedVariableName + ";" );
					writeSubtypes( sourceWriter, nestedClassType, nestedVariableName, nextPropertyIndex );
					sourceWriter.outdent();
					sourceWriter.println( "}" );
					continue;
				}
			}

			// ...or non-recursively

			sourceWriter.println( "if ( property.length > " + nextPropertyIndex + " ) throw new RuntimeException( \"Cannot traverse into property '" + propertyName + ".\" + property[" + nextPropertyIndex + "] + \"'\" );" );
			sourceWriter.println( "return " + currentVariableName + StringUtils.SEPARATOR_DOT_CHAR + methodName + "();" );
			sourceWriter.outdent();
			sourceWriter.println( "}" );
		}

		// Close the block

		if ( writtenAProperty && writeInstanceOf )
		{
			sourceWriter.outdent();
			sourceWriter.println( "}" );
		}
	}
}
