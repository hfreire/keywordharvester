(function() {
	Backbone.View.prototype.close = function() {
		if (this.beforeClose) {
			this.beforeClose();
		}
		this.remove();
		this.unbind();
	};

	var AppRouter = Backbone.Router.extend({
		routes : {
			"" : "showSearch",
		},

		showView : function(selector, view) {
			if (this.currentView)
				this.currentView.close();
			
			$(selector).html(view.render().el);
			this.currentView = view;
			return view;
		},
		
		showSearch : function(id) {
			app.showView('#search', new SearchView({model: app.keyword}));
		}
	});
	
	app = new AppRouter();
	app.keyword = new KeywordModel();
	Backbone.history.start();
	
	//$('#search-box').spin('large', 'gray');
})();

_.templateSettings = {
	    interpolate: /\<\@\=(.+?)\@\>/gim,
	    evaluate: /\<\@([\s\S]+?)\@\>/gim,
	    escape: /\<\@\-(.+?)\@\>/gim
	};