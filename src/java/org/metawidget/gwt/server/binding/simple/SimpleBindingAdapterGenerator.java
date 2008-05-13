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

package org.metawidget.gwt.server.binding.simple;

import java.io.PrintWriter;

import org.metawidget.gwt.client.binding.simple.SimpleBindingAdapter;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.StringUtils;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
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
 * type are traversed into</li>
 * </ul>
 * Clients needing to avoid such restrictions must write their own class that implements
 * <code>Binding</code>.
 *
 * @author Richard Kennard
 */

// Note: there is no equivalent BindingFactoryGenerator like there is a LayoutFactoryGenerator,
// because whereas all Layouts extend a well defined base class and therefore discovering them a
// well-constrained process, bindings are associated with domain objects so we need the user to
// specify the base class in their own application.gwt.xml
public class SimpleBindingAdapterGenerator
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
	 * We prefix variable names (eg. 'theContact') rather than lowercasing them (eg. 'contact') to
	 * avoid keyword clashes (eg. 'class')
	 */

	private final static String	VARIABLE_NAME_PREFIX	= "the";

	private final static int	WRITE_GETTER			= 0;

	private final static int	WRITE_TYPE_GETTER		= 1;

	private final static int	WRITE_SETTER			= 2;

	//
	//
	// Private members
	//
	//

	private TypeOracle			mTypeOracle;

	//
	//
	// Public methods
	//
	//

	@Override
	public String generate( TreeLogger logger, GeneratorContext context, String typeName )
	{
		// Lookup the type

		mTypeOracle = context.getTypeOracle();
		JClassType classType;

		try
		{
			classType = mTypeOracle.getType( typeName );
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
		composer.addImplementedInterface( SimpleBindingAdapter.class.getName() + "<" + classType.getQualifiedSourceName() + ">" );
		SourceWriter sourceWriter = composer.createSourceWriter( context, printWriter );

		if ( sourceWriter != null )
		{
			String variableName = VARIABLE_NAME_PREFIX + sourceClassName;
			sourceWriter.println();
			sourceWriter.println( "// Public methods" );

			// getProperty method

			sourceWriter.println();
			sourceWriter.println( "public Object getProperty( " + classType.getQualifiedSourceName() + " " + variableName + ", String... property ) {" );
			sourceWriter.indent();

			// Sanity check

			sourceWriter.println();
			sourceWriter.println( "// Sanity check" );
			sourceWriter.println();
			sourceWriter.println( "if ( property == null || property.length == 0 ) throw new RuntimeException( \"No property specified\" );" );

			// Write subtypes

			writeSubtypes( sourceWriter, classType, variableName, 0, WRITE_GETTER );

			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// getPropertyType method

			sourceWriter.println();
			sourceWriter.println( "public Class<?> getPropertyType( " + classType.getQualifiedSourceName() + " " + variableName + ", String... property ) {" );
			sourceWriter.indent();

			// Sanity check

			sourceWriter.println();
			sourceWriter.println( "// Sanity check" );
			sourceWriter.println();
			sourceWriter.println( "if ( property == null || property.length == 0 ) throw new RuntimeException( \"No property specified\" );" );

			// Write subtypes

			writeSubtypes( sourceWriter, classType, variableName, 0, WRITE_TYPE_GETTER );

			sourceWriter.outdent();
			sourceWriter.println( "}" );

			// setProperty method

			sourceWriter.println();
			sourceWriter.println( "public void setProperty( " + classType.getQualifiedSourceName() + " " + variableName + ", Object value, String... property ) {" );
			sourceWriter.indent();

			// Sanity check

			sourceWriter.println();
			sourceWriter.println( "// Sanity check" );
			sourceWriter.println();
			sourceWriter.println( "if ( property == null || property.length == 0 ) throw new RuntimeException( \"No property specified\" );" );

			// Write subtypes

			writeSubtypes( sourceWriter, classType, variableName, 0, WRITE_SETTER );

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

	private void writeSubtypes( SourceWriter sourceWriter, JClassType classType, String variableName, int propertyIndex, int writeType )
	{
		// For each subclass...

		for ( JClassType subtype : classType.getSubtypes() )
		{
			// ...write its subclass-level properties...

			writeProperties( sourceWriter, subtype, variableName, propertyIndex, true, classType, writeType );
		}

		// ...and for the base class write every superclass

		JClassType typeTraversal = classType;

		while ( typeTraversal != null )
		{
			writeProperties( sourceWriter, typeTraversal, variableName, propertyIndex, false, classType, writeType );

			typeTraversal = typeTraversal.getSuperclass();
		}

		// ...or error for an unknown property

		sourceWriter.println();
		sourceWriter.println( "// Unknown" );
		sourceWriter.println();
		sourceWriter.println( "throw new RuntimeException( \"Unknown property '\" + property[" + propertyIndex + "] + \"'\" );" );
	}

	private void writeProperties( SourceWriter sourceWriter, JClassType classType, String variableName, int propertyIndex, boolean writeInstanceOf, JClassType parentType, int writeType )
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
				propertyName = methodName.substring( ClassUtils.JAVABEAN_GET_PREFIX.length() );
			}
			else if ( methodName.startsWith( ClassUtils.JAVABEAN_IS_PREFIX ) )
			{
				propertyName = methodName.substring( ClassUtils.JAVABEAN_IS_PREFIX.length() );
			}
			else
			{
				continue;
			}

			String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

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

			sourceWriter.println( "if ( \"" + lowercasedPropertyName + "\".equals( property[" + propertyIndex + "] )) {" );
			sourceWriter.indent();

			int nextPropertyIndex = propertyIndex + 1;

			// ...recursively if necessary...

			JClassType nestedClassType = returnType.isClass();

			if ( nestedClassType != null )
			{
				if ( nestedClassType.getPackage().getName().startsWith( parentType.getPackage().getName() ) )
				{
					String nestedVariableName = VARIABLE_NAME_PREFIX + nestedClassType.getSimpleSourceName();
					sourceWriter.println( nestedClassType.getParameterizedQualifiedSourceName() + " " + nestedVariableName + " = " + currentVariableName + StringUtils.SEPARATOR_DOT_CHAR + methodName + "();" );

					switch( writeType )
					{
						case WRITE_GETTER:
							sourceWriter.println( "if ( property.length == " + nextPropertyIndex + " ) return " + nestedVariableName + ";" );
							break;

						case WRITE_TYPE_GETTER:
							sourceWriter.println( "if ( property.length == " + nextPropertyIndex + " ) return " + getWrapperType( returnType ).getQualifiedSourceName() + ".class;" );
							break;

						case WRITE_SETTER:
							try
							{
								String setterMethodName = "set" + propertyName;
								classType.getMethod( setterMethodName, new JType[] { returnType } );
								sourceWriter.println( "if ( property.length == " + nextPropertyIndex + " ) { " + currentVariableName + StringUtils.SEPARATOR_DOT_CHAR + setterMethodName + "( (" + getWrapperType( returnType ).getParameterizedQualifiedSourceName() + ") value ); return; }" );
							}
							catch ( NotFoundException e )
							{
								sourceWriter.println( "if ( property.length == " + nextPropertyIndex + " ) throw new RuntimeException( \"No setter for property '" + lowercasedPropertyName + "'\" );" );
							}
							break;
					}

					writeSubtypes( sourceWriter, nestedClassType, nestedVariableName, nextPropertyIndex, writeType );
					sourceWriter.outdent();
					sourceWriter.println( "}" );
					continue;
				}
			}

			// ...or non-recursively

			sourceWriter.println( "if ( property.length > " + nextPropertyIndex + " ) throw new RuntimeException( \"Cannot traverse into property '" + lowercasedPropertyName + ".\" + property[" + nextPropertyIndex + "] + \"'\" );" );

			switch( writeType )
			{
				case WRITE_GETTER:
					sourceWriter.println( "return " + currentVariableName + StringUtils.SEPARATOR_DOT_CHAR + methodName + "();" );
					break;

				case WRITE_TYPE_GETTER:
					sourceWriter.println( "return " + getWrapperType( returnType ).getQualifiedSourceName() + ".class;" );
					break;

				case WRITE_SETTER:
					try
					{
						String setterMethodName = "set" + propertyName;
						classType.getMethod( setterMethodName, new JType[] { returnType } );
						sourceWriter.println( currentVariableName + StringUtils.SEPARATOR_DOT_CHAR + setterMethodName + "( (" + getWrapperType( returnType ).getParameterizedQualifiedSourceName() + ") value );" );
						sourceWriter.println( "return;" );
					}
					catch ( NotFoundException e )
					{
						sourceWriter.println( "throw new RuntimeException( \"No setter for property '" + lowercasedPropertyName + "'\" );" );
					}
					break;
			}

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

	private JType getWrapperType( JType classType )
	{
		JPrimitiveType primitiveType = classType.isPrimitive();

		if ( primitiveType == null )
			return classType;

		if ( primitiveType.equals( JPrimitiveType.BOOLEAN ) )
			return mTypeOracle.findType( Boolean.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.BYTE ) )
			return mTypeOracle.findType( Byte.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.CHAR ) )
			return mTypeOracle.findType( Character.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.DOUBLE ) )
			return mTypeOracle.findType( Double.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.FLOAT ) )
			return mTypeOracle.findType( Float.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.INT ) )
			return mTypeOracle.findType( Integer.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.LONG ) )
			return mTypeOracle.findType( Long.class.getName() );

		if ( primitiveType.equals( JPrimitiveType.SHORT ) )
			return mTypeOracle.findType( Short.class.getName() );

		throw new RuntimeException( "No wrapper for " + primitiveType );
	}
}
