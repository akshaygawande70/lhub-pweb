<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="web.ntuc.eshop.registeredcourse.dto.RegisteredCourseDto"%>
<%@page import="java.util.List"%>
<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames" %>
<%@ include file="../init.jsp"%>
<%
	List<RegisteredCourseDto> attendanceListObj = (List<RegisteredCourseDto>) request.getAttribute("attendanceList");
	JSONArray attandanceListJson = JSONFactoryUtil.createJSONArray(attendanceListObj);
%>
<portlet:resourceURL
	id="<%=MVCCommandNames.COURSE_ATTENDANCES_DETAIL_RESOURCE%>"
	var="attendanceDetailURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="accType" value="${accType}" />
	<portlet:param name="nric" value="${nric}" />
	<portlet:param name="birthDate" value="${birthDate}" />
	<portlet:param name="companyCode" value="${companyCode}" />
	<portlet:param name="companyName" value="${companyName}" />
	<portlet:param name="uenNumber" value="${uenNumber}" />
</portlet:resourceURL>

<section class="general-info myaccount-cxrus">
	<%@include file="/layout/header.jsp"%>
	<div class="container">
		<div class="row">
			<div class="box-name">
				<div class="name" id="capital-name">ATTENDANCES</div>
				<div class="email">Your Attendances</div>
			</div>
			<hr class="hr-bold" />
		</div>
	</div>
	<div class="container">
		<div class="row">
			<%@include file="/layout/side-menu.jsp"%>
			<div class="col-md-9">
				<!--Start content-->
				<div class="content active" id="pi">
					<div class="box-nav-content">
						<div class="text-link">
							<a href="/account/course_listing"><i
								class="fas fa-angle-left"></i> Back To Courses</a>&nbsp;
						</div>
						<div class="box-nav-dp">
							<div class="btn-list list-download">
								<i class="fas fa-download"></i><a onclick="downloadPdf()">
									Download PDF</a>
							</div>
							<!-- <div class="btn-list list-print">
								<i class="fas fa-print"></i> <a
									onclick="javascript:printDiv('print-area');">Print</a>
							</div> -->
						</div>
					</div>
					<div class="row" id="print-area">
						<div class="col-md-12">
							<div class="heading-content">${fullName}</div>
							<div class="box-info">
								<div class="row">
									<div class="col-md-6">
										<div class="box-table">
											<div class="title">Course Title</div>
											<div class="isi">${courseTitle}</div>
										</div>
										<div class="box-table">
											<div class="title">Batch ID</div>
											<div class="isi">${batchId}</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="box-table">
											<div class="title">Course Start Date & Time</div>
											<div class="isi">${startDate}</div>
										</div>
										<div class="box-table">
											<div class="title">Course End Date & Time</div>
											<div class="isi">${endDate}</div>
										</div>
									</div>
								</div>
							</div>
							<div class="heading-content">COURSE SESSIONS AND ATTENDANCE</div>
							<!-- Create your own class for the containing div -->
						<!-- 	<div class="slick-carousel-date date-slick">
								<div>Mon, 21 Jun - Sun, 27 Jul</div>
								<div>Mon, 21 Jan - Sun, 27 Jul</div>
								<div>Mon, 21 Feb - Sun, 27 Jul</div>
								<div>Mon, 21 Mei - Sun, 27 Jul</div>
								<div>Mon, 21 Juli - Sun, 27 Jul</div>
							</div> -->
							
							<div class="box-session">
								<div class="slick-carousel-session session-slick"
									id="attendance-list">
									<c:choose>
										<c:when test="${empty attendanceList}">
										<div class="no-data">
											<span><%=ContentConstants.NO_DATA_AVAILABLE%></span>
										</div>
										</c:when>
										<c:otherwise>
											<c:forEach items="${attendanceList}" var="data">
												<div class="item">
													<div class="title">Session ${data.session}</div>
													<div class="date">${data.attendanceDateTime }</div>
													<div class="status">
														<c:choose>
															<c:when test="${data.isAttended == 'YES'}">
																<i class="fas fa-check-circle"></i> Attend													
													</c:when>
															<c:otherwise>
																<i class="fas fa-times-circle"></i> No Present													
													</c:otherwise>
														</c:choose>
													</div>
												</div>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--End content-->
			</div>
		</div>
	</div>
</section>
<iframe name="print_frame" src="about:blank"
	style="border: 0px; display: none"></iframe>
<textarea id="printing-css" style="display: none;">
html,body,div,span,applet,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,a,abbr,acronym,address,big,cite,code,del,dfn,em,img,ins,kbd,q,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,article,aside,canvas,details,embed,figure,figcaption,footer,header,hgroup,menu,nav,output,ruby,section,summary,time,mark,audio,video{
	margin:0;
	padding:0;
	border:0;
	font-size:100%;
	font:inherit;
	vertical-align:baseline
}
article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{
	display:block
}
body{
	line-height:1
}
table[border="1"] th,table[border="1"] td,table[border="1"] caption{
	border:1px solid #000;
	padding:.5em 1em;
	text-align:left;
	vertical-align:top;
	margin-top:20px;
	text-align:center;
}
th{
   padding:10px 8px;
	  text-align: center;
	  background-color: #18355f;
	  color: white;
	 vertical-align: middle;
}
td{
	 padding:10px 8px;
	 text-align: center;
	 background-color:#dcedf5;
	 vertical-align: middle;
}
.btn-1{
	background: #18355f;
	border: #fcd205 solid 2px;
	color: #fcd205 !important;
	display:block;
	padding:10px 8px;
	text-decoration:none;
	display:none;
}
.type-rs{
	display:none;
}
.payment{
		opacity:1;
	}
table[border="1"] caption{
	border:none;
	font-style:italic
}
.no-print{
	display:none;
}

#status{
	opacity:1;
}
</textarea>
<script type="text/javascript">
var imgStr = "";
$(document).ready(function() {
	getImgStr();
});

function getImgStr(){
	var src = "<%=request.getContextPath()%>/img/ntuclhub-logo.png";
		console.log("img: " + src);
		var img = new Image();
		img.crossOrigin = 'Anonymous';
		img.onload = function() {
			var canvas = document.createElement('CANVAS');
			var ctx = canvas.getContext('2d');
			var dataURL;
			canvas.height = this.naturalHeight;
			canvas.width = this.naturalWidth;
			ctx.drawImage(this, 0, 0);
			dataURL = canvas.toDataURL();
			imgStr = dataURL;
		};
		img.src = src;
		if (img.complete || img.complete === undefined) {
			img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
			img.src = src;
		}
	}

	function downloadPdf() {
		var courseTitle = "${courseTitle}";
		var batchId = "${batchId}";
		var startDate = "${startDate}";
		var endDate = "${endDate}";
		var fullName = "${fullName}";

		var attendanceList = <%=attandanceListJson%>;
		
		var rows = [];
		rows.push([ 'Session', 'Course Date', 'Is Attended' ]);
		attendanceList.forEach(function(data){
			rows.push([ data.session, data.attendanceDateTime, data.isAttended ]);
		});
		
		try {
			var dd = {
				footer : {
					columns : [ {
						text : 'NTUC Learning Hub',
						alignment : 'center',
						style: 'boldHeader'
					} ]
				},
				content : [
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										width : 120,
										image : imgStr
									// image: 'sampleImage.jpg'
									},
									{
										stack : [
												{text:'Registered Courses', style:"boldHeader"},
												{text:'Attendances', style:"boldHeader"},
												{text:'GST Reg No.: 20-0409359-E ',style:"boldHeader", margin:[0,5,0,5]},
												'NTUC LEARNINGHUB PTE LTD',
												'73 BRAS BASAH ROAD',
												'#02-01 NTUC TRADE UNION HOUSE',
												'SINGAPORE 189556',
												'FAX 65 64867824\nwww.ntuclearninghub.com',
												'Company Registration Number: 200409359E' 
												],
										style: 'header',
										width : '*',
										margin: [70,0,0,0]
										
									} ],
							// optional space between columns
							columnGap : 130
						},
						'\n\n',
						{text:'ATTENDANCE INFO: \n\n', style:'boldHeader'},						
						{
							columns : [
									{
										// auto-sized columns have their widths based on their content
										table: {
											widths : [ '*','auto','auto' ],
											body: [
												['Course Title',' : ', courseTitle],
												['Batch ID',' : ', batchId]
											]
										},
										layout: 'noBorders',
										style: 'content',
										
									},
									{
										// % width
										table: {
											widths : [ '*','auto','auto' ],
											body: [
												['Course Start Date & Time',' : ', startDate],
												['Course End Date & Time',' : ', endDate]
											]
										},
										layout: 'noBorders',
										style: 'content',
										
									} ],
							// optional space between columns
							columnGap : 80
						}, '\n\n', {
							style : 'content',
							table : {
								widths : [ '*', '*', '*' ],
								body : rows,
							}
						
						}, ],
				styles : {
					rightalign : {
						alignment : 'right'
					},
					content : {
						fontSize : 8
					},
					header : {
						fontSize: 8, color:'grey', alignment:'left'
					},
					boldHeader : {
						fontSize: 10, bold:true,
					}
				}
			};
			//console.log(dd);
			var today = new Date();
			var date = today.getFullYear() + '' + (today.getMonth() + 1) + ''
					+ today.getDate();
			var fileName = 'download' + '-' + date;
			//console.log(fileName);
			pdfMake.createPdf(dd).download(fileName);
		} catch (err) {
			alert(err.message);
		}
	}

	function printDiv(element) {
		var a = document.getElementById('printing-css').value;
		var b = document.getElementById(element).innerHTML;
		window.frames["print_frame"].document.body.innerHTML =

		window.frames["print_frame"].document.body.innerHTML = '<style>' + a
				+ '</style>' + b;
		window.frames["print_frame"].window.focus();
		window.frames["print_frame"].window.print();
	}
</script>