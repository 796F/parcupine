
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="/web/templates/nav.jsp" />
	<jsp:include page="/web/templates/main_map.jsp" />
</head>
<body>
	<div class="container-fluid">
		<div class="sidebar">
			<p class="well"><font style="color:red">Red Spaces</font> are Taken.</p>
			<p class="well"><font style="color:green">Green Spaces</font> are Available.</p>
		</div>
		<div class="content">
			<div id="map_canvas"></div>
			<p style="clear: both"></p>
		</div>
	</div>
	<jsp:include page="/web/templates/footer.jsp" />
</body>


</html>


