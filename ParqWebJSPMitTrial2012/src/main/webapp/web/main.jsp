<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Parqupine</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/web/css/parq.css" />
	<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/web/js/jquery.scrollTo-min.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/web/js/jquery.localscroll-min.js'></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$.localScroll({
				duration:500,
			});
		});
	</script>
	<jsp:include page="/web/templates/nav.jsp" />
</head>

<body>
    <table cellpadding="0" cellspacing="0" width=100%>
      <tr><td width="40px"/><td align="left">
      <table cellpadding="0" cellspacing="0">
         <tr>
            <td width="200px"></td>
            <td width="715px">
               <table cellpadding="0" cellspacing="0">
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7"><img src="<%=request.getContextPath()%>/web/images/header_0.png"/></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="20px"></td>
                  </tr>
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7">
                     	<!-- Video Frame, need to add link to youtube video -->
                     	<iframe width=715px height=400px> 
                     	</iframe>
                     </td>
                  </tr>
                  <tr>
                      <td colspan="8" height="50px"></td>
                  </tr>
                  <tr>
                     <td colspan="8"><img src="<%=request.getContextPath()%>/web/images/line.png" /></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="50px"></td>
                  </tr>
               </table>
            </td>
         </tr>
      </table>
      </td></tr>

      <tr><td align="left" colspan="2">
      <table cellpadding="0" cellspacing="0">
         <tr>
            <td width="240px" valign="top" class="team" style="margin-left: -40px">
	            <span class="bb">TEAM</span><br><br>
	
	            Carlo Ratti - <span class="position">Directors</span><br>
	            Assaf Biderman - <span class="position">Directors</span><br>
	            Aurimas Bukauskas - <span class="position">Research team</span><br>
	            David Lee - <span class="position">Research team</span><br>
	            Eric Baczuk - <span class="position">Research team</span><br>
	            Gordon Zheng - <span class="position">App team</span><br>
	            Mark Yen - <span class="position">App team</span><br>
	            Mike Xia - <span class="position">App team</span><br>
	            Sunny Long - <span class="position">App team</span><br>
	            E Roon Kang - <span class="position">Design team</span><br>
	            Kael Greco - <span class="position">Design team</span><br>
	            Kyuha Shim - <span class="position">Design team</span><br>
	            Nicholas Marchesi - <span class="position">Design team</span><br>


            </td>
            <td width="715px">
               <table cellpadding="0" cellspacing="0">
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7"><img src="<%=request.getContextPath()%>/web/images/header_1.png"/></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="35px"></td>
                  </tr>
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7"><img src="<%=request.getContextPath()%>/web/images/subHeader_0.png"/></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="12px"></td>
                  </tr>
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7">
                     	<table cellpadding="0" cellspacing="0">
                           <tr>
                              <td class="main" width="275px">
                              The average American spends 50 hours looking for parking each year. Cruising for parking spots accounts for up to 30% of traffic congestion in downtown areas. Meanwhile, we spend millions of dollars to install and maintain parking meters, collect payments, and patrol enforcement officers across every street parking spot, regardless of demand or compliance. Despite all this effort, we still face longer trips, lower revenues, and complete opacity on where open spots are.

Street parking spaces also comprise a huge chunk of public urban space that is inflexibly reserved for cars and not people. They represent major opportunity cost in places that could make innovative use of them during parts of the day, such as pocket parks, cafe seating, recycling drop-offs, and bike racks. Turning over this space to local users in a flexible, equitable way is an unmet need in modern cities.
                              </td>
                              <td width="65px"></td>
                              <td valign="top"><img src="<%=request.getContextPath()%>/web/images/about_0.png" /></td>
                           </tr>
                        </table>
                     </td>
                  </tr>
                  <tr>
                    <td colspan="8" height="50px"></td>
                  </tr>
                    <tr>
                     <td width="14px"></td>
                     <td colspan="7"><img src="<%=request.getContextPath()%>/web/images/subHeader_1.png"/></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="12px"></td>
                  </tr>
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7">
                     	<table cellpadding="0" cellspacing="0">
                           <tr>
                              <td class="main" width="275px">
                              Parkupine investigates how mobile technology can transform parking in cities. We hypothesize that using mobile phones to locate, pay for, and manage parking spaces can make both the price and availability of these spots transparent, in real-time, to everyone. By cutting down the time it takes to find an open spot and handling payment electronically, we can improve the experience of parking while greatly reducing energy use, air pollution, traffic congestion, and enforcement cost. We can do this without need for street meters or embedded car sensors, relying on digital traces and crowdsourced information to monitor the availability of spots. This would further reduce maintenance, collection, and enforcement costs.
                              </td>
                              <td width="65px"></td>
                              <td valign="top"><img src="<%=request.getContextPath()%>/web/images/about_1.png" /></td>
                           </tr>
                           <tr>
                              <td colspan="3" height="25px"></td>
                           </tr>
                           <tr>
                              <td width="275px" valign="top"><img src="<%=request.getContextPath()%>/web/images/about_2.png" / style="padding-left:5px;"/></td>
                              <td width="65px"></td>
                              <td class="main2" width="250px">
                              The first phase of Parkupine is a pilot study on the MIT campus, where we will test our mobile app in the field with real drivers and parking spaces. We will observe how users interact with the system in several ways: finding parking spots using the real-time map, checking their car in and out of their spot, and reporting observed parking availability in other spots along the same street. There will be cash incentives to minimize parking time and contribute reports to the crowdsourced platform. Finally, we will experiment with utilizing the space for non-parking uses at different times of the day.

                              </td>
                           </tr>
                        </table>
                     </td>
                  </tr>
                  <tr>
                     <td colspan="8" height="50px"></td>
                  </tr>
                  <tr>
                     <td colspan="8"><img src="<%=request.getContextPath()%>/web/images/line.png" /></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="50px"></td>
                  </tr>
               </table>
            </td>
         </tr>
      </table>
      </td></tr>
      <tr><td width="40px"/><td align="left">
      <table cellpadding="0" cellspacing="0">
         <tr>
            <td width="200px"></td>
            <td width="715px">
               <table cellpadding="0" cellspacing="0">
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7"><img src="<%=request.getContextPath()%>/web/images/header_2.png"/></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="35px"></td>
                  </tr>
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7">
                     	<table cellpadding="0" cellspacing="0">
                           <tr>
                              <td class="main" width="275px">
                              We are looking for volunteers to participate in our pilot study at MIT, between September 24 - October 5. Participants must be present on the MIT campus at some point during those two weeks, and need an iPhone or compatible iOS device. The site of the study will be six street parking spots on Amherst Alley, near its intersection with Massachusetts Avenue near the center of the MIT campus.
You will be able to park for free in these marked parking spots between 8am and 6pm, as long as you use our iPhone application to "check in" and "check out" you car.You will also be asked to use the app to send us reports on the occupancy of the other five parking spots (non-drivers can also participate in this task). Each report will earn you points, which can be redeemed for more free parking time or cash rewards at the end of the study.
If interested, please visit this link to see the rules, confirm consent, and download the application.

                              </td>
                              <td width="65px"></td>
                              <td valign="top"><img src="<%=request.getContextPath()%>/web/images/about_0.png" /></td>
                           </tr>
                        </table>
                     </td>
                  </tr>
                  <tr>
                     <td colspan="8" height="50px"></td>
                  </tr>
                  <tr>
                     <td colspan="8"><img src="<%=request.getContextPath()%>/web/images/line.png" /></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="50px"></td>
                  </tr>
               </table>
            </td>
         </tr>
      </table>
      </td></tr>
      <tr><td width="40px"/><td align="left">
      <table cellpadding="0" cellspacing="0">
         <tr>
            <td width="200px"></td>
            <td width="715px">
               <table cellpadding="0" cellspacing="0">
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7"><img src="<%=request.getContextPath()%>/web/images/header_4.png"/></td>
                  </tr>
                  <tr>
                     <td colspan="8" height="35px"></td>
                  </tr>
                  <tr>
                     <td width="14px"></td>
                     <td colspan="7">
                     	<table cellpadding="0" cellspacing="0" class="bottom">
                        	<tr>
                            	<td><img src="<%=request.getContextPath()%>/web/images/press_0.png" /></td>
                            </tr>
                            <tr>
                            	<td><img src="<%=request.getContextPath()%>/web/images/press_1.png" /></td>
                            </tr>
                            <tr>
                            	<td><img src="<%=request.getContextPath()%>/web/images/press_2.png" /></td>
                            </tr>
                            <tr>
                            	<td><img src="<%=request.getContextPath()%>/web/images/press_3.png" /></td>
                            </tr>
                            <tr>
                            	<td><img src="<%=request.getContextPath()%>/web/images/press_4.png" /></td>
                            </tr>
                        </table>

                     </td>
                  </tr>
                  <tr>
                     <td colspan="8" height="50px"></td>
                  </tr>
               </table>
            </td>
         </tr>
      </table>
      </td></tr>
    </table>
    <jsp:include page="/web/templates/footer.jsp" />
</body>
</html>
