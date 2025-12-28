package web.ntuc.nlh.courses.config;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "web.nlh.ntuc.courses.config.CoursesConfig")
public interface CoursesConfig {
	
	public static final String THEMES = "themes";
	
	public static final String TOPICS = "topics";

	@Meta.AD(required = false)
	public String themesConfig();
	
	@Meta.AD(required = false)
	public String topicsConfig();
}
