package api.ntuc.common.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class CurrencyUtil {
	
	private CurrencyUtil() {}

	public static String getRupiahReadableFormat(long number) {
		if (number >= 10000000000L) {
			return String.format(new Locale("id"), "Rp %,.2f T", number / 10000000000.0).replace('.', ',');
		} else if (number >= 1000000000) {
			return String.format(new Locale("id"), "Rp %.2f M", number / 1000000000.0);
		} else if (number >= 1000000) {
			return String.format(new Locale("id"), "Rp %.2f JT", number / 1000000.0);
		}

		return String.format("Rp %,.0f", number / 1.0);
	}
	
	public static String roundUpDollarAmount(String amount) {
		Double doubleAmount = Double.valueOf(amount);
		DecimalFormat f = new DecimalFormat("##.00");
		return f.format(doubleAmount);
	}

}
