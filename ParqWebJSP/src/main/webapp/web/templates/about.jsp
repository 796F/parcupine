<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="/web/templates/base_header.jsp" />
</head>

<body>
	<jsp:include page="/web/templates/base_body_start.jsp" />

	<div class="container">
		<div id="about-block" class="well">
			<h2>About PARQ</h2>
			<p>Founded in 2011, Parq aims to streamline parking utilizing
				current smartphone technologies. It is a "one-touch" parking
				application. Simply park, pull out your smartphone, and with the
				press of a button be on your way.</p>
			<p>Furthermore, Parq also allows you to refill your meter
				wherever you are. It can also help get back your car. Parq does away
				with the hassle of finding coins, the mad dash to refill a meter
				running low on time, as well as the stuggle to recall where you
				parked. Parq is the all-in-one solution to parking.</p>
		</div>
		<div id="about-team">
			<h2>Meet the team</h2>
			<table cellpadding="3px">
				<tr>
					<td><img
						src="<%=request.getContextPath()%>/web/images/unknown_user.png" />
					</td>
					<td>
						<h2>Sunny Long</h2>
						<p>I'm a rising Junior at MIT double majoring in Electrical
							Engineering/Computer Science and Economics. In my spare time I
							love developing new startup ideas and keeping up with the latest
							trends (sometimes combining both of these). Outside of school and
							innovating, I play squash and basketball. I am the founder of
							Parq and am responsible for its expansion and vision.</p>
					</td>
				</tr>
				<tr>
					<td><img
						src="<%=request.getContextPath()%>/web/images/unknown_user.png" />
					</td>
					<td>
						<h2>Gordon Zheng</h2>
						<p>Georgetown MBA alumni, focusing on creating a start-up
							company that will leverage Smartphone and Smartphone technologies
							to change the way people interacts, and exchanges information.
							Extensive industry experience working on software development in
							the area of information security, information exchange, and
							government IT security, combined with a passion for technology
							and innovation.</p>
					</td>
				</tr>
				<tr>
					<td><img
						src="<%=request.getContextPath()%>/web/images/unknown_user.png" />
					</td>
					<td>
						<h2>Michael Xia</h2>
						<p>Michael Xia is an impatient coder. He's worked with SortIQ,
							Booz Allen Hamilton, and various non-profit groups. Currently he acts as lead
							developer at Parq, leading mostly his impatient self, but none
							the less developing the application and server backend. He wrote
							his own search engine completely in C, during which time he
							developed his unrivaled hatred for segfaults and road rage.</p>
					</td>
				</tr>
				<tr>
					<td><img
						src="<%=request.getContextPath()%>/web/images/unknown_user.png" />
					</td>
					<td>
						<h2>Mark Yen</h2>
						<p>MIT CS Master student.</p>
					</td>
				</tr>
			</table>
		</div>
		<jsp:include page="/web/templates/base_body_end.jsp" />
</body>

</html>
