package web.ntuc.page.audit.web.portlet;


import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouter;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.security.audit.event.generators.constants.EventTypes;
import com.liferay.portal.security.audit.event.generators.util.Attribute;
import com.liferay.portal.security.audit.event.generators.util.AttributesBuilder;
import com.liferay.portal.security.audit.event.generators.util.AuditMessageBuilder;
import com.ntuc.page.audit.web.configuration.LayoutAuditConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.page.audit.web.constants.NtucPageAuditWebPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


/**
 * @author Sanjay
 */
@Component(immediate = true, service = ModelListener.class, configurationPid = "com.liferay.mcv.layout.audit.configuration.LayoutAuditConfiguration")
public class NtucPageAuditWebPortlet extends BaseModelListener<Layout>  {
	private static final Log log = LogFactoryUtil.getLog(NtucPageAuditWebPortlet.class);
	private static String regExpression = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
	@Override
	public void onBeforeCreate(Layout layout) throws ModelListenerException {

		auditOnEvent(EventTypes.ADD, layout);

		super.onBeforeCreate(layout);
	}

	@Override
	public void onBeforeRemove(Layout layout) throws ModelListenerException {

		auditOnEvent(EventTypes.DELETE, layout);

		super.onBeforeRemove(layout);
	}

//	@Override
//	public void onBeforeUpdate(Layout layout) throws ModelListenerException {
//
//		auditOnEvent(EventTypes.UPDATE, layout);
//
//		super.onBeforeUpdate(layout);
//	}

	protected List<Attribute> getModifiedAttributes(Layout newLayout, Layout currentLayout) {

		AttributesBuilder attributesBuilder = new AttributesBuilder(newLayout, currentLayout);

		attributesBuilder.add(NtucPageAuditWebPortletKeys.NAME_ATTR);
		attributesBuilder.add(NtucPageAuditWebPortletKeys.FRIENDLY_URL_ATTR);

		return attributesBuilder.getAttributes();
	}

	protected void auditOnEvent(String eventType, Layout layout) throws ModelListenerException {
		if (_configuration.enabled()) {
			try {
				List<Attribute> attributes = new ArrayList<>();

				if (EventTypes.UPDATE.equals(eventType)) {
					Layout currentLayout = _layoutLocalService.getLayout(layout.getPlid());
					attributes = getModifiedAttributes(layout, currentLayout);
				}

				if ((EventTypes.UPDATE.equals(eventType) && !attributes.isEmpty())
						|| !EventTypes.UPDATE.equals(eventType)) {
					AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(eventType, Layout.class.getName(),
							layout.getLayoutId(), attributes);

					JSONObject additionalInfoJSONObject = auditMessage.getAdditionalInfo();
					String strFriendlyURL = layout.getFriendlyURL();
					
					log.info("NtucPageAuditWebPortlet::::auditOnEvent methid 11112024: "+layout.getFriendlyURL());
					if(strFriendlyURL != null && strFriendlyURL.length() > 0 && strFriendlyURL.substring(1).matches(regExpression)) {
						//do not capture audit event if friendly url matches regex ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$  do not store two entries for same page name event
						log.info("if reg matches NtucPageAuditWebPortlet::::auditOnEvent methid 11112024: "+layout.getFriendlyURL());
					}else {
						log.info("else reg not matches NtucPageAuditWebPortlet::::auditOnEvent methid 11112024: "+layout.getFriendlyURL());
						additionalInfoJSONObject.put(NtucPageAuditWebPortletKeys.NAME_ATTR, layout.getName(LocaleUtil.getDefault()))
						.put(NtucPageAuditWebPortletKeys.LAYOUT_ID_ATTR, layout.getPlid()).put(NtucPageAuditWebPortletKeys.FRIENDLY_URL_ATTR, layout.getFriendlyURL())
						.put(NtucPageAuditWebPortletKeys.GROUP_ID_ATTR, layout.getGroupId())
						.put(NtucPageAuditWebPortletKeys.GROUP_NAME_ATTR, layout.getGroup().getName(LocaleUtil.getDefault()));

						_auditRouter.route(auditMessage);
					}
					
				}

			} catch (Exception e) {
				throw new ModelListenerException(e);
			}

		}
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_configuration = ConfigurableUtil.createConfigurable(LayoutAuditConfiguration.class, properties);
	}

	private volatile LayoutAuditConfiguration _configuration;

	@Reference
	private AuditRouter _auditRouter;

	@Reference
	private LayoutLocalService _layoutLocalService;


}