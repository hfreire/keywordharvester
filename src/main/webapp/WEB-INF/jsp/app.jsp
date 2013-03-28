
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
	<div id="app" class="container-fluid">
		<div id="header">
			<div class="row-fluid">
				<div class="offset2 span8">
					<h1>Keyword Harvester</h1>
					<h4>A utility for finding keywords related to a given keyword</h4>
				</div>
			</div>
		</div>
		<div id="search"></div>
		<div class="row-fluid">
			<div id="footer" class="span12">
				<a href="http://www.veryrelated.com/">Powered by VeryRelated Mind Maps API</a>
				<a href="http://exec.sh">Developed by hfreire@exec.sh</a>
			</div>
		</div>
	</div>
	<script type="text/html" id='search-view'>
		<div class="row-fluid">
			<div class="span12">
				<form class="navbar-search" onsubmit="return false">
	  					<input type="text" class="input-xxlarge" placeholder="Search">
	  					<button id="search-button" class="btn btn-large btn-primary" type="button" data-loading-text="Searching..."><i class="icon-search"></i> Search</button>

					<div class="api-selection">
					<label class="radio inline">
  						<input type="radio" value="veryrelated" checked="checked"> VeryRelated API
					</label>
					<label class="radio inline">
  						<input type="radio" value="bing" disabled=disabled> Bing Search API
					</label>
					<label class="radio inline">
  						<input type="radio" value="google" disabled=disabled> Google AdWords API
					</label>
					</div>
				</form>
			</div>
		</div>
		<div class="row-fluid">
			<div id="search-details" class="span12">
				<h5></h5>
			</div>
		</div>
		<div class="row-fluid">
			<div id="search-tabs" class="span12">

				<div class="tabbable">
  					<ul class="nav nav-tabs">
    					<li class="active"><a href="#list" data-toggle="tab"><i class="icon-list"></i> List</a></li>
    					<li><a href="#graph" data-toggle="tab"><i class="icon-sitemap"></i> Graph</a></li>
  					</ul>
  					<div class="tab-content">
    					<div class="tab-pane active" id="list">
							<div class="row-fluid">
								<div id="search-list" class="span12"></div>
							</div>
    					</div>
    					<div class="tab-pane" id="graph">
							<div class="row-fluid">
								<div id="search-graph"></div>
							</div>
    					</div>
  					</div>
				</div>
			</div>
		</div
	</script>
	<script type="text/html" id='search-list-item'>
			<td><@= keyword.text @></td>
			<td><@= relevancy @></td>
			<td><a href="#search/<@= keyword.text @>" style="width:100%;display:block;"><i class="icon-search"></i></a></td>
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
	<script src="/js/lib/vivagraph.min.js"></script>
	<script src="/js/models/keywordmodel.js"></script>
	<script src="/js/models/relatedkeywordmodel.js"></script>
	<script src="/js/views/searchview.js"></script>
	<script src="/js/app.js"></script>
</body>
</html>