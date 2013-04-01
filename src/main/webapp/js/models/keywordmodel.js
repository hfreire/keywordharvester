window.KeywordModel = Backbone.RelationalModel.extend({
        urlRoot: "/api/keyword",
        defaults: {
            "id": null,
            "text": "",
          },
          relations: [{
      		type: 'HasMany',
      		key: 'relatedKeywords',
      		relatedModel: 'RelatedKeywordModel'
      	}]
});