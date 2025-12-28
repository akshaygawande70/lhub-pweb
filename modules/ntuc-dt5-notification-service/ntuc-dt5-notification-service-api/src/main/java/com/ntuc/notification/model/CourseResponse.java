package com.ntuc.notification.model;

import java.util.List;

/**
 * DTO representing CLS course payload.
 *
 * Pure API contract:
 * - No JSON annotations
 * - No validation/compute logic
 * - No references to service-internal context objects
 */
public class CourseResponse {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "CourseResponse{body=" + body + '}';
    }

    public static class Body {
        private String courseCode;
        private List<CourseImgUrl> courseImgUrls;
        private String courseName;
        private Boolean popular;
        private Boolean funded;
        private String duration;
        private String whoAttend;
        private String courseAssessment;
        private String whatForMe;
        private String courseOverview;
        private String objective;

        private Prerequisites prerequisites;

        private String outline;
        private String outlinePDFS3Url;
        private String certificateObtained;
        private String termsCondition;

        private PricingTable pricingTable;
        private List<Subsidy> subsidy;
        private List<FundingEligibilityCriteria> fundingEligibilityCriteria;

        private String intakeOpenTo;
        private String courseStatus;
        private String courseCatalogueStatus;
        private String lxpScheduleUrl;
        private CourseCategory courseCategory;
        private CourseType courseType;
        private List<AreaOfInterest> areaOfInterest;
        private String additionalInfo;

        // Derived value is allowed as data, but the computation must be done in service
        private Double price;

        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

        public List<CourseImgUrl> getCourseImgUrls() { return courseImgUrls; }
        public void setCourseImgUrls(List<CourseImgUrl> courseImgUrls) { this.courseImgUrls = courseImgUrls; }

        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }

        public Boolean getPopular() { return popular; }
        public void setPopular(Boolean popular) { this.popular = popular; }

        public Boolean getFunded() { return funded; }
        public void setFunded(Boolean funded) { this.funded = funded; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public String getWhoAttend() { return whoAttend; }
        public void setWhoAttend(String whoAttend) { this.whoAttend = whoAttend; }

        public String getCourseAssessment() { return courseAssessment; }
        public void setCourseAssessment(String courseAssessment) { this.courseAssessment = courseAssessment; }

        public String getWhatForMe() { return whatForMe; }
        public void setWhatForMe(String whatForMe) { this.whatForMe = whatForMe; }

        public String getCourseOverview() { return courseOverview; }
        public void setCourseOverview(String courseOverview) { this.courseOverview = courseOverview; }

        public String getObjective() { return objective; }
        public void setObjective(String objective) { this.objective = objective; }

        public Prerequisites getPrerequisites() { return prerequisites; }
        public void setPrerequisites(Prerequisites prerequisites) { this.prerequisites = prerequisites; }

        public String getOutline() { return outline; }
        public void setOutline(String outline) { this.outline = outline; }

        public String getOutlinePDFS3Url() { return outlinePDFS3Url; }
        public void setOutlinePDFS3Url(String outlinePDFS3Url) { this.outlinePDFS3Url = outlinePDFS3Url; }

        public String getCertificateObtained() { return certificateObtained; }
        public void setCertificateObtained(String certificateObtained) { this.certificateObtained = certificateObtained; }

        public String getTermsCondition() { return termsCondition; }
        public void setTermsCondition(String termsCondition) { this.termsCondition = termsCondition; }

        public PricingTable getPricingTable() { return pricingTable; }
        public void setPricingTable(PricingTable pricingTable) { this.pricingTable = pricingTable; }

        public List<Subsidy> getSubsidy() { return subsidy; }
        public void setSubsidy(List<Subsidy> subsidy) { this.subsidy = subsidy; }

        public List<FundingEligibilityCriteria> getFundingEligibilityCriteria() { return fundingEligibilityCriteria; }
        public void setFundingEligibilityCriteria(List<FundingEligibilityCriteria> fundingEligibilityCriteria) {
            this.fundingEligibilityCriteria = fundingEligibilityCriteria;
        }

        public String getIntakeOpenTo() { return intakeOpenTo; }
        public void setIntakeOpenTo(String intakeOpenTo) { this.intakeOpenTo = intakeOpenTo; }

        public String getCourseStatus() { return courseStatus; }
        public void setCourseStatus(String courseStatus) { this.courseStatus = courseStatus; }

        public String getCourseCatalogueStatus() { return courseCatalogueStatus; }
        public void setCourseCatalogueStatus(String courseCatalogueStatus) { this.courseCatalogueStatus = courseCatalogueStatus; }

        public String getLxpScheduleUrl() { return lxpScheduleUrl; }
        public void setLxpScheduleUrl(String lxpScheduleUrl) { this.lxpScheduleUrl = lxpScheduleUrl; }

        public CourseCategory getCourseCategory() { return courseCategory; }
        public void setCourseCategory(CourseCategory courseCategory) { this.courseCategory = courseCategory; }

        public CourseType getCourseType() { return courseType; }
        public void setCourseType(CourseType courseType) { this.courseType = courseType; }

        public List<AreaOfInterest> getAreaOfInterest() { return areaOfInterest; }
        public void setAreaOfInterest(List<AreaOfInterest> areaOfInterest) { this.areaOfInterest = areaOfInterest; }

        public String getAdditionalInfo() { return additionalInfo; }
        public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }

        @Override
        public String toString() {
            return "Body[courseCode=" + courseCode +
                    ", courseImgUrls=" + courseImgUrls +
                    ", courseName=" + courseName +
                    ", popular=" + popular +
                    ", funded=" + funded +
                    ", duration=" + duration +
                    ", whoAttend=" + whoAttend +
                    ", courseAssessment=" + courseAssessment +
                    ", whatForMe=" + whatForMe +
                    ", courseOverview=" + courseOverview +
                    ", objective=" + objective +
                    ", prerequisites=" + prerequisites +
                    ", outline=" + outline +
                    ", outlinePDFS3Url=" + outlinePDFS3Url +
                    ", certificateObtained=" + certificateObtained +
                    ", termsCondition=" + termsCondition +
                    ", pricingTable=" + pricingTable +
                    ", subsidy=" + subsidy +
                    ", fundingEligibilityCriteria=" + fundingEligibilityCriteria +
                    ", intakeOpenTo=" + intakeOpenTo +
                    ", courseStatus=" + courseStatus +
                    ", courseCatalogueStatus=" + courseCatalogueStatus +
                    ", lxpScheduleUrl=" + lxpScheduleUrl +
                    ", courseCategory=" + courseCategory +
                    ", courseType=" + courseType +
                    ", areaOfInterest=" + areaOfInterest +
                    ", additionalInfo=" + additionalInfo +
                    ", price=" + price +
                    "]";
        }
    }

    public static class CourseImgUrl {
        private String typeOfImage;
        private String url;

        public String getTypeOfImage() { return typeOfImage; }
        public void setTypeOfImage(String typeOfImage) { this.typeOfImage = typeOfImage; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        @Override
        public String toString() {
            return "CourseImgUrl{typeOfImage='" + typeOfImage + "', url='" + url + "'}";
        }
    }

    public static class PricingTable {
        private List<Row> table;
        private String disclaimer;
        private String templateType;

        public List<Row> getTable() { return table; }
        public void setTable(List<Row> table) { this.table = table; }

        public String getDisclaimer() { return disclaimer; }
        public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }

        public String getTemplateType() { return templateType; }
        public void setTemplateType(String templateType) { this.templateType = templateType; }

        @Override
        public String toString() {
            return "PricingTable{table=" + table + ", disclaimer='" + disclaimer + "', templateType='" + templateType + "'}";
        }

        public static class Row {
            private String eligibilityDescription;
            private List<PricingDetail> pricingDetails;

            public String getEligibilityDescription() { return eligibilityDescription; }
            public void setEligibilityDescription(String eligibilityDescription) { this.eligibilityDescription = eligibilityDescription; }

            public List<PricingDetail> getPricingDetails() { return pricingDetails; }
            public void setPricingDetails(List<PricingDetail> pricingDetails) { this.pricingDetails = pricingDetails; }

            @Override
            public String toString() {
                return "Row{eligibilityDescription='" + eligibilityDescription + "', pricingDetails=" + pricingDetails + "}";
            }
        }
    }

    public static class PricingDetail {
        private String sponsorType;
        private String sponsorSubType;
        private Double beforeGST;
        private Double afterGST;

        public String getSponsorType() { return sponsorType; }
        public void setSponsorType(String sponsorType) { this.sponsorType = sponsorType; }

        public String getSponsorSubType() { return sponsorSubType; }
        public void setSponsorSubType(String sponsorSubType) { this.sponsorSubType = sponsorSubType; }

        public Double getBeforeGST() { return beforeGST; }
        public void setBeforeGST(Double beforeGST) { this.beforeGST = beforeGST; }

        public Double getAfterGST() { return afterGST; }
        public void setAfterGST(Double afterGST) { this.afterGST = afterGST; }

        @Override
        public String toString() {
            return "PricingDetail{sponsorType='" + sponsorType + "', sponsorSubType='" + sponsorSubType +
                    "', beforeGST=" + beforeGST + ", afterGST=" + afterGST + "}";
        }
    }

    public static class Prerequisites {
        private Mer mer;
        private String entryRequirementRemark;
        private List<FileRef> files;

        public Mer getMer() { return mer; }
        public void setMer(Mer mer) { this.mer = mer; }

        public String getEntryRequirementRemark() { return entryRequirementRemark; }
        public void setEntryRequirementRemark(String entryRequirementRemark) { this.entryRequirementRemark = entryRequirementRemark; }

        public List<FileRef> getFiles() { return files; }
        public void setFiles(List<FileRef> files) { this.files = files; }

        @Override
        public String toString() {
            return "Prerequisites{mer=" + mer + ", entryRequirementRemark='" + entryRequirementRemark + "', files=" + files + "}";
        }
    }

    public static class Mer {
        private String operator;
        private List<Condition> conditions;

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }

        public List<Condition> getConditions() { return conditions; }
        public void setConditions(List<Condition> conditions) { this.conditions = conditions; }

        @Override
        public String toString() {
            return "Mer{operator='" + operator + "', conditions=" + conditions + "}";
        }
    }

    public static class Condition {
        private String operator;
        private List<SubCondition> subConditions;

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }

        public List<SubCondition> getSubConditions() { return subConditions; }
        public void setSubConditions(List<SubCondition> subConditions) { this.subConditions = subConditions; }

        @Override
        public String toString() {
            return "Condition{operator='" + operator + "', subConditions=" + subConditions + "}";
        }
    }

    public static class SubCondition {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "SubCondition{title='" + title + "', description='" + description + "'}";
        }
    }

    public static class FileRef {
        private String fileName;
        private String fileUrl;

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

        @Override
        public String toString() {
            return "FileRef{fileName='" + fileName + "', fileUrl='" + fileUrl + "'}";
        }
    }

    public static class Subsidy {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "Subsidy{title='" + title + "', description='" + description + "'}";
        }
    }

    public static class FundingEligibilityCriteria {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "FundingEligibilityCriteria{title='" + title + "', description='" + description + "'}";
        }
    }

    public static class CourseCategory {
        private String categoryId;
        private String categoryName;

        public String getCategoryId() { return categoryId; }
        public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

        @Override
        public String toString() {
            return "CourseCategory{categoryId='" + categoryId + "', categoryName='" + categoryName + "'}";
        }
    }

    public static class CourseType {
        private String courseTypeId;
        private String courseTypeName;

        public String getCourseTypeId() { return courseTypeId; }
        public void setCourseTypeId(String courseTypeId) { this.courseTypeId = courseTypeId; }

        public String getCourseTypeName() { return courseTypeName; }
        public void setCourseTypeName(String courseTypeName) { this.courseTypeName = courseTypeName; }

        @Override
        public String toString() {
            return "CourseType{courseTypeId='" + courseTypeId + "', courseTypeName='" + courseTypeName + "'}";
        }
    }

    public static class AreaOfInterest {
        private String areaOfInterestId;
        private String areaOfInterestName;

        public String getAreaOfInterestId() { return areaOfInterestId; }
        public void setAreaOfInterestId(String areaOfInterestId) { this.areaOfInterestId = areaOfInterestId; }

        public String getAreaOfInterestName() { return areaOfInterestName; }
        public void setAreaOfInterestName(String areaOfInterestName) { this.areaOfInterestName = areaOfInterestName; }

        @Override
        public String toString() {
            return "AreaOfInterest{areaOfInterestId='" + areaOfInterestId + "', areaOfInterestName='" + areaOfInterestName + "'}";
        }
    }
}
