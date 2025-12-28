package com.ntuc.notification.rest.internal.processor.validation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlockingRecordViewTest {

    @Test
    public void constructor_setsAllFields() {
        BlockingRecordView view = new BlockingRecordView(123L, "PROCESSING");

        assertEquals(123L, view.getNtucDTId());
        assertEquals("PROCESSING", view.getProcessingStatus());
    }
}
