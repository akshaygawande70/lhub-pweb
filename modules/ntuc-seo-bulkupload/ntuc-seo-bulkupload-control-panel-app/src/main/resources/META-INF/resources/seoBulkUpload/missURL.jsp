<%@ include file="../init.jsp" %>
<portlet:resourceURL  var="MissingURL" id="MissingURL"></portlet:resourceURL>

<portlet:renderURL var="redirectPage">
    <portlet:param name="mvcRenderCommandName" value="<%=MVCCommandNames.CONFIGURE_MAIL %>" />
</portlet:renderURL>

<div class="container mt-3">
	<h3>Crawl Pages With Missing SEO</h3>
	<div class="mb-3 management-bar-light p-3">
		<div class="dl-btn d-flex justify-content-between">
			<button type="button" class="button" id="MissingURL" class="close" data-dismiss="alert" aria-label="Close">
				<span class="button__text">Download</span>
			</button>
			<% if(BulkUploadUtil.isAdminRole()){ %>
			<a href="<%=redirectPage%>">
				<clay:icon symbol="cog" />
			</a>
			<%}%>
		</div>
		<div class="container pt-2">
			<div class="alert alert-info d-none" role="alert" id="alertinfo">
			  The excel will be generated, you can download it from <strong>Content & Data > Documents and Media > SEO Missing Url Excels</strong> folder
			  <button type="button" class="close">
			    <span aria-hidden="true">&times;</span>
			  </button>
			</div>
		</div>
	</div>
</div>


<style>

.dl-btn .lexicon-icon{
	height: 2em;
    width: 1.4em;
    margin-top: auto;
    color: midnightblue;
}
.button {
  position: relative;
  padding: 7px 15px;
  background: #0b5fff;
  border: 1px solid transparent;
  outline: none;
  border-radius: 0.25rem;
  cursor: pointer;
}

.button:active {
  background: #0b5fff;
}

.button__text {
  font-family: system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,Oxygen-Sans,Ubuntu,Cantarell,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol";
  color: #ffffff;
  transition: all 0.2s;
  font-size: 1rem;
  font-weight: 600;
  line-height: 1.5;
}

.button--loading .button__text {
  visibility: hidden;
  opacity: 0;
}

.button--loading::after {
  content: "";
  position: absolute;
  width: 16px;
  height: 16px;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  border: 4px solid transparent;
  border-top-color: #ffffff;
  border-radius: 50%;
  animation: button-loading-spinner 1s ease infinite;
}
.bulk-upload-box {
	display: inline-flex;
	width: 100%;
}
.bulk-upload-box .btn {
	height: 100%;
	margin-right: 10px
}

@keyframes button-loading-spinner {
  from {
    transform: rotate(0turn);
  }
  to {
    transform: rotate(1turn);
  }
}
</style>

<script>
	$(document).on("click", ".close", function(){
		$(this).parent().addClass("d-none");
	})
</script>

<aui:script>
		AUI().use('aui-base', 'io', 'aui-io-request', function(A){
		const btn = document.querySelector(".button");
			var missURLButton = A.one('#MissingURL');
			  missURLButton.on('click', function(event){
				btn.classList.add("button--loading");
				console.log("testing data");
				document.getElementById('alertinfo').classList.remove('d-none');
				A.io.request("<%=MissingURL%>", {    
					on: {
						success: function() {
					 		btn.classList.remove("button--loading");
	                       	<%-- window.location.href = "<%=MissingURL%>"; --%>
	                       	console.log("Success comming ");
						},
						failure: function (error) {
				      		console.log("Failure Come ERROR ! ! :- " + data);
				          }
					}
				
				});
				
			});
		});
</aui:script>