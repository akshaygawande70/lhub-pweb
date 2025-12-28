package api.ntuc.common.filter;

import com.liferay.portal.kernel.util.ParamUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.ResourceFilter;

import api.ntuc.common.override.OverrideResourceRequestParam;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.XSSValidationUtil;

public abstract class NtucResourceFilter implements ResourceFilter {

	@Override
	public void init(FilterConfig filterConfig) throws PortletException {
		// Do nothing because not used.
	}

	@Override
	public void destroy() {
		// Do nothing because not used.
	}

	@Override
	public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain)
			throws IOException, PortletException {
		Map<String, Object> newParam = new HashMap<>();

		/*
		 * Check Permission for access page
		 */
		newParam.put("isAuthorized", true);

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
		Set<String> names = request.getResourceParameters().getNames();
		for (String name : names) {
			String param = request.getResourceParameters().getValue(name);
			if (!XSSValidationUtil.isValid(param)) {
				xssPass = false;
				break;
			}
		}
		newParam.put("xssPass", xssPass);

		OverrideResourceRequestParam over = new OverrideResourceRequestParam(request, newParam);

		chain.doFilter(over, response);
	}

}
