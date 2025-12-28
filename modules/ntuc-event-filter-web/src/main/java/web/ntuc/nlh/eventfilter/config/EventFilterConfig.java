package web.ntuc.nlh.eventfilter.config;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(id = "web.nlh.ntuc.eventfilter.config.EventFilterConfig")
public interface EventFilterConfig {
	public static final String EVENTTYPE = "eventType";
	
	public static final String TOPICS = "topics";

	@Meta.AD(required = false)
	public String eventTypeConfig();
	
	@Meta.AD(required = false)
	public String topicsConfig();

}
