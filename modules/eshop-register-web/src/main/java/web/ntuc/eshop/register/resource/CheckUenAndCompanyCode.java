package web.ntuc.eshop.register.resource;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.register.constants.MVCCommandNames;
import web.ntuc.eshop.register.constants.RegisterPortletKeys;
import web.ntuc.eshop.register.util.RegisterAccount;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.CHECK_UEN_AND_COMPANY_RESOURCE,
		"javax.portlet.name=" + RegisterPortletKeys.REGISTER }, service = MVCResourceCommand.class)
public class CheckUenAndCompanyCode implements MVCResourceCommand {
	private static Log log = LogFactoryUtil.getLog(CheckUenAndCompanyCode.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("check email resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(resourceRequest,themeDisplay);
			String uenNumber = ParamUtil.getString(resourceRequest, "uenNumber");
			String companyCode = ParamUtil.getString(resourceRequest, "companyCode");
			
			
			
			PrintWriter printWriter = resourceResponse.getWriter();
			printWriter.write(String.valueOf(RegisterAccount.corporateValidate(uenNumber, companyCode)));
			printWriter.close();
		}catch (Exception e) {
			log.error("Error while checking uen number and company code : "+ e.getMessage());
			return true;
		}
		log.info("check email resources - end");
		return false;
	}
	
	
}
