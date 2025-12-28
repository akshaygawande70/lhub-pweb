package api.ntuc.common.util;

import com.liferay.portal.kernel.model.Role;

import java.util.List;
import java.util.regex.Pattern;

public class RoleUtil {
	private RoleUtil() {}
	
	public static boolean matchByPartialRoleName(List<Role> roles, String roleName) {
		for(Role role : roles) {
			if(Pattern.compile(Pattern.quote(roleName), Pattern.CASE_INSENSITIVE).matcher(role.getName()).find()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean matchByFullRoleName(List<Role> roles, String roleName) {
		for(Role role : roles) {
			if(role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}
}
