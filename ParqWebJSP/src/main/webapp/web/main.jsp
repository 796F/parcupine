
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="/web/templates/base_header.jsp" />

<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">
	
	<jsp:include page="/web/templates/main_map.jsp"/>
	</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/web/js/jquery.autocomplete-min.js"></script>
<link href="<%=request.getContextPath()%>/web/css/autocomplete.css"
	type="text/css/" rel="stylesheet" />
<script type="text/javascript">
	$(document).ready(function() { 
	   $('#search-location').autocomplete({
	      'serviceUrl':'/actions/autocomplete_location/'
	   });
	});
	</script>
</head>
<body>
	<jsp:include page="/web/templates/base_body_start.jsp" />

	<%-- code currently broken, need time to fix later --%>
	<div id="parqform-container">
		<div class="container">
			<span></span>
			<form id="parqform" action="/user/view/location" method="get">
				<div class="pull-left span16">
					<div class="pull-left span13">
						<input type="text" id="search-location" name="location"	placeholder="Find a spot near..." disabled="disabled"/>
					</div>
					<input type="submit" id="search-loction-button"	class="btn large pull-right span3" value="PARQ me!" disabled="disabled" />
				</div>
				<p class="clear"></p>
			</form>
		</div>
	</div>

	<div class="container-fluid">
		<div class="sidebar">
			<p class="well">Looking for a place to park? Tell us where you
				are and let us guide you to the nearest parking space.</p>
		</div>
		<div class="content">
			<div id="map_canvas"></div>
			<p style="clear: both"></p>
		</div>
	</div>

	<jsp:include page="/web/templates/base_body_end.jsp" />
</body>

</html>


