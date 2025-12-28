package com.ntuc.notification.portlet.configuration;

import com.liferay.configuration.admin.category.ConfigurationCategory;

import org.osgi.service.component.annotations.Component;

/**
 * Registers "DT5 Email" as a System Settings category under the "Other" section.
 *
 * This enables ConfigurationScreen entries using categoryKey="dt5-email"
 * to appear under:
 * Control Panel → Configuration → System Settings → Other → DT5 Email
 */
@Component(service = ConfigurationCategory.class)
public class Dt5EmailConfigurationCategory implements ConfigurationCategory {

    public static final String CATEGORY_KEY = "dt5-email";

    @Override
    public String getCategoryKey() {
        return CATEGORY_KEY;
    }

    @Override
    public String getCategorySection() {
        // 7.3-safe: return literal section key (constants vary by patch level)
        return "other";
    }
}
