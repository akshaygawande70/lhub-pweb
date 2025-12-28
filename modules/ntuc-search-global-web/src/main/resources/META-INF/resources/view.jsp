<%@page import="web.ntuc.nlh.search.global.constants.MVCCommandNames"%>
<%@ include file="/init.jsp" %>


<portlet:resourceURL id="<%=MVCCommandNames.SEARCH_DATA_RESOURCES %>" var="searchResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
	<portlet:param name="keyword" value="${keyword}" />
	<portlet:param name="course" value="0" />
</portlet:resourceURL>

<div class="quick-search">
	<a href="#search" class="btn-search"><span>Search</span></a>
	<div id="search" class="search-sub">
		<form action="${base_url}/search-result?" class="input-group">
			<input id="txsearch" type="text" class="form-control"
				placeholder="Search by keywords ..." name="keyword" autocomplete="off" /> <input
				id="txcourse" type="hidden" class="form-control" name="course"
				value="0" />
			<div class="input-group-append">
				<button class="btn" type="submit">Search</button>
			</div>
		</form>
		<div id="resultsearch" class="results hscroll">
			<div class="row"></div>
			<div class="text-center"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
//setup before functions
var typingTimer;                //timer identifier
var doneTypingInterval = 1000;  //time in ms, 5 second for example
var $input = $('#txsearch');

//on keyup, start the countdown
$input.on('keyup', function () {
    clearTimeout(typingTimer);
    typingTimer = setTimeout(doneTyping, doneTypingInterval);
});

//on keydown, clear the countdown
$input.on('keydown', function () {
    clearTimeout(typingTimer);
});
$('.quick-search .input-group').submit(function() {
    if($('#txsearch').val().length<3){
       return false;  
    }
 });
//user is "finished typing," do something
function doneTyping () {
    $('#resultsearch .row').html('');
    $('#resultsearch .text-center').html('');
	var searchText=$('#txsearch').val();
	if(searchText!="" && searchText.length>2 ){
		 $.ajax({
		        url: '<%=searchResourceURL.toString()%>',
		        type:"POST",
		        dataType:'json',
		        data: {
		        	<portlet:namespace/>keyword: $('#txsearch').val(),
		        },
		        async: "true",
		        cache: "true",
		        success: function(data,textStatus,XMLHttpRequest){
		        	var html = '';
		            html += '<div class="col-sm-6">';
		            $.each(data, function( index, value ) {
		                if(index === 'courses'){
		                    html += '<ul><h4>Courses</h4>';
		                    $.each(value, function( course_id, course_details ) {
		                        html += '<li><a href="'+course_details['urlMore']+'">'+course_details['title']+'</a></li>';
		                    });
		                    html += '</ul>';
		                }
		                if(index === 'others'){
		                    html += '<ul><h4>Pages and Others</h4>';
		                    $.each(value, function( other_id, other_details ) {
		                        html += '<li><a href="'+other_details['urlMore']+'">'+other_details['title']+'</a></li>';
		                    });
		                    html += '</ul>';
		                }
		            });
		            html += '</div>';
					
		            
		            html += '<div class="col-sm-6">';
		            $.each(data, function( index, value ) {
		                if(index === 'blogs'){
		                    html += '<h4>Blogs</h4>';
		                    $.each(value, function( blog_id, blog_details ) {
		                        html += '<div class="grid-6 clearfix"><figure><a href="'+blog_details['urlMore']+'"><img src="'+blog_details['urlImage']+'" alt="" /></a></figure>';
		                        html += '<div class="descripts">' +
		                            '<p><a href="'+blog_details['urlMore']+'">'+blog_details['title']+'</a></p>' +
		                            '<span class="status">'+blog_details['category'] +
		                            '</div></div>';
		                    });
		                }
		            });
		            html += '</div>';
					
		            $('#resultsearch .row').html(html);
		            $('#resultsearch .text-center').html('<a href="/search-result?keyword='+$("#txsearch").val()+'&course=0">See all results for "'+$('#txsearch').val()+'"</a>');
		           
		        },
		        error: function(data){
		            $('#resultsearch .row').html(
		                '<div class="col-sm-12">Failed to get search results, please try again later!</div>'

		            );

		        }

		    });
	}
}

</script>