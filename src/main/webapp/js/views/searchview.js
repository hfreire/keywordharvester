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

							console.debug("error");
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

					var Renderer = function(canvas) {
						var canvas = $(canvas).get(0);
						var ctx = canvas.getContext("2d");
						var gfx = arbor.Graphics(canvas);
						var relatedKeywordGraph;

						var that = {
							init : function(system) {
								relatedKeywordGraph = system;
								relatedKeywordGraph.screenSize(canvas.width,
										canvas.height);
								relatedKeywordGraph.screenPadding(80);
								//that.initMouseHandling();
							},

							redraw : function() {
								ctx.fillStyle = "white";
								ctx.fillRect(0, 0, canvas.width, canvas.height);

								relatedKeywordGraph.eachEdge(function(edge,
										pt1, pt2) {
									ctx.strokeStyle = "rgba(0,0,0, .333)";
									ctx.lineWidth = 1;
									ctx.beginPath();
									ctx.moveTo(pt1.x, pt1.y);
									ctx.lineTo(pt2.x, pt2.y);
									ctx.stroke();
								});

								relatedKeywordGraph
										.eachNode(function(node, pt) {
											var w = Math
													.max(
															20,
															20 + ctx
																	.measureText(node.name).width);
											gfx.oval(pt.x - w / 2,
													pt.y - w / 2, w, w, {
														fill : "#006DCC",
														alpha : node.data.alpha
													})
											gfx.text(node.name, pt.x, pt.y + 7,
													{
														color : "white",
														align : "center",
														font : "Arial",
														size : 12
													})
										})
							},

							initMouseHandling : function() {
								var dragged = null;
								var handler = {
									clicked : function(e) {
										var pos = $(canvas).offset();
										_mouseP = arbor.Point(e.pageX
												- pos.left, e.pageY - pos.top)
										dragged = relatedKeywordGraph
												.nearest(_mouseP);

										if (dragged && dragged.node !== null) {
											// while we're dragging, don't let
											// physics move the node
											dragged.node.fixed = true
										}

										$(canvas).bind('mousemove',
												handler.dragged)
										$(window).bind('mouseup',
												handler.dropped)

										return false
									},
									dragged : function(e) {
										var pos = $(canvas).offset();
										var s = arbor.Point(e.pageX - pos.left,
												e.pageY - pos.top)

										if (dragged && dragged.node !== null) {
											var p = relatedKeywordGraph
													.fromScreen(s)
											dragged.node.p = p
										}

										return false
									},

									dropped : function(e) {
										if (dragged === null
												|| dragged.node === undefined)
											return

										if (dragged.node !== null)
											dragged.node.fixed = false
										dragged.node.tempMass = 1000
										dragged = null
										$(canvas).unbind('mousemove',
												handler.dragged)
										$(window).unbind('mouseup',
												handler.dropped)
										_mouseP = null
										return false
									}
								}

								$(canvas).mousedown(handler.clicked);

							},

						}
						return that
					}

					var sys = arbor.ParticleSystem();
					sys.parameters({
						gravity : false
					});
					sys.renderer = Renderer("#search-graph");

					var rootNode = sys.addNode(this.model.get('text'),
							this.model);

					for ( var i = 0; i < this.model.get('relatedKeywords').length; i++) {
						var relatedKeyword = this.model.get('relatedKeywords').models[i];
						var node = sys.addNode(
								relatedKeyword.get('keyword').text,
								relatedKeyword);
						sys.addEdge(rootNode, node);
					}
				}
			}
		});

window.SearchListView = Backbone.View.extend({
	tagName : 'table',

	className : 'table table-hover',

	initialize : function() {

		this.model.bind("reset", this.render, this);
		var self = this;
		this.model.bind("add", function(relatedKeyword) {
			$(self.el).append(new SearchListItemView({
				model : relatedKeyword
			}).render().el);
		});
	},

	render : function(eventName) {
		_.each(this.model.models, function(relatedKeyword) {
			$(this.el).append(new SearchListItemView({
				model : relatedKeyword
			}).render().el);
		}, this);
		return this;
	}
});

window.SearchListItemView = Backbone.View.extend({
	tagName : 'tr',

	initialize : function() {
		this.template = _.template($('#search-list-item').html());
		this.model.bind("change", this.render, this);
		this.model.bind("destroy", this.close, this);
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}
});