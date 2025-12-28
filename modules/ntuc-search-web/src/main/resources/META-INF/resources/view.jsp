<%@page import="web.ntuc.nlh.search.constants.MVCCommandNames"%>
<%@ include file="/init.jsp"%>
<portlet:actionURL name="<%=MVCCommandNames.SEARCH_ACTION %>" var="actionURL" >
	<portlet:param name="authToken" value="${authToken}" />
</portlet:actionURL>

<%-- <portlet:resourceURL id="<%=MVCCommandNames.TOPIC_DATA_RESOURCES %>" var="topicResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL>

<portlet:resourceURL id="<%=MVCCommandNames.THEME_DATA_RESOURCES %>" var="themeResourceURL">
	<portlet:param name="authToken" value="${authToken}" />
</portlet:resourceURL> --%>

<div>
	<form method="post" name="fm" data-senna-off="true" id="search-center">
		<input name="post_type" type="hidden" value="course" />
		<div class="row sp-row-3">
			<div class="col-lg bcol">
				<div class="row sp-row-3">
					<div class="bcol col-lg-12">
						<aui:input label="By Keywords" type="text" name="keywords" showRequiredLabel="false"  placeholder="input.keyword"/>
					</div>
				</div>
			</div>
			<div class="col-lg-auto bcol align-self-end sp-991-2">
				<button class="btn-1 btn-go-up" type="submit">Go</button>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
	<%-- function loadOption(url, target) {
		jQuery.ajax({
			url : url,
			type: "POST",
			dataType : "json",
			async : "false",
			cache : "false",
			success : function(data,textStatus,XMLHttpRequest){
				$.each(data, function(index, value){
					$("#<portlet:namespace/>"+target).append('<option value="'+value.categoryId+'">'+value.title+'</option >');
				}); 
			},
			error : function(data,textStatus,XMLHttpRequest){
				console.log("failed");
			}
		});
	}
	$(document).ready(function() {
		loadOption("<%=topicResourceURL.toString()%>", 'topics');
		loadOption("<%=themeResourceURL.toString()%>", 'themes');
	}); --%>
	$("#search-center").submit(function(e){
		debugger;
		var keyword=$("#<portlet:namespace/>keywords").val();
		e.preventDefault();
		 if(keyword.length>2){
			 var url=themeDisplay.getPortalURL()+"/search-result?course=1&keyword="+keyword;
			 location.replace(url);
		    }else{
		    	 return false;  
		    }
    });
</script>
