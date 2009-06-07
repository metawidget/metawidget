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

package org.metawidget.test.inspector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.metawidget.test.inspector.annotation.MetawidgetAnnotationInspectorTest;
import org.metawidget.test.inspector.commons.jexl.JexlInspectorTest;
import org.metawidget.test.inspector.commons.jexl.JexlXmlInspectorTest;
import org.metawidget.test.inspector.commons.validator.CommonsValidatorInspectorTest;
import org.metawidget.test.inspector.composite.CompositeInspectorTest;
import org.metawidget.test.inspector.faces.FacesInspectorTest;
import org.metawidget.test.inspector.hibernate.HibernateInspectorTest;
import org.metawidget.test.inspector.hibernate.validator.HibernateValidatorInspectorTest;
import org.metawidget.test.inspector.impl.actionstyle.metawidget.MetawidgetActionStyleTest;
import org.metawidget.test.inspector.impl.actionstyle.swing.SwingAppFrameworkActionStyleTest;
import org.metawidget.test.inspector.impl.propertystyle.groovy.GroovyPropertyStyleTest;
import org.metawidget.test.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleTest;
import org.metawidget.test.inspector.impl.propertystyle.javassist.JavassistPropertyStyleDebugOffTest;
import org.metawidget.test.inspector.impl.propertystyle.javassist.JavassistPropertyStyleTest;
import org.metawidget.test.inspector.impl.propertystyle.scala.ScalaPropertyStyleTest;
import org.metawidget.test.inspector.impl.propertystyle.struts.StrutsActionFormPropertyStyleTest;
import org.metawidget.test.inspector.java5.Java5InspectorTest;
import org.metawidget.test.inspector.jbpm.JbpmInspectorTest;
import org.metawidget.test.inspector.jpa.JpaInspectorTest;
import org.metawidget.test.inspector.jsp.JspAnnotationInspectorTest;
import org.metawidget.test.inspector.oval.OvalInspectorTest;
import org.metawidget.test.inspector.propertytype.PropertyTypeInspectorTest;
import org.metawidget.test.inspector.remote.RemoteInspectorTest;
import org.metawidget.test.inspector.spring.SpringAnnotationInspectorTest;
import org.metawidget.test.inspector.struts.StrutsAnnotationInspectorTest;
import org.metawidget.test.inspector.struts.StrutsInspectorTest;
import org.metawidget.test.inspector.swing.SwingAppFrameworkInspectorTest;
import org.metawidget.test.inspector.xml.XmlInspectorTest;

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
		suite.addTestSuite( CommonsValidatorInspectorTest.class );
		suite.addTestSuite( ConfigReaderTest.class );
		suite.addTestSuite( FacesInspectorTest.class );
		suite.addTestSuite( GroovyPropertyStyleTest.class );
		suite.addTestSuite( HibernateInspectorTest.class );
		suite.addTestSuite( HibernateValidatorInspectorTest.class );
		suite.addTestSuite( Java5InspectorTest.class );
		suite.addTestSuite( JavaBeanPropertyStyleTest.class );
		suite.addTestSuite( JavassistPropertyStyleTest.class );
		suite.addTestSuite( JavassistPropertyStyleDebugOffTest.class );
		suite.addTestSuite( JbpmInspectorTest.class );
		suite.addTestSuite( JexlInspectorTest.class );
		suite.addTestSuite( JexlXmlInspectorTest.class );
		suite.addTestSuite( JpaInspectorTest.class );
		suite.addTestSuite( JspAnnotationInspectorTest.class );
		suite.addTestSuite( CompositeInspectorTest.class );
		suite.addTestSuite( MetawidgetAnnotationInspectorTest.class );
		suite.addTestSuite( MetawidgetActionStyleTest.class );
		suite.addTestSuite( OvalInspectorTest.class );
		suite.addTestSuite( PropertyTypeInspectorTest.class );
		suite.addTestSuite( RemoteInspectorTest.class );
		suite.addTestSuite( ScalaPropertyStyleTest.class );
		suite.addTestSuite( SpringAnnotationInspectorTest.class );
		suite.addTestSuite( StrutsInspectorTest.class );
		suite.addTestSuite( StrutsAnnotationInspectorTest.class );
		suite.addTestSuite( StrutsActionFormPropertyStyleTest.class );
		suite.addTestSuite( SwingAppFrameworkInspectorTest.class );
		suite.addTestSuite( SwingAppFrameworkActionStyleTest.class );
		suite.addTestSuite( XmlInspectorTest.class );

		return suite;
	}
}
