package com.ntuc.notification.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps CourseResponseWrapper into CourseResponse.
 *
 * Pure mapping utility:
 * - No OSGi
 * - No Liferay services
 * - Safe for unit testing
 *
 * Non-negotiable rule:
 * - Body.price MUST always be derived from PricingDetail.beforeGST ONLY.
 * - afterGST is never used (not even as fallback).
 */
public final class CourseResponseMapper {

    private static final String FULL_COURSE_FEE_KEY = "full course fee";

    private CourseResponseMapper() {
        // Utility class
    }

    public static CourseResponse mapToCourseResponse(CourseResponseWrapper wrapper) {
        if (wrapper == null) {
            return null;
        }

        CourseResponse response = new CourseResponse();
        CourseResponse.Body body = new CourseResponse.Body();

        // Map basic fields
        body.setCourseCode(wrapper.getCourseCode());
        body.setPricingTable(mapPricingTable(wrapper.getPricingTable()));
        body.setSubsidy(mapSubsidies(wrapper.getSubsidy()));
        body.setFundingEligibilityCriteria(mapFundingEligibilityCriteria(wrapper.getFundingEligibilityCriteria()));

        // Map course details if they exist
        if (wrapper.getCourse() != null) {
            CourseResponseWrapper.Course wrapperCourse = wrapper.getCourse();

            body.setCourseImgUrls(mapCourseImgUrls(wrapperCourse.getCourseImgUrls()));
            body.setCourseName(wrapperCourse.getCourseName());
            body.setPopular(wrapperCourse.getPopular());
            body.setFunded(wrapperCourse.getFunded());
            body.setDuration(wrapperCourse.getDuration());
            body.setWhoAttend(wrapperCourse.getWhoAttend());
            body.setCourseAssessment(wrapperCourse.getCourseAssessment());
            body.setWhatForMe(wrapperCourse.getWhatForMe());
            body.setCourseOverview(wrapperCourse.getCourseOverview());
            body.setObjective(wrapperCourse.getObjective());
            body.setPrerequisites(mapPrerequisites(wrapperCourse.getPrerequisites()));
            body.setOutline(wrapperCourse.getOutline());

            // outlinePDFS3Url: wrapper uses List<String>, API expects String
            body.setOutlinePDFS3Url(firstOrNull(wrapperCourse.getOutlinePDFS3Url()));

            body.setCertificateObtained(wrapperCourse.getCertificateObtained());
            body.setTermsCondition(wrapperCourse.getTermsCondition());
            body.setIntakeOpenTo(wrapperCourse.getIntakeOpenTo());
            body.setCourseCatalogueStatus(wrapperCourse.getCourseCatalogueStatus());
            body.setLxpScheduleUrl(wrapperCourse.getLxpScheduleUrl());
            body.setCourseCategory(mapCourseCategory(wrapperCourse.getCourseCategory()));
            body.setCourseType(mapCourseType(wrapperCourse.getCourseType()));
            body.setAreaOfInterest(mapAreasOfInterest(wrapperCourse.getAreaOfInterest()));
            body.setAdditionalInfo(wrapperCourse.getAdditionalInfo());

            // Derived price: strictly beforeGST only.
            body.setPrice(derivePriceFromPricingTable(wrapper.getPricingTable()));
        }

        response.setBody(body);
        return response;
    }

    /**
     * Derives "price" from PricingTable.
     *
     * Rule (non-negotiable):
     * - Price is derived ONLY from PricingDetail.beforeGST.
     * - afterGST is never used (not even as fallback).
     *
     * Strategy:
     * 1) Prefer the row where eligibilityDescription contains "full course fee" (case-insensitive).
     * 2) If not found, fallback to the first parseable beforeGST anywhere in the table.
     */
    static Double derivePriceFromPricingTable(CourseResponseWrapper.PricingTable pricingTable) {
        if (pricingTable == null || pricingTable.getTable() == null || pricingTable.getTable().isEmpty()) {
            return null;
        }

        // 1) Preferred: "full course fee" row
        Double preferred = deriveFromFullCourseFeeRow(pricingTable);
        if (preferred != null) {
            return preferred;
        }

        // 2) Fallback: first beforeGST anywhere (still beforeGST-only)
        return deriveFromFirstBeforeGstAnywhere(pricingTable);
    }

    private static Double deriveFromFullCourseFeeRow(CourseResponseWrapper.PricingTable pricingTable) {
        for (CourseResponseWrapper.PricingTable.Row row : pricingTable.getTable()) {
            if (row == null || row.getPricingDetails() == null || row.getPricingDetails().isEmpty()) {
                continue;
            }

            String elig = (row.getEligibilityDescription() == null)
                ? ""
                : row.getEligibilityDescription().toLowerCase();

            if (!elig.contains(FULL_COURSE_FEE_KEY)) {
                continue;
            }

            for (CourseResponseWrapper.PricingDetail detail : row.getPricingDetails()) {
                if (detail == null) {
                    continue;
                }

                Double price = parsePriceToDouble(detail.getBeforeGST());
                if (price != null) {
                    return price;
                }
            }
        }

        return null;
    }

    private static Double deriveFromFirstBeforeGstAnywhere(CourseResponseWrapper.PricingTable pricingTable) {
        for (CourseResponseWrapper.PricingTable.Row row : pricingTable.getTable()) {
            if (row == null || row.getPricingDetails() == null || row.getPricingDetails().isEmpty()) {
                continue;
            }

            for (CourseResponseWrapper.PricingDetail detail : row.getPricingDetails()) {
                if (detail == null) {
                    continue;
                }

                Double price = parsePriceToDouble(detail.getBeforeGST());
                if (price != null) {
                    return price;
                }
            }
        }

        return null;
    }

    private static String firstOrNull(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    private static List<CourseResponse.CourseImgUrl> mapCourseImgUrls(List<CourseResponseWrapper.CourseImgUrl> wrapperUrls) {
        if (wrapperUrls == null) {
            return null;
        }

        List<CourseResponse.CourseImgUrl> result = new ArrayList<>();

        for (CourseResponseWrapper.CourseImgUrl wrapperUrl : wrapperUrls) {
            if (wrapperUrl == null) {
                continue;
            }

            CourseResponse.CourseImgUrl url = new CourseResponse.CourseImgUrl();
            url.setTypeOfImage(wrapperUrl.getTypeOfImage());
            url.setUrl(wrapperUrl.getUrl());
            result.add(url);
        }

        return result;
    }

    private static CourseResponse.PricingTable mapPricingTable(CourseResponseWrapper.PricingTable wrapperTable) {
        if (wrapperTable == null) {
            return null;
        }

        CourseResponse.PricingTable table = new CourseResponse.PricingTable();
        table.setDisclaimer(wrapperTable.getDisclaimer());
        table.setTemplateType(wrapperTable.getTemplateType());
        table.setTable(mapPricingRows(wrapperTable.getTable()));
        return table;
    }

    private static List<CourseResponse.PricingTable.Row> mapPricingRows(List<CourseResponseWrapper.PricingTable.Row> wrapperRows) {
        if (wrapperRows == null) {
            return null;
        }

        List<CourseResponse.PricingTable.Row> result = new ArrayList<>();

        for (CourseResponseWrapper.PricingTable.Row wrapperRow : wrapperRows) {
            if (wrapperRow == null) {
                continue;
            }

            CourseResponse.PricingTable.Row row = new CourseResponse.PricingTable.Row();
            row.setEligibilityDescription(wrapperRow.getEligibilityDescription());
            row.setPricingDetails(mapPricingDetails(wrapperRow.getPricingDetails()));
            result.add(row);
        }

        return result;
    }

    private static List<CourseResponse.PricingDetail> mapPricingDetails(List<CourseResponseWrapper.PricingDetail> wrapperDetails) {
        if (wrapperDetails == null) {
            return null;
        }

        List<CourseResponse.PricingDetail> result = new ArrayList<>();

        for (CourseResponseWrapper.PricingDetail wrapperDetail : wrapperDetails) {
            if (wrapperDetail == null) {
                continue;
            }

            CourseResponse.PricingDetail detail = new CourseResponse.PricingDetail();
            detail.setSponsorType(wrapperDetail.getSponsorType());
            detail.setSponsorSubType(wrapperDetail.getSponsorSubType());

            // In response payload we still expose both numeric values.
            // But derived "price" uses beforeGST only.
            detail.setBeforeGST(parsePriceToDouble(wrapperDetail.getBeforeGST()));
            detail.setAfterGST(parsePriceToDouble(wrapperDetail.getAfterGST()));

            result.add(detail);
        }

        return result;
    }

    /**
     * Parses numeric values safely.
     * - Number => doubleValue
     * - String => accepts "1,234.50", "$1,234.50", "SGD 1234.50"
     */
    static Double parsePriceToDouble(Object priceValue) {
        if (priceValue == null) {
            return null;
        }

        if (priceValue instanceof Number) {
            return ((Number) priceValue).doubleValue();
        }

        if (priceValue instanceof String) {
            String s = ((String) priceValue).trim();
            if (s.isEmpty()) {
                return null;
            }

            // Remove currency/labels, keep digits, commas, dot
            s = s.replaceAll("[^0-9,\\.]", "");

            // Normalize commas (thousands separators)
            s = s.replace(",", "");

            if (s.isEmpty() || ".".equals(s)) {
                return null;
            }

            try {
                return new BigDecimal(s).doubleValue();
            }
            catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    private static CourseResponse.Prerequisites mapPrerequisites(CourseResponseWrapper.Prerequisites wrapperPrereq) {
        if (wrapperPrereq == null) {
            return null;
        }

        CourseResponse.Prerequisites prereq = new CourseResponse.Prerequisites();
        prereq.setMer(mapMer(wrapperPrereq.getMer()));
        prereq.setEntryRequirementRemark(wrapperPrereq.getEntryRequirementRemark());
        prereq.setFiles(mapFileRefs(wrapperPrereq.getFiles()));
        return prereq;
    }

    private static CourseResponse.Mer mapMer(CourseResponseWrapper.Mer wrapperMer) {
        if (wrapperMer == null) {
            return null;
        }

        CourseResponse.Mer mer = new CourseResponse.Mer();
        mer.setOperator(wrapperMer.getOperator());
        mer.setConditions(mapConditions(wrapperMer.getConditions()));
        return mer;
    }

    private static List<CourseResponse.Condition> mapConditions(List<CourseResponseWrapper.Condition> wrapperConditions) {
        if (wrapperConditions == null) {
            return null;
        }

        List<CourseResponse.Condition> result = new ArrayList<>();

        for (CourseResponseWrapper.Condition wrapperCondition : wrapperConditions) {
            if (wrapperCondition == null) {
                continue;
            }

            CourseResponse.Condition condition = new CourseResponse.Condition();
            condition.setOperator(wrapperCondition.getOperator());
            condition.setSubConditions(mapSubConditions(wrapperCondition.getSubConditions()));
            result.add(condition);
        }

        return result;
    }

    private static List<CourseResponse.SubCondition> mapSubConditions(List<CourseResponseWrapper.SubCondition> wrapperSubConditions) {
        if (wrapperSubConditions == null) {
            return null;
        }

        List<CourseResponse.SubCondition> result = new ArrayList<>();

        for (CourseResponseWrapper.SubCondition wrapperSubCondition : wrapperSubConditions) {
            if (wrapperSubCondition == null) {
                continue;
            }

            CourseResponse.SubCondition subCondition = new CourseResponse.SubCondition();
            subCondition.setTitle(wrapperSubCondition.getTitle());
            subCondition.setDescription(wrapperSubCondition.getDescription());
            result.add(subCondition);
        }

        return result;
    }

    private static List<CourseResponse.FileRef> mapFileRefs(List<CourseResponseWrapper.FileRef> wrapperFiles) {
        if (wrapperFiles == null) {
            return null;
        }

        List<CourseResponse.FileRef> result = new ArrayList<>();

        for (CourseResponseWrapper.FileRef wrapperFile : wrapperFiles) {
            if (wrapperFile == null) {
                continue;
            }

            CourseResponse.FileRef fileRef = new CourseResponse.FileRef();
            fileRef.setFileName(wrapperFile.getFileName());
            fileRef.setFileUrl(wrapperFile.getFileUrl());
            result.add(fileRef);
        }

        return result;
    }

    private static List<CourseResponse.Subsidy> mapSubsidies(List<CourseResponseWrapper.Subsidy> wrapperSubsidies) {
        if (wrapperSubsidies == null) {
            return null;
        }

        List<CourseResponse.Subsidy> result = new ArrayList<>();

        for (CourseResponseWrapper.Subsidy wrapperSubsidy : wrapperSubsidies) {
            if (wrapperSubsidy == null) {
                continue;
            }

            CourseResponse.Subsidy subsidy = new CourseResponse.Subsidy();
            subsidy.setTitle(wrapperSubsidy.getTitle());
            subsidy.setDescription(wrapperSubsidy.getDescription());
            result.add(subsidy);
        }

        return result;
    }

    private static List<CourseResponse.FundingEligibilityCriteria> mapFundingEligibilityCriteria(
        List<CourseResponseWrapper.FundingEligibilityCriteria> wrapperCriteria) {

        if (wrapperCriteria == null) {
            return null;
        }

        List<CourseResponse.FundingEligibilityCriteria> result = new ArrayList<>();

        for (CourseResponseWrapper.FundingEligibilityCriteria wrapperCriterion : wrapperCriteria) {
            if (wrapperCriterion == null) {
                continue;
            }

            CourseResponse.FundingEligibilityCriteria criterion = new CourseResponse.FundingEligibilityCriteria();
            criterion.setTitle(wrapperCriterion.getTitle());
            criterion.setDescription(wrapperCriterion.getDescription());
            result.add(criterion);
        }

        return result;
    }

    private static CourseResponse.CourseCategory mapCourseCategory(CourseResponseWrapper.CourseCategory wrapperCategory) {
        if (wrapperCategory == null) {
            return null;
        }

        CourseResponse.CourseCategory category = new CourseResponse.CourseCategory();
        category.setCategoryId(wrapperCategory.getCategoryId());
        category.setCategoryName(wrapperCategory.getCategoryName());
        return category;
    }

    private static CourseResponse.CourseType mapCourseType(CourseResponseWrapper.CourseType wrapperType) {
        if (wrapperType == null) {
            return null;
        }

        CourseResponse.CourseType type = new CourseResponse.CourseType();
        type.setCourseTypeId(wrapperType.getCourseTypeId());
        type.setCourseTypeName(wrapperType.getCourseTypeName());
        return type;
    }

    private static List<CourseResponse.AreaOfInterest> mapAreasOfInterest(List<CourseResponseWrapper.AreaOfInterest> wrapperAreas) {
        if (wrapperAreas == null) {
            return null;
        }

        List<CourseResponse.AreaOfInterest> result = new ArrayList<>();

        for (CourseResponseWrapper.AreaOfInterest wrapperArea : wrapperAreas) {
            if (wrapperArea == null) {
                continue;
            }

            CourseResponse.AreaOfInterest area = new CourseResponse.AreaOfInterest();
            area.setAreaOfInterestId(wrapperArea.getAreaOfInterestId());
            area.setAreaOfInterestName(wrapperArea.getAreaOfInterestName());
            result.add(area);
        }

        return result;
    }
}
