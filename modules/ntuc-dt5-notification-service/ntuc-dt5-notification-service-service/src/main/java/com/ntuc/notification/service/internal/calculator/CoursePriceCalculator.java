package com.ntuc.notification.service.internal.calculator;

import com.ntuc.notification.model.CourseResponse;

import java.util.List;

/**
 * Computes derived values for CourseResponse.
 *
 * Plain Java -> unit-testable without OSGi.
 */
public class CoursePriceCalculator {

    public Double computePrice(CourseResponse.Body body) {
        if (body == null) {
            return null;
        }

        CourseResponse.PricingTable pricingTable = body.getPricingTable();
        if (pricingTable == null) {
            return null;
        }

        List<CourseResponse.PricingTable.Row> rows = pricingTable.getTable();
        if (rows == null) {
            return null;
        }

        for (CourseResponse.PricingTable.Row row : rows) {
            if (row == null || row.getPricingDetails() == null) {
                continue;
            }

            String elig = (row.getEligibilityDescription() == null) ? "" : row.getEligibilityDescription().toLowerCase();
            if (!elig.contains("full course fee")) {
                continue;
            }

            for (CourseResponse.PricingDetail detail : row.getPricingDetails()) {
                if (detail != null && detail.getBeforeGST() != null) {
                    return detail.getBeforeGST();
                }
            }
        }

        return null;
    }

    public void computeAndSetPrice(CourseResponse.Body body) {
        body.setPrice(computePrice(body));
    }
}
