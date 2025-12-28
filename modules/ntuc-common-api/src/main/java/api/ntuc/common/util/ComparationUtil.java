package api.ntuc.common.util;

public class ComparationUtil {
	
	private ComparationUtil() {}
	public static boolean compare(Object obj1, Object obj2) {
		return (obj1 == null ? obj2 == null : obj1.equals(obj2));
	}
}
