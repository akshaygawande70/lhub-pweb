<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="web.ntuc.eshop.registeredcourse.dto.TraineeDto"%>
<%@page import="java.util.List"%>
<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames"%>
<%@ include file="../init.jsp"%>

<style type="text/css">
#status {
	opacity: 0;
}
</style>

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
				<div class="content active">
					<div class="box-nav-content">
						<div class="text-link">
							<a href="/account/course_listing">
							<i class="fas fa-angle-left"></i> Back To Courses</a>&nbsp;
						</div>
						<div class="box-nav-dp">
							<div class="btn-list list-download">
								<i class="fas fa-download"></i><a onclick="downloadPdf()" id="btnSubmit">
									Download PDF</a>
							</div>
							<!-- <div class="btn-list list-print">
								<i class="fas fa-print"></i> <a
									onclick="javascript:printDiv('print-area');">Print</a>
							</div> -->
						</div>
					</div>
					<div class="row">
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
						</div>
					</div>
					<div class="content active box-traine">
						<div class="row">
							<div class="col-12 box-grey">
								<ul>
									<li class="first">Legend</li>
									<li>
										<div class="status">
											<i class="fas fa-check-circle"></i>&nbsp; Attend
										</div>
									</li>
									<li>
										<div class="status">
											<i class="fas fa-times-circle"></i>&nbsp; No Present
										</div>
									</li>
								</ul>
							</div>
						</div>

						<div class="row" id="print-area">
							<div class="col-12">
								<table class="table dataTable no-footer table-bordered w-100" id="my-table">
									<thead>
										<c:forEach var="t" items="${traineeList}" begin="0" end="0">
											<tr>
                                                <th class="wd-check"><input name="trainee-name-th" class="checkAll" 
                                                    type="checkbox" onclick="checkAll(this)" value="Trainee Name"></th>
												<th>Trainee Name</th>
												<c:forEach var="a" items="${t.attendanceList}">
													<th>
														<div class="title">Session ${a.session}</div>
														<div class="date">${a.attendanceDateTime}</div>
													</th>
													<%-- <th style="min-width: unset; width: 75px">
														<div class="title" style="width: 75px">Session ${a.session}</div>
														<div class="date" style="width: 70px">${a.attendanceDateTime}</div>
													</th> --%>
												</c:forEach>
											</tr>
										</c:forEach>
									</thead>
									<tbody>
										<c:forEach var="t" items="${traineeList}" varStatus="loop">
											<tr>
                                                <td class="wd-check"><input name="trainee-name-td" id="check${loop.index}"
                                                    type="checkbox" value="Trainee Name"></td>
												<td>${t.traineeName}</td>
												<c:forEach var="a" items="${t.attendanceList}">
													<td>
														<div class="status">
															<c:choose>
																<c:when test="${a.isAttended == 'YES'}">
																	<i class="fas fa-check-circle"></i>
																	<span id="status">Attend</span>
																</c:when>
																<c:otherwise>
																	<i class="fas fa-times-circle"></i>
																	<span id="status">No Present</span>
																</c:otherwise>
															</c:choose>
														</div>
													</td>
												</c:forEach>
											</tr>
										</c:forEach>
									</tbody>
								</table>
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
/* var listTbl;
	$(document).ready(function () {
		listTbl = $('#my-table').DataTable({
			"pageLength" : 10,
			"sPaginationType" : "full_numbers",
			"bLengthChange": false,
		    "bFilter": false,
		    "bInfo": false,
		    "scrollX": true,
			"bAutoWidth": true,
			"bSortable" : false,
			"processing": true,
            "serverSide": false,
            "deferRender": true,
            "select": {style: 'api'}, 
			"columnDefs": [
			       {
			           "orderable": false,
			           "targets": 0,
			           "checkboxes": {
			               selectRow: true,
			               selectAllPages: true
			           },
			       }
			] 
		});
	}); */
	
	var oTable;
	$(document).ready(function () { 
	    	oTable = $('#my-table').dataTable({
	    		"pageLength" : 10,
				"sPaginationType" : "full_numbers",
				"bLengthChange": false,
			    "bFilter": false,
			    "bInfo": false, //show entries
			    "scrollX": true, //dikomen jika ingin menggunakan scroll responsive
				"bAutoWidth": true,
				"bSortable" : true,
				"processing": true,
	            "serverSide": false,
	            "deferRender": true,
	            /* "aaSorting": [], */ //disable first column auto sorting placeholder
	            /* "ordering": false, */
	            "select": {style: 'api'}, 
				"columnDefs": [
				       {
				    	   "bSortable": false, //disable first column auto sorting
				           "targets": 0
				       }
				]
	            // scroll responsive
				/* "initComplete": function (settings, json) {  
				    $("#my-table").wrap("<div style='overflow:auto; width:100%;position:relative;'></div>");   
				    $("#my-table").wrap("<div class='table-responsive'></div>"); 
				  } */
	    });
	});
	
	/* function checkAll(ele) {
	    var checkboxes1 = document.getElementsByTagName('input');
	     if (ele.checked) {
	         for (var i = 0; i < checkboxes1.length; i++) {
	        	 console.log(i, "select all")
	             if (checkboxes1[i].type == 'checkbox') {
	                 checkboxes1[i].checked = true;
	             }
	         }
	     } else {
	         for (var i = 0; i < checkboxes1.length; i++) {
	             console.log(i, "unselect all")
	             if (checkboxes1[i].type == 'checkbox') {
	                 checkboxes1[i].checked = false;
	             }
	         }
	     }
	 } */
	
	var checkboxes = document.querySelectorAll('input[type=checkbox]');
	function checkAll(myCheckbox){
		if(myCheckbox.checked == true){
			checkboxes.forEach(function(checkbox){
				checkbox.checked = true;
			});
		}
		else{
			checkboxes.forEach(function(checkbox){
				checkbox.checked = false;
			});
		}
	} 
	
	/* function cekAll(event){
		var cek = $(event.target).prop("checked");
		console.log("cek", cek);
		if(cek == true){
			$(".cekData").prop("checked", true);
		}else{
			$(".cekData").prop("checked", false);
		}
	} 
	
	function singleCek(event){
		var cek = $(event.target).prop("checked");
		if(cek == "true"){
		$(event.target).prop("checked", false);
		}else if(cek == "false"){
		$(event.target).prop("checked", true);
		}
	} */

	var imgStr = "";
	$(document).ready(function() {
		getImgStr();
		var traineeList = <%=traineeListJson%>;
		console.log(traineeList);
	});

	function printDiv(element) {
		var a = document.getElementById('printing-css').value;
		var b = document.getElementById(element).innerHTML;
		window.frames["print_frame"].document.body.innerHTML =

		window.frames["print_frame"].document.body.innerHTML = '<style>' + a
				+ '</style>' + b;
		window.frames["print_frame"].window.focus();
		window.frames["print_frame"].window.print();
	}
	
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
		var headers = ['Trainee Name'];
		var width = ['*'];
		console.log(traineeList);
		//checkbox to download
		/* debugger; */
		var table = document.getElementById('my-table');
		var all = oTable.fnGetNodes();
		/* var list = [].entries.call(document.querySelectorAll("input[type=checkbox]")); */
		let body = [];
		console.log(body, "ini body");
	   for (var y = 0; y < oTable.fnGetNodes().length+1; y++){
		   	/* var hiddenRows = [y].entries.call(document.querySelectorAll("input[type=checkbox]"));
		   	console.log(hiddenRows, "hiddenRows")  */
	    	/* var listInput = oTable.fnGetNodes()[y]; */
	    	var validasi = $('input:checkbox#check'+y, all).prop('checked');
	    	console.log("ini Y", y);
	    	if(validasi == true){
	    		body.push(y);
	    	}
	    }
	    
	   /*  var checkboxes = document.querySelectorAll('input[type=checkbox]');
	    for(var y = 0; y < checkboxes.length; y++){
	    	var validasi = $(checkboxes[y]).prop("checked");
	    	console.log("ini Y", y);
	    	if(validasi == true){
	    		body.push(y);
	    	}
	    	console.log(body, "ini body");
	    	console.log(validasi, "ini validasi");
		} */
	    
		console.log(traineeList, "ini traineeList");
        const rows = [];
        const dataBaru = [];
        dataBaru.push(headers);
        //startIndex selalu dimulai dari 0 jadi ga perlu didefinisikan lagi
        traineeList.forEach(function(training, startIndex){
        	console.log('Index: ' + startIndex);
            let row = [];
            for(var j = 0; j < body.length; j++){
            	if(body[j] == startIndex){
            		row.push(training.traineeName);
                    training.attendanceList.forEach(function(data){
                        row.push(data.isAttended);
                    });
            	}
            }
            rows.push(row);
		});
        
        for(var i = 0; i < rows.length; i++){
        	var obj = rows[i];
        	if(obj.length > 0){
        		dataBaru.push(obj);
        		/* obj[0] = obj[0] + i;
        		console.log(obj[0], "ini obj") */
        		console.log(dataBaru, "ini dataBaru")
        	}
        }
		
        traineeList.at(0).attendanceList.forEach(function(data){
			let header = "Session "+data.session+"\n"+data.attendanceDateTime;
			headers.push(header);
			width.push('*');
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
										//width : '50%',
										
											table: {
												widths : [ '*','auto','auto' ],
												body: [
													['Course Title', ':', courseTitle ],
													['Batch ID', ':', batchId ],
													['Sub Booking ID', ':', subBokingId ]
												]
											},
											layout: 'noBorders',
											style: 'content',
									},
									{
										// % width
										//width : '50%',
										
											table: {
												widths : [ '*','auto','auto' ],
												body: [
													['Course Start Date & Time',' : ', startDate],
													['Course End Date & Time',' : ', endDate],
												]
											},
											layout: 'noBorders',
											style: 'content',						
									} ],
							// optional space between columns
							columnGap : 80
						},  
						'\n\n', {
							style : 'content',
							table : {
								widths : '*',
								body : dataBaru 
								  /*  [
							           [ "Trainee Name", sessionHeaders.join(', ')],
							          [ dataArray, value.join(', ')] 
							          [ 'First''Second''Third''The last one' ],
							          [ 'Value 1', 'Value 2', 'Value 3', 'Value 4' ],
							          [ { text: 'Bold value', bold: true }, 'Val 2', 'Val 3', 'Val 4' ] 
							     ] */
							}
						} 
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
			console.log(dd, "pdfmake");
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
	
/* 	function buildTableBody(data, columns) {
		var body = [];
		body.push(columns);
		data.forEach(function(row) {
		var dataRow = [];
		columns.forEach(function(column) {
		dataRow.push(row[column].toString());
		})
		body.push(dataRow);
		});
		return body;
		}
		function table(data, columns) {
		return {
		table: {
		headerRows: 1,
		body: buildTableBody(data, columns)
		}};
	}
		 */

    
</script>