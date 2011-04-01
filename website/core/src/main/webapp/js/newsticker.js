// Based on http://www.bartelme.at/journal/archive/accessible_javascript_newsticker

// Create namespace
if (at == undefined) var at = {};
if (at.bartelme == undefined) at.bartelme = {};

// Newsticker Class
at.bartelme.newsticker = Class.create();
at.bartelme.newsticker.prototype = {
	initialize: function()
	{
		// Get elements
		this.interval = 8000;
		this.newsticker = $("newsticker");
		this.messages  = $A(this.newsticker.getElementsByTagName("li"));
		this.number_of_messages = this.messages.length;
		this.current_message = this.randomMessage();
		this.previous_message = null;

		// Show ticker
		this.hideMessages();
		this.showMessage();
		this.quote_left = $("quote-left");
		this.quote_right = $("quote-right");
		this.quote_left.style.display = 'block';
		this.quote_right.style.display = 'block';
		this.newsticker.style.display = 'block';

		// Install timer
		this.timer = setInterval(this.showMessage.bind(this), this.interval);
  	},
	showMessage: function()
	{
		Effect.Appear(this.messages[this.current_message]);
		setTimeout(this.fadeMessage.bind(this), this.interval-1000);
		if (this.current_message < this.number_of_messages-1)
		{
			this.previous_message = this.current_message;
			this.current_message = this.current_message + 1;
		} else {
			this.current_message = 0;
			this.previous_message = this.number_of_messages - 1;
		}
	},
	fadeMessage: function()
	{
		Effect.Fade(this.messages[this.previous_message]);
	},
	hideMessages: function()
	{
		this.messages.each(function(message)
		{
			Element.hide(message);
		})
	},
	randomMessage: function()
	{
		return Math.floor( Math.random() * this.number_of_messages );
	}
}

Event.observe(window, "load", function(){new at.bartelme.newsticker()}, false);
