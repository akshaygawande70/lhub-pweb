<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@page import="api.ntuc.nlh.content.constant.ContentConstants"%>
<%@ page import="com.liferay.portal.kernel.workflow.WorkflowConstants" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%@page import="web.ntuc.eshop.reports.constants.MVCCommandNames"%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/DataTables/dataTables.bootstrap4.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/DataTables/datatables.min.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap-datepicker.min.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/slick.min.js"></script>