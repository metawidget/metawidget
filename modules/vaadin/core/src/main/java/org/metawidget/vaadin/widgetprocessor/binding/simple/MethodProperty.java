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

/**
 *
 *
 *
 * @author Loghman Barari
 */
@SuppressWarnings({ "serial", "unchecked" })
/*package private*/class MethodProperty extends com.vaadin.data.util.MethodProperty implements Property {

	private Converter converter;

	public MethodProperty(Object object, String property) {
		super(object, property);
	}

	public Converter getConvertor() {
		return converter;
	}

	public void setConvertor(Converter converter) {
		this.converter = converter;
	}

	@Override
	public Object getValue() {
		Object value = super.getValue();
		if ((value == null) && (getType() == String.class)) {
			value = "";
		}

		return value;
	}

	@Override
	public void setValue(Object newValue) throws ReadOnlyException,
			ConversionException {
		if ((converter != null) && (newValue != null)) {
			newValue = converter.convert(newValue);
		}

		super.setValue(newValue);
	}
}
