<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@page import="api.ntuc.nlh.content.constant.ContentConstants"%>
<%@page import="web.ntuc.eshop.invoice.constants.MVCCommandNames"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Locale"%>
<%@page
	import="com.liferay.commerce.constants.CommerceOrderPaymentConstants"
%>
<%@page
	import="com.liferay.commerce.service.CommerceOrderLocalServiceUtil"
%>
<%@page import="com.liferay.commerce.model.CommerceOrder"%>
<%@page
	import="com.liferay.commerce.tax.engine.fixed.service.CommerceTaxFixedRateLocalServiceUtil"
%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.log.Log"%>
<%@page import="javax.portlet.PortletSession"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="web.ntuc.eshop.invoice.dto.OrderDto"%>
<%@page import="web.ntuc.eshop.invoice.dto.InvoiceDto"%>
<%@page import="javax.ws.rs.sse.InboundSseEvent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<liferay-theme:defineObjects />

<portlet:defineObjects />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/slick.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.72/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.72/vfs_fonts.js"></script>