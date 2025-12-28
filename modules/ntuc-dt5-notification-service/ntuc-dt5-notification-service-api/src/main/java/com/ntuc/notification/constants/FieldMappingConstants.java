package com.ntuc.notification.constants;

import java.util.*;

/**
 * Central mapping between Liferay JournalArticle fields and external course
 * source fields. Supports both forward and reverse lookup with helpers.
 */
public class FieldMappingConstants {

	public static final Map<String, String> LIFERAY_TO_SOURCE_FIELD_MAP;
	public static final Map<String, List<String>> SOURCE_TO_LIFERAY_FIELDS_MAP;

	// Prevent instantiation
	private FieldMappingConstants() {
		throw new UnsupportedOperationException("This class cannot be instantiated");
	}

	private static final Set<String> COMPLEX_FIELDS = new HashSet<>(
			Arrays.asList("pricingTable", "courseImgUrls", "certificateObtained"));

	static {
		Map<String, String> map = new HashMap<>();
		map.put("courseTitle", "courseName");
		map.put("courseCode", "courseCode");
		map.put("clsImage", "courseImgUrls[0].url");
		map.put("popular", "popular");
		map.put("funded", "funded");
		map.put("CourseDuration", "duration");
		map.put("modeofAssesment", "courseAssessment");
		map.put("whoShouldAttend", "whoAttend");
		map.put("detailWhatsInItforMe", "whatForMe");
		map.put("courseOverview.DetailCourseOverview", "courseOverview");
		map.put("courseObjectives.detailCourseObjectives", "objective");
		// map.put("prerequisites.detailPrerequisites", "prerequisites");

		map.put("prerequisites.detailPrerequisites.files", "prerequisites.files");
		map.put("prerequisites.detailPrerequisites.mer", "prerequisites.mer");
		map.put("prerequisites.detailPrerequisites.entryRequirementRemark", "prerequisites.entryRequirementRemark");
		map.put("courseOutline.detailCourseOutline", "outline");
		map.put("certificateObtainedAndConferredBy.detailCertificateObtainedAndConferredBy", "certificateObtained");
		map.put("additionalDetails.detailAdditionalDetails", "additionalInfo");
		map.put("termsCondition", "termsCondition");
		map.put("price", "price");
		map.put("pricingTable", "pricingTable");
		map.put("courseSchedule", "lxpScheduleUrl");
		map.put("startDate", "");
		map.put("endDate", "");
		map.put("venue", "");
		map.put("batchID", "");
		map.put("noStudentEnrolled", "");
		map.put("fundingEligibilityCriteria", "fundingEligibilityCriteria");
		map.put("subsidy", "subsidy");

		// Extras
		map.put("outlinePDFS3Url", "outlinePDFS3Url");
		map.put("intakeOpenTo", "intakeOpenTo");
		map.put("courseCatalogueStatus", "courseCatalogueStatus");
		map.put("courseCategory.categoryId", "courseCategory.categoryId");
		map.put("courseCategory.categoryName", "courseCategory.categoryName");
		map.put("courseType.courseTypeId", "courseType.courseTypeId");
		map.put("courseType.courseTypeName", "courseType.courseTypeName");
		map.put("areaOfInterest", "areaOfInterest");

		// Schedule fields/
		map.put("schedule_intakeNumber", "intakeNumber");
		map.put("schedule_startDate", "startDate");
//      map.put("schedule_courseSchedule", "courseSchedule");
		map.put("schedule_endDate", "endDate");
		map.put("schedule_duration.hours", "duration.hours");
		map.put("schedule_duration.minutes", "duration.minutes");
		map.put("schedule_availability", "availability");
		map.put("schedule_venue", "venue");
		map.put("schedule_description", "description");
		map.put("schedule_availablePax", "availablePax");
		map.put("schedule_availableWaitlist", "availableWaitlist");
		map.put("schedule_lxpBuyUrl", "lxpBuyUrl");
		map.put("schedule_lxpRoiUrl", "lxpRoiUrl");
		map.put("schedule_importantNote", "importantNote");
		map.put("schedule_scheduleDownloadUrl", "scheduleDownloadUrl");

		LIFERAY_TO_SOURCE_FIELD_MAP = Collections.unmodifiableMap(map);

		// Build reverse mapping
		Map<String, List<String>> reverseMap = new HashMap<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String liferayField = entry.getKey();
			String sourceField = entry.getValue();

			if (sourceField != null && !sourceField.isEmpty()) {
				reverseMap.computeIfAbsent(sourceField, k -> new ArrayList<>()).add(liferayField);
			}
		}

		Map<String, List<String>> finalReverseMap = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : reverseMap.entrySet()) {
			finalReverseMap.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}

		SOURCE_TO_LIFERAY_FIELDS_MAP = Collections.unmodifiableMap(finalReverseMap);
	}

	/**
	 * Returns the external JSON field path for a given Liferay journal field.
	 */
	public static String getSourceField(String liferayField) {
		return LIFERAY_TO_SOURCE_FIELD_MAP.get(liferayField);
	}

	/**
	 * Returns the list of Liferay journal fields that map to a given external JSON
	 * field path.
	 */
	public static List<String> getLiferayFields(String sourceField) {
		return SOURCE_TO_LIFERAY_FIELDS_MAP.getOrDefault(sourceField, Collections.emptyList());
	}

	/**
	 * Indicates whether a given source field is complex and may require special
	 * handling.
	 */
	public static boolean isComplexField(String sourceField) {
		return COMPLEX_FIELDS.contains(sourceField);
	}

	/**
	 * Extracts nested values from a Map based on dot-separated paths like
	 * "a.b[0].c".
	 */
	public static Object getValueFromPath(Map<String, Object> map, String path) {
		if (map == null || path == null || path.isEmpty())
			return null;

		String[] keys = path.split("\\.");
		Object current = map;

		for (String key : keys) {
			if (current == null)
				return null;

			if (key.contains("[")) {
				int idxStart = key.indexOf('[');
				String listKey = key.substring(0, idxStart);
				int index;
				try {
					index = Integer.parseInt(key.substring(idxStart + 1, key.indexOf(']')));
				} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
					return null;
				}

				if (!(current instanceof Map))
					return null;
				Object listObj = ((Map<?, ?>) current).get(listKey);
				if (!(listObj instanceof List))
					return null;
				List<?> lst = (List<?>) listObj;
				if (index < 0 || index >= lst.size())
					return null;
				current = lst.get(index);
			} else {
				if (current instanceof Map) {
					current = ((Map<?, ?>) current).get(key);
				} else {
					return null;
				}
			}
		}

		return current;
	}
}
