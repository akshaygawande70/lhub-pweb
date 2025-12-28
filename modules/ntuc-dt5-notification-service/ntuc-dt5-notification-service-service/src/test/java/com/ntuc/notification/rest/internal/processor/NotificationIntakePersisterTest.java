package com.ntuc.notification.rest.internal.processor;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.model.NtucSB;
import com.ntuc.notification.service.NtucSBLocalService;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationIntakePersisterTest {

    @Mock private NtucSBLocalService ntucSBLocalService;
    @Mock private CounterLocalService counterLocalService;

    @Test
    public void persist_setsCompanyId_canRetryTrue_andMapsFields() {
        when(counterLocalService.increment(anyString())).thenReturn(9999L);

        NtucSB created = mock(NtucSB.class);
        when(ntucSBLocalService.createNtucSB(9999L)).thenReturn(created);

        when(ntucSBLocalService.addNtucSB(any(NtucSB.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        NotificationIntakePersister p =
                new NotificationIntakePersister(ntucSBLocalService, counterLocalService);

        CourseEvent e = new CourseEvent();
        e.setNotificationId("ff1feb90-63bb-49b8-bf57-3703bfced789");
        e.setEventType("published");
        e.setTimestamp("13/04/2025 10:30:15");
        e.setCourseCodeSingle("SFDW021");
        e.setCourseTypeSingle("TMS");
        e.setChangeFrom(Collections.singletonList("course"));

        long companyId = 20097L;

        NtucSB saved = p.persist(e, companyId);

        Assert.assertNotNull(saved);

        verify(counterLocalService, times(1)).increment(anyString());
        verify(ntucSBLocalService, times(1)).createNtucSB(9999L);
        verify(ntucSBLocalService, times(1)).addNtucSB(created);

        verify(created, times(1)).setCompanyId(companyId);
        verify(created, atLeastOnce()).setCanRetry(true);

        verify(created, atLeastOnce()).setNotificationId("ff1feb90-63bb-49b8-bf57-3703bfced789");
        verify(created, atLeastOnce()).setEvent("published");
        verify(created, atLeastOnce()).setCourseCode("SFDW021");
        verify(created, atLeastOnce()).setCourseType("TMS");
        verify(created, atLeastOnce()).setChangeFrom("course");
    }

    @Test
    public void persist_companyIdZero_throwsIllegalArgumentException() {
        NotificationIntakePersister p =
                new NotificationIntakePersister(ntucSBLocalService, counterLocalService);

        CourseEvent e = new CourseEvent();
        e.setCourseCodeSingle("SFDW021");

        try {
            p.persist(e, 0L);
            Assert.fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("companyId"));
        }

        verify(counterLocalService, never()).increment(anyString());
        verify(ntucSBLocalService, never()).createNtucSB(anyLong());
        verify(ntucSBLocalService, never()).addNtucSB(any(NtucSB.class));
    }
}
