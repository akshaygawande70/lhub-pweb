<%@page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>
<%@ include file="/init.jsp" %>

<style>
	.successmsg{
	   height: auto;
       width: 555px;
       background-color: #b3dab3;
       text-align: center;
       padding: 20px;
       border: dashed #045404;
	}
	.container-box{
	    width: 100vw;
	    height: 66vh;
	    display: flex;
	    align-content: center;
	    justify-content: center;
	    align-items: center;
	}
</style>
<div class="container-box">
<div class="successmsg">
    <p>Web content label update started and it will be running in the background, you will get the email notification once it is completed</p>
    <p> Please click <a href="<%=themeDisplay.getPortalURL()+"/group"+themeDisplay.getScopeGroup().getFriendlyURL()+ "/~/control_panel/manage?p_p_id=com_liferay_journal_web_portlet_JournalPortlet" %>">here</a> to view the changes</p>
    
</div>
</div>