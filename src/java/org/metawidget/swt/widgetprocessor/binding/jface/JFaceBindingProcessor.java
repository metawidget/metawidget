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

package org.metawidget.swt.widgetprocessor.binding.jface;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

/**
 * Property binding implementation based on JFace.
 * <p>
 * JFace is the SWT's typical approach to binding UI controls to Java objects. JFace sits atop the
 * <code>eclipse.core.databinding</code> functionality and adds SWT support, as discussed here
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=153630.
 *
 * @author Richard Kennard
 */

public class JFaceBindingProcessor
	implements AdvancedWidgetProcessor<Control, SwtMetawidget>
{
	//
	// Public methods
	//

	@Override
	public void onStartBuild( SwtMetawidget metawidget )
	{
		Realm realm = SWTObservables.getRealm( metawidget.getDisplay() );
		metawidget.setData( JFaceBindingProcessor.class.getName(), new DataBindingContext( realm ) );
	}

	@Override
	public Control processWidget( Control control, String elementName, Map<String, String> attributes, SwtMetawidget metawidget )
	{
		if ( !PROPERTY.equals( elementName ) )
			return control;

		// Decide how to observe the SWT widget

		IObservableValue uiElement;

		if ( control instanceof Text )
			uiElement = SWTObservables.observeText( control, SWT.Modify );
		else if ( control instanceof Combo )
			uiElement = SWTObservables.observeSelection( control );
		else
			return control;

		// Decide how to observe the model

		Object toInspect = metawidget.getToInspect();
		String propertyName = attributes.get( NAME );

		TypeAndNames typeAndNames = PathUtils.parsePath( metawidget.getInspectionPath() );
		if ( typeAndNames.getNamesAsArray().length > 0 )
			propertyName = typeAndNames.getNames().replace( StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR ) + StringUtils.SEPARATOR_DOT_CHAR + propertyName;

		DataBindingContext bindingContext = (DataBindingContext) metawidget.getData( JFaceBindingProcessor.class.getName() );
		IObservableValue modelElement = BeanProperties.value( propertyName ).observe( bindingContext.getValidationRealm(), toInspect );

		// Bind it

		UpdateValueStrategy targetToModel = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );
		UpdateValueStrategy modelToTarget = new UpdateValueStrategy( UpdateValueStrategy.POLICY_ON_REQUEST );
		bindingContext.bindValue( uiElement, modelElement, targetToModel, modelToTarget );

		return control;
	}

	@Override
	public void onEndBuild( SwtMetawidget metawidget )
	{
		DataBindingContext bindingContext = (DataBindingContext) metawidget.getData( JFaceBindingProcessor.class.getName() );
		bindingContext.updateTargets();
	}

	public void save( final SwtMetawidget metawidget )
	{
		DataBindingContext bindingContext = (DataBindingContext) metawidget.getData( JFaceBindingProcessor.class.getName() );
		bindingContext.updateModels();
	}
}
