package org.metawidget.gwt.client.rpc;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.RemoteService;

public interface InspectorService
	extends RemoteService
{
	String inspect( Serializable toInspect, String type, String... names )
		throws Exception;
}
