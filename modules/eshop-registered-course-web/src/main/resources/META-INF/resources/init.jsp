<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@page
	import="web.ntuc.eshop.registeredcourse.constants.MVCCommandNames"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="java.util.List"%>
<%@page import="api.ntuc.nlh.content.constant.ContentConstants"%>
<liferay-theme:defineObjects />

<portlet:defineObjects />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/slick.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.72/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.72/vfs_fonts.js"></script>