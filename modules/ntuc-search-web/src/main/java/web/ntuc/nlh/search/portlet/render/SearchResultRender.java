package web.ntuc.nlh.search.portlet.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.nlh.search.constants.MVCCommandNames;
import web.ntuc.nlh.search.constants.SearchPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.SEARCH_RESULT_RENDER,
		"javax.portlet.name=" + SearchPortletKeys.SEARCH_PORTLET,
		"com.liferay.portlet.action-url-redirect=true" }, service = MVCRenderCommand.class)
public class SearchResultRender implements MVCRenderCommand {
	private static Log log = LogFactoryUtil.getLog(SearchResultRender.class);

	private static String searchResultPage = "/search/search-result.jsp";
	private static String errorPage = "/error.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("Search result render - start");

		try {
			PortletCommandUtil.renderCommand(renderRequest);
			
		} catch (Exception e) {
			log.error("Error while rendering search result : " + e.getMessage());
			return errorPage;
		}
		log.info("Search result render - end");
		return searchResultPage;
	}

}
