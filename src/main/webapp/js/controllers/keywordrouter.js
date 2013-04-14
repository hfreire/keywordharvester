define(['backbone.marionette'], function(Marionette) {
    'use strict';

    var KeywordRouter = {};

    KeywordRouter.Router = Marionette.AppRouter.extend({
        appRoutes: {
        	"" : "emptySearch",
			"search/:keywordText" : "search",
        }
    });

    return KeywordRouter;
});