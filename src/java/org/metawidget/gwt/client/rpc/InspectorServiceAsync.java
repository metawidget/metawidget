package org.metawidget.gwt.client.rpc;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InspectorServiceAsync
{
	void inspect( Serializable toInspect, String type, String[] names, AsyncCallback<String> callback )
		throws Exception;
}
