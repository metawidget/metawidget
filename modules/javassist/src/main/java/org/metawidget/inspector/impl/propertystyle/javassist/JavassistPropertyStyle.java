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

package org.metawidget.inspector.impl.propertystyle.javassist;

import java.lang.reflect.Method;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.CollectionUtils;

/**
 * PropertyStyle for environments that use JavaBeans and have Javassist available.
 * <p>
 * This PropertyStyle extends <code>JavaBeanPropertyStyle</code> to use the debug line numbering
 * information embedded in JVM bytecode to sort getters and setters according to their original
 * declaration order in the source code.
 * <p>
 * This saves business objects having to use the <code>UiComesAfter</code> annotation (or an XML
 * file, or some other method) to impose an ordering.
 * <p>
 * However, a danger of this approach is that if the business objects are ever recompiled
 * <em>without</em> debug line numbering information (eg. when moving from development to
 * production) the UI fields will lose their ordering. Such a subtle bug may not be picked up, so as
 * a safeguard <code>JavassistPropertyStyle</code> 'fails hard' with an
 * <code>InspectorException</code> if line numbers are not available.
 * <p>
 * <code>JavassistPropertyStyle</code> uses the following sorting algorithm:
 * <ul>
 * <li>superclass public fields come first, sorted by name</li>
 * <li>superclass methods come next, sorted by getter line number (or, if no getter, setter line
 * number)</li>
 * <li>public fields come next, sorted by name</li>
 * <li>methods come last, sorted by getter line number (or, if no getter, setter line number)</li>
 * </ul>
 * Note this algorithm is less flexible than the <code>UiComesAfter</code> annotation, which can
 * interleave superclass and subclass properties. However, it is possible to use both
 * <code>UiComesAfter</code> and <code>JavassistPropertyStyle</code> together to get the best of
 * both worlds.
 *
 * @author Richard Kennard, inspired by Tapestry 5's BeanEditForm
 */

// We explored implementing this as an <code>Inspector</code> rather than a
// <code>PropertyStyle</code>, but it came out worse. First, it is impractical to have a
// <code>JavassistInspector</code> output <code>comes-after</code> attributes, because the
// <code>Inspector</code> cannot know which properties will survive through to the
// <code>ComesAfterInspectionResultProcessor</code>. Therefore every property has to be given a
// <code>comes-after</code> attribute that has multiple names. Second, it requires the developer to
// swap in both a <code>JavassistInspector</code> <em>and</em> some kind of
// <code>SortByLineNumberInspectionResultProcessor</code>. Third, it is unclear how a
// <code>JavassistInspector</code> should work with non-JavaBean property styles. On balance, it
// just seemed easier to implement as a <code>PropertyStyle</code>.

public class JavassistPropertyStyle
	extends JavaBeanPropertyStyle {

	//
	// Constructor
	//

	public JavassistPropertyStyle() {

		super();
	}

	public JavassistPropertyStyle( JavaBeanPropertyStyleConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Inspect the given Classes and merge their results.
	 * <p>
	 * This version of <code>inspectProperties</code> is used when inspecting the interfaces of a
	 * proxied class. Overridden to use a <code>LinkedHashMap</code> to combine the properties.
	 */

	@Override
	protected Map<String, Property> inspectProperties( Class<?>[] classes ) {

		Map<String, Property> propertiesToReturn = CollectionUtils.newLinkedHashMap();

		for ( Class<?> clazz : classes ) {
			Map<String, Property> properties = getCachedProperties( clazz );

			if ( properties == null ) {
				properties = inspectProperties( clazz );
				cacheProperties( clazz, properties );
			}

			propertiesToReturn.putAll( properties );
		}

		return propertiesToReturn;
	}

	@Override
	protected Map<String, Property> inspectProperties( Class<?> clazz ) {

		try {
			Map<ClassAndLineNumberAndName, Property> lineNumberedProperties = CollectionUtils.newTreeMap();

			ClassPool pool = ClassPool.getDefault();
			CtClass ctClass = pool.get( clazz.getName() );

			// For each JavaBean property...

			Map<String, Property> properties = super.inspectProperties( clazz );

			for ( Property property : properties.values() ) {
				String propertyName = property.getName();

				// ...lookup the corresponding Javassist method...

				if ( property instanceof JavaBeanProperty ) {
					CtMethod ctMethod = null;
					JavaBeanProperty javaBeanProperty = (JavaBeanProperty) property;
					Method method = javaBeanProperty.getReadMethod();

					// ...by its getter...

					if ( method != null ) {
						ctMethod = getCtMethod( ctClass, method );
					}

					// ...or its setter...

					else {
						method = javaBeanProperty.getWriteMethod();
						ctMethod = getCtMethod( ctClass, method, pool.get( property.getType().getName() ) );
					}

					// ...and remember its line number

					MethodInfo methodInfo = ctMethod.getMethodInfo();
					int lineNumber = methodInfo.getLineNumber( 0 );

					if ( lineNumber == -1 && !ctClass.isInterface() ) {
						throw InspectorException.newException( "Line number information for " + clazz + " not available. Did you compile without debug info?" );
					}

					lineNumberedProperties.put( new ClassAndLineNumberAndName( method.getDeclaringClass(), lineNumber, propertyName ), property );
					continue;
				}

				// ...or the corresponding field

				if ( property instanceof FieldProperty ) {
					FieldProperty fieldProperty = (FieldProperty) property;
					lineNumberedProperties.put( new ClassAndLineNumberAndName( fieldProperty.getField().getDeclaringClass(), 0, propertyName ), property );
					continue;
				}

				throw InspectorException.newException( "Unknown property type " + property.getClass() );
			}

			// ...and sort them by line number

			Map<String, Property> sortedProperties = CollectionUtils.newLinkedHashMap();

			for ( Property property : lineNumberedProperties.values() ) {
				sortedProperties.put( property.getName(), property );
			}

			return sortedProperties;
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Private methods
	//

	/**
	 * Adapter method to avoid having to use <code>CtClass.getMethod</code>, which requires us to
	 * construct a JVM signature String.
	 */

	private CtMethod getCtMethod( CtClass ctClass, Method method, CtClass... parameters )
		throws NotFoundException {

		CtClass superClass = ctClass;

		while ( true ) {
			try {
				return superClass.getDeclaredMethod( method.getName(), parameters );
			} catch ( NotFoundException e ) {
				superClass = superClass.getSuperclass();
			}
		}
	}

	//
	// Inner class
	//

	private static class ClassAndLineNumberAndName
		implements Comparable<ClassAndLineNumberAndName> {

		//
		// Private members
		//

		private Class<?>	mClass;

		private int			mLineNumber;

		private String		mName;

		//
		// Constructor
		//

		public ClassAndLineNumberAndName( Class<?> clazz, int lineNumber, String name ) {

			mClass = clazz;
			mLineNumber = lineNumber;
			mName = name;
		}

		//
		// Public methods
		//

		public int compareTo( ClassAndLineNumberAndName that ) {

			// If classes not equal...

			if ( !mClass.equals( that.mClass ) ) {
				// ...superclasses come first

				if ( mClass.isAssignableFrom( that.mClass ) ) {
					return -1;
				}

				return 1;
			}

			// ...otherwise, sort by line number...

			if ( mLineNumber != that.mLineNumber ) {
				return mLineNumber - that.mLineNumber;
			}

			// ...otherwise, sort by name (so that we're deterministic)

			return mName.compareTo( that.mName );
		}
	}
}
