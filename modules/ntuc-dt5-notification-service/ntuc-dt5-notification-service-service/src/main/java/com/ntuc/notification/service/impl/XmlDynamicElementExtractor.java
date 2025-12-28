package com.ntuc.notification.service.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Extracts values from Liferay DDM XML content.
 *
 * Plain Java helper (no OSGi) - unit-test this.
 */
public class XmlDynamicElementExtractor {

    private XmlDynamicElementExtractor() {
        // util
    }

    public static String extractDynamicElementValue(String xml, String name) {
        if (xml == null || name == null) {
            return null;
        }

        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            NodeList elements = doc.getElementsByTagName("dynamic-element");
            for (int i = 0; i < elements.getLength(); i++) {
                Element el = (Element) elements.item(i);
                if (name.equals(el.getAttribute("name"))) {
                    NodeList contentNodes = el.getElementsByTagName("dynamic-content");
                    if (contentNodes.getLength() > 0) {
                        String value = contentNodes.item(0).getTextContent();
                        return (value != null) ? value.trim() : null;
                    }
                }
            }
        } catch (Exception ignore) {
            // Intentionally silent: caller handles null as "not found"
        }

        return null;
    }
}
