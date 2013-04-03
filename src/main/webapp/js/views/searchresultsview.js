define([
  'jquery', 
  'underscore', 
  'backbone-relational',
  'views/searchresultlistview',
  'vivagraph'
], function($, _, Backbone){
	
	window.SearchResultsView = Backbone.View
	.extend({
		initialize : function() {
			this.template = _.template($('#search-results-view').html());
			this.model.bind("change", this.render, this);
		},

		render : function(eventName) {
			$(this.el).html(this.template());
			return this;
		},

		renderResults : function(model) {
			if (this.model.get('id') == null) {
				$('#search-list').html(
						"<h5>Type in a keyword to search!</h5>");
				$('#search-graph').html(
						"<h5>Type in a keyword to search!</h5>");
			} else {
				$('#search-list').html(new SearchListView({
					model : this.model.get('relatedKeywords')
				}).render().el);

				
				var graph = Viva.Graph.graph();
				var rootNode = graph.addNode(this.model.get('text'), this.model);
				rootNode.isPinned = true;
				rootNode.position = {x: 2000, y: 100};
				
				for (var i = 0; i < this.model.get('relatedKeywords').length; i++) {
					var relatedKeyword = this.model.get('relatedKeywords').models[i];
				    graph.addNode(relatedKeyword.get('keyword').text, relatedKeyword);
				    graph.addLink(this.model.get('text'), relatedKeyword.get('keyword').text);
				}
				
				var graphics = Viva.Graph.View.svgGraphics();
				graphics.node(function(node) {

					var ui = Viva.Graph.svg('g'),
						svgText = Viva.Graph.svg('text').attr('y', -24).text(node.id),
						img = Viva.Graph.svg('circle').attr('r', node.id == model.get('text') ? 32 : 12).attr('fill', '#006DCC');
					ui.append(img);
		            ui.append(svgText);
		            
		            return ui;
		        }).placeNode(function(nodeUI, pos){
		                nodeUI.attr('transform', 
		                        'translate(' + 
		                              (pos.x) + ',' + (pos.y) + 
		                        ')');
				});
				
				var layout = Viva.Graph.Layout.forceDirected(graph, {
	                   springLength : 128,
	                   springCoeff : 0.0008,
	                   dragCoeff : 0.02,
	                   gravity : -1.2
	               });
		
				var renderer = Viva.Graph.View.renderer(graph, {
					graphics   : graphics,
					layout	   : layout,
					container  : document.getElementById('search-graph'),
				});
				renderer.run();
			}
		}
	});
	
	return window.SearchResultsView;
});