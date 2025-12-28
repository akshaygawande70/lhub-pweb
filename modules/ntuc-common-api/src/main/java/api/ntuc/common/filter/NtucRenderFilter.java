package api.ntuc.common.filter;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

import api.ntuc.common.override.OverrideRenderRequestParam;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.XSSValidationUtil;

public abstract class NtucRenderFilter implements RenderFilter {

	@Override
	public void init(FilterConfig filterConfig) throws PortletException {
		// Do nothing because not used.
	}

	@Override
	public void destroy() {
		// Do nothing because not used.
	}

	@Override
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain)
			throws IOException, PortletException {		Map<String, Object> newParam = new HashMap<String, Object>();
			XSSValidationUtil.removeSanitizeJSString(request);

		/*
		 * Check Permission for access page
		 */
		boolean isAuthorized = true;
		String renderName = ParamUtil.getString(request, "mvcRenderCommandName");
		newParam.put("isAuthorized", isAuthorized);

		/*
		 * CSRF Validation
		 */
		boolean validCSRF = false;
		if (Validator.isNull(renderName)) {
			// open module paramter for the 1st time no need to validate
			validCSRF = true;
		} else {
			String authToken = ParamUtil.getString(request, "authToken");
			if (CSRFValidationUtil.isValidRequest(request, authToken)) {
				validCSRF = true;
			}
		}
		newParam.put("validCSRF", validCSRF);

		/*
		 * XSS Validation
		 */
		boolean xssPass = true;
		Set<String> names = request.getRenderParameters().getNames();
		for (String name : names) {
			String param = request.getRenderParameters().getValue(name);
			if (!XSSValidationUtil.isValid(param)) {
				xssPass = false;
				break;
			}
		}
		newParam.put("xssPass", xssPass);

		OverrideRenderRequestParam over = new OverrideRenderRequestParam(request, newParam);
		chain.doFilter(over, response);
	}

}
