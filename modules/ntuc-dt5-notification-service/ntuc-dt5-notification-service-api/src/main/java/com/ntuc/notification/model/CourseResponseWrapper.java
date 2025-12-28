package com.ntuc.notification.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponseWrapper {

    private String courseCode;
    private Course course;
    private PricingTable pricingTable;
    private List<Subsidy> subsidy;
    private List<FundingEligibilityCriteria> fundingEligibilityCriteria;

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public PricingTable getPricingTable() { return pricingTable; }
    public void setPricingTable(PricingTable pricingTable) { this.pricingTable = pricingTable; }

    public List<Subsidy> getSubsidy() { return subsidy; }
    public void setSubsidy(List<Subsidy> subsidy) { this.subsidy = subsidy; }

    public List<FundingEligibilityCriteria> getFundingEligibilityCriteria() { return fundingEligibilityCriteria; }
    public void setFundingEligibilityCriteria(List<FundingEligibilityCriteria> fundingEligibilityCriteria) {
        this.fundingEligibilityCriteria = fundingEligibilityCriteria;
    }

    @Override
    public String toString() {
        return "CourseResponseWrapper{" +
            "courseCode='" + courseCode + '\'' +
            ", course=" + course +
            ", pricingTable=" + pricingTable +
            ", subsidy=" + subsidy +
            ", fundingEligibilityCriteria=" + fundingEligibilityCriteria +
            '}';
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Course {
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
        private List<String> outlinePDFS3Url;
        private String certificateObtained;
        private String termsCondition;
        private String intakeOpenTo;
        private String courseCatalogueStatus;
        private String lxpScheduleUrl;
        private CourseCategory courseCategory;
        private CourseType courseType;
        private List<AreaOfInterest> areaOfInterest;
        private String additionalInfo;

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

        public List<String> getOutlinePDFS3Url() { return outlinePDFS3Url; }
        public void setOutlinePDFS3Url(List<String> outlinePDFS3Url) { this.outlinePDFS3Url = outlinePDFS3Url; }

        public String getCertificateObtained() { return certificateObtained; }
        public void setCertificateObtained(String certificateObtained) { this.certificateObtained = certificateObtained; }

        public String getTermsCondition() { return termsCondition; }
        public void setTermsCondition(String termsCondition) { this.termsCondition = termsCondition; }

        public String getIntakeOpenTo() { return intakeOpenTo; }
        public void setIntakeOpenTo(String intakeOpenTo) { this.intakeOpenTo = intakeOpenTo; }

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

        @Override
        public String toString() {
            return "Course{" +
                "courseImgUrls=" + courseImgUrls +
                ", courseName='" + courseName + '\'' +
                ", popular=" + popular +
                ", funded=" + funded +
                ", duration='" + duration + '\'' +
                ", whoAttend='" + whoAttend + '\'' +
                ", courseAssessment='" + courseAssessment + '\'' +
                ", whatForMe='" + whatForMe + '\'' +
                ", courseOverview='" + courseOverview + '\'' +
                ", objective='" + objective + '\'' +
                ", prerequisites=" + prerequisites +
                ", outline='" + outline + '\'' +
                ", outlinePDFS3Url=" + outlinePDFS3Url +
                ", certificateObtained='" + certificateObtained + '\'' +
                ", termsCondition='" + termsCondition + '\'' +
                ", intakeOpenTo='" + intakeOpenTo + '\'' +
                ", courseCatalogueStatus='" + courseCatalogueStatus + '\'' +
                ", lxpScheduleUrl='" + lxpScheduleUrl + '\'' +
                ", courseCategory=" + courseCategory +
                ", courseType=" + courseType +
                ", areaOfInterest=" + areaOfInterest +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
        }

        public List<String> validateRequiredFields() {
            List<String> missing = new ArrayList<>();
            if (courseImgUrls == null || courseImgUrls.isEmpty()) missing.add("courseImgUrls");
            if (courseName == null || courseName.trim().isEmpty()) missing.add("courseName");
            if (popular == null) missing.add("popular");
            if (funded == null) missing.add("funded");
            if (duration == null || duration.trim().isEmpty()) missing.add("duration");
            return missing;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CourseImgUrl {
        private String typeOfImage;
        private String url;

        public String getTypeOfImage() { return typeOfImage; }
        public void setTypeOfImage(String typeOfImage) { this.typeOfImage = typeOfImage; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        @Override
        public String toString() {
            return "CourseImgUrl{" +
                "typeOfImage='" + typeOfImage + '\'' +
                ", url='" + url + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
            return "PricingTable{" +
                "table=" + table +
                ", disclaimer='" + disclaimer + '\'' +
                ", templateType='" + templateType + '\'' +
                '}';
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Row {
            private String eligibilityDescription;
            private List<PricingDetail> pricingDetails;

            public String getEligibilityDescription() { return eligibilityDescription; }
            public void setEligibilityDescription(String eligibilityDescription) { this.eligibilityDescription = eligibilityDescription; }

            public List<PricingDetail> getPricingDetails() { return pricingDetails; }
            public void setPricingDetails(List<PricingDetail> pricingDetails) { this.pricingDetails = pricingDetails; }

            @Override
            public String toString() {
                return "Row{" +
                    "eligibilityDescription='" + eligibilityDescription + '\'' +
                    ", pricingDetails=" + pricingDetails +
                    '}';
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PricingDetail {
        private String sponsorType;
        private String sponsorSubType;
        private Object beforeGST;
        private Object afterGST;

        public String getSponsorType() { return sponsorType; }
        public void setSponsorType(String sponsorType) { this.sponsorType = sponsorType; }

        public String getSponsorSubType() { return sponsorSubType; }
        public void setSponsorSubType(String sponsorSubType) { this.sponsorSubType = sponsorSubType; }

        public Object getBeforeGST() { return beforeGST; }
        public void setBeforeGST(Object beforeGST) { this.beforeGST = beforeGST; }

        public Object getAfterGST() { return afterGST; }
        public void setAfterGST(Object afterGST) { this.afterGST = afterGST; }

        @Override
        public String toString() {
            return "PricingDetail{" +
                "sponsorType='" + sponsorType + '\'' +
                ", sponsorSubType='" + sponsorSubType + '\'' +
                ", beforeGST=" + beforeGST +
                ", afterGST=" + afterGST +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
            return "Prerequisites{" +
                "mer=" + mer +
                ", entryRequirementRemark='" + entryRequirementRemark + '\'' +
                ", files=" + files +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Mer {
        private String operator;
        private List<Condition> conditions;

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }

        public List<Condition> getConditions() { return conditions; }
        public void setConditions(List<Condition> conditions) { this.conditions = conditions; }

        @Override
        public String toString() {
            return "Mer{" +
                "operator='" + operator + '\'' +
                ", conditions=" + conditions +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Condition {
        private String operator;
        private List<SubCondition> subConditions;

        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }

        public List<SubCondition> getSubConditions() { return subConditions; }
        public void setSubConditions(List<SubCondition> subConditions) { this.subConditions = subConditions; }

        @Override
        public String toString() {
            return "Condition{" +
                "operator='" + operator + '\'' +
                ", subConditions=" + subConditions +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SubCondition {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "SubCondition{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FileRef {
        @JsonAlias("title")
        private String fileName;

        @JsonAlias("url")
        private String fileUrl;

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

        @Override
        public String toString() {
            return "FileRef{" +
                "fileName='" + fileName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Subsidy {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "Subsidy{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FundingEligibilityCriteria {
        private String title;
        private String description;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "FundingEligibilityCriteria{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CourseCategory {
        private String categoryId;
        private String categoryName;

        public String getCategoryId() { return categoryId; }
        public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

        @Override
        public String toString() {
            return "CourseCategory{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CourseType {
        private String courseTypeId;
        private String courseTypeName;

        public String getCourseTypeId() { return courseTypeId; }
        public void setCourseTypeId(String courseTypeId) { this.courseTypeId = courseTypeId; }

        public String getCourseTypeName() { return courseTypeName; }
        public void setCourseTypeName(String courseTypeName) { this.courseTypeName = courseTypeName; }

        @Override
        public String toString() {
            return "CourseType{" +
                "courseTypeId='" + courseTypeId + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AreaOfInterest {
        private String areaOfInterestId;
        private String areaOfInterestName;

        public String getAreaOfInterestId() { return areaOfInterestId; }
        public void setAreaOfInterestId(String areaOfInterestId) { this.areaOfInterestId = areaOfInterestId; }

        public String getAreaOfInterestName() { return areaOfInterestName; }
        public void setAreaOfInterestName(String areaOfInterestName) { this.areaOfInterestName = areaOfInterestName; }

        @Override
        public String toString() {
            return "AreaOfInterest{" +
                "areaOfInterestId='" + areaOfInterestId + '\'' +
                ", areaOfInterestName='" + areaOfInterestName + '\'' +
                '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error {
        private String code;
        private String message;
        private List<ErrorDetail> errors;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public List<ErrorDetail> getErrors() { return errors; }
        public void setErrors(List<ErrorDetail> errors) { this.errors = errors; }

        @Override
        public String toString() {
            return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ErrorDetail {
            private String courseCode;
            private String message;

            public String getCourseCode() { return courseCode; }
            public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

            public String getMessage() { return message; }
            public void setMessage(String message) { this.message = message; }

            @Override
            public String toString() {
                return "ErrorDetail{" +
                    "courseCode='" + courseCode + '\'' +
                    ", message='" + message + '\'' +
                    '}';
            }
        }
    }
}
