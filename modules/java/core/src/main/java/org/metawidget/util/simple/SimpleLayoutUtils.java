// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.util.simple;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.iface.Immutable;

/**
 * Utilities for working with Layouts.
 * <p>
 * Some of the logic behind Layout decisions can be a little involved, so we refactor it here.
 * <p>
 * In this context, 'simple' means 'with minimal class dependencies, suitable to be compiled into
 * JavaScript' (eg. for GWT).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>, Bernhard Huber
 */

public final class SimpleLayoutUtils {

	//
	// Public methods
	//

	/**
	 * Returns true if the field is 'large' or 'wide'.
	 */

	public static boolean isSpanAllColumns( Map<String, String> attributes ) {

		if ( attributes == null ) {
			return false;
		}

		if ( TRUE.equals( attributes.get( LARGE ) ) ) {
			return true;
		}

		if ( TRUE.equals( attributes.get( WIDE ) ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Returns true if the label is blank or if the element is an 'action'.
	 */

	public static boolean needsLabel( String labelText, String elementName ) {

		if ( labelText == null ) {
			return false;
		}

		if ( labelText.trim().length() == 0 ) {
			return false;
		}

		if ( ACTION.equals( elementName ) ) {
			return false;
		}

		return true;
	}

	/**
	 * Strips the given text and returns the text without any MNEMONIC_INDICATOR (&) and also the
	 * index of the first marker.
	 * <p>
	 * For escaping purposes, treats two MNEMONIC_INDICATORs together as an escaped
	 * MNEMONIC_INDICATOR. Also, ignores MNEMONIC_INDICATOR followed by a space, because it is not
	 * likely this is intended to be a mnemonic.
	 */

	public static StrippedMnemonicAndFirstIndex stripMnemonic( String withMnemonic ) {

		// Find initial marker (if any)

		int markerIndex = withMnemonic.indexOf( MNEMONIC_INDICATOR );

		if ( markerIndex == -1 ) {
			return new StrippedMnemonicAndFirstIndex( withMnemonic, MNEMONIC_INDEX_NONE );
		}

		// Find subsequent markers

		int mnemonicIndex = MNEMONIC_INDEX_NONE;
		int beginIndex = 0;
		int length = withMnemonic.length();
		int numberOfDoubleMarkers = 0;
		StringBuilder builder = new StringBuilder();

		do {
			markerIndex++;

			// Marker index with nothing after it? Invalid, but fail gracefully

			if ( markerIndex == length ) {
				break;
			}

			char markerChar = withMnemonic.charAt( markerIndex );

			switch ( markerChar ) {

				// If the next character is a mnemonic marker too (eg. '&&'), skip it...

				case MNEMONIC_INDICATOR:
					numberOfDoubleMarkers++;
					break;

				// ...if it is a space, ignore it...

				case ' ':
					break;

				// ...otherwise record the first mnemonic

				default:
					markerIndex--;
					if ( mnemonicIndex == -1 ) {
						mnemonicIndex = markerIndex - numberOfDoubleMarkers;
					}
			}

			// Record the string without mnemonics

			builder.append( withMnemonic.substring( beginIndex, markerIndex ) );
			beginIndex = markerIndex + 1;

			// Special support for preserving MNEMONIC_INDICATOR followed by a space

			if ( markerChar == ' ' ) {
				builder.append( ' ' );
			}

			// Calculate next markerIndex, starting from begin

			if ( beginIndex < length ) {
				markerIndex = withMnemonic.indexOf( MNEMONIC_INDICATOR, beginIndex );
			} else {
				break;
			}
		} while ( markerIndex != -1 );

		// Record any remainder

		builder.append( withMnemonic.substring( beginIndex ) );

		// Return the stripped mnemonic

		return new StrippedMnemonicAndFirstIndex( builder.toString(), mnemonicIndex );
	}

	//
	// Inner class
	//

	/**
	 * Simple immutable structure to store a component and its value property.
	 *
	 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
	 */

	public static class StrippedMnemonicAndFirstIndex
		implements Immutable {

		//
		// Private members
		//

		private String	mStrippedMnemonic;

		private int		mFirstIndex;

		//
		// Constructor
		//

		public StrippedMnemonicAndFirstIndex( String strippedMnemonic, int firstIndex ) {

			mStrippedMnemonic = strippedMnemonic;
			mFirstIndex = firstIndex;
		}

		//
		// Public methods
		//

		public String getStrippedMnemonic() {

			return mStrippedMnemonic;
		}

		public int getFirstIndex() {

			return mFirstIndex;
		}
	}

	//
	// Private statics
	//

	private static final char	MNEMONIC_INDICATOR	= '&';

	private static final int	MNEMONIC_INDEX_NONE	= -1;

	//
	// Private constructor
	//

	private SimpleLayoutUtils() {

		// Can never be called
	}
}
