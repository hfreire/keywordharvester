define([
  'jquery',
  'underscore',
  'backbone-relational',
  'bootstrap',
  'jquery.spin',
  'views/searchresultsview'
], function($, _, Backbone, Spinner){

	window.SearchBoxView = Backbone.View.extend({
		tagName : 'div',
	
		initialize : function() {
			this.template = _.template($('#search-box-view').html());
		},
	
		render : function(eventName) {
			$(this.el).html(this.template());
			this.results = new SearchResultsView({
				model : this.model
			});
			$('#search-results').html(this.results.render().el);
			return this;
		},
	
		events : {
			"click form button" : "submitted",
		},
	
		submitted : function(event) {
			if ($('input[placeholder=Search]').val() == "") {
				this.model.set({
					id : null
				});
				this.results.renderResults();
			} else {
				if (this.model.get('message'))
					this.model.unset('message');
	
				var start = Date.now();
	
				this.model.set({
					id : $('input[placeholder=Search]').val()
				}).fetch(
						{
							data : $.param({
								'api' : $("input:checked").val()
							}),
							success : function(model) {
								var stop = Date.now();
								$('#search-details h5').html(
										"Found "
												+ model.get('relatedKeywords')
														.size()
												+ " related keywords in "
												+ (stop - start) / 1000
												+ " seconds");
	
								$('input[placeholder=Search]').val(model.id);
								$('#search-button').button('reset');
								$('#search-list').spin(false);
	
								if (model.get('message')) {
									$('#search-list')
											.html(
													"<h5>" + model.get('message')
															+ "</h5>");
									$('#search-graph')
											.html(
													"<h5>" + model.get('message')
															+ "</h5>");
									return;
								}
	
								app.currentView.results.renderResults(model);
							},
							error : function() {
								$('#search-button').button('reset');
								$('#search-list').spin(false);
							}
						});
				this.model.set('relatedKeywords', '');
	
				if ($('#search-details h5').html())
					$('#search-details h5').empty();
	
				$('#search-button').button('loading');
				$('#search-list').spin('small', 'gray');
	
			}
		}
	});
	
	return window.SearchBoxView;
});