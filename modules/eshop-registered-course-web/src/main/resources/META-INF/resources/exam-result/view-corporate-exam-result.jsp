<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="web.ntuc.eshop.registeredcourse.dto.TraineeDto"%>
<%@ include file="../init.jsp"%>

<%
	List<TraineeDto> traineeListObj = (List<TraineeDto>) request.getAttribute("traineeList");
	JSONArray traineeListJson = JSONFactoryUtil.createJSONArray(traineeListObj);
%>

<section class="general-info myaccount-cxrus">
	<%@include file="/layout/header.jsp"%>
	<div class="container">
		<div class="row">
			<div class="box-name">
				<div class="name" id="capital-name">${companyName}</div>
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
								<i class="fas fa-download"></i> <a onclick="downloadPdf()">
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
										<div class="box-table">
											<div class="title">Sub Booking ID</div>
											<div class="isi">${subBookingId}</div>
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
							
							 <!-- <div class="heading-content grey">Certified Auto Cad Professional</div>
                            Create your own class for the containing div
                            <div class="box-scedule-detail">
                                <div class="select-exam">Exam Data:22 July 2021</div>
                                <div class="toogle-select-exam">
                                    <table class="table cxrus-table">
                                        <thead>
                                            <tr>
                                                <th class="wd-check"></th>
                                                <th>Traine Name</th>
                                                <th>
                                                    Exam Result
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="wd-check"><input name="vehicle1" type="checkbox"
                                                        value="Bike" /></td>
                                                <td>Kiberly</td>
                                                <td>
                                                    <div class="green-color">Pass</div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="wd-check"><input name="vehicle1" type="checkbox"
                                                        value="Bike" /></td>
                                                <td>Kiberly</td>
                                                <td>
                                                    <div class="red-color">Fail</div>
                                      	          </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div> -->
							<table class="table cxrus-table">
								<thead>
									<tr>
										<!-- <th class="wd-check"><input name="vehicle1"
											type="checkbox" value="Bike"></th> -->
										<th>Trainee Name</th>
										<th>Exam Name</th>
										<th>Exam Date</th>
										<th>Exam Results</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="t" items="${traineeList}">
										<c:choose>
											<c:when test="${empty t.examList}">
												<tr>
													<!-- <td class="wd-check"><input name="vehicle1"
														type="checkbox" value="Bike"></td> -->
													<td>${t.traineeName}</td>
													<td>NA</td>
													<td>NA</td>
													<td>NA</td>

												</tr>
											</c:when>
											<c:otherwise>
												<c:forEach var="a" items="${t.examList}">
													<tr>
														<td class="wd-check"><input name="vehicle1"
															type="checkbox" value="Bike"></td>
														<td>${t.traineeName}</td>
														<td>${a.examName}</td>
														<td><fmt:formatDate value="${a.examDate}" pattern="dd-MM-yyyy"/></td>
														<td><div class="${a.examResult == 'PASS' ? 'green-color' : 'red-color'}">${a.examResult}</div></td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</tbody>
							</table> 
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
	display:none
}
</textarea>
<script type="text/javascript">	
   var imgStr = "";

	$(document).ready(function() {
		getImgStr();
		$(".select-exam").click(function () {
			$(this).toggleClass('active');
			$(".toogle-select-exam").toggleClass('active');
		});
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
		var subBokingId = "${subBookingId}"

		var traineeList = <%=traineeListJson%>;
		
		var headers = ['Trainee Name', 'Exam Name', 'Exam Date', 'Exam Result'];
		var width = ['*','*','*','*'];
		
		var rows = [];
		rows.push(headers);
		traineeList.forEach(function(training){
			let row = [];
			row.push(training.traineeName);
			if(training.examList.length > 0) {
				training.examList.forEach(function(data){
					row.push(data.courseTitle);
					row.push(dateFormater(data.examDate)+' SGT');
					row.push(data.examResult);
				});
			}else {
				row.push('NA');
				row.push('NA');
				row.push('NA');
			}
			
			rows.push(row);
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
												['Batch ID',' : ', batchId],
												['Sub Booking ID',' : ', subBokingId]
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
						}
						,  
						'\n\n', {
							style : 'content',
							table : {
								widths : width,
								body : rows
							}
						},
						],
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
	
	function dateFormater(date) {
		const monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
];
		var m = new Date(date);
		var dateString =
			("0" + m.getUTCDate()).slice(-2) + " " +
		    monthNames[m.getUTCMonth()] + " " +
		    m.getUTCFullYear() + " " +
		    ("0" + m.getUTCHours()).slice(-2) + ":" +
		    ("0" + m.getUTCMinutes()).slice(-2); 

		return dateString;
	}
</script>