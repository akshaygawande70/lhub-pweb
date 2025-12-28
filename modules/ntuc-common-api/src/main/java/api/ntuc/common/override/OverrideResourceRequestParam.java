package api.ntuc.common.override;

import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.filter.ResourceRequestWrapper;

public class OverrideResourceRequestParam extends ResourceRequestWrapper {

	private Map<String, Object> newParam;

	public OverrideResourceRequestParam(ResourceRequest request, Map<String, Object> newParam) {
		super(request);
		this.newParam = newParam;
	}

	@Override
	public String getParameter(String name) {
		if (getResourceParameters().getValue(name) == null) {
			Object param = this.newParam.get(name);
			if (param == null) {
				return null;
			} else {
				return param.toString();
			}
		}
		if (getResourceParameters().getValues(name).length == 1) {
			String param = getResourceParameters().getValue(name);
			return param;
		} else {
			String temps = "";
			for (String s : getResourceParameters().getValues(name)) {
				temps = s + ",";
			}
			temps = temps.substring(0, temps.length() - 1);
			return temps;
		}
	}

}
