package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link NotificationIntakePersister}.
 *
 * Command:
 * ./gradlew test --tests com.ntuc.notification.rest.internal.processor.NotificationIntakePersisterTest
 */
@RunWith(MockitoJUnitRunner.class)
public class NotificationIntakePersisterTest {

    @Mock
    private NtucSBLocalService ntucSBLocalService;

    @Mock
    private CounterLocalService counterLocalService;

    @Mock
    private CourseEvent event;

    @Mock
    private NtucSB sb;

    @Test
    public void constructor_nullNtucSBLocalService_throwsNpe() {
        try {
            new NotificationIntakePersister(null, counterLocalService);
            Assert.fail("Expected NullPointerException");
        }
        catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void constructor_nullCounterLocalService_throwsNpe() {
        try {
            new NotificationIntakePersister(ntucSBLocalService, null);
            Assert.fail("Expected NullPointerException");
        }
        catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void persist_nullEvent_throwsNpe() {
        NotificationIntakePersister persister = new NotificationIntakePersister(
                ntucSBLocalService, counterLocalService);

        try {
            persister.persist(null, 123L);
            Assert.fail("Expected NullPointerException");
        }
        catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void persist_companyIdNonPositive_throwsIllegalArgumentException_andDoesNotWrite() {
        NotificationIntakePersister persister = new NotificationIntakePersister(
                ntucSBLocalService, counterLocalService);

        try {
            persister.persist(event, 0L);
            Assert.fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {
            Assert.assertTrue(expected.getMessage().contains("companyId must be > 0"));
        }

        Mockito.verifyNoInteractions(counterLocalService);
        Mockito.verifyNoInteractions(ntucSBLocalService);
    }

    @Test
    public void persist_validInput_mapsFields_setsDefaults_andPersists() {
        NotificationIntakePersister persister = new NotificationIntakePersister(
                ntucSBLocalService, counterLocalService);

        Mockito.when(counterLocalService.increment(NtucSB.class.getName())).thenReturn(101L);
        Mockito.when(ntucSBLocalService.createNtucSB(101L)).thenReturn(sb);
        Mockito.when(ntucSBLocalService.addNtucSB(sb)).thenReturn(sb);

        Mockito.when(event.getCourseCodeSingle()).thenReturn("  C-001  ");
        Mockito.when(event.getCourseTypeSingle()).thenReturn(" TMS ");
        Mockito.when(event.getNotificationId()).thenReturn("  guid-123  ");
        Mockito.when(event.getEventType()).thenReturn("  changed  ");
        Mockito.when(event.getChangeFrom()).thenReturn(Arrays.asList(" venue ", " ", null, "startDate"));
        Mockito.when(event.getTimestamp()).thenReturn("28/12/2025 21:16:00");

        NtucSB result = persister.persist(event, 2001L);
        Assert.assertSame(sb, result);

        // PK allocation and creation happens before any field mapping.
        InOrder inOrder = Mockito.inOrder(counterLocalService, ntucSBLocalService, sb);

        inOrder.verify(counterLocalService).increment(NtucSB.class.getName());
        inOrder.verify(ntucSBLocalService).createNtucSB(101L);

        // Tenant correctness (non-negotiable)
        inOrder.verify(sb).setCompanyId(2001L);

        // Best-effort fields (trimmed)
        inOrder.verify(sb).setCourseCode("C-001");
        inOrder.verify(sb).setCourseType("TMS");
        inOrder.verify(sb).setNotificationId("guid-123");
        inOrder.verify(sb).setEvent("changed");
        inOrder.verify(sb).setChangeFrom("venue,startDate");

        // Dates (systemDate always set, notificationDate parsed)
        ArgumentCaptor<Date> systemDateCaptor = ArgumentCaptor.forClass(Date.class);
        inOrder.verify(sb).setSystemDate(systemDateCaptor.capture());
        Assert.assertNotNull(systemDateCaptor.getValue());

        ArgumentCaptor<Date> notifDateCaptor = ArgumentCaptor.forClass(Date.class);
        inOrder.verify(sb).setNotificationDate(notifDateCaptor.capture());
        Assert.assertNotNull(notifDateCaptor.getValue());

        // Initial processing state
        inOrder.verify(sb).setProcessingStatus("RECEIVED");
        inOrder.verify(sb).setCourseStatus("");
        inOrder.verify(sb).setIsCriticalProcessed(false);
        inOrder.verify(sb).setIsNonCriticalProcessed(false);
        inOrder.verify(sb).setIsCronProcessed(false);

        // HARD RULE: always retryable
        inOrder.verify(sb).setCanRetry(true);

        // Retry counters / lock flags
        inOrder.verify(sb).setLastRetried(null);
        inOrder.verify(sb).setTotalRetries(0);
        inOrder.verify(sb).setIsRowLockFailed(false);

        inOrder.verify(ntucSBLocalService).addNtucSB(sb);

        Mockito.verifyNoMoreInteractions(counterLocalService, ntucSBLocalService, sb);
    }

    @Test
    public void persist_invalidTimestamp_setsNotificationDateNull_andStillPersists() {
        NotificationIntakePersister persister = new NotificationIntakePersister(
                ntucSBLocalService, counterLocalService);

        Mockito.when(counterLocalService.increment(NtucSB.class.getName())).thenReturn(55L);
        Mockito.when(ntucSBLocalService.createNtucSB(55L)).thenReturn(sb);
        Mockito.when(ntucSBLocalService.addNtucSB(sb)).thenReturn(sb);

        Mockito.when(event.getCourseCodeSingle()).thenReturn("C-002");
        Mockito.when(event.getCourseTypeSingle()).thenReturn("TMS");
        Mockito.when(event.getNotificationId()).thenReturn("guid-999");
        Mockito.when(event.getEventType()).thenReturn("published");
        Mockito.when(event.getChangeFrom()).thenReturn(Collections.emptyList());

        // Invalid per strict dd/MM/yyyy HH:mm:ss
        Mockito.when(event.getTimestamp()).thenReturn("2025-12-28T21:16:00");

        persister.persist(event, 3001L);

        Mockito.verify(sb).setNotificationDate(Mockito.isNull());
        Mockito.verify(ntucSBLocalService).addNtucSB(sb);
    }

    @Test
    public void persist_blankTimestamp_setsNotificationDateNull() {
        NotificationIntakePersister persister = new NotificationIntakePersister(
                ntucSBLocalService, counterLocalService);

        Mockito.when(counterLocalService.increment(NtucSB.class.getName())).thenReturn(77L);
        Mockito.when(ntucSBLocalService.createNtucSB(77L)).thenReturn(sb);
        Mockito.when(ntucSBLocalService.addNtucSB(sb)).thenReturn(sb);

        Mockito.when(event.getCourseCodeSingle()).thenReturn("C-003");
        Mockito.when(event.getCourseTypeSingle()).thenReturn("TMS");
        Mockito.when(event.getNotificationId()).thenReturn("guid-777");
        Mockito.when(event.getEventType()).thenReturn("inactive");
        Mockito.when(event.getChangeFrom()).thenReturn(null);

        Mockito.when(event.getTimestamp()).thenReturn("   ");

        persister.persist(event, 4001L);

        Mockito.verify(sb).setNotificationDate(Mockito.isNull());
        Mockito.verify(sb).setChangeFrom(""); // null changeFrom list becomes empty string
        Mockito.verify(ntucSBLocalService).addNtucSB(sb);
    }
}
