package web.ntuc.eshop.checkout.validator.portlet;

import com.liferay.commerce.inventory.engine.CommerceInventoryEngine;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.CommerceOrderValidator;
import com.liferay.commerce.order.CommerceOrderValidatorResult;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author fandifadillah
 */
@Component(
	immediate = true,
	property = {
			"commerce.order.validator.key="+EshopCheckoutValidatorWebPortlet.NAME,
			"commerce.order.validator.priority:Integer=9"
	},
	service = CommerceOrderValidator.class
)
public class EshopCheckoutValidatorWebPortlet implements CommerceOrderValidator {
	private static final Log log = LogFactoryUtil.getLog(EshopCheckoutValidatorWebPortlet.class);
	public static final String NAME = "custom-checkout-validator";
	
	@Override
	public String getKey() {
		return NAME;
	}
	
	
	@Override
	public CommerceOrderValidatorResult validate(Locale locale, CommerceOrder commerceOrder, CPInstance cpInstance,
			int quantity) throws PortalException {
		log.info("============checkout validator start============");
		if (cpInstance == null) {
			return new CommerceOrderValidatorResult(false);
		}
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				"content.Language", locale, getClass());
		log.info("order items size = "+commerceOrder.getCommerceOrderItems().size());
		long cpDefinitionId = cpInstance.getCPDefinitionId();
		CPDefinition cpDefinition = CPDefinitionLocalServiceUtil.fetchCPDefinition(cpDefinitionId);
		int countingVirtual = 0;
		int countingVirtualSameProductId = 0;
		if(cpDefinition.getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true) {
			for (CommerceOrderItem commerceOrderItem : commerceOrder.getCommerceOrderItems()) {
				if(commerceOrderItem.getCPDefinition().getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true) {
					countingVirtual++;
					if(commerceOrderItem.getCProductId() == (cpInstance.getCPDefinition().getCProductId())) {
						countingVirtualSameProductId++;
					}
				}
			}
			log.info(countingVirtual + " countingVirtual");
			log.info(commerceOrder.getCommerceOrderItems().size() + " size");
			if(countingVirtual > 0 || countingVirtualSameProductId > 1) {
			return new CommerceOrderValidatorResult(
					false,
					LanguageUtil.format(
						resourceBundle,
						"cart-already-maximum",null));
			}
		}
		
		int availableQuantity = _commerceInventoryEngine.getStockQuantity(
				cpInstance.getCompanyId(), commerceOrder.getGroupId(),
				cpInstance.getSku());
		if (quantity > availableQuantity) {
			return new CommerceOrderValidatorResult(
					false,
					LanguageUtil.format(
						resourceBundle,
						"that-quantity-is-unavailable",null));
		}
		log.info("============checkout validator end============");
		return new CommerceOrderValidatorResult(true);
	}
	
	@Override
	public CommerceOrderValidatorResult validate(Locale locale, CommerceOrderItem commerceOrderItem)
			throws PortalException {
//		log.info("============checkout validator2 start============");
//		log.info("============checkout validator2 end============");
		return new CommerceOrderValidatorResult(true);
	}
	
	@Reference
	private Portal _portal;
	
	@Reference
	private CommerceInventoryEngine _commerceInventoryEngine;
}