package com.ntuc.notification.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String intakeNumber;
    private final String startDate;
    private final String endDate;
    private final Duration duration;
    private final Integer availability;
    private final String venue;
    private final String description;
    private final String availablePax;
    private final String availableWaitlist;
    private final String lxpBuyUrl;
    private final String lxpRoiUrl;
    private final String importantNote;        // v0.6
    private final String scheduleDownloadUrl;  // v0.6
    private final Error error;

    /**
     * Runtime flag - exposed in API response.
     * If true, caller knows schedule was served from cache.
     */
    @JsonProperty("fromCache")
    private boolean fromCache;

    /**
     * Primary JSON constructor used by Jackson.
     */
    @JsonCreator
    public ScheduleResponse(
        @JsonProperty("intakeNumber") String intakeNumber,
        @JsonProperty("startDate") String startDate,
        @JsonProperty("endDate") String endDate,
        @JsonProperty("duration") Duration duration,
        @JsonProperty("availability") Integer availability,
        @JsonProperty("venue") String venue,
        @JsonProperty("description") String description,
        @JsonProperty("availablePax") String availablePax,
        @JsonProperty("availableWaitlist") String availableWaitlist,
        @JsonProperty("lxpBuyUrl") String lxpBuyUrl,
        @JsonProperty("lxpRoiUrl") String lxpRoiUrl,
        @JsonProperty("importantNote") String importantNote,
        @JsonProperty("scheduleDownloadUrl") String scheduleDownloadUrl,
        @JsonProperty("error") Error error,
        @JsonProperty("fromCache") Boolean fromCache
    ) {
        this.intakeNumber = intakeNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.availability = availability;
        this.venue = venue;
        this.description = description;
        this.availablePax = availablePax;
        this.availableWaitlist = availableWaitlist;
        this.lxpBuyUrl = lxpBuyUrl;
        this.lxpRoiUrl = lxpRoiUrl;
        this.importantNote = importantNote;
        this.scheduleDownloadUrl = scheduleDownloadUrl;
        this.error = error;

        // Default false if not provided
        this.fromCache = (fromCache != null) && fromCache.booleanValue();
    }

    /**
     * Backward-compatible constructor (no fromCache).
     * Use this from service code when building DTOs manually.
     */
    public ScheduleResponse(
        String intakeNumber,
        String startDate,
        String endDate,
        Duration duration,
        Integer availability,
        String venue,
        String description,
        String availablePax,
        String availableWaitlist,
        String lxpBuyUrl,
        String lxpRoiUrl,
        String importantNote,
        String scheduleDownloadUrl,
        Error error
    ) {
        this(
            intakeNumber,
            startDate,
            endDate,
            duration,
            availability,
            venue,
            description,
            availablePax,
            availableWaitlist,
            lxpBuyUrl,
            lxpRoiUrl,
            importantNote,
            scheduleDownloadUrl,
            error,
            null
        );
    }

    public String getIntakeNumber() { return intakeNumber; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public Duration getDuration() { return duration; }
    public Integer getAvailability() { return availability; }
    public String getVenue() { return venue; }
    public String getDescription() { return description; }
    public String getAvailablePax() { return availablePax; }
    public String getAvailableWaitlist() { return availableWaitlist; }
    public String getLxpBuyUrl() { return lxpBuyUrl; }
    public String getLxpRoiUrl() { return lxpRoiUrl; }
    public String getImportantNote() { return importantNote; }
    public String getScheduleDownloadUrl() { return scheduleDownloadUrl; }
    public Error getError() { return error; }

    @JsonProperty("fromCache")
    public boolean isFromCache() {
        return fromCache;
    }

    public void setFromCache(boolean fromCache) {
        this.fromCache = fromCache;
    }

    @Override
    public String toString() {
        return "ScheduleResponse{" +
            "intakeNumber='" + intakeNumber + '\'' +
            ", startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            ", duration=" + duration +
            ", availability=" + availability +
            ", venue='" + venue + '\'' +
            ", description='" + description + '\'' +
            ", availablePax='" + availablePax + '\'' +
            ", availableWaitlist='" + availableWaitlist + '\'' +
            ", lxpBuyUrl='" + lxpBuyUrl + '\'' +
            ", lxpRoiUrl='" + lxpRoiUrl + '\'' +
            ", importantNote='" + importantNote + '\'' +
            ", scheduleDownloadUrl='" + scheduleDownloadUrl + '\'' +
            ", error=" + error +
            ", fromCache=" + fromCache +
            '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Duration implements Serializable {
        private static final long serialVersionUID = 1L;

        private final Integer hours;
        private final Integer minutes;

        @JsonCreator
        public Duration(
            @JsonProperty("hours") Integer hours,
            @JsonProperty("minutes") Integer minutes
        ) {
            this.hours = hours;
            this.minutes = minutes;
        }

        public Integer getHours() { return hours; }
        public Integer getMinutes() { return minutes; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String code;
        private final String message;

        @JsonCreator
        public Error(
            @JsonProperty("code") String code,
            @JsonProperty("message") String message
        ) {
            this.code = code;
            this.message = message;
        }

        public String getCode() { return code; }
        public String getMessage() { return message; }
    }
}
