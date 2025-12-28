package api.ntuc.common.filter;

import com.liferay.portal.kernel.util.ParamUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;

import api.ntuc.common.override.OverrideActionRequestParam;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.XSSValidationUtil;

public abstract class NtucActionFilter implements ActionFilter {

	@Override
	public void init(FilterConfig filterConfig) throws PortletException {
		// Do nothing because not used.
	}

	@Override
	public void destroy() {
		// Do nothing because not used.
	}

	@Override
	public void doFilter(ActionRequest request, ActionResponse response, FilterChain chain)
			throws IOException, PortletException {

		Map<String, Object> newParam = new HashMap<>();

		/*
		 * Check Permission for access page
		 */
		boolean isAuthorized = true;

		newParam.put("isAuthorized", isAuthorized);

		/*
		 * CSRF Validation
		 */
		boolean validCSRF = false;
		String authToken = ParamUtil.getString(request, "authToken");
		if (CSRFValidationUtil.isValidRequest(request, authToken)) {
			validCSRF = true;
		}
		newParam.put("validCSRF", validCSRF);

		/*
		 * XSS Validation
		 */
		boolean xssPass = true;
		Set<String> names = request.getActionParameters().getNames();
		for (String name : names) {
			String param = request.getActionParameters().getValue(name);
			if (!XSSValidationUtil.isValid(param)) {
				xssPass = false;
				break;
			}
		}
		newParam.put("xssPass", xssPass);

		OverrideActionRequestParam over = new OverrideActionRequestParam(request, newParam);
		chain.doFilter(over, response);
	}

}
