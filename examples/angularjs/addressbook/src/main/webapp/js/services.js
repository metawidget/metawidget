'use strict';

/* Services */

angular.module('addressBookServices', [])
	.factory('contacts', function() {
		var _all = [
		            	{
		            	 "id": 1,
		        		 "firstname": "Homer",
		        		 "surname": "Simpson",
		        		 "communications": "0402 123 456",
		        		 "type": "personal"
		            	},
		        		{
		        		 "id": 2,
		        		 "firstname": "Marge",
		        		 "surname": "Simpson",
		        		 "communications": "0402 123 789",
		        		 "type": "personal"
		        		},
		        		{
		        		 "id": 3,
		        		 "firstname": "Montgomery",
		        		 "surname": "Burns",
		        		 "communications": "0402 123 999",
		        		 "type": "business"
		        		}
					];
	
		return _all;
	})
	.factory('metawidgetConfig', function() {
		return {
			inspector: new metawidget.CompositeInspector( [
        		    metawidget.PropertyInspector,
        		    function( toInspect, type ) {
        		    	
        		    	switch( type ) {
        		    		case 'search':
        		    			return [ { "name": "type", "lookup": "personal,business" } ];
        		    			
        		    		case 'current':
        		    			return [ { "name": "id", "hidden": "true" } ];        		    		
        		    	}
        		    }
        	] )
		};
	});