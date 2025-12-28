package api.ntuc.nlh.content.engine;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

public interface PortletURLFactory {
	public abstract PortletURL getPortletURL() throws PortletException;
}
