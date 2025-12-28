<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ include file="/init.jsp" %>

<liferay-theme:defineObjects />

<link rel="stylesheet"
      href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap4.min.css" />

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap4.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<style>
    .cs-table-actions { white-space: nowrap; }

    .row-updated-highlight {
        animation: glow 1.5s ease-out 0s 2;
        background-color: #d2f8d2 !important;
    }

    @keyframes glow {
        0% { background-color: #c0f7c0; }
        100% { background-color: #ffffff; }
    }

    .cs-pager {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 12px;
        margin: 10px 0;
    }

    .cs-pager .cs-meta {
        font-size: 12px;
        color: #666;
    }

    .cs-pager .btn-group .btn {
        min-width: 84px;
    }

    /* Hard override: if some theme/plugin forces .modal { display:block }, prevent hidden modals from blocking clicks */
    body #csEditModal.modal,
    body #csDetailsModal.modal {
        display: none;
        pointer-events: none;
    }

    body #csEditModal.modal.show,
    body #csDetailsModal.modal.show {
        display: block;
        pointer-events: auto;
    }

    /* --- Added: validation UX --- */
    .invalid-feedback { display: none; }
    .is-invalid + .invalid-feedback { display: block; }
</style>

<portlet:resourceURL id="/dt5/update_schedule" var="updateScheduleAjaxURL" />

<portlet:renderURL var="csPrevUrl">
    <portlet:param name="cur" value="${courseSchedulesCur - 1}" />
    <portlet:param name="delta" value="${courseSchedulesDelta}" />
</portlet:renderURL>

<portlet:renderURL var="csNextUrl">
    <portlet:param name="cur" value="${courseSchedulesCur + 1}" />
    <portlet:param name="delta" value="${courseSchedulesDelta}" />
</portlet:renderURL>

<div class="container-fluid mt-3">
    <h3>Course Schedule Snapshot</h3>

    <c:set var="csTotal" value="${empty courseSchedulesTotal ? 0 : courseSchedulesTotal}" />
    <c:set var="csCur" value="${empty courseSchedulesCur ? 1 : courseSchedulesCur}" />
    <c:set var="csDelta" value="${empty courseSchedulesDelta ? 25 : courseSchedulesDelta}" />
    <c:set var="csStart" value="${empty courseSchedulesStart ? 0 : courseSchedulesStart}" />
    <c:set var="csEnd" value="${empty courseSchedulesEnd ? 0 : courseSchedulesEnd}" />

    <div class="cs-pager">
        <div class="cs-meta">
            <c:choose>
                <c:when test="${csTotal <= 0}">
                    Showing 0 of 0
                </c:when>
                <c:otherwise>
                    Showing ${csStart + 1}–${csEnd} of ${csTotal}
                    (Page ${csCur}, ${csDelta}/page)
                </c:otherwise>
            </c:choose>
        </div>

        <div class="btn-group" role="group" aria-label="Pagination">
            <c:set var="csHasPrev" value="${csCur > 1}" />
            <c:set var="csHasNext" value="${csEnd < csTotal}" />

            <a class="btn btn-sm btn-outline-secondary ${csHasPrev ? '' : 'disabled'}"
               href="${csHasPrev ? csPrevUrl : '#'}"
               <c:if test="${not csHasPrev}">aria-disabled="true" tabindex="-1"</c:if>>
                Prev
            </a>

            <a class="btn btn-sm btn-outline-secondary ${csHasNext ? '' : 'disabled'}"
               href="${csHasNext ? csNextUrl : '#'}"
               <c:if test="${not csHasNext}">aria-disabled="true" tabindex="-1"</c:if>>
                Next
            </a>
        </div>
    </div>

    <c:if test="${empty courseSchedules}">
        <div class="alert alert-info mt-3">No schedules found.</div>
    </c:if>

    <c:if test="${not empty courseSchedules}">
        <div class="table-responsive mt-3">
            <table id="courseScheduleTable"
                   class="table table-striped table-bordered table-sm"
                   style="width: 100%;">
                <thead class="thead-light">
                <tr>
                    <th>#</th>
                    <th>Course Code</th>
                    <th>Intake No</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Availability</th>
                    <th>Venue</th>
                    <th>Modified Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="schedule" items="${courseSchedules}" varStatus="i">

                    <fmt:formatDate value="${schedule.startDate}" pattern="dd/MM/yyyy HH:mm" var="startStr" />
                    <fmt:formatDate value="${schedule.endDate}" pattern="dd/MM/yyyy HH:mm" var="endStr" />
                    <fmt:formatDate value="${schedule.modifiedDate}" pattern="dd/MM/yyyy HH:mm:ss" var="modifiedStr" />

                    <%
                        Object _o = pageContext.findAttribute("schedule");

                        com.ntuc.notification.model.CourseSchedule _s = null;
                        if (_o instanceof com.ntuc.notification.model.CourseSchedule) {
                            _s = (com.ntuc.notification.model.CourseSchedule)_o;
                        }

                        String courseCodeRaw = (_s == null) ? "" : String.valueOf(_s.getCourseCode());
                        String intakeRaw = (_s == null) ? "" : String.valueOf(_s.getIntakeNumber());
                        String venueRaw = (_s == null) ? "" : String.valueOf(_s.getVenue());
                        String paxRaw = (_s == null) ? "" : String.valueOf(_s.getAvailablePax()); // YES/NO/blank field
                        String waitlistRaw = (_s == null) ? "" : String.valueOf(_s.getAvailableWaitlist()); // YES/NO/blank field
                        String buyUrlRaw = (_s == null) ? "" : String.valueOf(_s.getLxpBuyUrl());
                        String dlUrlRaw = (_s == null) ? "" : String.valueOf(_s.getScheduleDownloadUrl());
                        String errorCodeRaw = (_s == null) ? "" : String.valueOf(_s.getErrorCode());
                        String errorMsgRaw = (_s == null) ? "" : String.valueOf(_s.getErrorMessage());

                        String availabilityRaw = (_s == null) ? "" : String.valueOf(_s.getAvailability());
                        String durationHoursRaw = (_s == null) ? "" : String.valueOf(_s.getDurationHours());
                        String durationMinutesRaw = (_s == null) ? "" : String.valueOf(_s.getDurationMinutes());

                        String _courseCodeAttr = HtmlUtil.escapeAttribute(courseCodeRaw);
                        String _intakeAttr = HtmlUtil.escapeAttribute(intakeRaw);
                        String _venueAttr = HtmlUtil.escapeAttribute(venueRaw);
                        String _paxAttr = HtmlUtil.escapeAttribute(paxRaw);
                        String _waitlistAttr = HtmlUtil.escapeAttribute(waitlistRaw);
                        String _buyUrlAttr = HtmlUtil.escapeAttribute(buyUrlRaw);
                        String _dlUrlAttr = HtmlUtil.escapeAttribute(dlUrlRaw);
                        String _errorCodeAttr = HtmlUtil.escapeAttribute(errorCodeRaw);
                        String _errorMsgAttr = HtmlUtil.escapeAttribute(errorMsgRaw);

                        String _availabilityAttr = HtmlUtil.escapeAttribute(availabilityRaw);
                        String _durationHoursAttr = HtmlUtil.escapeAttribute(durationHoursRaw);
                        String _durationMinutesAttr = HtmlUtil.escapeAttribute(durationMinutesRaw);

                        String _startAttr = HtmlUtil.escapeAttribute(String.valueOf(pageContext.getAttribute("startStr")));
                        String _endAttr = HtmlUtil.escapeAttribute(String.valueOf(pageContext.getAttribute("endStr")));
                        String _modifiedAttr = HtmlUtil.escapeAttribute(String.valueOf(pageContext.getAttribute("modifiedStr")));
                    %>

                    <tr>
                        <td>${i.index + 1}</td>
                        <td>${schedule.courseCode}</td>
                        <td>${schedule.intakeNumber}</td>
                        <td>${startStr}</td>
                        <td>${endStr}</td>
                        <td>${schedule.availability}</td>
                        <td>${schedule.venue}</td>

                        <td class="cs-modified" data-order="${schedule.modifiedDate.time}">
                            ${modifiedStr}
                        </td>

                        <td class="cs-table-actions">

                            <button type="button"
                                    class="btn btn-sm btn-outline-primary btn-view"
                                    data-schedule-id="${schedule.courseScheduleId}"
                                    data-course-code="<%=_courseCodeAttr%>"
                                    data-intake="<%=_intakeAttr%>"
                                    data-start="<%=_startAttr%>"
                                    data-end="<%=_endAttr%>"
                                    data-availability="<%=_availabilityAttr%>"
                                    data-venue="<%=_venueAttr%>"
                                    data-duration-hours="<%=_durationHoursAttr%>"
                                    data-duration-min="<%=_durationMinutesAttr%>"
                                    data-pax="<%=_paxAttr%>"
                                    data-waitlist="<%=_waitlistAttr%>"
                                    data-buyurl="<%=_buyUrlAttr%>"
                                    data-dlurl="<%=_dlUrlAttr%>"
                                    data-errorcode="<%=_errorCodeAttr%>"
                                    data-errormsg="<%=_errorMsgAttr%>"
                                    data-modified="<%=_modifiedAttr%>">
                                View
                            </button>

                            <button type="button"
                                    class="btn btn-sm btn-primary btn-edit ml-1"
                                    data-schedule-id="${schedule.courseScheduleId}"
                                    data-course-code="<%=_courseCodeAttr%>"
                                    data-intake="<%=_intakeAttr%>"
                                    data-start="<%=_startAttr%>"
                                    data-end="<%=_endAttr%>"
                                    data-availability="<%=_availabilityAttr%>"
                                    data-venue="<%=_venueAttr%>"
                                    data-duration-hours="<%=_durationHoursAttr%>"
                                    data-duration-min="<%=_durationMinutesAttr%>"
                                    data-pax="<%=_paxAttr%>"
                                    data-waitlist="<%=_waitlistAttr%>"
                                    data-buyurl="<%=_buyUrlAttr%>"
                                    data-dlurl="<%=_dlUrlAttr%>"
                                    data-errorcode="<%=_errorCodeAttr%>"
                                    data-errormsg="<%=_errorMsgAttr%>">
                                Edit
                            </button>

                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </c:if>
</div>

<!-- View details modal -->
<div class="modal fade" id="csDetailsModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable" role="document">
        <div class="modal-content">
            <div class="modal-header py-2">
                <h5 class="modal-title mb-0" id="csDetailsTitle">Schedule details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body p-2">
                <table class="table table-sm table-bordered mb-0">
                    <tbody id="csDetailsBody"></tbody>
                </table>
            </div>
            <div class="modal-footer py-2">
                <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">
                    Close
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Edit modal (AJAX submit) -->
<div class="modal fade" id="csEditModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable" role="document">
        <div class="modal-content">
            <form action="" method="post" id="csEditForm">
                <div class="modal-header py-2">
                    <h5 class="modal-title mb-0">Edit schedule</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">

                    <input type="hidden"
                           id="csIdField"
                           name="<portlet:namespace />courseScheduleId" />

                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label class="small mb-1">Course Code</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="courseCodeField"
                                   name="<portlet:namespace />courseCode"
                                   readonly />
                        </div>
                        <div class="form-group col-md-4">
                            <label class="small mb-1">Intake No</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="intakeField"
                                   name="<portlet:namespace />intakeNumber"
                                   readonly/>
                        </div>
                        <div class="form-group col-md-4">
                            <label class="small mb-1">Availability</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="availabilityField"
                                   name="<portlet:namespace />availability" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label class="small mb-1">Start Date (dd/MM/yyyy HH:mm)</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="startField"
                                   name="<portlet:namespace />startDate" />
                            <div class="invalid-feedback">Invalid format. Use dd/MM/yyyy HH:mm (e.g., 28/12/2025 09:30).</div>
                        </div>
                        <div class="form-group col-md-6">
                            <label class="small mb-1">End Date (dd/MM/yyyy HH:mm)</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="endField"
                                   name="<portlet:namespace />endDate" />
                            <div class="invalid-feedback">Invalid format. Use dd/MM/yyyy HH:mm (e.g., 28/12/2025 18:00).</div>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label class="small mb-1">Venue</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="venueField"
                                   name="<portlet:namespace />venue" />
                        </div>
                        <div class="form-group col-md-3">
                            <label class="small mb-1">Duration (hours)</label>
                            <input type="number"
                                   class="form-control form-control-sm"
                                   id="durationHoursField"
                                   name="<portlet:namespace />durationHours" />
                        </div>
                        <div class="form-group col-md-3">
                            <label class="small mb-1">Duration (minutes)</label>
                            <input type="number"
                                   class="form-control form-control-sm"
                                   id="durationMinutesField"
                                   name="<portlet:namespace />durationMinutes" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-3">
                            <label class="small mb-1">Available Pax</label>
                            <select class="form-control form-control-sm"
                                    id="paxField"
                                    name="<portlet:namespace />availablePax">
                                <option value="">—</option>
                                <option value="YES">YES</option>
                                <option value="NO">NO</option>
                            </select>
                        </div>

                        <div class="form-group col-md-3">
                            <label class="small mb-1">Available Waitlist</label>
                            <select class="form-control form-control-sm"
                                    id="waitlistField"
                                    name="<portlet:namespace />availableWaitlist">
                                <option value="">—</option>
                                <option value="YES">YES</option>
                                <option value="NO">NO</option>
                            </select>
                        </div>

                        <div class="form-group col-md-6">
                            <label class="small mb-1">LXP Buy URL</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="buyUrlField"
                                   name="<portlet:namespace />lxpBuyUrl" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="small mb-1">Schedule Download URL</label>
                        <input type="text"
                               class="form-control form-control-sm"
                               id="dlUrlField"
                               name="<portlet:namespace />scheduleDownloadUrl" />
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label class="small mb-1">Error Code</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="errorCodeField"
                                   name="<portlet:namespace />errorCode"
                                   readonly />
                        </div>
                        <div class="form-group col-md-8">
                            <label class="small mb-1">Error Message</label>
                            <input type="text"
                                   class="form-control form-control-sm"
                                   id="errorMsgField"
                                   name="<portlet:namespace />errorMessage"
                                   readonly />
                        </div>
                    </div>

                </div>

                <div class="modal-footer py-2">
                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">
                        Cancel
                    </button>
                    <button type="submit" class="btn btn-primary btn-sm">
                        Save changes
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    $(function () {

        const MODIFIED_COL = 7;

        const table = $('#courseScheduleTable').DataTable({
            paging: false,
            info: false,
            order: [[MODIFIED_COL, 'desc']]
        });

        const $detailsModal = $('#csDetailsModal');
        const $detailsBody = $('#csDetailsBody');
        const $detailsTitle = $('#csDetailsTitle');

        function showToast(message, type) {
            if (window.Liferay && Liferay.Util && Liferay.Util.openToast) {
                Liferay.Util.openToast({ message: message, type: type || 'success' });
            } else {
                alert(message);
            }
        }

        function normalizeYesNo(v) {
            if (v === undefined || v === null) return '';
            const s = String(v).trim().toUpperCase();
            if (s === '') return '';
            if (s === 'YES' || s === 'Y' || s === 'TRUE' || s === '1') return 'YES';
            if (s === 'NO'  || s === 'N' || s === 'FALSE' || s === '0') return 'NO';
            return '';
        }

        function isSafeHttpUrl(raw) {
            if (!raw) return false;
            try {
                const u = new URL(String(raw), window.location.href);
                return u.protocol === 'http:' || u.protocol === 'https:';
            } catch (e) {
                return false;
            }
        }

        function addViewRow(label, rawValue, isLink) {
            const $tr = $('<tr>');
            const $th = $('<th>').addClass('w-25').text(label);
            const $td = $('<td>');

            const v = (rawValue === undefined || rawValue === null || rawValue === '') ? '—' : String(rawValue);

            if (isLink && rawValue && isSafeHttpUrl(rawValue)) {
                $td.append(
                    $('<a>')
                        .attr('href', String(rawValue))
                        .attr('target', '_blank')
                        .attr('rel', 'noopener noreferrer')
                        .text(String(rawValue))
                );
            } else {
                $td.text(v);
            }

            $tr.append($th).append($td);
            $detailsBody.append($tr);
        }

        function updateButtonData($btn, data) {
            $btn.data('course-code', data.courseCode || '');
            $btn.data('intake', data.intakeNumber || '');
            $btn.data('start', data.startDate || '');
            $btn.data('end', data.endDate || '');
            $btn.data('availability', data.availability || '');
            $btn.data('venue', data.venue || '');
            $btn.data('duration-hours', data.durationHours || '');
            $btn.data('duration-min', data.durationMinutes || '');
            $btn.data('pax', normalizeYesNo(data.availablePax));
            $btn.data('waitlist', normalizeYesNo(data.availableWaitlist));
            $btn.data('buyurl', data.lxpBuyUrl || '');
            $btn.data('dlurl', data.scheduleDownloadUrl || '');
            $btn.data('errorcode', data.errorCode || '');
            $btn.data('errormsg', data.errorMessage || '');
            if (data.modifiedDateDisplay) {
                $btn.data('modified', data.modifiedDateDisplay);
            }
        }

        function highlightRow($row) {
            $row.addClass('row-updated-highlight');
            setTimeout(function () { $row.removeClass('row-updated-highlight'); }, 3000);
        }

        /* ------------------------------------------------------------------
           Added: strict dd/MM/yyyy HH:mm validation (calendar-valid + range)
           ------------------------------------------------------------------ */

        function clearInvalid($el) {
            $el.removeClass('is-invalid');
        }

        function markInvalid($el) {
            $el.addClass('is-invalid');
        }

        // Returns a Date object in local timezone if valid; otherwise null.
        // Strict rules:
        // - exact format dd/MM/yyyy HH:mm
        // - valid calendar date
        // - valid time 00:00..23:59
        function parseDdMmYyyyHhMmStrict(raw) {
            if (!raw) return null;

            const s = String(raw).trim();

            // Exact: 2/2/4 digits + space + 2/2 digits
            const m = /^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})$/.exec(s);
            if (!m) return null;

            const dd = parseInt(m[1], 10);
            const mm = parseInt(m[2], 10);
            const yyyy = parseInt(m[3], 10);
            const HH = parseInt(m[4], 10);
            const min = parseInt(m[5], 10);

            if (mm < 1 || mm > 12) return null;
            if (dd < 1 || dd > 31) return null;
            if (HH < 0 || HH > 23) return null;
            if (min < 0 || min > 59) return null;

            // Create date and re-check components (catches 31/02, 29/02 non-leap, etc.)
            const d = new Date(yyyy, mm - 1, dd, HH, min, 0, 0);
            if (
                d.getFullYear() !== yyyy ||
                d.getMonth() !== (mm - 1) ||
                d.getDate() !== dd ||
                d.getHours() !== HH ||
                d.getMinutes() !== min
            ) {
                return null;
            }

            return d;
        }

        function validateStartEnd() {
            const $start = $('#startField');
            const $end = $('#endField');

            clearInvalid($start);
            clearInvalid($end);

            const startRaw = $start.val();
            const endRaw = $end.val();

            const startDt = parseDdMmYyyyHhMmStrict(startRaw);
            const endDt = parseDdMmYyyyHhMmStrict(endRaw);

            let ok = true;

            if (!startDt) {
                markInvalid($start);
                ok = false;
            }

            if (!endDt) {
                markInvalid($end);
                ok = false;
            }

            if (ok && startDt.getTime() > endDt.getTime()) {
                markInvalid($start);
                markInvalid($end);
                showToast('Start Date must be earlier than or equal to End Date.', 'danger');
                return false;
            }

            if (!ok) {
                showToast('Invalid date/time format. Use dd/MM/yyyy HH:mm (e.g., 28/12/2025 09:30).', 'danger');
                return false;
            }

            return true;
        }

        // Live validation on blur (optional but helpful)
        $('#startField, #endField').on('blur', function () {
            validateStartEnd();
        });

        /* ------------------------------------------------------------------ */

        $('#courseScheduleTable').on('click', '.btn-view', function () {
            const btn = $(this);
            $detailsBody.empty();

            const courseCode = btn.data('course-code') || '';
            const intake = btn.data('intake') || '';
            $detailsTitle.text(courseCode ? (intake ? courseCode + ' - ' + intake : courseCode) : 'Schedule details');

            const durationText =
                (btn.data('duration-hours') || '') + 'h ' +
                (btn.data('duration-min') || '') + 'm';

            addViewRow('Course Code', courseCode, false);
            addViewRow('Intake No', intake, false);
            addViewRow('Start Date', btn.data('start'), false);
            addViewRow('End Date', btn.data('end'), false);
            addViewRow('Availability', btn.data('availability'), false);
            addViewRow('Venue', btn.data('venue'), false);
            addViewRow('Duration', durationText, false);
            addViewRow('Available Pax', normalizeYesNo(btn.data('pax')), false);
            addViewRow('Available Waitlist', normalizeYesNo(btn.data('waitlist')), false);
            addViewRow('LXP Buy URL', btn.data('buyurl'), true);
            addViewRow('Schedule Download URL', btn.data('dlurl'), true);
            addViewRow('Error Code', btn.data('errorcode'), false);
            addViewRow('Error Message', btn.data('errormsg'), false);
            addViewRow('Modified Date', btn.data('modified'), false);

            $detailsModal.modal('show');
        });

        $('#courseScheduleTable').on('click', '.btn-edit', function () {
            const btn = $(this);

            $('#csIdField').val(btn.data('schedule-id') || '');
            $('#courseCodeField').val(btn.data('course-code') || '');
            $('#intakeField').val(btn.data('intake') || '');
            $('#availabilityField').val(btn.data('availability') || '');
            $('#startField').val(btn.data('start') || '');
            $('#endField').val(btn.data('end') || '');
            $('#venueField').val(btn.data('venue') || '');
            $('#durationHoursField').val(btn.data('duration-hours') || '');
            $('#durationMinutesField').val(btn.data('duration-min') || '');

            $('#paxField').val(normalizeYesNo(btn.data('pax')));
            $('#waitlistField').val(normalizeYesNo(btn.data('waitlist')));

            $('#buyUrlField').val(btn.data('buyurl') || '');
            $('#dlUrlField').val(btn.data('dlurl') || '');
            $('#errorCodeField').val(btn.data('errorcode') || '');
            $('#errorMsgField').val(btn.data('errormsg') || '');

            // Reset invalid markers when opening modal
            clearInvalid($('#startField'));
            clearInvalid($('#endField'));

            $('#csEditModal').modal('show');
        });

        $('#csEditForm').on('submit', function (e) {
            e.preventDefault();

            // Added: block AJAX submit on invalid start/end
            if (!validateStartEnd()) {
                return;
            }

            $.ajax({
                url: '${updateScheduleAjaxURL}',
                method: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function (resp) {

                    if (!(resp && resp.success)) {
                        showToast((resp && resp.message) || 'Failed to update schedule.', 'danger');
                        return;
                    }

                    $('#csEditModal').modal('hide');

                    const scheduleId = String(resp.courseScheduleId || '');

                    const $editBtn = $('#courseScheduleTable')
                        .find('.btn-edit[data-schedule-id="' + scheduleId + '"]');

                    const $row = $editBtn.closest('tr');

                    if (!$row.length) {
                        showToast('Updated, but row not found in this page window (try refresh).', 'warning');
                        return;
                    }

                    const updated = {
                        courseScheduleId: scheduleId,
                        courseCode: resp.courseCode,
                        intakeNumber: resp.intakeNumber,
                        startDate: resp.startDate,
                        endDate: resp.endDate,
                        availability: resp.availability,
                        venue: resp.venue,
                        durationHours: resp.durationHours,
                        durationMinutes: resp.durationMinutes,
                        availablePax: normalizeYesNo(resp.availablePax),
                        availableWaitlist: normalizeYesNo(resp.availableWaitlist),
                        lxpBuyUrl: resp.lxpBuyUrl,
                        scheduleDownloadUrl: resp.scheduleDownloadUrl,
                        errorCode: resp.errorCode,
                        errorMessage: resp.errorMessage,
                        modifiedDateDisplay: resp.modifiedDateDisplay,
                        modifiedDateOrder: resp.modifiedDateOrder
                    };

                    $row.find('td').eq(1).text(updated.courseCode || '');
                    $row.find('td').eq(2).text(updated.intakeNumber || '');
                    $row.find('td').eq(3).text(updated.startDate || '');
                    $row.find('td').eq(4).text(updated.endDate || '');
                    $row.find('td').eq(5).text(updated.availability !== undefined && updated.availability !== null ? String(updated.availability) : '');
                    $row.find('td').eq(6).text(updated.venue || '');

                    if (updated.modifiedDateDisplay) {
                        const $modifiedTd = $row.find('td').eq(MODIFIED_COL);
                        $modifiedTd.text(updated.modifiedDateDisplay);
                        if (updated.modifiedDateOrder) {
                            $modifiedTd.attr('data-order', String(updated.modifiedDateOrder));
                        }
                    }

                    const $viewBtn = $row.find('.btn-view');
                    updateButtonData($viewBtn, updated);
                    updateButtonData($editBtn, updated);

                    table.row($row).invalidate();
                    table.order([MODIFIED_COL, 'desc']).draw(false);

                    highlightRow($row);
                    showToast('Schedule updated successfully.', 'success');
                },
                error: function () {
                    showToast('Error while updating schedule.', 'danger');
                }
            });
        });

    });
</script>
