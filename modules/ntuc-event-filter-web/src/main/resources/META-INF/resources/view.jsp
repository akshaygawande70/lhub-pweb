<%@page import="web.ntuc.nlh.eventfilter.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.LIST_TOPICS_RESOURCE%>"
	var="dataResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="topicId" value="${topicId}" />
	<portlet:param name="eventTypeId" value="${eventTypeId}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.LIST_TOPICS_COUNT_RESOURCE%>"
	var="dataCountResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="topicId" value="${topicId}" />
	<portlet:param name="eventTypeId" value="${eventTypeId}" />
</portlet:resourceURL>

<section class="cx-events-ntuc">
	<div class="container sp-main-bot sp-main-top">
		<div class="row title-wrap-2 break-1360">
			<div class="col-xl-4 order-xl-12 actions bcol" id="filter">
				<div class="cate">
					<select id="category-filter" name="select-category"
						onchange="triggerChange()" tabindex="-98">
						<option value="0">All Categories</option>
						<c:forEach items="${events}" var="e">
							<option value="${e.categoryId}">${e.name}</option>
						</c:forEach>

					</select>
				</div>
				<div class="calendar-view">
					<div class="checkbox">
						<input type="checkbox" onchange="resetFilter()" id="calendarview"
							name="calendarview" value="1"> <label for="calendarview">Calendar
							view</label> <input type="hidden" name="past_event" value="1">
					</div>
					<div class="row sp-row-1 yearmonth">
						<div class="col-5 bcol">
							<select name="filter-year" id="search-event-year"
								onchange="triggerChange()" tabindex="-98">
								<option value="0">Year</option>
								<c:forEach items="${years}" var="year">
									<option value="${year}">${year}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-7 bcol">
							<select name="filter-month" id="search-event-month"
								onchange="triggerChange()" tabindex="-98">
								<option value="0">All Months</option>
								<option value="1">January</option>
								<option value="2">February</option>
								<option value="3">March</option>
								<option value="4">April</option>
								<option value="5">May</option>
								<option value="6">June</option>
								<option value="7">July</option>
								<option value="8">August</option>
								<option value="9">September</option>
								<option value="10">October</option>
								<option value="11">November</option>
								<option value="12">December</option>
							</select>
						</div>
					</div>
					<div class="date-wrap" style="display: none;">
						<input name="specifieddate" id="specifieddate"
							onblur="triggerChange()" type="text" value=""
							class="form-control nogetdate"> <em
							class="fas fa-calendar-alt"></em>
					</div>
				</div>
			</div>
			<div class="col-xl-8 sp-1360-1 bcol">
				<div class="sly-wrap nav-tabs-1 event-tabs">
					<div class="multisly" data-slide="1" style="overflow: hidden;">
						<ul class="nav sly-content tabs"
							style="transform: translateZ(0px); width: 344px;">
							<li class="item" id="all-event" style="width: 65px;"><a
								href="/events-list/events">All Event</a></li>
							<li class="item" id="upcoming-events" style="width: 132px;">
								<a href="/events-list/upcoming-events">Upcoming Events</a>
							</li>
							<li class="item" id="past-events" style="width: 87px;"><a
								href="/events-list/past-events">Past Events</a></li>
						</ul>
					</div>
					<div class="scrollbar" style="visibility: hidden;">
						<div class="handle"
							style="transform: translateZ(0px) translateX(0px); width: 830px;">
							<div class="mousearea"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row sp-row-2 break-480 grid-1" id="topic-list">
			<div class="loader"></div>
			<!--  Generate Content by JS  -->
		</div>
		<div id="content-pagination" class="pager"></div>
	</div>
</section>

<script type="text/javascript">
	
	var dataList = "";
	var paginationSize = 12;
	
	$(document).ready(function() {
		$("#specifieddate").attr('disabled', 'disabled');
		loadCount("<%=dataCountResourceURL.toString()%>");	
	});
	
	function triggerChange() {
		loadCount('<%=dataCountResourceURL.toString()%>');
	}
	
	function generatePagination(url, total, filters){
   		var container = $('#content-pagination');
   		container.pagination({
   			dataSource: url,
   			locator: 'topicListParam',
   			totalNumber: total,
   			ulClassName: 'justify-content-center',
   		    pageSize: paginationSize,
	   		alias: {
	   		    pageNumber: '<portlet:namespace/>pageNum',
	   		    pageSize: '<portlet:namespace/>limit'
	   		},
   		    ajax: {
   		    	type : "POST",
   		    	data : {
   		    		<portlet:namespace/>filter : filters
   		    	},
   		    	cache : false,
   		        beforeSend: function() {
   		        	container.prev().html(pageTransition());
   		        }
   		    },
            callback: function(response, pagination){   	
            	var htmlArticles = '';
              	if(response.length == 0) {
              		htmlArticles += generateNullResult();
              	} else {
              		$.each(response, function (index, topic) {
              			htmlArticles += generateTopicList(topic); 
	               	});	
              	}
               	container.prev().html(htmlArticles);
               	$('html, body').scrollTop(280);
              }
   		})
 	}
	
	function loadCount(url) {
	 	let filters = filterTopics();
		return jQuery.ajax({
			url : url,
			type : "POST",
			dataType : "json",
			data : {
		    		<portlet:namespace/>filter : filters
		    	},
			async : "true",
			cache : "true",
			beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
                $('#topic-list .loader').removeClass('hidden')
        	},
	        success : function(data,textStatus,XMLHttpRequest){
	        	setActive(${eventTypeId});
                generatePagination('<%=dataResourceURL.toString()%>',data.total, filters);
			},
			complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            	$('#topic-list .loader').hide();
            },
			error : function(data,textStatus,XMLHttpRequest){
				
			}
		});
	}
	
	function generateNullResult() {
		var html = '<div class="col-xl-12 col-lg-12 col-12 bcol">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		html += '<p class="text-center"> No Data Found </p>'
		html += '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}
	
	function pageTransition() {
		var html = '<div class="col-xl-12 col-lg-12 col-12 bcol" style="height:200vh;">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		html += '<p class="text-center"> Loading Data ... </p>'
		html += '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}
	
	function generateTopicList(data){
		var tempDate = getFormattedDate(data.endDate);
		var html = '<div class="col-xl-3 col-lg-4 col-6 bcol">';
		html += '<div class="inner imgeffect">';
		html += '<figure class="imgwrap">';
		html += '<img src="' + data.urlImage + '" class="attachment-event-thumb size-event-thumb wp-post-image" alt="" loading="lazy">';
		
		if(data.status != ""){
			html += '<div class="status">' + data.status + '</div>'			
		}
		
		html += '</figure>';
		html += '<div class="content" style="height: 148.889px;">';
		html += '<p class="title" style="height: 80px;">' + data.title + '</p>';
		html += '<div class="row sp-row-1 align-items-end">';
		html += '<div class="bcol col-8">';
		html += '<p class="date">' + tempDate + '</p>';
		html += '</div>';
		html += '<div class="bcol col-4 last">';
		html += '<p class="more">Read more</p>';
		html += '</div>';
		html += '</div>';
		html += '</div>';
		var url = ''+data.urlMore;
		html += '<a href="' + url.split('?')[0] + '" class="fxlink">View detail</a>';
		html += '</div>';
		html += '</div>';
		
		return html;
	}
	
	
	function filterTopics(){
		var filterList = {};
		
		var eventTypeId = ${eventTypeId};
		var filterCategory = parseInt($('#category-filter option:selected').val());
		var yearParam = parseInt($('#search-event-year option:selected').val());
		var monthParam = parseInt($('#search-event-month option:selected').val());
		var dateParam = document.getElementById("specifieddate").value;
		
		filterList['eventTypeId'] = eventTypeId;
		filterList['categoryId'] = filterCategory;
		filterList['years'] = yearParam;
		filterList['month'] = monthParam;
		filterList['specifiedDate'] = dateParam;
		
		return JSON.stringify(filterList);
	}
	
	function getFormattedDate(convertDate){
		const months = ["Jan", "Feb", "Mar","Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var d = new Date(convertDate);
		var month = months[d.getMonth()];
		var day = '' + d.getDate();
		if(day.length < 2){
			day = '0' + day;
		}
		var year = d.getFullYear();
		
		return day + " " + month + " " + year;
	}
	
	function setActive(typeId){
		if(typeId == 1){
			$('#filter').show();
			$('#all-event').addClass('active');
			$('#upcoming-events').removeClass('active');
			$('#past-events').removeClass('active');
		}else if(typeId == 2){
			$('#filter').hide();
			$('#all-event').removeClass('active');
			$('#upcoming-events').addClass('active');
			$('#past-events').removeClass('active');
		}else if(typeId == 3){
			$('#filter').show();
			$('#all-event').removeClass('active');
			$('#upcoming-events').removeClass('active');
			$('#past-events').addClass('active');
		}
	}
	
	function resetFilter(){
		var specifiedCheck = $("input[name='calendarview']:checked").val();
		if(specifiedCheck == null){
			$('#search-blog-year').prop('selectedIndex',0);
			$('#search-blog-month').prop('selectedIndex',0);
			document.getElementById("specifieddate").value = '';
		}else{
			$("#specifieddate").removeAttr('disabled');
		}
		loadCount('<%=dataCountResourceURL.toString()%>');
	}
	
</script>