package web.ntuc.eshop.stripe.payment.util;

import java.text.DecimalFormat;

import api.ntuc.common.util.CurrencyUtil;

public class StripeFormatCurrencyUtil {
	
	public static String formatCurrency(double amount){
		
//		String val = String.valueOf(amount);
//		
//		if(val.contains(".00")) {
//			return val.replace(".00","0");
//		} else {
//			return CurrencyUtil.roundUpDollarAmount(val);
//		}
		if (amount == .00)
		    return String.valueOf(0);
		else
		    return String.format("%.02f", amount);
	}
	
	
	public static String formatGSTCurrency(double taxAmount) {
		if(taxAmount == (long) taxAmount)
	        return String.format("%d",(long) taxAmount);
	    else
	        return String.format("%s", taxAmount);
	}
}
