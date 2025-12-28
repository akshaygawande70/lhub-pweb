package com.ntuc.notification.service.internal.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntuc.notification.model.CourseResponse;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoursePayloadJsonCodecTest {

    private final CoursePayloadJsonCodec codec = new CoursePayloadJsonCodec();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void toJson_nulls_safe() {
        assertEquals("", codec.toJson(null, null));
        assertEquals("", codec.toJson(mapper, null));
    }

    @Test
    public void fromJson_blank_returnsNull() {
        assertNull(codec.fromJson(mapper, null));
        assertNull(codec.fromJson(mapper, ""));
        assertNull(codec.fromJson(mapper, "   "));
    }

    @Test
    public void roundTrip_basic() {
        CourseResponse cr = new CourseResponse();

        // CourseResponse shape varies; we only validate JSON roundtrip doesn't crash.
        String json = codec.toJson(mapper, cr);
        assertNotNull(json);

        CourseResponse back = codec.fromJson(mapper, json);
        assertNotNull(back);
    }

    @Test
    public void fromJson_invalid_returnsNull() {
        assertNull(codec.fromJson(mapper, "{not-json"));
    }
}
