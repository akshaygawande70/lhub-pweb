package web.ntuc.login.portlet;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

@Component(enabled = true, immediate = true, property = "key=login.events.pre", service = LifecycleAction.class)
public class PreLogin implements LifecycleAction{
	private static Log log = LogFactoryUtil.getLog(PreLogin.class);

	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
		try {
//			log.info("================before login nich===================");
			HttpServletRequest request = lifecycleEvent.getRequest();
//			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
//			User user = themeDisplay.getUser();
			User user = PortalUtil.getUser(request);
			CommerceAccount account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
			List<CommerceOrder> commerceOrders = CommerceOrderLocalServiceUtil
					.getCommerceOrdersByCommerceAccountId(account.getCommerceAccountId(), -1, -1, null);
			List<CommerceOrder> filteredCommerceOrders = commerceOrders.stream()
					.filter(x -> x.getOrderStatus() == CommerceOrderConstants.ORDER_STATUS_OPEN)
					.collect(Collectors.toList());
			for (CommerceOrder order : filteredCommerceOrders) {
				CommerceOrderLocalServiceUtil.deleteCommerceOrder(order);
			}

		} catch (Exception e) {
			log.error("error while pre login : " + e.getMessage());
		}
		
	}
			
}
