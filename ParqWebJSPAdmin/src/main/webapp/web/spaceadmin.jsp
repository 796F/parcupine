<%@ page import="com.parq.web.model.WebParkingSpot"%>
<%@ page import="com.parq.web.service.ParqWebServiceFactory"%>
<%@ page import="com.parq.web.service.ParqWebService"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link href="<%=request.getContextPath()%>/web/css/bootstrap.css" type="text/css" rel="stylesheet" />
	</head>
	
	<%
		ParqWebService service = ParqWebServiceFactory.getParqWebServiceInstance();
		List<WebParkingSpot> parkingLocations = service.findParkingLocations();
		
		WebParkingSpot space0 = null;
		WebParkingSpot space1 = null;
		WebParkingSpot space2 = null;
		WebParkingSpot space3 = null;
		WebParkingSpot space4 = null;
		WebParkingSpot space5 = null;
			
		for (WebParkingSpot ploc : parkingLocations) {
			if (ploc.getSpotName().contains("1")) {
				space0 = ploc;
			} else if (ploc.getSpotName().contains("2")) {
				space1 = ploc;
			} else if (ploc.getSpotName().contains("3")) {
				space2 = ploc;
			} else if (ploc.getSpotName().contains("4")) {
				space3 = ploc;
			} else if (ploc.getSpotName().contains("5")) {
				space4 = ploc;
			} else {
				space5 = ploc;
			}
		}
	%>
	
	<body>
		<div class="well span12 container modal-body">
			<form method="post"	action="<%=request.getContextPath()%>/web/action/spaceadminaction.jsp">
				<h2>Parqupine Admin</h2>
				<table>
					<tbody>
						<tr>
							<th><label for="space_0">101:</label></th>
							<th><label><%= (space0 == null || space0.isAvailable()) ? "Free" : "Parked" %></label></th>
							<td>
								<select id="space_0" name="space_0">
									<option value=""></option>
									<option value="Free">Free</option>
									<option value="Parked">Parked</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="space_1">102:</label></th>
							<th><label><%= (space1 == null || space1.isAvailable()) ? "Free" : "Parked" %></label></th>
							<td>
								<select id="space_1" name="space_1">
									<option value=""></option>
									<option value="Free">Free</option>
									<option value="Parked">Parked</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="space_2">103:</label></th>
							<th><label><%= (space2 == null || space2.isAvailable()) ? "Free" : "Parked" %></label></th>
							<td>
								<select id="space_2" name="space_2">
									<option value=""></option>
									<option value="Free">Free</option>
									<option value="Parked">Parked</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="space_3">104:</label></th>
							<th><label><%= (space3 == null || space3.isAvailable()) ? "Free" : "Parked" %></label></th>
							<td>
								<select id="space_3" name="space_3">
									<option value=""></option>
									<option value="Free">Free</option>
									<option value="Parked">Parked</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="space_4">105:</label></th>
							<th><label><%= (space4 == null || space4.isAvailable()) ? "Free" : "Parked" %></label></th>
							<td>
								<select id="space_4" name="space_4">
									<option value=""></option>
									<option value="Free">Free</option>
									<option value="Parked">Parked</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="space_5">106:</label></th>
							<th><label><%= (space5 == null || space5.isAvailable()) ? "Free" : "Parked" %></label></th>
							<td>
								<select id="space_5" name="space_5">
									<option value=""></option>
									<option value="Free">Free</option>
									<option value="Parked">Parked</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<td>
								<center>
									<input type="button" class="btn large" value="Refresh" onclick="history.go(0)"/>
								</center>
							</td>
							<td/>
							<td>
								<center>
									<input type="submit" class="btn large" value="Update" />
								</center>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</body>
</html>
