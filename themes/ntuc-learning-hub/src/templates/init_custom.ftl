<#assign
	show_footer = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-footer"))
	show_header = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-header"))
	show_header_search = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-header-search"))
	wrap_widget_page_content = getterUtil.getBoolean(themeDisplay.getThemeSetting("wrap-widget-page-content"))
/>

<#if wrap_widget_page_content && ((layout.isTypeContent() && themeDisplay.isStateMaximized()) || (layout.getType() == "portlet"))>
	<#assign portal_content_css_class = "container" />
<#else>
	<#assign portal_content_css_class = "" />
</#if>
<!--Start-->
<#assign journalArticleLocalService = serviceLocator.findService("com.liferay.journal.service.JournalArticleLocalService")>
<#--  This is Code for embed web content  -->
<#macro embedJournalArticle journalArticleTitle portletInstanceId>
    <#if validator.isNotNull(serviceLocator)>
        <#if validator.isNotNull(journalArticleLocalService)>
            <#if validator.isNotNull(journalArticleTitle) && !validator.isBlank(journalArticleTitle)>
                <#assign journalArticleUrlTitle=journalArticleTitle?lower_case?replace(" ", "-")?replace("_", "-")/>
                <#if validator.isNotNull(journalArticleUrlTitle) && !validator.isBlank(journalArticleUrlTitle)>
                    <#assign journalArticle=journalArticleLocalService.fetchArticleByUrlTitle(group_id,journalArticleUrlTitle)/> 
                </#if>
            </#if>
        </#if>  
    </#if>  
  <#if validator.isNotNull(journalArticle)> 
        <#assign journalArticlePreferencesMap = 
                    {
                        "portletSetupPortletDecoratorId": "barebone", 
                        "groupId": getterUtil.getString(group_id),
                        "articleId":getterUtil.getString(journalArticle.getArticleId())
                    } 
                    
                />
        <#assign journalArticlePreferences = freeMarkerPortletPreferences.getPreferences(journalArticlePreferencesMap) />
        <@liferay_portlet["runtime"]
                defaultPreferences= journalArticlePreferences
                instanceId=portletInstanceId
                portletProviderAction=portletProviderAction.VIEW
                portletName="com_liferay_journal_content_web_portlet_JournalContentPortlet" 
                />
    </#if>
</#macro>
<#--  End code to embed web content  -->

<#assign navTransparent = getterUtil.getBoolean(theme_settings["nav-transparent"])>
<#assign navBlack= getterUtil.getBoolean(theme_settings["nav-black"])>
<#assign navBlog = getterUtil.getBoolean(theme_settings["nav-blog"])>
<#assign wrap_widget_page_content = getterUtil.getBoolean(theme_settings["wrap-widget-page-content"])>
<#assign hideHeader = getterUtil.getBoolean(theme_settings["hide-header"])>
<#assign hideFooter = getterUtil.getBoolean(theme_settings["hide-footer"])>
<#assign pswdProtect = getterUtil.getBoolean(theme_settings["pswd-protect"])>
<#assign pswdText = getterUtil.getString(theme_settings["pswd-text"])>
<#assign gtmBot = getterUtil.getBoolean(theme_settings["gtm-bot"])>


<#assign usrRoles  = user.getRoles() >

<#assign showControlMenu  = false >
<#assign userRoleName = "DEFAULT" >
<#list usrRoles as uRole>
    <#if uRole.getName() == "Administrator" 
|| uRole.getName() == "Analytics Administrator" 
|| uRole.getName() == "Approver_BZS" 
|| uRole.getName() == "Approver_EIT" 
|| uRole.getName() == "Approver_GMM"
|| uRole.getName() == "Approver_ITT"
|| uRole.getName() == "Approver_LAN"
|| uRole.getName() == "Approver_LDE"
|| uRole.getName() == "Approver_SEI"
|| uRole.getName() == "Approver_SSE"
|| uRole.getName() == "Approver_SSE_BRAND"
|| uRole.getName() == "Approver_TRD"
|| uRole.getName() == "Approver_WSH"
|| uRole.getName() == "Brand Approver"
|| uRole.getName() == "Brand Editor"
|| uRole.getName() == "Editor"
|| uRole.getName() == "Exam Unit Admin"
|| uRole.getName() == "Exam Unit Operations"
|| uRole.getName() == "ExternalBlogPartiies"
|| uRole.getName() == "Finance Team"
|| uRole.getName() == "Get in-Touch & Locate US Editor"
|| uRole.getName() == "HR-Career-Editor"
|| uRole.getName() == "Marketing"
|| uRole.getName() == "Owner"
|| uRole.getName() == "PD"
|| uRole.getName() == "PD_BZS"
|| uRole.getName() == "PD_EIT"
|| uRole.getName() == "PD_ITT"
|| uRole.getName() == "PD_LAN"
|| uRole.getName() == "PD_LDE"
|| uRole.getName() == "PD_SEI"
|| uRole.getName() == "PD_SSE"
|| uRole.getName() == "PD_SSE_BRAND"
|| uRole.getName() == "PD_TRD"
|| uRole.getName() == "PD_WSH"
|| uRole.getName() == "Portal Content Reviewer"
|| uRole.getName() == "Site Admin NTUC LHUB" >
        <#assign showControlMenu = true />
        <#assign userRoleName = uRole.getName() />
        <#break>
        <#elseif uRole.getName() == "General Role" || uRole.getName() == "Eshop_Role">
            <#assign showControlMenu = false />
            <#assign userRoleName = uRole.getName() />
            <#break>
    </#if>
    <#assign userRoleName = uRole.getName() />
</#list>


<#assign base_url = "/web/guest">
<#assign logout_url = htmlUtil.escape(theme_display.getURLSignOut())>
<#assign base_portal = "/c/portal/login">
<#assign base_login = "/web/guest/login">

<#assign noIndex = getterUtil.getBoolean(theme_settings["no-index"])>