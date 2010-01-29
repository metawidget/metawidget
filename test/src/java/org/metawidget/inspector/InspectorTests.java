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

package org.metawidget.inspector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.inspector.annotation.MetawidgetAnnotationInspectorTest;
import org.metawidget.inspector.beanvalidation.BeanValidationInspectorTest;
import org.metawidget.inspector.commons.jexl.JexlInspectorTest;
import org.metawidget.inspector.commons.jexl.JexlXmlInspectorTest;
import org.metawidget.inspector.commons.validator.CommonsValidatorInspectorTest;
import org.metawidget.inspector.composite.CompositeInspectorTest;
import org.metawidget.inspector.composite.ValidatingCompositeInspectorTest;
import org.metawidget.inspector.faces.FacesInspectorTest;
import org.metawidget.inspector.hibernate.HibernateInspectorTest;
import org.metawidget.inspector.hibernate.validator.HibernateValidatorInspectorTest;
import org.metawidget.inspector.impl.BaseObjectInspectorTest;
import org.metawidget.inspector.impl.BasePropertyStyleTest;
import org.metawidget.inspector.impl.BaseXmlInspectorTest;
import org.metawidget.inspector.impl.actionstyle.metawidget.MetawidgetActionStyleTest;
import org.metawidget.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyleTest;
import org.metawidget.inspector.impl.propertystyle.groovy.GroovyPropertyStyleTest;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleTest;
import org.metawidget.inspector.impl.propertystyle.javassist.JavassistPropertyStyleDebugOffTest;
import org.metawidget.inspector.impl.propertystyle.javassist.JavassistPropertyStyleTest;
import org.metawidget.inspector.impl.propertystyle.scala.ScalaPropertyStyleTest;
import org.metawidget.inspector.java5.Java5InspectorTest;
import org.metawidget.inspector.jbpm.PageflowInspectorTest;
import org.metawidget.inspector.jpa.JpaInspectorTest;
import org.metawidget.inspector.jsp.JspAnnotationInspectorTest;
import org.metawidget.inspector.oval.OvalInspectorTest;
import org.metawidget.inspector.propertytype.PropertyTypeInspectorTest;
import org.metawidget.inspector.remote.RemoteInspectorTest;
import org.metawidget.inspector.seam.SeamInspectorTest;
import org.metawidget.inspector.spring.SpringAnnotationInspectorTest;
import org.metawidget.inspector.struts.StrutsAnnotationInspectorTest;
import org.metawidget.inspector.struts.StrutsInspectorTest;
import org.metawidget.inspector.swing.SwingAppFrameworkInspectorTest;
import org.metawidget.inspector.xml.XmlInspectorTest;

/**
 * @author Richard Kennard
 */

public class InspectorTests
	extends TestCase
{
	//
	// Public statics
	//

	public static Test suite()
	{
		TestSuite suite = new TestSuite( "Inspector Tests" );
		suite.addTestSuite( BaseObjectInspectorTest.class );
		suite.addTestSuite( BasePropertyStyleTest.class );
		suite.addTestSuite( BeanValidationInspectorTest.class );
		suite.addTestSuite( BaseXmlInspectorTest.class );
		suite.addTestSuite( CommonsValidatorInspectorTest.class );
		suite.addTestSuite( CompositeInspectorTest.class );
		suite.addTestSuite( FacesInspectorTest.class );
		suite.addTestSuite( GroovyPropertyStyleTest.class );
		suite.addTestSuite( HibernateInspectorTest.class );
		suite.addTestSuite( HibernateValidatorInspectorTest.class );
		suite.addTestSuite( Java5InspectorTest.class );
		suite.addTestSuite( JavaBeanPropertyStyleTest.class );
		suite.addTestSuite( JavassistPropertyStyleTest.class );
		suite.addTestSuite( JavassistPropertyStyleDebugOffTest.class );
		suite.addTestSuite( JexlInspectorTest.class );
		suite.addTestSuite( JexlXmlInspectorTest.class );
		suite.addTestSuite( JpaInspectorTest.class );
		suite.addTestSuite( JspAnnotationInspectorTest.class );
		suite.addTestSuite( MetawidgetAnnotationInspectorTest.class );
		suite.addTestSuite( MetawidgetActionStyleTest.class );
		suite.addTestSuite( OvalInspectorTest.class );
		suite.addTestSuite( PageflowInspectorTest.class );
		suite.addTestSuite( PropertyTypeInspectorTest.class );
		suite.addTestSuite( RemoteInspectorTest.class );
		suite.addTestSuite( SeamInspectorTest.class );
		suite.addTestSuite( ScalaPropertyStyleTest.class );
		suite.addTestSuite( SpringAnnotationInspectorTest.class );
		suite.addTestSuite( StrutsInspectorTest.class );
		suite.addTestSuite( StrutsAnnotationInspectorTest.class );
		suite.addTestSuite( SwingAppFrameworkInspectorTest.class );
		suite.addTestSuite( SwingAppFrameworkActionStyleTest.class );
		suite.addTestSuite( ValidatingCompositeInspectorTest.class );
		suite.addTestSuite( XmlInspectorTest.class );

		return suite;
	}
}
