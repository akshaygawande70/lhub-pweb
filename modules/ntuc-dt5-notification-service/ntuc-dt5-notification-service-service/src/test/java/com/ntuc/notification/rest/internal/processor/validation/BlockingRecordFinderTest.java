package com.ntuc.notification.rest.internal.processor.validation;

import com.ntuc.notification.constants.NotificationType;
import com.ntuc.notification.constants.ProcessingStatusConstants;
import com.ntuc.notification.model.CourseEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlockingRecordFinderTest {

    @Test
    public void published_processingRecord_blocks_whenNotCurrent() {
        BlockingRecordLookup lookup = mock(BlockingRecordLookup.class);

        CourseEvent event = new CourseEvent();
        event.setEventType(NotificationType.PUBLISHED);
        event.setCourseCodeSingle("C1");

        when(lookup.fetchLatestByCourseAndEvent("C1", NotificationType.PUBLISHED, 99L))
            .thenReturn(Optional.of(new BlockingRecordView(10L, ProcessingStatusConstants.PROCESSING)));

        Optional<BlockingRecordView> result =
            BlockingRecordFinder.findBlockingRecord(99L, event, lookup);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getNtucDTId());
        verify(lookup).fetchLatestByCourseAndEvent("C1", NotificationType.PUBLISHED, 99L);
        verifyNoMoreInteractions(lookup);
    }

    @Test
    public void published_processingRecord_doesNotBlock_whenSameDtId() {
        BlockingRecordLookup lookup = mock(BlockingRecordLookup.class);

        CourseEvent event = new CourseEvent();
        event.setEventType(NotificationType.PUBLISHED);
        event.setCourseCodeSingle("C1");

        when(lookup.fetchLatestByCourseAndEvent("C1", NotificationType.PUBLISHED, 10L))
            .thenReturn(Optional.of(new BlockingRecordView(10L, ProcessingStatusConstants.PROCESSING)));

        Optional<BlockingRecordView> result =
            BlockingRecordFinder.findBlockingRecord(10L, event, lookup);

        assertFalse(result.isPresent());
    }

    @Test
    public void published_nonProcessing_doesNotBlock() {
        BlockingRecordLookup lookup = mock(BlockingRecordLookup.class);

        CourseEvent event = new CourseEvent();
        event.setEventType(NotificationType.PUBLISHED);
        event.setCourseCodeSingle("C1");

        when(lookup.fetchLatestByCourseAndEvent("C1", NotificationType.PUBLISHED, 99L))
            .thenReturn(Optional.of(new BlockingRecordView(10L, "DONE")));

        Optional<BlockingRecordView> result =
            BlockingRecordFinder.findBlockingRecord(99L, event, lookup);

        assertFalse(result.isPresent());
    }

    @Test
    public void changed_blocks_onFirstMatchingChangeFromProcessing() {
        BlockingRecordLookup lookup = mock(BlockingRecordLookup.class);

        CourseEvent event = new CourseEvent();
        event.setEventType(NotificationType.CHANGED);
        event.setCourseCodeSingle("C1");
        event.setChangeFrom(Arrays.asList("COURSE", "SUBSIDY"));

        // First changeFrom returns empty, second returns blocking PROCESSING record
        when(lookup.fetchLatestByCourseEventAndChangeFrom("C1", NotificationType.CHANGED, "COURSE", 99L))
            .thenReturn(Optional.empty());
        when(lookup.fetchLatestByCourseEventAndChangeFrom("C1", NotificationType.CHANGED, "SUBSIDY", 99L))
            .thenReturn(Optional.of(new BlockingRecordView(77L, ProcessingStatusConstants.PROCESSING)));

        Optional<BlockingRecordView> result =
            BlockingRecordFinder.findBlockingRecord(99L, event, lookup);

        assertTrue(result.isPresent());
        assertEquals(77L, result.get().getNtucDTId());
    }

    @Test
    public void changed_doesNotBlock_whenChangeFromNull() {
        BlockingRecordLookup lookup = mock(BlockingRecordLookup.class);

        CourseEvent event = new CourseEvent();
        event.setEventType(NotificationType.CHANGED);
        event.setCourseCodeSingle("C1");
        event.setChangeFrom(null);

        Optional<BlockingRecordView> result =
            BlockingRecordFinder.findBlockingRecord(99L, event, lookup);

        assertFalse(result.isPresent());
        verifyNoInteractions(lookup);
    }

    @Test
    public void unsupportedEventType_returnsEmpty() {
        BlockingRecordLookup lookup = mock(BlockingRecordLookup.class);

        CourseEvent event = new CourseEvent();
        event.setEventType("SOMETHING_ELSE");
        event.setCourseCodeSingle("C1");

        Optional<BlockingRecordView> result =
            BlockingRecordFinder.findBlockingRecord(99L, event, lookup);

        assertFalse(result.isPresent());
        verifyNoInteractions(lookup);
    }
}
