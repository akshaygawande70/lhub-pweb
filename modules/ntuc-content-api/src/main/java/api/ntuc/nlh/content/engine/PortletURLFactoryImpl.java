package api.ntuc.nlh.content.engine;

import com.liferay.portal.kernel.portlet.PortletURLUtil;

import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

public class PortletURLFactoryImpl implements PortletURLFactory {
	private final MimeResponse mimeResponse;
	private final PortletRequest portletRequest;

	public PortletURLFactoryImpl(PortletRequest portletRequest, MimeResponse mimeResponse) {
		this.portletRequest = portletRequest;
		this.mimeResponse = mimeResponse;
	}

	public PortletURL getPortletURL() throws PortletException {
		PortletURL portletURL = PortletURLUtil.getCurrent(this.portletRequest, this.mimeResponse);

		return PortletURLUtil.clone(portletURL, this.mimeResponse);
	}
}