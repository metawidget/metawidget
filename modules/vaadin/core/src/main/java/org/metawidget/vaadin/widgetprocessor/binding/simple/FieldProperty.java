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

package org.metawidget.vaadin.widgetprocessor.binding.simple;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedList;

import org.metawidget.util.ClassUtils;

/**
 * FieldProperty
 *
 *
 * @author Loghman Barari
 */
@SuppressWarnings("serial")
/*package private*/class FieldProperty  implements Property, Property.ValueChangeNotifier,
	Property.ReadOnlyStatusChangeNotifier {

	private final Field field;

	private Converter<?> converter;

    /**
     * A boolean value storing the Property's read-only status information.
     */
    private boolean readOnly = false;

    /**
     * The object that contains property.
     */
    private Object object;

    /**
     * Type of the property.
     */
    private transient Class<?> type;

    /**
     * Internal list of registered value change listeners.
     */
	@SuppressWarnings("unchecked")
	private LinkedList valueChangeListeners = null;

    /**
     * Internal list of registered read-only status change listeners.
     */
    @SuppressWarnings("unchecked")
	private LinkedList readOnlyStatusChangeListeners = null;

    /**
     * Creates a new instance of FieldProperty with the field of the given object. The type
     * of the property is automatically initialized to be the type of the the field of given
     * object.
     *
     * @param object
     *            	Parent Object.
     * @param property
     * 				the field of the object.
     */
	public FieldProperty(Object object, String property) throws SecurityException, NoSuchFieldException{

		this( object, object.getClass().getField(property));

	}

	public FieldProperty(Object object, Field field) {
		this.field = field;

		this.object = object;

		this.type = this.field.getType();

		if (this.type.isPrimitive()) {
			this.type = ClassUtils.getWrapperClass(this.type);
		}
	}


    /**
     * Returns the type of the FieldProperty. The methods <code>getValue</code>
     * and <code>setValue</code> must be compatible with this type: one must be
     * able to safely cast the value returned from <code>getValue</code> to the
     * given type and pass any variable assignable to this type as an argument
     * to <code>setValue</code>.
     *
     * @return type of the Property
     */
    public final Class<?> getType() {
        return this.type;
    }

    /**
     * Gets the value stored in the Property.
     *
     * @return the value stored in the Property
     */
    public Object getValue() {

    	Object value = null;

		try {
			value = field.get( this.object );

			if ((value == null) && (getType() == String.class)) {
				value = "";
			}

		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

		return value;
    }

    /**
     * Returns the value of the FieldProperty in human readable textual format.
     * The return value should be assignable to the <code>setValue</code> method
     * if the Property is not in read-only mode.
     *
     * @return <code>String</code> representation of the value stored in the
     *         FieldProperty
     */
    @Override
    public String toString() {
        final Object value = getValue();
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    /**
     * Tests if the Property is in read-only mode. In read-only mode calls to
     * the method <code>setValue</code> will throw
     * <code>ReadOnlyException</code>s and will not modify the value of the
     * Property.
     *
     * @return <code>true</code> if the Property is in read-only mode,
     *         <code>false</code> if it's not
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the Property's read-only mode to the specified status.
     *
     * @param newStatus
     *            the new read-only status of the Property.
     */
    public void setReadOnly(boolean newStatus) {
        if (newStatus != readOnly) {
            readOnly = newStatus;
            fireReadOnlyStatusChange();
        }
    }

    /**
     * Sets the value of the property. This method supports setting from
     * <code>String</code> if either <code>String</code> is directly assignable
     * to property type, or the type class contains a string constructor.
     *
     * @param newValue
     *            the New value of the property.
     * @throws <code>Property.ReadOnlyException</code> if the object is in
     *         read-only mode
     * @throws <code>Property.ConversionException</code> if the newValue can't
     *         be converted into the Property's native type directly or through
     *         <code>String</code> or determined convertor.
     */
    public void setValue(Object newValue) throws Property.ReadOnlyException,
            Property.ConversionException {

        // Checks the mode
        if (isReadOnly()) {
            throw new Property.ReadOnlyException();
        }

        // If it is not possible to assign the value directly
        if (newValue != null && !field.getType().isAssignableFrom(newValue.getClass())) {

            try {

            	if (this.converter != null) {
            		newValue = converter.convert( newValue );
            	}
            	else {

	                // Gets the string constructor
	                final Constructor<?> constructor = getType().getConstructor(
	                        new Class[] { String.class });

	                // Creates new object from the string
	                newValue = constructor
	                        .newInstance(new Object[] { newValue.toString() });

            	}

            } catch (final java.lang.Exception e) {
                throw new Property.ConversionException(e);
            }
        }

    	try {
			field.set( object, newValue );
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

        fireValueChange();
    }


    /* Events */

    /**
     * An <code>Event</code> object specifying the FieldProperty whose value
     * has changed.
     *
     * @author Loghman Barari
     */
    private class ValueChangeEvent extends java.util.EventObject implements
            Property.ValueChangeEvent {

        /**
         * Constructs a new value change event for this object.
         *
         * @param source
         *            the source object of the event.
         */
        protected ValueChangeEvent(FieldProperty source) {
            super(source);
        }

        /**
         * Gets the Property whose read-only state has changed.
         *
         * @return source the Property of the event.
         */
        public Property getProperty() {
            return (Property) getSource();
        }
    }

    /**
     * An <code>Event</code> object specifying the Property whose read-only
     * status has been changed.
     *
     * @author Loghman Barari
     *
     */
    private class ReadOnlyStatusChangeEvent extends java.util.EventObject
            implements Property.ReadOnlyStatusChangeEvent {

        /**
         * Constructs a new read-only status change event for this object.
         *
         * @param source
         *            source object of the event
         */
        protected ReadOnlyStatusChangeEvent(FieldProperty source) {
            super(source);
        }

        /**
         * Gets the Property whose read-only state has changed.
         *
         * @return source Property of the event.
         */
        public Property getProperty() {
            return (Property) getSource();
        }
    }

    /**
     * Removes a previously registered value change listener.
     *
     * @param listener
     *            the listener to be removed.
     */
    public void removeListener(Property.ValueChangeListener listener) {
        if (valueChangeListeners != null) {
            valueChangeListeners.remove(listener);
        }
    }

    /**
     * Registers a new value change listener for this FieldProperty.
     *
     * @param listener
     *            the new Listener to be registered
     */
    @SuppressWarnings("unchecked")
	public void addListener(Property.ValueChangeListener listener) {
        if (valueChangeListeners == null) {
            valueChangeListeners = new LinkedList();
        }
        valueChangeListeners.add(listener);
    }

    /**
     * Registers a new read-only status change listener for this Property.
     *
     * @param listener
     *            the new Listener to be registered
     */
    @SuppressWarnings("unchecked")
	public void addListener(Property.ReadOnlyStatusChangeListener listener) {
        if (readOnlyStatusChangeListeners == null) {
            readOnlyStatusChangeListeners = new LinkedList();
        }
        readOnlyStatusChangeListeners.add(listener);
    }

    /**
     * Removes a previously registered read-only status change listener.
     *
     * @param listener
     *            the listener to be removed.
     */
    public void removeListener(Property.ReadOnlyStatusChangeListener listener) {
        if (readOnlyStatusChangeListeners != null) {
            readOnlyStatusChangeListeners.remove(listener);
        }
    }

    /**
     * Sends a value change event to all registered listeners.
     */
    private void fireValueChange() {
        if (valueChangeListeners != null) {
            final Object[] l = valueChangeListeners.toArray();
            final Property.ValueChangeEvent event = new FieldProperty.ValueChangeEvent(
                    this);
            for (int i = 0; i < l.length; i++) {
                ((Property.ValueChangeListener) l[i]).valueChange(event);
            }
        }
    }

    /**
     * Sends a read only status change event to all registered listeners.
     */
    private void fireReadOnlyStatusChange() {
        if (readOnlyStatusChangeListeners != null) {
            final Object[] l = readOnlyStatusChangeListeners.toArray();
            final Property.ReadOnlyStatusChangeEvent event = new FieldProperty.ReadOnlyStatusChangeEvent(
                    this);
            for (int i = 0; i < l.length; i++) {
                ((Property.ReadOnlyStatusChangeListener) l[i])
                        .readOnlyStatusChange(event);
            }
        }
    }

	public Converter<?> getConvertor() {
		return converter;
	}

	public void setConvertor(Converter<?> converter) {
		this.converter = converter;
	}
}
