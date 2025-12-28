package web.ntuc.nlh.datefilter.config;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "web.nlh.ntuc.datefilter.config.DateFilterConfig")
public interface DateFilterConfig {
	
	public static final String TOPICS = "topics";
	
	public static final String SUBTOPICS = "subTopics";
	
	public static final String TOPICTITLE = "topicTitle";
	
	@Meta.AD(required = false)
	public String topicsConfig();
	
	@Meta.AD(required = false)
	public String subTopicsConfig();
	
	@Meta.AD(required = false)
	public String topicTitleConfig();
	
}
