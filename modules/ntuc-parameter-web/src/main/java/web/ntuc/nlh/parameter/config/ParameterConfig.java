package web.ntuc.nlh.parameter.config;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "web.afi.parameter.config.ParameterConfig")
public interface ParameterConfig {

	public static final String GROUP_ID = "groupId";

	@Meta.AD(required = false)
	public String groupId();

}