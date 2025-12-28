package com.ntuc.notification.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class CourseResponseMapperTest {

    @Test
    public void derivePrice_fullCourseFeeRow_beforeGstReturned() {
        CourseResponseWrapper.PricingDetail d = new CourseResponseWrapper.PricingDetail();
        d.setBeforeGST("100.00");
        d.setAfterGST("999.99");

        CourseResponseWrapper.PricingTable.Row row = new CourseResponseWrapper.PricingTable.Row();
        row.setEligibilityDescription("Full Course Fee - Individual");
        row.setPricingDetails(Collections.singletonList(d));

        CourseResponseWrapper.PricingTable table = new CourseResponseWrapper.PricingTable();
        table.setTable(Collections.singletonList(row));

        assertEquals(100.0, CourseResponseMapper.derivePriceFromPricingTable(table), 0.000001);
    }

    @Test
    public void derivePrice_noFullCourseFeeRow_fallsBackToFirstBeforeGstAnywhere() {
        CourseResponseWrapper.PricingDetail d1 = new CourseResponseWrapper.PricingDetail();
        d1.setBeforeGST(null);

        CourseResponseWrapper.PricingDetail d2 = new CourseResponseWrapper.PricingDetail();
        d2.setBeforeGST("250");

        CourseResponseWrapper.PricingTable.Row row = new CourseResponseWrapper.PricingTable.Row();
        row.setEligibilityDescription("Some Other Fee");
        row.setPricingDetails(Arrays.asList(d1, d2));

        CourseResponseWrapper.PricingTable table = new CourseResponseWrapper.PricingTable();
        table.setTable(Collections.singletonList(row));

        assertEquals(250.0, CourseResponseMapper.derivePriceFromPricingTable(table), 0.000001);
    }

    @Test
    public void derivePrice_beforeGstNull_afterGstPresent_priceStillNull() {
        CourseResponseWrapper.PricingDetail d = new CourseResponseWrapper.PricingDetail();
        d.setBeforeGST(null);
        d.setAfterGST("123.45");

        CourseResponseWrapper.PricingTable.Row row = new CourseResponseWrapper.PricingTable.Row();
        row.setEligibilityDescription("Full Course Fee");
        row.setPricingDetails(Collections.singletonList(d));

        CourseResponseWrapper.PricingTable table = new CourseResponseWrapper.PricingTable();
        table.setTable(Collections.singletonList(row));

        assertNull(CourseResponseMapper.derivePriceFromPricingTable(table));
    }

    @Test
    public void parsePriceToDouble_currencyAndCommaString_parses() {
        assertEquals(1234.50, CourseResponseMapper.parsePriceToDouble("$1,234.50"), 0.000001);
    }

    @Test
    public void mapToCourseResponse_setsBodyPrice_fromBeforeGstOnly() {
        CourseResponseWrapper.Course c = new CourseResponseWrapper.Course();
        c.setCourseName("X");

        CourseResponseWrapper.PricingDetail d = new CourseResponseWrapper.PricingDetail();
        d.setBeforeGST("88.88");
        d.setAfterGST("99.99");

        CourseResponseWrapper.PricingTable.Row row = new CourseResponseWrapper.PricingTable.Row();
        row.setEligibilityDescription("Full Course Fee");
        row.setPricingDetails(Collections.singletonList(d));

        CourseResponseWrapper.PricingTable table = new CourseResponseWrapper.PricingTable();
        table.setTable(Collections.singletonList(row));

        CourseResponseWrapper w = new CourseResponseWrapper();
        w.setCourseCode("C1");
        w.setCourse(c);
        w.setPricingTable(table);

        CourseResponse resp = CourseResponseMapper.mapToCourseResponse(w);

        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(88.88, resp.getBody().getPrice(), 0.000001);
    }
}
