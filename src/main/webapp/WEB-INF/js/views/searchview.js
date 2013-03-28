window.SearchView = Backbone.View.extend({
	tagName: 'div',
	
	initialize:function() {
		this.template = _.template($('#search-view').html());
		this.model.bind("change", this.render, this);
	},
	
	render:function(eventName) {
		$(this.el).html(this.template());
		return this;
	},
	
	events : {
		"click form button" : "submitted",
	},
	
	submitted : function(event) {
		if ($('input[placeholder=Search]').val() == "") {
			this.model.set({id: null});
			$('#search-list').html("<h5>Type in a keyword to search!</h5>");
			$('#search-graph').html("<h5>Type in a keyword to search!</h5>");
			return;
		}

		var start = Date.now();
		this.model.set({id: $('input[placeholder=Search]').val()}).fetch({
			data : $.param({ 'api': $('input[checked=checked]').val()}),
			success : function(model) {
				var stop = Date.now();
				$('#search-details h5').html(model.get('relatedKeywords').size() + " related keywords in " + (stop-start)/1000 + " seconds");
				
				$('input[placeholder=Search]').val(model.id);
				$('#search-button').button('reset');
				$('#search-list').spin(false);
				
				if (model.get('message')) {
					$('#search-list').html("<h5>" + model.get('message') + "</h5>");
					$('#search-graph').html("<h5>" + model.get('message') + "</h5>");
					return;
				}
				
				$('#search-list').html(new SearchListView({model : model.get('relatedKeywords')}).render().el);
				
				var graph = Viva.Graph.graph();
				graph.addNode(model.get('text'), model);
				
				for (var i = 0; i < model.get('relatedKeywords').length; i++) {
					var relatedKeyword = model.get('relatedKeywords').models[i];
				    graph.addNode(relatedKeyword.get('keyword').text, relatedKeyword);
				    graph.addLink(model.get('text'), relatedKeyword.get('keyword').text);
				}

				var graphics = Viva.Graph.View.svgGraphics();
				graphics.node(function(node) {

				var ui = Viva.Graph.svg('g'),
		                  // Create SVG text element with user id as content
		                  svgText = Viva.Graph.svg('text').attr('y', '-36px').text(node.id),
		                  img = Viva.Graph.svg('circle')
		                     .attr('r', node.id == model.get('text') ? 32 : 12)
		                     .attr('fill', '#004DFF');
		            
		              ui.append(svgText);
		              ui.append(img);
		              return ui;
				       
				    })
				    .placeNode(function(nodeUI, pos){
				        // Shift image to let links go to the center:
		                nodeUI.attr('transform', 
	                            'translate(' + 
	                                  (pos.x - 12/2) + ',' + (pos.y - 12/2) + 
	                            ')');
				    });

				var renderer = Viva.Graph.View.renderer(graph, {
					graphics   : graphics,
					container  : document.getElementById('search-graph'),
				});
				renderer.run();

			},
			error : function() {
				$('#search-button').button('reset');
				$('#search-list').spin(false);
				
				console.debug("error");
			}
		});
		$('#search-button').button('loading');
		$('#search-list').spin('small', 'gray');
	}
});

window.SearchListView = Backbone.View.extend({
    tagName:'table',
    
    className: 'table table-hover',

    initialize:function () {

        this.model.bind("reset", this.render, this);
        var self = this;
        this.model.bind("add", function (relatedKeyword) {
            $(self.el).append(new SearchListItemView({model:relatedKeyword}).render().el);
        });
    },

    render:function (eventName) {
        _.each(this.model.models, function (relatedKeyword) {
            $(this.el).append(new SearchListItemView({model:relatedKeyword}).render().el);
        }, this);
        return this;
    }
});

window.SearchListItemView = Backbone.View.extend({
	tagName:'tr',

    initialize:function () {
        this.template = _.template($('#search-list-item').html());
        this.model.bind("change", this.render, this);
        this.model.bind("destroy", this.close, this);
    },

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    }
});