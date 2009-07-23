<%@ page language="java" %>
<%@ taglib uri="http://metawidget.org/html" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	// Manual mapping
	
	org.metawidget.shared.allwidgets.model.AllWidgets allWidgets = new org.metawidget.shared.allwidgets.model.AllWidgets();
	pageContext.setAttribute( "allWidgets", allWidgets );
	
	allWidgets.setTextbox( request.getParameter( "allWidgets.textbox" ) );
	allWidgets.setLimitedTextbox( request.getParameter( "allWidgets.limitedTextbox" ) );
	allWidgets.setTextarea( request.getParameter( "allWidgets.textarea" ) );
	allWidgets.setPassword( request.getParameter( "allWidgets.password" ) );
	allWidgets.setByte( Byte.parseByte( request.getParameter( "allWidgets.byte" ) ) );

	String byteObject = request.getParameter( "allWidgets.byteObject" );

	if ( byteObject == null || "".equals( byteObject ) )
		allWidgets.setByteObject( null );
	else
		allWidgets.setByteObject( Byte.valueOf( byteObject ) );

	allWidgets.setShort( Short.parseShort( request.getParameter( "allWidgets.short" ) ) );

	String shortObject = request.getParameter( "allWidgets.shortObject" );

	if ( shortObject == null || "".equals( shortObject ) )
		allWidgets.setShortObject( null );
	else
		allWidgets.setShortObject( Short.valueOf( shortObject ) );

	allWidgets.setInt( Integer.parseInt( request.getParameter( "allWidgets.int" ) ) );

	String integerObject = request.getParameter( "allWidgets.integerObject" );

	if ( integerObject == null || "".equals( integerObject ) )
		allWidgets.setIntegerObject( null );
	else
		allWidgets.setIntegerObject( Integer.valueOf( integerObject ) );

	allWidgets.setRangedInt( Integer.parseInt( request.getParameter( "allWidgets.rangedInt" ) ) );

	String rangedInteger = request.getParameter( "allWidgets.rangedInteger" );

	if ( rangedInteger == null || "".equals( rangedInteger ) )
		allWidgets.setRangedInteger( null );
	else
		allWidgets.setRangedInteger( Integer.valueOf( rangedInteger ) );

	allWidgets.setLong( Long.parseLong( request.getParameter( "allWidgets.long" ) ) );

	String longObject = request.getParameter( "allWidgets.longObject" );

	if ( longObject == null || "".equals( longObject ) )
		allWidgets.setLongObject( null );
	else
		allWidgets.setLongObject( Long.valueOf( longObject ) );

	allWidgets.setFloat( Float.parseFloat( request.getParameter( "allWidgets.float" ) ) );

	String floatObject = request.getParameter( "allWidgets.floatObject" );

	if ( floatObject == null || "".equals( floatObject ) )
		allWidgets.setFloatObject( null );
	else
		allWidgets.setFloatObject( Float.valueOf( floatObject ) );

	allWidgets.setDouble( Double.parseDouble( request.getParameter( "allWidgets.double" ) ) );

	String doubleObject = request.getParameter( "allWidgets.doubleObject" );

	if ( doubleObject == null || "".equals( doubleObject ) )
		allWidgets.setDoubleObject( null );
	else
		allWidgets.setDoubleObject( Double.valueOf( doubleObject ) );

	allWidgets.setChar( ( request.getParameter( "allWidgets.char" ) ).charAt( 0 ) );

	if ( "on".equals( request.getParameter( "allWidgets.boolean" )))
		allWidgets.setBoolean( true );
		
	String booleanObject = request.getParameter( "allWidgets.booleanObject" );

	if ( booleanObject == null || "".equals( booleanObject ) )
		allWidgets.setBooleanObject( null );
	else
		allWidgets.setBooleanObject( Boolean.valueOf( booleanObject ) );

	allWidgets.setDropdown( request.getParameter( "allWidgets.dropdown" ) );
	allWidgets.setDropdownWithLabels( request.getParameter( "allWidgets.dropdownWithLabels" ) );
	allWidgets.setNotNullDropdown( Byte.parseByte( request.getParameter( "allWidgets.notNullDropdown" ) ));
	allWidgets.setNotNullObjectDropdown( request.getParameter( "allWidgets.notNullObjectDropdown" ) );
	
	allWidgets.getNestedWidgets().getFurtherNestedWidgets().setNestedTextbox1( request.getParameter( "allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox1" ) );
	allWidgets.getNestedWidgets().getFurtherNestedWidgets().setNestedTextbox2( request.getParameter( "allWidgets.nestedWidgets.furtherNestedWidgets.nestedTextbox2" ) );
	allWidgets.getNestedWidgets().setNestedTextbox1( request.getParameter( "allWidgets.nestedWidgets.nestedTextbox1" ) );
	allWidgets.getNestedWidgets().setNestedTextbox2( request.getParameter( "allWidgets.nestedWidgets.nestedTextbox2" ) );
	allWidgets.getReadOnlyNestedWidgets().setNestedTextbox1( request.getParameter( "allWidgets.readOnlyNestedWidgets.nestedTextbox1" ) );
	allWidgets.getReadOnlyNestedWidgets().setNestedTextbox2( request.getParameter( "allWidgets.readOnlyNestedWidgets.nestedTextbox2" ) );
	
	String[] values = org.metawidget.util.ArrayUtils.fromString( request.getParameter( "allWidgets.nestedWidgetsDontExpand" ) );

	if ( values.length != 0 )
	{
		org.metawidget.shared.allwidgets.model.AllWidgets.NestedWidgets nestedWidgetsDontExpand = new org.metawidget.shared.allwidgets.model.AllWidgets.NestedWidgets();
		nestedWidgetsDontExpand.setNestedTextbox1( values[0] );
	
		if ( values.length > 1 )
			nestedWidgetsDontExpand.setNestedTextbox2( values[1] );	
		
		allWidgets.setNestedWidgetsDontExpand( nestedWidgetsDontExpand );
	}

	String date = request.getParameter( "allWidgets.date" );

	if ( date == null || "".equals( date ) )
	{
		allWidgets.setDate( null );
	}
	else
	{
		allWidgets.setDate( new java.text.SimpleDateFormat( "EEE MMM dd HH:mm:ss zzz yyyy" ).parse( date ) );
	}

	allWidgets.setReadOnly( request.getParameter( "allWidgets.readOnly" ));
	allWidgets.setHidden( request.getParameter( "allWidgets.hidden" ));	
%>

<html>
	<body>

		<fmt:setBundle basename="org.metawidget.shared.allwidgets.resource.Resources" var="bundle"/>
		
		<m:metawidget value="allWidgets" bundle="${bundle}" createHiddenFields="true" readOnly="true" config="config/metawidget.xml">
			<m:param name="numberOfColumns" value="2"/>
			<m:stub value="longObject"/>				
		</m:metawidget>
		
	</body>	    
</html>
