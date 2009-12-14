package org.metawidget.gwt.quirks.client.model;

import java.io.Serializable;

import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;

public class GwtTabQuirks
	implements Serializable
{
	private final static long	serialVersionUID	= 1l;

	public String				abc;

	@UiSection( { "Foo", "Bar" } )
	public boolean				def;

	@UiLarge
	public String				ghi;

	@UiSection( { "Foo", "Baz" } )
	public String				jkl;

	@UiSection( { "Foo", "" } )
	public boolean				mno;

	@UiSection( { "Foo", "Moo" } )
	public String				pqr;

	@UiSection( "" )
	public String				stu;
}
