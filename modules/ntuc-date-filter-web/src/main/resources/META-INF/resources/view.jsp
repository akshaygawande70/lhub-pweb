<%@page import="web.ntuc.nlh.datefilter.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>

<portlet:resourceURL id="<%=MVCCommandNames.LIST_TOPICS_RESOURCE%>"
	var="dataResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="topicId" value="${topicId}" />
	<portlet:param name="subTopicId" value="${subTopicId}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.LIST_TOPICS_COUNT_RESOURCE%>"
	var="dataCountResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="topicId" value="${topicId}" />
	<portlet:param name="subTopicId" value="${subTopicId}" />
</portlet:resourceURL>

<div class="container sp-main-bot sp-main-top box-detail-list-category">
	<div class="title-wrap-2">
		<div class="row align-items-center">
			<div class="col-md-5 order-md-12 mb-3 text-right">
				<div class="calendar-view">
					<div class="checkbox">
						<input type="checkbox" onchange="resetFilter()" id="calendarview"
							name="calendarview" value="show"> <label
							for="calendarview">Calendar view</label>
					</div>
					<div class="row sp-row-1 yearmonth">
						<div class="col-5 bcol">
							<select name="filter_year" id="search-blog-year"
								onchange="triggerChange()" tabindex="-98">
								<option value="0">Year</option>
								<c:forEach items="${years}" var="year">
									<option value="${year}">${year}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-7 bcol">
							<select name="filter_month" id="search-blog-month"
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
							class="form-control nogetdate" autocomplete="off"> <em
							class="fas fa-calendar-alt"></em>
					</div>
				</div>
			</div>
			<div class="col-md-7 mb-3">
				<h1>${topicTitle}</h1>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-xl-12">
			<section class="thumbnail-in-listing">
				<div class="row sp-row-1" id="topic-list">
					<div class="loader"></div>
					<!-- INSERTED BY JS -->
				</div>
				<div id="content-pagination" class="pager"></div>
			</section>
		</div>
	</div>
</div>

<script type="text/javascript">
	
	var dataList = "";
	var paginationSize = 9;
	
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
   			locator: 'topicList',
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
               	$('html, body').animate({
	                 scrollTop: container.prev().offset().top - 300
	            }, 1000);
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
                $('#topic-list .loader').removeClass('hidden');
        	},
	        success : function(data,textStatus,XMLHttpRequest){
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
		var html = '<div class="col-lg-4 col-sm-6 bcol">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		html += '<p class="text-center"> No Data Found </p>'
		html += '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}
	
	function pageTransition() {
		var html = '<div class="col-lg-4 col-sm-6 bcol" style="height:200vh;">';
		html += '<div class="card inner imgeffect hoverpp">'
		html += '<div class="card-body">'
		html += '<p class="text-center"> Loading Data ... </p>'
		html += '</div>';
		html += '</div>';
		html += '</div>';
		return html;
	}
	
	function generateTopicList(data){
		var tempDate = getFormattedDate(data.date);
		var html = '<div class="col-lg-4 col-sm-6 bcol">';
		html += '<div class="box-3 imgeffect">';
		html += '<figure class="imgwrap">';
		html += '<img src="' + data.urlImage + '" class="attachment-blog-mid size-blog-mid wp-post-image" alt="" loading="lazy">';
		html += '<div class="status">' + data.status + '</div>';
		html += '</figure>';
		html += '<div class="descripts">';	
		html += '<div class="date">' + tempDate + '</div>';	
		html += '<h3>' + data.title + '</h3>';	
		
		var desc = maxParagraph(data.desc);
		html += '<div class="blogparagraph">' + desc + '</div>';	
		html += '</div>';	
		var url = ''+data.urlMore;
		html += '<a class="fxlink" href="' + url.split('?')[0] + '">View details</a>';	
		html += '</div>';
		html += '</div>';
		
		return html;
	}
	
	function filterTopics(){
		var filterList = {};
		var yearParam = parseInt($('#search-blog-year option:selected').val());
		var monthParam = parseInt($('#search-blog-month option:selected').val());
		var dateParam = document.getElementById("specifieddate").value;
		
		filterList['years'] = yearParam;
		filterList['month'] = monthParam;
		filterList['specifiedDate'] = dateParam;

		return JSON.stringify(filterList);
	}
	
	function getFormattedDate(convertDate){
		const months = ["Jan", "Feb", "Mar","Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		var d = new Date(convertDate);
		var month = months[d.getMonth()];
		var day = d.getDate();
		var year = d.getFullYear();
		
		return day + " " + month + " " + year;
	}
	
	function maxParagraph(description){
		return description.substr(0, 160) + " ...";
	}
	
	function resetFilter(){
		var specifiedCheck = $("input[name='calendarview']:checked").val();
		if(specifiedCheck == null ){
			$('#search-blog-year').prop('selectedIndex',0);
			$('#search-blog-month').prop('selectedIndex',0);
			document.getElementById("specifieddate").value = '';
		}else{
			$("#specifieddate").removeAttr('disabled');
		}
		loadCount('<%=dataCountResourceURL.toString()%>');
	}
</script>