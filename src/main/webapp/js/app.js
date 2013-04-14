define([
  'underscore', 
  'backbone.marionette',
  'vent'
], function(_, Marionette, vent){
	
	var app = new Marionette.Application();
	
	app.addRegions({
		content: '#content'
    });
	
	app.addInitializer(function(options) {
        new options.router.Router({
            controller: options.controller
        });
        
		_.templateSettings = {
			    interpolate: /\<\@\=(.+?)\@\>/gim,
			    evaluate: /\<\@([\s\S]+?)\@\>/gim,
			    escape: /\<\@\-(.+?)\@\>/gim
		};
    });
	
	app.on("initialize:after", function() {
        Backbone.history.start();
    });
	
	vent.on('app:show', function(appView) {
        app.content.show(appView);
    });
   
    return app;
});