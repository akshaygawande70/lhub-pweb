package com.ntuc.notification.service.internal.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Assert;
import org.junit.Test;

public class XmlContentXmlUtilTest {

    @Test
    public void upsertFieldElement_isIdempotent_instanceIdStable_andNoDuplicateDynamicContent() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("root")
                .addAttribute("available-locales", "en_US")
                .addAttribute("default-locale", "en_US");

        String[] langs = new String[] { "en_US" };

        // "Real" type map example
        String ddmTypeCourseTitle = "text"; // => index-type keyword

        // First upsert
        XmlContentXmlUtil.upsertFieldElement(
                root,
                "courseTitle",
                ddmTypeCourseTitle,
                langs,
                "My Course");

        Element e1 = XmlContentXmlUtil.findDirectChildByName(root, "courseTitle");
        Assert.assertNotNull(e1);
        Assert.assertEquals("text", e1.attributeValue("type"));
        Assert.assertEquals("keyword", e1.attributeValue("index-type"));

        String instanceId1 = e1.attributeValue("instance-id");
        Assert.assertEquals(XmlContentXmlUtil.instanceId("courseTitle", 0), instanceId1);

        // Second upsert with same value should not change instance-id or add duplicate dynamic-content
        XmlContentXmlUtil.upsertFieldElement(
                root,
                "courseTitle",
                ddmTypeCourseTitle,
                langs,
                "My Course");

        Element e2 = XmlContentXmlUtil.findDirectChildByName(root, "courseTitle");
        Assert.assertNotNull(e2);

        String instanceId2 = e2.attributeValue("instance-id");
        Assert.assertEquals(instanceId1, instanceId2);

        int dynamicContentCount = e2.elements("dynamic-content").size();
        Assert.assertEquals("Must have exactly 1 dynamic-content for en_US", 1, dynamicContentCount);
    }

    @Test
    public void upsertFieldElement_nestedPath_createsDeterministicTree() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("root");

        String[] langs = new String[] { "en_US" };

        XmlContentXmlUtil.upsertFieldElement(
                root,
                "schedule.duration.hours",
                "text",
                langs,
                "8");

        Element schedule = XmlContentXmlUtil.findDirectChildByName(root, "schedule");
        Assert.assertNotNull(schedule);

        Element duration = XmlContentXmlUtil.findDirectChildByName(schedule, "duration");
        Assert.assertNotNull(duration);

        Element hours = XmlContentXmlUtil.findDirectChildByName(duration, "hours");
        Assert.assertNotNull(hours);

        Assert.assertEquals("text", hours.attributeValue("type"));
        Assert.assertEquals("keyword", hours.attributeValue("index-type"));

        // Deterministic instance-id on leaf creation
        Assert.assertEquals(
                XmlContentXmlUtil.instanceId("schedule.duration.hours", 0),
                hours.attributeValue("instance-id")
        );
    }
}
