package web.ntuc.nlh.assetfilter.config;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "web.nlh.ntuc.assetfilter.config.AssetFilterConfig")
public interface AssetFilterConfig {
	
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
