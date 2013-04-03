require.config({
	paths: {
        "jquery": "lib/jquery.min",
        "underscore": "lib/underscore",
        "backbone": "lib/backbone",
        "backbone-relational": "lib/backbone-relational",
        "bootstrap": "lib/bootstrap.min",
        "jquery.spin": "lib/jquery.spin",
        "spin": "lib/spin.min",
        "vivagraph": "lib/vivagraph.min"
    },
    shim: {
        "underscore": {
            deps: [],
            exports: "_"
        },
        "backbone": {
            deps: ["jquery", "underscore"],
            exports: "Backbone"
        },
        "backbone-relational": {
            deps: ["backbone"],
            exports: "Backbone"
        },
        "bootstrap": {
        	deps: ["jquery"]
        },
        "jquery.spin": {
        	deps: ["jquery", "spin"],
        	exports: "Spinner"
        }
    } 
});

require([
  'app',

], function(App){
  App.initialize();
  
});