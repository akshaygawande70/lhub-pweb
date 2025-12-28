<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ taglib uri="http://liferay.com/tld/asset" prefix="liferay-asset" %><%@
taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %>

<%@ taglib prefix="clay" uri="http://liferay.com/tld/clay" %>
<%@ taglib prefix="liferay-item-selector" uri="http://liferay.com/tld/item-selector" %>
<%@ taglib prefix="liferay-frontend" uri="http://liferay.com/tld/frontend" %>
<%@page import="java.util.List"%>
<%@page import="com.liferay.petra.string.StringPool"%>
<liferay-theme:defineObjects />

<portlet:defineObjects />
<%@ page import="com.liferay.portal.kernel.servlet.SessionErrors" %>
<%@page import="web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.NtucSeoBulkuploadControlPanelAppPortletKeys"%>
<%@page import="web.ntuc.nlh.seo.bulkupload.control.panel.app.constants.MVCCommandNames"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.render.StatusCommonUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.liferay.portal.kernel.json.JSONObject"%>
<%@page import="com.liferay.portal.kernel.json.JSONArray"%>
<%@page import="com.liferay.portal.kernel.json.JSONFactoryUtil"%>
<%@page import="java.util.TimeZone"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="svc.ntuc.nlh.seo.bulkupload.model.NtucBulkUpload"%>
<%@page import="web.ntuc.nlh.seo.bulkupload.control.panel.app.portlet.util.BulkUploadUtil"%>

<style>
	.bulkupload-modal .modal-dialog .modal-body .button-holder {
		display: flex;
		justify-content: center;
		gap: 25px;
	}

	.btn-convertSubmit {
	    color: #fff;
	    background-color: #007bff !important;
	    border-color: #007bff;
	}
	
	.bulkupload-modal .modal-dialog {
        max-width: fit-content !important;
    }
    
    .bulkupload-modal .modal-dialog{
    	position: fixed;
    	left: 0;
    	right: 0;
    }

	.bulkupload-modal .modal-dialog .modal-body .button-holder .btn-primary,
	.bulkupload-modal .modal-dialog .modal-body .button-holder .btn-danger
	 {
		padding: 11px 35px;
		border-radius: 32px;
		font-size: 18px;
		line-height: 24px;
		margin-top: 30px !important;
	}
</style>