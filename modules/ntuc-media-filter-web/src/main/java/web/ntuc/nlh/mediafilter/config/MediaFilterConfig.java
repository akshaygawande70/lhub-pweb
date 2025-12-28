package web.ntuc.nlh.mediafilter.config;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "web.nlh.ntuc.mediafilter.config.MediaFilterConfig")
public interface MediaFilterConfig {
	
	public static final String TOPICS = "topics";
	
	public static final String SUBTOPICS = "subTopics";
	
	@Meta.AD(required = false)
	public String topicsConfig();
	
	@Meta.AD(required = false)
	public String subTopicsConfig();
	
}
