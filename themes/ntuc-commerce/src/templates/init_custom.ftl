<#assign
	copyright = getterUtil.getString(themeDisplay.getThemeSetting("copyright"))
	show_top_menu = getterUtil.getBoolean(themeDisplay.getThemeSetting("show-top-menu"))
	my_account_url = themeDisplay.getPathFriendlyURLPublic() + themeDisplay.getScopeGroup().getFriendlyURL() + "/my-account"
	wishlistUrl = commerceWishListHttpHelper.getCommerceWishListPortletURL(request)
	wish_lists_text = commerceThemeMiniumHttpHelper.getMyListsLabel(locale)
	notifications_text = languageUtil.get(locale, "notifications")
	notification_url = commerceThemeMiniumHttpHelper.getNotificationsURL(request)
	notification_count = commerceThemeMiniumHttpHelper.getNotificationsCount(themeDisplay)
	wide_layout = getterUtil.getBoolean(themeDisplay.getThemeSetting("wide-layout"))
	back_url = paramUtil.getString(request, "p_r_p_backURL")
	speedwell_content_css_class = "speedwell-content"
	translucent_topbar = getterUtil.getBoolean(themeDisplay.getThemeSetting("translucent-topbar"))
	speedwell_topbar_css_class = "speedwell-topbar"
/>


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
<#assign navWhite = getterUtil.getBoolean(theme_settings["nav-white"])>
<#assign navBlack = getterUtil.getBoolean(theme_settings["nav-black"])>
<#assign gtmBot = getterUtil.getBoolean(theme_settings["gtm-bot"])>


<#if !is_setup_complete && is_signed_in>
	<#assign translucent_topbar = false />
</#if>

<#if is_maximized>
	<#assign
		translucent_topbar = false
		wide_layout = false
	/>
</#if>

<#if wide_layout>
	<#assign
	speedwell_content_css_class = "speedwell-content speedwell-content--wide"
	/>
</#if>

<#if translucent_topbar>
	<#assign
	speedwell_topbar_css_class = "speedwell-topbar speedwell-topbar--translucent"
	/>
</#if>

<#macro site_navigation_menu_main default_preferences = "">
	<@liferay_portlet["runtime"]
		defaultPreferences=default_preferences
		instanceId="siteNavigationMenuPortlet_main"
		portletName="com_liferay_site_navigation_menu_web_portlet_SiteNavigationMenuPortlet"
	/>
</#macro>

<#macro site_navigation_menu_sub_navigation default_preferences = "">
	<@liferay_portlet["runtime"]
		defaultPreferences=default_preferences
		instanceId="siteNavigationMenuPortlet_sub_navigation"
		portletName="com_liferay_site_navigation_menu_web_portlet_SiteNavigationMenuPortlet"
	/>
</#macro>

<#macro site_navigation_menu_account default_preferences = "">
	<@liferay_portlet["runtime"]
		defaultPreferences=default_preferences
		instanceId="siteNavigationMenuPortlet_account"
		portletName="com_liferay_site_navigation_menu_web_portlet_SiteNavigationMenuPortlet"
	/>
</#macro>

<#macro commerce_category_navigation_menu default_preferences = "">
	<@liferay_portlet["runtime"]
		defaultPreferences=default_preferences
		instanceId="cpAssetCategoriesNavigationPortlet_navigation_menu"
		portletName="com_liferay_commerce_product_asset_categories_navigation_web_internal_portlet_CPAssetCategoriesNavigationPortlet"
	/>
</#macro>

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