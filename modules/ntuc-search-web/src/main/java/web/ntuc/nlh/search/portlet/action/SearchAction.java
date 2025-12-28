package web.ntuc.nlh.search.portlet.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.nlh.search.constants.MVCCommandNames;
import web.ntuc.nlh.search.constants.SearchMessagesKey;
import web.ntuc.nlh.search.constants.SearchPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_ACTION,
		"javax.portlet.name=" + SearchPortletKeys.SEARCH_PORTLET,
		"com.liferay.portlet.action-url-redirect=true" }, service = MVCActionCommand.class)
public class SearchAction extends BaseMVCActionCommand {
	private static Log log = LogFactoryUtil.getLog(SearchAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Search action - start");

		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long scopeGroupId = themeDisplay.getScopeGroupId();

			PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);

			String keywords = ParamUtil.getString(actionRequest, "keywords");
			String groupFriendlyUrl = GroupLocalServiceUtil.getGroup(scopeGroupId).getFriendlyURL();
			String urlPrefix = "/web" + groupFriendlyUrl;
			actionResponse
					.sendRedirect(urlPrefix + MVCCommandNames.SEARCH_ACTION_TARGET + "?course=1&keyword=" + keywords);
		} catch (Exception e) {
			log.error("Error while save parameter group: " + e.getMessage(), e);
		}
		log.info("Search action - end");

	}

}
