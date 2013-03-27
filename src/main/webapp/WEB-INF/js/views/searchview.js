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
		"click form button" : "submitted"
	},
	
	submitted : function(event) {		 
		this.model.set({id: $('input[placeholder=Search]').val()}).fetch({
			success : function(model) {
				$('#search-list').html(new SearchListView({model : model.get('relatedKeywords')}).render().el);
			},
			error : function() {
				console.debug("error");
			}
		});
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