<%@page import="web.ntuc.nlh.assetfilter.constants.MVCCommandNames"%>
<%@ include file="init.jsp"%>
<style>

.box-3 .descripts .date {
color: #666666;
}

@media screen and (max-width: 450px)
{

 .thumbnail-in-listing { padding-top:0px !important;}


}




@media (max-width: 991px){

.sp-main-top {  padding-top: 0px!important;
}


}

@media (max-width: 376px){
.title-wrap-2 {
 margin-top: -84px !important;
}
}
.uniqueCard {
	margin-bottom: 10px;
}

.title-wrap-2 {
   
 margin-top: -45px !important;
}

.box-3 .imgeffect
{
border-radius:20px !important;
/*margin-bottom: 32px !important;*/
}

.box-3 figure {
    border-radius: 20px 20px 0px 0px !important;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    border-bottom-right-radius: 0px;
    border-bottom-left-radius: 0px;
    margin: 0;
    position: relative;
}

.box-detail-list-category .thumbnail-in-listing .descripts {

height: 240px !important;
}


.pager ul .active a, .pager ul .current {
padding-top: 2px;
border-style: none;
color: black;
border-radius: 5px;
background: var(--lhub-blue-optimism-blue-50, #E6F5FA);

}

.pager ul .current:hover, .pager ul a:hover {
/*adding-top: 2px;*/
border-style: none;
color:  #18355E;
border-radius: 5px;
background: border-box;

}

.pager ul .current, .pager ul a {
padding-top: 1px;
color: #666666;

}

.paginationjs-ellipsis{
	display: block !important;
}


.paginationjs-prev > a{

width:100px !important;

}

.paginationjs-next > a{

width:100px !important;

}
.paginationClassName1{
width: 100% !important;
font-weight: 600;

}

.nextClassName {
	
	width: 94px !important;
height: 32px;
top: 1888px;
left: 1064px;
}
.prevClassName {
width: 94px !important;
height: 32px;
top: 1888px;
left: 1064px;
}

/* .J-paginationjs-next{ */
/* 	width: 5px !important; */
/* } */

/* .J-paginationjs-next{ */
/* 	width: 5px !important; */
/* } */

/* .J-paginationjs-previous{ */
/* 	width: 5px !important; */
/* } */


.thumbnail-in-listing .imgwrap {
    height: 175px;
    object-fit: cover;
}
.descripts{
    padding: 16px !important;
}
.tag-and-date{
    display: none;
}
.status{
    font-family: OpenSans !important;
    text-align: left;
    background: #E6F5FA;
    color: #18355E;
    padding: 5px 10px;
    font-size: 12px;
    font-weight: 500;
    border-radius: 10px;
}
.date{
    font-size: 12px;
    font-weight: 500;
}
.date-right{
    float: right;
}
.date-left{
    margin-top: 16px;
}
.title-h3-stories{
    font-family: OpenSans !important;
    font-size: 16px !important;
    line-height: 24px !important;
    font-weight: 500 !important;
    -webkit-line-clamp: 2;
    overflow: hidden;
    -webkit-box-orient: vertical;
    display: -webkit-box;
    height: auto!important;
    margin-top: 24px!important;
}
.maxparag-stories{
    font-family: OpenSans !important;
    font-size: 14px !important;
    line-height: 20px !important;
    font-weight: 400 !important;
    -webkit-line-clamp: 3;
    overflow: hidden;
    -webkit-box-orient: vertical;
    display: -webkit-box;
    transition: all 1 ease-out;
    margin-top: 24px !important;
}
.thumbnail-in-listing{
 /*       padding: 32px 125px;*/
 /*           padding: 0px 0px;
    margin: 0px -169px;*/
    }
@media screen and (max-width: 575px){
    .tag-and-date{
        display: none !important;
    }
    .tag-date{
        display: block !important;
    }
    .thumbnail-in-listing .imgwrap{
        height: 150px!important;
    }
    
    .sp-row-1 {
    margin-left: -32px;
    margin-right: -32px;
}
}

@media (min-width: 577px) and (max-width: 1199px) {
  .sp-row-1 {
    margin-left: -40px;
    margin-right: -40px;
}
}


@media screen and (max-width: 760px){



.paginationjs-prev > a{

width:12px !important;
height: 40px !important;
font-size: 25px;

}

.paginationjs-next > a{

width:12px !important;
height: 40px !important;
font-size: 25px;
}
}

@media screen and (max-width: 950px){
    .thumbnail-in-listing{
        padding: 32px 25px;
    }
}

</style>


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
				<div class="calendar-view" style="display: none !important">
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

	<div class="row" id="scrolltohhere">
		<div class="col-xl-12">
			<section class="listing-stories thumbnail-in-listing">
				<div class="row sp-row-1" id="topic-list">
					<div class="loader"></div>
					<!-- INSERTED BY JS -->
				</div>
				<div id="content-pagination" class="pager "></div>
			</section>
		</div>
	</div>
</div>

<script type="text/javascript">
	
	var dataList = "";
	var paginationSize = 12;
	var showpageNo = true;
	var  prevText1='< Previous';
	var nextText1='Next >';
	
	$(document).ready(function() {
		
		  const isMobile = window.matchMedia("only screen and (max-width: 760px)").matches;

		  if (isMobile) {
			//  paginationSize = 12;
			  showpageNo = false;
			  prevText1='<';
			  nextText1='>';
				
		  }else{
		    //alert("desktop");
			//  paginationSize = 12;
			  showpageNo = true;
			  prevText1='< Previous';
			  nextText1='Next >';

		    }
		
		
		$("#specifieddate").attr('disabled', 'disabled');
		loadCount("<%=dataCountResourceURL.toString()%>");
		
	//	$(".pager ul a").attr('href', 'disabled');
		
	//	$(".pager ul a").click(function(e) {
		//    alert('clicked');  
		   // return false;  
		//});  
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
   			className:'paginationClassName1',
   			prevClassName:'prevClassName',
   			nextClassName:'nextClassName',
   			pageClassName:'pageClassName',
   		    pageSize: paginationSize,
   			pageRange: 1,
   		//	hideFirstOnEllipsisShow: true,
   		//	hideLastOnEllipsisShow: true,
   			visiblePages: 5,
   		//	showPageNumbers: showpageNo,
   		//	showFirstOnEllipsisShow:false,
   		//	showLastOnEllipsisShow:false,
 		    showPrevious: true,
   	    	showNext: true,
   	 		prevText :prevText1,
   			nextText :nextText1,
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
            //	window.scrollTo(0,0);
            jQuery("html, body").animate( {scrollTop: container.prev().offset().top - 600}, "fast");
            	//  $('html, body').animate( 
	           //             { 
	           //             scrollTop: 0
	            //        }, 
	            //        'fast');
           // 	alert()
         //   document.body.scrollTop = 0; // For Safari
	      //      document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
            	var htmlArticles = '';
              	if(response.length == 0) {
              		htmlArticles += generateNullResult();
              	} else {
              		$.each(response, function (index, topic) {
              			htmlArticles += generateTopicList(topic); 
	               	});	
              	}
               	container.prev().html(htmlArticles);
              // 	window.scrollTo(0,container.prev().offset().top - 300);
               	
            //   	document.getElementById("content").scrollIntoView({ behavior: "smooth" });
              // 	scrolldelay = setTimeout(pageScroll,10);
                	//$('html, body').animate({
	                 //scrollTop: container.prev().offset().top - 300
	            //}); 
                	
           //     $('html, body').animate( 
            //            { 
            //            scrollTop: container.prev().offset().top - 300
            //        }, 
            //        'fast'); 
             //       return false; 
	           
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
		
		const isTwoCard =   window.matchMedia('only screen and (max-width: 992px)');
		
		const isOneCard = window.matchMedia("only screen and (max-width: 575px)").matches;
		var html = '<div class="col-lg-3 col-sm-12 col-xs-12 bcol uniqueCard">';
		if(isOneCard){
			html = '<div class="col-lg-3 col-sm-12 col-xs-12 bcol uniqueCard">';
		//	console.log('isOneCard');
			
		}else if(isTwoCard){
			html = '<div class="col-lg-3 col-sm-6 col-xs-6 bcol uniqueCard">';
			//console.log('isTwoCard');
			
		}else{
			//html = '<div class="col-lg-3 col-sm-12 col-xs-12 bcol">';
			//console.log('isDesktop');
		}
		
		var tempDate = getFormattedDate(data.date);
		
		html += '<div style="border-radius:20px" class="box-3 imgeffect">';
		html += '<figure class="imgwrap">';
		html += '<img src="' + data.urlImage + '" class="attachment-blog-mid size-blog-mid wp-post-image" alt="" loading="lazy">';
	//	html += '<div class="status">' + data.status + '</div>';
		html += '</figure>';
		html += '<div class="descripts">';	
		
		var customStatus = data.status;
		if(customStatus != null ){
			customStatus = customStatus.replace('LHUB GO','LXP');
		}
		
		//console.log('customStatus::0711::::::::::'+customStatus)
		
		//new start
		html += '<div class="tag-date">';	
		html += '<span class="status">' +customStatus + '</span>';
		html += '<span class="date date-right"><i class="fa fa-calendar"style="margin-right: 4px"></i>'+ tempDate +'</span>';
		html += ' </div>';
		html += '<div class="tag-and-date">';
		html += ' <span class="status">' + customStatus + '</span>';
		var desc = maxParagraph(data.desc);
		html += '</div>';
		html += '<div class="date date-left tag-and-date"><i class="fa fa-calendar"style="margin-right: 4px"></i>'+ tempDate +'</div>';
		html += ' <h3 class="title-h3-stories">' + data.title + '</h3>';
		html += '<div class="maxparag-stories">'+desc+'</div>';
		html += '</div>';
		var url = ''+data.urlMore;
		html += '  <a class="fxlink" href="' + url.split('?')[0] + '">View details</a>';
		html += '</div>';
		html += '</div>';
		html += '</div>';
		
	
		
		
		
		//new end
		
		
		
	//	html += '<div class="date">' + tempDate + '</div>';	
	//	html += '<h3>' + data.title + '</h3>';	
		
	//	var desc = maxParagraph(data.desc);
	//	html += '<div class="blogparagraph">' + desc + '</div>';	
	//	html += '</div>';	
	//	var url = ''+data.urlMore;
	//	html += '<a class="fxlink" href="' + url.split('?')[0] + '">View details</a>';	
	//	html += '</div>';
	//	html += '</div>';
		
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
