package api.ntuc.common.override;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.filter.ActionRequestWrapper;

public class OverrideActionRequestParam extends ActionRequestWrapper {

	private Map<String, Object> newParam;

	public OverrideActionRequestParam(ActionRequest request, Map<String, Object> newParam) {
		super(request);
		this.newParam = newParam;
	}

	@Override
	public String getParameter(String name) {
		if (getActionParameters().getValue(name) == null) {
			Object param = this.newParam.get(name);
			if (param == null) {
				return null;
			} else {
				return param.toString();
			}
		}
		if (getActionParameters().getValues(name).length == 1) {
			String param = getActionParameters().getValue(name);
			return param;
		} else {
			String temps = "";
			for (String s : getActionParameters().getValues(name)) {
				temps = s + ",";
			}
			temps = temps.substring(0, temps.length() - 1);
			return temps;
		}
	}

}
