package com.ntuc.notification.portlet.configuration;

import com.liferay.portal.kernel.settings.definition.ConfigurationBeanDeclaration;
import com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration;

import org.osgi.service.component.annotations.Component;

/**
 * Declares the configuration bean edited by the DT5 Email Templates screen.
 *
 * <p>This is UI wiring only. No alerting or email logic here.</p>
 */
@Component(service = ConfigurationBeanDeclaration.class)
public class Dt5EmailTemplateConfigurationBeanDeclaration
        implements ConfigurationBeanDeclaration {

    @Override
    public Class<?> getConfigurationBeanClass() {
        return Dt5EmailTemplateConfiguration.class;
    }
}
