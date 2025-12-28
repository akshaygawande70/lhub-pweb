package api.ntuc.nlh.content.engine;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.OpenSearch;
import com.liferay.portal.kernel.search.OpenSearchRegistryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.asset.util.AssetUtil;

import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

public class SearchUtil {

	private SearchUtil() {
	}

	public static List<OpenSearch> getOpenSearchInstances(String primarySearch) {

		List<OpenSearch> openSearchInstances = ListUtil.filter(OpenSearchRegistryUtil.getOpenSearchInstances(),
				OpenSearch::isEnabled);

		if (Validator.isNotNull(primarySearch)) {
			for (int i = 0; i < openSearchInstances.size(); i++) {
				OpenSearch openSearch = openSearchInstances.get(i);

				if (primarySearch.equals(openSearch.getClassName())) {
					if (i != 0) {
						openSearchInstances.remove(i);

						openSearchInstances.add(0, openSearch);
					}

					break;
				}
			}
		}

		return openSearchInstances;
	}

	public static String getSearchResultViewURL(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
			String className, long classPK, boolean viewInContext, String currentURL) {
		try {
			PortletURL viewContentURL = resourceResponse.createRenderURL();

			viewContentURL.getRenderParameters().setValue("mvcPath", "/view_content.jsp");
			viewContentURL.getRenderParameters().setValue("redirect", currentURL);
			viewContentURL.setPortletMode(PortletMode.VIEW);
			viewContentURL.setWindowState(WindowState.MAXIMIZED);
			if ((Validator.isNull(className)) || (classPK <= 0L)) {
				return viewContentURL.toString();
			}
			AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(className, classPK);

			AssetRendererFactory<?> assetRendererFactory = AssetRendererFactoryRegistryUtil
					.getAssetRendererFactoryByClassName(className);
			if (assetRendererFactory == null) {
				return viewContentURL.toString();
			}
			viewContentURL.getRenderParameters().setValue("assetEntryId", String.valueOf(assetEntry.getEntryId()));
			viewContentURL.getRenderParameters().setValue("type", assetRendererFactory.getType());
			if (viewInContext) {
				AssetRenderer<?> assetRenderer = assetRendererFactory.getAssetRenderer(classPK);

				String viewURL = assetRenderer.getURLViewInContext(PortalUtil.getLiferayPortletRequest(resourceRequest),
						PortalUtil.getLiferayPortletResponse(resourceResponse), null);
				ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute("LIFERAY_SHARED_THEME_DISPLAY");
				if (Validator.isNotNull(viewURL)) {
					return AssetUtil.checkViewURL(assetEntry, viewInContext, viewURL, currentURL, themeDisplay);
				} else {
					return null;
				}
			}
			return viewContentURL.toString();
		} catch (Exception e) {
			_log.error("Unable to get search result  view URL for class " + className + " with primary key " + classPK,
					e);
		}
		return "";
	}

	private static final Log _log = LogFactoryUtil.getLog(SearchUtil.class);

}
