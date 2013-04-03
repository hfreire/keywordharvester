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
		<div id="search-box"></div>
		<div id="search-results"></div>
		<div class="row-fluid">
			<div id="footer" class="span12">
				<a href="http://www.veryrelated.com/">Powered by VeryRelated Mind Maps API</a>
				<a href="http://exec.sh">Developed by hfreire@exec.sh</a>
			</div>
		</div>
	</div>
	<script type="text/html" id='search-box-view'>
		<div class="row-fluid">
			<div class="span12">
				<form class="navbar-search" onsubmit="return false">
	  					<input type="text" class="input-xxlarge" placeholder="Search">
	  					<button id="search-button" class="btn btn-large btn-primary" type="button" data-loading-text="Searching..."><i class="icon-search"></i> Search</button>

					<div class="api-selection">
					<label class="radio inline">
  						<input type="radio" name="api" value="veryrelated" checked> VeryRelated API
					</label>
					<label class="radio inline">
  						<input type="radio" name="api" value="webknox"> WebKnox API
					</label>
					<label class="radio inline">
  						<input type="radio" name="api" value="bing" disabled=disabled> Bing Search API
					</label>
					<label class="radio inline">
  						<input type="radio" name="api" value="google" disabled=disabled> Google AdWords API
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
			</div>
		</div
	</script>
	<script type="text/html" id='search-results-view'>
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
								<div class="offset2 span8">
									<div id="search-graph"></div>
								</div> 
								<!--<div class="offset2 span8">
									<canvas id="search-graph" width="800" height="600"></canvas>
								</div>-->
							</div>
    					</div>
  					</div>
				</div>
	</script>
	<script type="text/html" id='search-list-item'>
			<td><@= keyword.text @></td>
			<td><@= relevancy @></td>
			<td><a href="#search/<@= keyword.text @>" style="width:100%;display:block;"><i class="icon-search"></i></a></td>
	</script>

	<script data-main="js/main" src="js/lib/require.js"></script>
</body>
</html>