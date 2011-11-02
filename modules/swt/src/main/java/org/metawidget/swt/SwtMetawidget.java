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

package org.metawidget.swt;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.config.ConfigReader;
import org.metawidget.iface.Immutable;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspectionresultprocessor.iface.InspectionResultProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.layout.iface.Layout;
import org.metawidget.pipeline.w3c.W3CPipeline;
import org.metawidget.swt.layout.SwtLayoutDecorator;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.w3c.dom.Element;

/**
 * Metawidget for SWT environments.
 *
 * @author Stefan Ackermann, Richard Kennard
 */

public class SwtMetawidget
	extends Composite {

	//
	// Private statics
	//

	private static final long			serialVersionUID		= 1l;

	private static final ConfigReader	CONFIG_READER			= new ConfigReader();

	private static final String			DEFAULT_CONFIG			= ClassUtils.getPackagesAsFolderNames( SwtMetawidget.class ) + "/metawidget-swt-default.xml";

	//
	// Private members
	//

	private Object						mToInspect;

	private String						mInspectionPath;

	private String						mConfig;

	private ResourceBundle				mBundle;

	private boolean						mNeedToBuildWidgets;

	private Element						mLastInspection;

	private Map<String, Facet>			mFacets					= CollectionUtils.newHashMap();

	/**
	 * Set of existing, manually added components.
	 */

	private Set<Control>				mExistingControls		= CollectionUtils.newHashSet();

	/**
	 * List of existing, manually added, but unused by Metawidget controls.
	 * <p>
	 * This is a List, not a Set, for consistency during endBuild.
	 */

	private List<Control>				mExistingUnusedControls	= CollectionUtils.newArrayList();

	private Set<Control>				mControlsToDispose		= CollectionUtils.newHashSet();

	/* package private */Composite		mCurrentLayoutComposite;

	private Pipeline					mPipeline;

	//
	// Constructor
	//

	public SwtMetawidget( Composite parent, int style ) {

		super( parent, style );
		mPipeline = newPipeline();

		// This covers most cases

		addControlListener( new ControlListener() {

			public void controlResized( ControlEvent arg0 ) {

				buildWidgets();
			}

			public void controlMoved( ControlEvent arg0 ) {

				buildWidgets();
			}
		} );

		// This covers, say, clicking 'Edit' and going from read-only to non-read-only

		addPaintListener( new PaintListener() {

			public void paintControl( PaintEvent event ) {

				if ( event.count == 0 ) {
					buildWidgets();
				}
			}
		} );
	}

	//
	// Public methods
	//

	/**
	 * Sets the Object to inspect.
	 * <p>
	 * If <code>setPath</code> has not been set, or points to a previous <code>setToInspect</code>,
	 * sets it to point to the given Object.
	 */

	public void setToInspect( Object toInspect ) {

		updateToInspectWithoutInvalidate( toInspect );
		invalidateInspection();
	}

	/**
	 * Updates the Object to inspect, without invalidating the previous inspection results.
	 * <p>
	 * <strong>This is an internal API exposed for WidgetProcessor rebinding support. Clients should
	 * not call it directly.</strong>
	 */

	public void updateToInspectWithoutInvalidate( Object toInspect ) {

		if ( mToInspect == null ) {
			if ( mInspectionPath == null && toInspect != null ) {
				mInspectionPath = toInspect.getClass().getName();
			}
		} else if ( mToInspect.getClass().getName().equals( mInspectionPath ) ) {
			if ( toInspect == null ) {
				mInspectionPath = null;
			} else {
				mInspectionPath = toInspect.getClass().getName();
			}
		}

		mToInspect = toInspect;
	}

	/**
	 * Gets the Object being inspected.
	 * <p>
	 * Exposed for binding implementations.
	 *
	 * @return the object. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>Person p = getToInspect()</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getToInspect() {

		return (T) mToInspect;
	}

	/**
	 * Sets the path to be inspected.
	 */

	public void setInspectionPath( String inspectionPath ) {

		mInspectionPath = inspectionPath;
		invalidateInspection();
	}

	public String getInspectionPath() {

		return mInspectionPath;
	}

	public void setConfig( String config ) {

		mConfig = config;
		mPipeline.setNeedsConfiguring();
		invalidateInspection();
	}

	public void setInspector( Inspector inspector ) {

		mPipeline.setInspector( inspector );
		invalidateInspection();
	}

	/**
	 * Useful for WidgetBuilders to perform nested inspections (eg. for Collections).
	 */

	public String inspect( Object toInspect, String type, String... names ) {

		return mPipeline.inspect( toInspect, type, names );
	}

	public void addInspectionResultProcessor( InspectionResultProcessor<SwtMetawidget> inspectionResultProcessor ) {

		mPipeline.addInspectionResultProcessor( inspectionResultProcessor );
		invalidateWidgets();
	}

	public void removeInspectionResultProcessor( InspectionResultProcessor<SwtMetawidget> inspectionResultProcessor ) {

		mPipeline.removeInspectionResultProcessor( inspectionResultProcessor );
		invalidateWidgets();
	}

	public void setInspectionResultProcessors( InspectionResultProcessor<SwtMetawidget>... inspectionResultProcessors ) {

		mPipeline.setInspectionResultProcessors( inspectionResultProcessors );
		invalidateWidgets();
	}

	public void setWidgetBuilder( WidgetBuilder<Control, SwtMetawidget> widgetBuilder ) {

		mPipeline.setWidgetBuilder( widgetBuilder );
		invalidateWidgets();
	}

	public void addWidgetProcessor( WidgetProcessor<Control, SwtMetawidget> widgetProcessor ) {

		mPipeline.addWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void removeWidgetProcessor( WidgetProcessor<Control, SwtMetawidget> widgetProcessor ) {

		mPipeline.removeWidgetProcessor( widgetProcessor );
		invalidateWidgets();
	}

	public void setWidgetProcessors( WidgetProcessor<Control, SwtMetawidget>... widgetProcessors ) {

		mPipeline.setWidgetProcessors( widgetProcessors );
		invalidateWidgets();
	}

	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass ) {

		buildWidgets();
		return mPipeline.getWidgetProcessor( widgetProcessorClass );
	}

	/**
	 * Set the layout for this Metawidget.
	 * <p>
	 * Named <code>setMetawidgetLayout</code>, rather than the usual <code>setLayout</code>, because
	 * Swing already defines a <code>setLayout</code>. Overloading Swing's <code>setLayout</code>
	 * was considered cute, but ultimately confusing and dangerous. For example, what should
	 * <code>setLayout( null )</code> do?
	 */

	public void setMetawidgetLayout( Layout<Control, Composite, SwtMetawidget> layout ) {

		mPipeline.setLayout( layout );
		invalidateWidgets();
	}

	public void setBundle( ResourceBundle bundle ) {

		mBundle = bundle;
		invalidateWidgets();
	}

	public String getLabelString( Map<String, String> attributes ) {

		if ( attributes == null ) {
			return "";
		}

		// Explicit label

		String label = attributes.get( LABEL );

		if ( label != null ) {
			// (may be forced blank)

			if ( "".equals( label ) ) {
				return null;
			}

			// (localize if possible)

			String localized = getLocalizedKey( StringUtils.camelCase( label ) );

			if ( localized != null ) {
				return localized.trim();
			}

			return label.trim();
		}

		// Default name

		String name = attributes.get( NAME );

		if ( name != null ) {
			// (localize if possible)

			String localized = getLocalizedKey( name );

			if ( localized != null ) {
				return localized.trim();
			}

			return StringUtils.uncamelCase( name );
		}

		return "";
	}

	/**
	 * @return null if no bundle, ???key??? if bundle is missing a key
	 */

	public String getLocalizedKey( String key ) {

		if ( mBundle == null ) {
			return null;
		}

		try {
			return mBundle.getString( key );
		} catch ( MissingResourceException e ) {
			return StringUtils.RESOURCE_KEY_NOT_FOUND_PREFIX + key + StringUtils.RESOURCE_KEY_NOT_FOUND_SUFFIX;
		}
	}

	public boolean isReadOnly() {

		return mPipeline.isReadOnly();
	}

	public void setReadOnly( boolean readOnly ) {

		if ( mPipeline.isReadOnly() == readOnly ) {
			return;
		}

		mPipeline.setReadOnly( readOnly );
		invalidateWidgets();
	}

	public int getMaximumInspectionDepth() {

		return mPipeline.getMaximumInspectionDepth();
	}

	public void setMaximumInspectionDepth( int maximumInspectionDepth ) {

		mPipeline.setMaximumInspectionDepth( maximumInspectionDepth );
		invalidateWidgets();
	}

	/**
	 * Gets the value from the Control with the given name.
	 * <p>
	 * The value is returned as it was stored in the Control (eg. String for JTextField) so may need
	 * some conversion before being reapplied to the object being inspected. This obviously requires
	 * knowledge of which Control SwtMetawidget created, which is not ideal, so clients may prefer
	 * to use bindingClass instead.
	 *
	 * @return the value. Note this return type uses generics, so as to not require a cast by the
	 *         caller (eg. <code>String s = getValue(names)</code>)
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getValue( String... names ) {

		ControlAndValueProperty controlAndValueProperty = getControlAndValueProperty( names );
		return (T) ClassUtils.getProperty( controlAndValueProperty.getControl(), controlAndValueProperty.getValueProperty() );
	}

	/**
	 * Sets the Control with the given name to the specified value.
	 * <p>
	 * Clients must ensure the value is of the correct type to suit the Control (eg. String for
	 * JTextField). This obviously requires knowledge of which Control SwtMetawidget created, which
	 * is not ideal, so clients may prefer to use bindingClass instead.
	 */

	public void setValue( Object value, String... names ) {

		ControlAndValueProperty controlAndValueProperty = getControlAndValueProperty( names );
		ClassUtils.setProperty( controlAndValueProperty.getControl(), controlAndValueProperty.getValueProperty(), value );
	}

	/**
	 * Returns the property used to get/set the value of the control.
	 * <p>
	 * If the control is not known, returns <code>null</code>. Does not throw an Exception, as we
	 * want to fail gracefully if, say, someone tries to bind to a JPanel.
	 */

	public String getValueProperty( Control control ) {

		return getValueProperty( control, mPipeline.getWidgetBuilder() );
	}

	/**
	 * Finds the Control with the given name.
	 */

	@SuppressWarnings( "unchecked" )
	public <T extends Control> T getControl( String... names ) {

		if ( names == null || names.length == 0 ) {
			return null;
		}

		Control topControl = this;

		for ( int loop = 0, length = names.length; loop < length; loop++ ) {
			String name = names[loop];

			// May need building 'just in time' if we are calling getControl
			// immediately after a 'setToInspect'. See
			// SwtMetawidgetTest.testNestedWithManualInspector

			if ( topControl instanceof SwtMetawidget ) {
				( (SwtMetawidget) topControl ).buildWidgets();
			}

			// Try to find a control

			topControl = getControl( (Composite) topControl, name );

			if ( loop == length - 1 ) {
				return (T) topControl;
			}

			if ( topControl == null ) {
				throw MetawidgetException.newException( "No such control '" + name + "' of '" + ArrayUtils.toString( names, "', '" ) + "'" );
			}
		}

		return (T) topControl;
	}

	public Facet getFacet( String name ) {

		buildWidgets();

		return mFacets.get( name );
	}

	/**
	 * This method is public for use by WidgetBuilders to attach Controls to the current Composite
	 * as defined by the Layout. This allows the Layout to introduce new Composites, such as for
	 * TabFolders.
	 */

	public Composite getCurrentLayoutComposite() {

		if ( mCurrentLayoutComposite == null ) {
			return this;
		}

		return mCurrentLayoutComposite;
	}

	//
	// The following methods all kick off buildWidgets() if necessary
	//

	@Override
	public org.eclipse.swt.widgets.Layout getLayout() {

		buildWidgets();

		return super.getLayout();
	}

	@Override
	public Control[] getChildren() {

		buildWidgets();

		return super.getChildren();
	}

	//
	// Protected methods
	//

	/**
	 * Instantiate the Pipeline used by this Metawidget.
	 * <p>
	 * Subclasses wishing to use their own Pipeline should override this method to instantiate their
	 * version.
	 */

	protected Pipeline newPipeline() {

		return new Pipeline();
	}

	/**
	 * Invalidates the current inspection result (if any) <em>and</em> invalidates the widgets.
	 * <p>
	 * As an optimisation we only invalidate the widgets, not the entire inspection result, for some
	 * operations (such as adding/removing stubs, changing read-only etc.)
	 */

	protected void invalidateInspection() {

		mLastInspection = null;
		invalidateWidgets();
	}

	/**
	 * Invalidates the widgets.
	 */

	protected void invalidateWidgets() {

		if ( mNeedToBuildWidgets ) {
			return;
		}

		mNeedToBuildWidgets = true;
	}

	protected void configure() {

		// Special support for visual IDE builders

		if ( mInspectionPath == null ) {

			mPipeline.setNeedsConfiguring();
			return;
		}

		try {
			if ( mConfig != null ) {
				CONFIG_READER.configure( mConfig, this );
			}

			// SwtMetawidget uses setMetawidgetLayout, not setLayout

			if ( mPipeline.getLayout() == null ) {
				CONFIG_READER.configure( DEFAULT_CONFIG, this, "metawidgetLayout" );
			}

			mPipeline.configureDefaults( CONFIG_READER, DEFAULT_CONFIG, SwtMetawidget.class );
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}
	}

	protected void buildWidgets() {

		// No need to build?

		if ( !mNeedToBuildWidgets ) {
			return;
		}

		mPipeline.configureOnce();

		mNeedToBuildWidgets = false;

		// Metawidget needs a way to distinguish between manually added controls and generated
		// controls: the generated ones must be cleaned up on subsequent buildWidgets(), whereas
		// the manual ones must be left alone. SWT doesn't appear to have a mechanism for listening
		// for child add/remove events (as we use in Android, GWT, Swing etc), so instead we
		// implement this as the delta of 'what was here originally' versus 'what was generated'

		for ( Control control : mControlsToDispose ) {
			control.dispose();
		}

		mControlsToDispose.clear();
		mExistingUnusedControls = CollectionUtils.newArrayList( getChildren() );
		mExistingControls = CollectionUtils.newHashSet( mExistingUnusedControls );

		// Detect facets

		for ( Control control : getChildren() ) {
			if ( control instanceof Facet ) {
				mFacets.put( (String) control.getData( NAME ), (Facet) control );
				continue;
			}
		}

		// Build widgets

		try {
			if ( mLastInspection == null ) {
				mLastInspection = inspect();
			}

			mPipeline.buildWidgets( mLastInspection );

			// Work out the delta of 'what was here originally' versus 'what was generated'
			//
			// Note: we cannot simply do this in layoutWidget, because some controls may get created
			// just-in-time, such as Labels

			for ( Control control : getChildren() ) {
				if ( !mExistingControls.remove( control ) ) {
					mControlsToDispose.add( control );
				}
			}

			// Layout up the heirarchy so that all parents are laid out correctly (we're not sure of
			// the 'correctness' of this - it's just what worked after trial and error)

			Composite topParent = getParent();

			while ( topParent != null ) {
				topParent.layout();
				topParent = topParent.getParent();
			}
		} catch ( Exception e ) {
			throw MetawidgetException.newException( e );
		}
	}

	/**
	 * @param elementName
	 *            XML node name of the business field. Typically 'entity', 'property' or 'action'.
	 *            Never null
	 */

	protected void layoutWidget( Control control, String elementName, Map<String, String> attributes ) {

		// Set the name of the component.

		control.setData( NAME, attributes.get( NAME ) );

		// Re-order the component

		control.moveBelow( null );
		mExistingUnusedControls.remove( control );
		control.setLayoutData( null );

		// Look up any additional attributes

		Map<String, String> additionalAttributes = mPipeline.getAdditionalAttributes( control );

		if ( additionalAttributes != null ) {
			attributes.putAll( additionalAttributes );
		}

		// BasePipeline will call .layoutWidget
	}

	protected void endBuild() {

		for ( Control existingControl : CollectionUtils.newArrayList( mExistingUnusedControls ) ) {
			// Unused facets don't count

			if ( existingControl instanceof Facet ) {
				existingControl.moveBelow( null );
				continue;
			}

			// Manually created components default to no section

			Map<String, String> attributes = CollectionUtils.newHashMap();
			attributes.put( SECTION, "" );

			mPipeline.layoutWidget( existingControl, PROPERTY, attributes );
		}
	}

	protected void initNestedMetawidget( SwtMetawidget nestedMetawidget, Map<String, String> attributes ) {

		// Don't copy setConfig(). Instead, copy runtime values

		mPipeline.initNestedPipeline( nestedMetawidget.mPipeline, attributes );
		nestedMetawidget.setInspectionPath( mInspectionPath + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
		nestedMetawidget.setBundle( mBundle );
		nestedMetawidget.setToInspect( mToInspect );
	}

	//
	// Private methods
	//

	private Element inspect() {

		if ( mInspectionPath == null ) {
			return null;
		}

		TypeAndNames typeAndNames = PathUtils.parsePath( mInspectionPath );
		return mPipeline.inspectAsDom( mToInspect, typeAndNames.getType(), typeAndNames.getNamesAsArray() );
	}

	private ControlAndValueProperty getControlAndValueProperty( String... names ) {

		Control control = getControl( names );

		if ( control == null ) {
			throw MetawidgetException.newException( "No control named '" + ArrayUtils.toString( names, "', '" ) + "'" );
		}

		String valueProperty = getValueProperty( control );

		if ( valueProperty == null ) {
			throw MetawidgetException.newException( "Don't know how to getValue from a " + control.getClass().getName() );
		}

		return new ControlAndValueProperty( control, valueProperty );
	}

	private String getValueProperty( Control control, WidgetBuilder<Control, SwtMetawidget> widgetBuilder ) {

		// Recurse into CompositeWidgetBuilders

		try {
			if ( widgetBuilder instanceof CompositeWidgetBuilder<?, ?> ) {
				for ( WidgetBuilder<Control, SwtMetawidget> widgetBuilderChild : ( (CompositeWidgetBuilder<Control, SwtMetawidget>) widgetBuilder ).getWidgetBuilders() ) {
					String valueProperty = getValueProperty( control, widgetBuilderChild );

					if ( valueProperty != null ) {
						return valueProperty;
					}
				}

				return null;
			}
		} catch ( NoClassDefFoundError e ) {
			// May not be shipping with CompositeWidgetBuilder
		}

		// Interrogate ValuePropertyProviders

		if ( widgetBuilder instanceof SwtValuePropertyProvider ) {
			return ( (SwtValuePropertyProvider) widgetBuilder ).getValueProperty( control );
		}

		return null;
	}

	private Control getControl( Composite container, String name ) {

		for ( Control childComponent : container.getChildren() ) {
			// Drill into unnamed containers (ie. for TabFolders)

			if ( childComponent.getData( NAME ) == null && childComponent instanceof Composite ) {
				childComponent = getControl( (Composite) childComponent, name );

				if ( childComponent != null ) {
					return childComponent;
				}

				continue;
			}

			// Match by name

			if ( name.equals( childComponent.getData( NAME ) ) ) {
				return childComponent;
			}
		}

		// Not found

		return null;
	}

	//
	// Inner class
	//

	protected class Pipeline
		extends W3CPipeline<Control, Composite, SwtMetawidget> {

		//
		// Protected methods
		//

		@Override
		protected void configure() {

			SwtMetawidget.this.configure();
		}

		@Override
		protected Control buildWidget( String elementName, Map<String, String> attributes ) {

			if ( !ENTITY.equals( elementName ) ) {
				Layout<Control, Composite, SwtMetawidget> layout = getLayout();

				if ( layout instanceof SwtLayoutDecorator ) {
					mCurrentLayoutComposite = ( (SwtLayoutDecorator) layout ).startBuildWidget( elementName, attributes, SwtMetawidget.this, SwtMetawidget.this );
				}
			}

			return super.buildWidget( elementName, attributes );
		}

		@Override
		protected void layoutWidget( Control control, String elementName, Map<String, String> attributes ) {

			SwtMetawidget.this.layoutWidget( control, elementName, attributes );
			super.layoutWidget( control, elementName, attributes );
		}

		@Override
		protected Map<String, String> getAdditionalAttributes( Control control ) {

			if ( control instanceof Stub ) {
				return ( (Stub) control ).getAttributes();
			}

			return null;
		}

		@Override
		public SwtMetawidget buildNestedMetawidget( Map<String, String> attributes )
			throws Exception {

			SwtMetawidget nestedMetawidget = SwtMetawidget.this.getClass().getConstructor( Composite.class, int.class ).newInstance( getPipelineOwner().getCurrentLayoutComposite(), SWT.None );
			SwtMetawidget.this.initNestedMetawidget( nestedMetawidget, attributes );

			return nestedMetawidget;
		}

		@Override
		protected void endBuild() {

			SwtMetawidget.this.endBuild();
			super.endBuild();
		}

		@Override
		protected SwtMetawidget getPipelineOwner() {

			return SwtMetawidget.this;
		}
	}

	/**
	 * Simple immutable structure to store a component and its value property.
	 *
	 * @author Richard Kennard
	 */

	private static class ControlAndValueProperty
		implements Immutable {

		//
		// Private members
		//

		private Control	mControl;

		private String	mValueProperty;

		//
		// Constructor
		//

		public ControlAndValueProperty( Control control, String valueProperty ) {

			mControl = control;
			mValueProperty = valueProperty;
		}

		//
		// Public methods
		//

		public Control getControl() {

			return mControl;
		}

		public String getValueProperty() {

			return mValueProperty;
		}
	}
}
