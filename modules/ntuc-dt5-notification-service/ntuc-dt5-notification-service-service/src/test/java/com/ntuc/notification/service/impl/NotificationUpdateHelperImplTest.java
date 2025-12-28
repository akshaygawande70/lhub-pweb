/*
 * NotificationUpdateHelperImplTest.java
 *
 * Unit tests for NotificationUpdateHelperImpl branching and mapping behavior.
 *
 * @author @akshaygawande
 */
package com.ntuc.notification.service.impl;

import com.liferay.portal.kernel.workflow.WorkflowConstants;

import com.ntuc.notification.constants.CourseStatusConstants;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.NotificationUpdateHelper;
import com.ntuc.notification.service.NtucSBLocalService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests focused on deterministic branching logic and mapping behavior.
 *
 * Business purpose:
 * - Ensures processing flags and statuses are updated consistently for each workflow phase.
 *
 * Technical purpose:
 * - Validates which setters are invoked on the chosen NtucSB instance and verifies persistence calls to the local service.
 */
@RunWith(MockitoJUnitRunner.class)
public class NotificationUpdateHelperImplTest {

    @Mock
    private NtucSBLocalService ntucSBLocalService;

    @Mock
    private NtucSB base;

    @Mock
    private NtucSB persisted;

    private NotificationUpdateHelperImpl sut;

    @Before
    public void setUp() throws Exception {
        sut = new NotificationUpdateHelperImpl();
        injectLocalService(sut, ntucSBLocalService);

        when(base.getNtucDTId()).thenReturn(12345L);
        when(persisted.getNtucDTId()).thenReturn(12345L);
    }

    @Test
    public void update_whenBaseIsNull_doesNotPersist() {
        sut.update(null, NotificationUpdateHelper.Phase.CRITICAL, true, true, "PS", "CS");
        verifyNoInteractions(ntucSBLocalService);
    }

    @Test
    public void update_critical_usesPersistedWhenAvailable_andPersists() {
        when(ntucSBLocalService.fetchNtucSB(12345L)).thenReturn(persisted);

        sut.update(
                base,
                NotificationUpdateHelper.Phase.CRITICAL,
                true,
                false,
                "PROC_OK",
                "COURSE_OK");

        verify(persisted).setIsCriticalProcessed(true);
        verify(persisted, never()).setIsNonCriticalProcessed(anyBoolean());
        verify(persisted, never()).setIsCronProcessed(anyBoolean());

        verify(persisted).setCanRetry(false);
        verify(persisted).setProcessingStatus("PROC_OK");
        verify(persisted).setCourseStatus("COURSE_OK");

        verify(ntucSBLocalService).updateNtucSB(persisted);
    }

    @Test
    public void update_all_setsAllFlags_andPersists() {
        when(ntucSBLocalService.fetchNtucSB(12345L)).thenReturn(persisted);

        sut.update(
                base,
                NotificationUpdateHelper.Phase.ALL,
                true,
                true,
                "DONE",
                "PUBLISHED");

        verify(persisted).setIsCriticalProcessed(true);
        verify(persisted).setIsNonCriticalProcessed(true);
        verify(persisted).setIsCronProcessed(true);

        verify(persisted).setCanRetry(true);
        verify(persisted).setProcessingStatus("DONE");
        verify(persisted).setCourseStatus("PUBLISHED");

        verify(ntucSBLocalService).updateNtucSB(persisted);
    }

    @Test
    public void update_whenNoPersistedRow_updatesInputRecord_andPersists() {
        when(ntucSBLocalService.fetchNtucSB(12345L)).thenReturn(null);

        sut.update(
                base,
                NotificationUpdateHelper.Phase.NON_CRITICAL,
                true,
                false,
                "NC_DONE",
                "NC_STATUS");

        verify(base).setIsNonCriticalProcessed(true);
        verify(base, never()).setIsCriticalProcessed(anyBoolean());
        verify(base, never()).setIsCronProcessed(anyBoolean());

        verify(base).setCanRetry(false);
        verify(base).setProcessingStatus("NC_DONE");
        verify(base).setCourseStatus("NC_STATUS");

        verify(ntucSBLocalService).updateNtucSB(base);
    }

    @Test
    public void fetchOr_null_returnsNull() {
        assertNull(sut.fetchOr(null));
    }

    @Test
    public void fetchOr_whenPersistedExists_returnsPersisted() {
        when(ntucSBLocalService.fetchNtucSB(12345L)).thenReturn(persisted);

        NtucSB result = sut.fetchOr(base);

        assertEquals(persisted, result);
    }

    @Test
    public void fetchOr_whenPersistedMissing_returnsInputRecord() {
        when(ntucSBLocalService.fetchNtucSB(12345L)).thenReturn(null);

        NtucSB result = sut.fetchOr(base);

        assertEquals(base, result);
    }

    @Test
    public void courseStatusForWorkflowStatus_mapsKnownStatuses() {
        assertEquals(CourseStatusConstants.PUBLISHED,
                sut.courseStatusForWorkflowStatus(WorkflowConstants.STATUS_APPROVED));

        assertEquals(CourseStatusConstants.PENDING,
                sut.courseStatusForWorkflowStatus(WorkflowConstants.STATUS_PENDING));

        assertEquals(CourseStatusConstants.EXPIRED,
                sut.courseStatusForWorkflowStatus(WorkflowConstants.STATUS_EXPIRED));

        assertEquals(CourseStatusConstants.UNKNOWN,
                sut.courseStatusForWorkflowStatus(WorkflowConstants.STATUS_DRAFT));
    }

    private static void injectLocalService(
            NotificationUpdateHelperImpl target,
            NtucSBLocalService localService) throws Exception {

        Field field = NotificationUpdateHelperImpl.class.getDeclaredField("_ntucSBLocalService");
        field.setAccessible(true);
        field.set(target, localService);
    }
}
