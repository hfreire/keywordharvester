
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=1280" user-scalable=no
	minimum-scale=1>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<meta name="description"
	content="A utility for finding keywords related to a given keyword" />
<meta name="keywords" content="Keyword harvester" />
<meta name="author" content="hfreire@exec.sh" />
<title>Keyword Harvester</title>
<link rel="stylesheet" href="/css/lib/bootstrap.min.css">
<link rel="stylesheet" href="/css/lib/font-awesome.min.css">
<link rel="stylesheet" href="/css/app.css">
</head>
<body>
	<div id="app">
		<div id="search"></div>
	</div>
	<script type="text/html" id='search-view'>
		<div class="row">
			<div class="offset6 span6">
				<form class="navbar-search pull-left">
	  				<input type="text" class="search-query" placeholder="Search">
	  				<button class="btn btn-primary" type="button">Search</button>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="offset4 span8">
				<div id="search-list">
				</div>
			</div>
		</div
	</script>
	<script type="text/html" id='search-list-item'>
			<td><@= keyword.text @></td>
			<td><@= relevancy @></td>
	</script>
	<script src="/js/lib/jquery.min.js"></script>
	<script src="/js/lib/jquery.form.js"></script>
	<script src="/js/lib/bootstrap.min.js"></script>
	<script src="/js/lib/underscore.js"></script>
	<script src="/js/lib/json2.js"></script>
	<script src="/js/lib/backbone.js"></script>
	<script src="/js/lib/backbone-relational.js"></script>
	<script src="/js/lib/spin.min.js"></script>
	<script src="/js/lib/jquery.spin.js"></script>
	<script src="/js/models/keywordmodel.js"></script>
	<script src="/js/models/relatedkeywordmodel.js"></script>
	<script src="/js/views/searchview.js"></script>
	<script src="/js/app.js"></script>
</body>
</html>