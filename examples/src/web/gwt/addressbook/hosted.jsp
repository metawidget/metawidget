<%@ page language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<tags:page>

	<script type="application/javascript" src="org.metawidget.example.GwtAddressBook/org.metawidget.example.GwtAddressBook.nocache.js"></script>
	
	<!-- Don't complicate GWT Hosted mode with JSTL tags -->
	
	<script type="application/javascript">
	var bundle = {
	"add": "Add",
	"addBusiness": "Add Business Contact",
	"addPersonal": "Add Personal Contact",
	"address": "Address",
	"back": "Back",
	"businessContact": "Business Contact",
	"cancel": "Cancel",
	"city": "City",
	"communications": "Communications",
	"company": "Company",
	"contactDetails": "Contact Details",
	"dateOfBirth": "Date of Birth",
	"delete": "Delete",
	"edit": "Edit",
	"errors.required": "{0} is required",
	"firstname": "Firstname",
	"gender": "Gender",
	"javax.faces.component.UIInput.CONVERSION": "Conversion error",
	"javax.faces.component.UIInput.REQUIRED": "{0} is required",
	"javax.faces.component.UISelectMany.INVALID": "Not a valid option",
	"javax.faces.component.UISelectOne.INVALID": "Not a valid option",
	"javax.faces.validator.DoubleRangeValidator.MAXIMUM": "{1} must not be greater than {0}",
	"javax.faces.validator.DoubleRangeValidator.MINIMUM": "{1} must not be less than {0}",
	"javax.faces.validator.DoubleRangeValidator.NOT_IN_RANGE": "{2} must be between {0} and {1}",
	"javax.faces.validator.DoubleRangeValidator.TYPE": "Not correct type",
	"javax.faces.validator.LengthValidator.MAXIMUM": "{1} must not be longer than {0} characters",
	"javax.faces.validator.LengthValidator.MINIMUM": "{1} must not be shorter than {0} characters",
	"javax.faces.validator.LongRangeValidator.MAXIMUM": "{1} must not be greater than {0}",
	"javax.faces.validator.LongRangeValidator.MINIMUM": "{1} must not be less than {0}",
	"javax.faces.validator.LongRangeValidator.NOT_IN_RANGE": "{2} must be between {0} and {1}",
	"javax.faces.validator.LongRangeValidator.TYPE": "Not correct type",
	"notes": "Notes",
	"numberOfStaff": "Number of Staff",
	"org.apache.commons.beanutils.ConversionException": "Unable to convert {0}",
	"other": "Other",
	"personalContact": "Personal Contact",
	"postcode": "Postcode",
	"save": "Save",
	"search": "Search",
	"state": "State",
	"street": "Street",
	"surname": "Surname",
	"title": "Title",
	"type": "Type"
};
</script>
	
	<div id="page-image">
		<img src="media/addressbook.gif" alt=""/>
	</div>

	<div id="content">
	
		<div id="metawidget"></div>		
		<div id="contacts"></div>
		
	</div>
		
</tags:page>
