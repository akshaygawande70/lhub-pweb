package web.ntuc.nlh.mediafilter.portlet;

import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.nlh.mediafilter.config.MediaFilterConfig;
import web.ntuc.nlh.mediafilter.constants.MediaFilterPortletKeys;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=ntuc-module",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=MediaFilter", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + MediaFilterPortletKeys.MEDIAFILTER_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class MediaFilterPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(MediaFilterPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Topics Media Filter Portlet render - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String baseUrl = themeDisplay.getPortalURL();
			
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

			// Get id topic from configuration
			Long topic = Long.valueOf(renderRequest.getPreferences().getValue(MediaFilterConfig.TOPICS, "0"));

			String topicId = String.valueOf(topic);
			log.info("topicId : " + topicId);

			List<Integer> filterYears = new ArrayList<>();

//			Generate Years
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int lastFiveYear = year - 4;
			for (int i = year; i >= lastFiveYear; i--) {
				filterYears.add(i);
			}

			renderRequest.setAttribute("baseUrl", baseUrl);
			renderRequest.setAttribute("topicId", topicId);
			renderRequest.setAttribute("years", filterYears);
		} catch (Exception e) {
			log.error("Failed when render topics, error:" + e.getMessage());
		}
		log.info("Topics Media Filter Portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	@Reference
	AssetCategoryLocalService assetCategoryLocalService;
}