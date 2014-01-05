// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.bcel;

import junit.framework.TestCase;

import org.apache.bcel.Repository;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.iface.Immutable;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.faces.FacesAnnotationInspector;
import org.metawidget.inspector.faces.FacesInspectionResultConstants;
import org.metawidget.inspector.faces.UiFacesLookup;
import org.metawidget.inspector.hibernate.HibernateInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.actionstyle.ActionStyle;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javassist.JavassistPropertyStyle;
import org.metawidget.inspector.jbpm.PageflowInspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.jsp.JspInspectionResultConstants;
import org.metawidget.inspector.jsp.UiJspLookup;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.seam.SeamInspector;
import org.metawidget.inspector.spring.SpringAnnotationInspector;
import org.metawidget.inspector.spring.SpringInspectionResultConstants;
import org.metawidget.inspector.spring.UiSpringLookup;
import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.StrutsInspectionResultConstants;
import org.metawidget.inspector.struts.UiStrutsLookup;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag;
import org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTag;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GroupLayout;
import org.metawidget.swing.widgetprocessor.binding.beanutils.BeanUtilsBindingProcessor;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JdkVersionTest
	extends TestCase {

	//
	// Private statics
	//

	private static final int	JDK_50_MAJOR_VERSION	= 49;

	private static final int	JDK_60_MAJOR_VERSION	= 50;

	//
	// Public methods
	//

	public void testJdkVersion()
		throws Exception {

		// Core

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( MetawidgetException.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( BasePipeline.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( Immutable.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( Inspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( PropertyTypeInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( BaseObjectInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( PropertyStyle.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( ActionStyle.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( XmlInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( InspectionResultProcessor.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( WidgetBuilder.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( WidgetProcessor.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( Layout.class ).getMajor() );

		// Android

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SwtMetawidget.class ).getMajor() );

		// GWT

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( GwtMetawidget.class ).getMajor() );

		// Faces

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( UIMetawidget.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( FacesInspectionResultConstants.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( FacesAnnotationInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( UiFacesLookup.class ).getMajor() );

		// JSP

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( MetawidgetTag.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( JspInspectionResultConstants.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( JspAnnotationInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( UiJspLookup.class ).getMajor() );

		// Spring

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SpringMetawidgetTag.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SpringInspectionResultConstants.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SpringAnnotationInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( UiSpringLookup.class ).getMajor() );

		// Struts

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( StrutsMetawidgetTag.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( StrutsInspectionResultConstants.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( StrutsAnnotationInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( UiStrutsLookup.class ).getMajor() );

		// Swing

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SwingMetawidget.class ).getMajor() );
		assertEquals( JDK_60_MAJOR_VERSION, Repository.lookupClass( GroupLayout.class ).getMajor() );

		// SWT

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SwtMetawidget.class ).getMajor() );

		// Misc

		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( BeanUtilsBindingProcessor.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( HibernateInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( JavassistPropertyStyle.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( PageflowInspector.class ).getMajor() );
		assertEquals( JDK_50_MAJOR_VERSION, Repository.lookupClass( SeamInspector.class ).getMajor() );
	}
}