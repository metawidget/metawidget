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

package org.metawidget.inspectionresultprocessor.impl;

import org.metawidget.inspectionresultprocessor.iface.DomInspectionResultProcessor;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Convenience implementation for InspectionResultProcessors that process inspection results using
 * DOM trees.
 *
 * @author Richard Kennard
 */

public abstract class BaseDomInspectionResultProcessor<M>
	implements DomInspectionResultProcessor<Element, M> {

	//
	// Public methods
	//

	/**
	 * Process the given inspection result in context of the given Metawidget.
	 * <p>
	 * This method is marked <code>final</code> because most Metawidget implementations will call
	 * <code>processInspectionResultAsDom</code> directly instead. So subclasses need to override
	 * <code>processInspectionResultAsDom</code>, not <code>processInspectionResult</code>.
	 */

	public final String processInspectionResult( String inspectionResult, M metawidget ) {

		Document document = XmlUtils.documentFromString( inspectionResult );
		Element inspectionResultRoot = document.getDocumentElement();

		Element newInspectionResultRoot = processInspectionResultAsDom( inspectionResultRoot, metawidget );
		return XmlUtils.documentToString( newInspectionResultRoot.getOwnerDocument(), false );
	}
}
