 <#assign globalSearchPreferencesMap = 
                    {
                        "portletSetupPortletDecoratorId": "barebone"
                    } 
                    
  />
  <#assign globalSearchPreferences = freeMarkerPortletPreferences.getPreferences(globalSearchPreferencesMap) />
 <@liferay_portlet["runtime"] 
 	defaultPreferences= "${globalSearchPreferences}" 
 	portletName="web_ntuc_nlh_search_global_SearchGlobalPortlet"
 />