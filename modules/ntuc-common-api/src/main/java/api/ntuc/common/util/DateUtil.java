package api.ntuc.common.util;

import com.liferay.portal.kernel.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public DateUtil() {
		//Do nothing
	}

	private SimpleDateFormat dateTime = new SimpleDateFormat("dd MMM yyyy HH:mm");

	public static Date parse(String date, SimpleDateFormat sdf) {
		try {
			return sdf.parse(date);
		} catch (ParseException e1) {
			return null;
		}
	}

	public String toString(Date date) {
		return toString(date, dateTime);
	}

	public static String toString(Date date, SimpleDateFormat sdf) {
		if (Validator.isNull(date)) {
			return null;
		} else {
			return sdf.format(date);
		}
	}

}
