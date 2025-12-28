package com.ntuc.notification.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link DDMTemplateUtil}.
 *
 * Plain JUnit + Mockito is sufficient because Liferay dependencies are accessed via services/models
 * that can be mocked without an OSGi container.
 */
@RunWith(MockitoJUnitRunner.class)
public class DDMTemplateUtilTest {

    @Mock
    private DDMTemplateLocalService ddmTemplateLocalService;

    @Mock
    private DDMStructureLocalService ddmStructureLocalService;

    @Mock
    private DDMTemplate template;

    @Mock
    private DDMStructure structure;

    @Mock
    private DynamicQuery dynamicQuery;

    @Test
    public void getTemplateStructureInfo_blankName_returnsEmpty() {
        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        assertFalse(util.getTemplateStructureInfo(201L, null).isPresent());
        assertFalse(util.getTemplateStructureInfo(201L, "").isPresent());
        assertFalse(util.getTemplateStructureInfo(201L, "   ").isPresent());
    }

    @Test
    public void getTemplateStructureInfo_match_resolvesStructureAndReturnsDto() throws Exception {
        when(template.getNameCurrentValue()).thenReturn("Course Template A");
        when(template.getClassPK()).thenReturn(9001L);
        when(template.getTemplateId()).thenReturn(7001L);
        when(template.getTemplateKey()).thenReturn("TPL_KEY_1");

        when(structure.getStructureId()).thenReturn(9001L);
        when(structure.getStructureKey()).thenReturn("STR_KEY_1");
        when(structure.getNameCurrentValue()).thenReturn("Course Structure");
        when(ddmStructureLocalService.getDDMStructure(9001L)).thenReturn(structure);

        when(ddmTemplateLocalService.getTemplates(201L)).thenReturn(Collections.singletonList(template));

        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        Optional<DDMTemplateUtil.TemplateStructureInfo> infoOpt =
                util.getTemplateStructureInfo(201L, "course template a");

        assertTrue(infoOpt.isPresent());

        DDMTemplateUtil.TemplateStructureInfo info = infoOpt.get();
        assertEquals(9001L, info.getStructureId());
        assertEquals("STR_KEY_1", info.getStructureKey());
        assertEquals("Course Structure", info.getStructureName());
        assertEquals(7001L, info.getTemplateId());
        assertEquals("TPL_KEY_1", info.getTemplateKey());
        assertEquals("Course Template A", info.getTemplateName());
        assertNotNull(info.toString());
    }

    @Test
    public void getTemplateStructureInfo_portalException_returnsEmpty() throws Exception {
        when(ddmTemplateLocalService.getTemplates(201L)).thenThrow(new PortalException("boom"));

        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        Optional<DDMTemplateUtil.TemplateStructureInfo> infoOpt =
                util.getTemplateStructureInfo(201L, "Anything");

        assertFalse(infoOpt.isPresent());
    }

    @Test
    public void getTemplateByNameAndGroupId_blankName_returnsNull() {
        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        assertNull(util.getTemplateByNameAndGroupId(201L, null));
        assertNull(util.getTemplateByNameAndGroupId(201L, ""));
        assertNull(util.getTemplateByNameAndGroupId(201L, "   "));
    }

    @Test
    public void getTemplateByNameAndGroupId_resultsContainTemplate_returnsFirstTemplate() {
        when(ddmTemplateLocalService.dynamicQuery()).thenReturn(dynamicQuery);

        // Return a list that contains a non-template object first to validate defensive casting.
        when(ddmTemplateLocalService.dynamicQuery(eq(dynamicQuery)))
                .thenReturn(Arrays.asList("not-a-template", template));

        when(template.getNameCurrentValue()).thenReturn("My Template");
        when(template.getTemplateKey()).thenReturn("MY_TPL_KEY");

        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        DDMTemplate resolved = util.getTemplateByNameAndGroupId(201L, "Temp");

        assertNotNull(resolved);
        assertEquals(template, resolved);
    }

    @Test
    public void getTemplateByNameAndGroupId_noTemplateInResults_returnsNull() {
        when(ddmTemplateLocalService.dynamicQuery()).thenReturn(dynamicQuery);
        when(ddmTemplateLocalService.dynamicQuery(eq(dynamicQuery)))
                .thenReturn(Arrays.asList("x", 123L, new Object()));

        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        assertNull(util.getTemplateByNameAndGroupId(201L, "Temp"));
    }

    @Test
    public void getTemplateByNameAndGroupId_exception_returnsNull() {
        when(ddmTemplateLocalService.dynamicQuery()).thenThrow(new RuntimeException("fail"));

        DDMTemplateUtil util = new DDMTemplateUtil(ddmTemplateLocalService, ddmStructureLocalService);

        assertNull(util.getTemplateByNameAndGroupId(201L, "Temp"));
    }
}
