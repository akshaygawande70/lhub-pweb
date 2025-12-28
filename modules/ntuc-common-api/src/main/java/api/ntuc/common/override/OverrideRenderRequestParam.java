package api.ntuc.common.override;

import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.filter.RenderRequestWrapper;

public class OverrideRenderRequestParam extends RenderRequestWrapper {

	private Map<String, Object> newParam;

	public OverrideRenderRequestParam(RenderRequest request, Map<String, Object> newParam) {
		super(request);
		this.newParam = newParam;
	}

	@Override
	public String getParameter(String name) {
		if (getRenderParameters().getValue(name) == null) {
			Object param = this.newParam.get(name);
			if (param == null) {
				return null;
			} else {
				return param.toString();
			}
		}
		if (getRenderParameters().getValues(name).length == 1) {
			String param = getRenderParameters().getValue(name);
			return param;
		} else {
			String temps = "";
			for (String s : getRenderParameters().getValues(name)) {
				temps = s + ",";
			}
			temps = temps.substring(0, temps.length() - 1);
			return temps;
		}
	}

}
