<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.portal.kernel.theme.ThemeDisplay" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>

<portlet:defineObjects />

<%
    ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
    String NS = renderResponse.getNamespace();

    String ntucDtIdFilter  = ParamUtil.getString(request, NS + "ntucDTId", ParamUtil.getString(request, "ntucDTId"));

    String categoryFilter = ParamUtil.getString(request, NS + "category", ParamUtil.getString(request, "category"));
    String statusFilter   = ParamUtil.getString(request, NS + "status",   ParamUtil.getString(request, "status"));
    String severityFilter = ParamUtil.getString(request, NS + "severity", ParamUtil.getString(request, "severity"));
    String stepFilter     = ParamUtil.getString(request, NS + "step",     ParamUtil.getString(request, "step"));
    String qFilter        = ParamUtil.getString(request, NS + "q",        ParamUtil.getString(request, "q"));

    String fromParam      = ParamUtil.getString(request, NS + "from",     ParamUtil.getString(request, "from"));
    String toParam        = ParamUtil.getString(request, NS + "to",       ParamUtil.getString(request, "to"));

    java.time.ZoneId tz = (themeDisplay != null && themeDisplay.getTimeZone() != null)
        ? themeDisplay.getTimeZone().toZoneId()
        : java.time.ZoneId.systemDefault();

    java.time.LocalDate todayLD = java.time.ZonedDateTime.now(tz).toLocalDate();

    if (Validator.isNull(fromParam)) fromParam = todayLD.toString();
    if (Validator.isNull(toParam))   toParam   = todayLD.toString();

    java.util.List<String> categories = (java.util.List<String>) request.getAttribute("categories");
    java.util.List<String> statuses   = (java.util.List<String>) request.getAttribute("statuses");
    java.util.List<String> severities = (java.util.List<String>) request.getAttribute("severities");
    java.util.List<String> steps      = (java.util.List<String>) request.getAttribute("steps");

    if (categories == null) categories = java.util.Collections.emptyList();
    if (statuses == null) statuses = java.util.Collections.emptyList();
    if (severities == null) severities = java.util.Collections.emptyList();
    if (steps == null) steps = java.util.Collections.emptyList();
%>

<portlet:resourceURL id="/auditlog/datatables" var="dataTablesURL" />
<portlet:resourceURL id="auditLogDetails" var="detailsURL" />

<link rel="stylesheet" href="https://cdn.datatables.net/1.13.8/css/jquery.dataTables.min.css" />
<script src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>

<style>
  .audit-chip {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 4px 10px;
    border: 1px solid #ddd;
    border-radius: 999px;
    background: #f8f9fa;
    font-size: 12px;
    margin: 3px 6px 3px 0;
    max-width: 100%;
  }
  .audit-chip .k { opacity: .75; }
  .audit-chip .v { font-weight: 600; max-width: 520px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
  .audit-hint { font-size: 12px; opacity: .75; margin-top: 6px; }
  pre.audit-json {
    white-space: pre-wrap;
    word-break: break-word;
    background: #0b1020;
    color: #e6e6e6;
    padding: 12px;
    border-radius: 10px;
    max-height: 60vh;
    overflow: auto;
    font-size: 12px;
  }

#<portlet:namespace/>auditDetailsModal:not(.show) {
  display: none !important;
  pointer-events: none !important;
}

#<portlet:namespace/>auditDetailsModal.show {
  display: block !important;
  pointer-events: auto !important;
}

#<portlet:namespace/>auditDetailsPre {
  user-select: text !important;
  -webkit-user-select: text !important;
  -moz-user-select: text !important;
  -ms-user-select: text !important;
  pointer-events: auto !important;
  position: relative;
  z-index: 1060;
}

#<portlet:namespace/>auditDetailsModal .modal-content::before,
#<portlet:namespace/>auditDetailsModal .modal-content::after,
#<portlet:namespace/>auditDetailsModal .modal-body::before,
#<portlet:namespace/>auditDetailsModal .modal-body::after {
  pointer-events: none !important;
}
</style>

<div class="mb-3 mt-5">
  <div class="management-bar management-bar-light navbar navbar-expand-md shadow-sm rounded">
    <div class="container-fluid px-3">
      <div class="navbar-nav">
        <div class="navbar-text h5 mb-0">DT5 Audit Logs</div>
      </div>
    </div>
  </div>
</div>

<div class="card shadow-sm mb-3">
  <div class="card-body">
    <aui:form method="post" name="fmFilters" action="javascript:void(0);">
      <div class="row">
        <div class="col-12 col-md-3 mb-2">
          <label class="form-label">Category</label>
          <select class="form-control" name="<portlet:namespace/>category" id="<portlet:namespace/>category">
            <option value="">All</option>
            <%
              for (String v : categories) {
            %>
              <option value="<%= HtmlUtil.escapeAttribute(v) %>" <%= v.equals(categoryFilter) ? "selected" : "" %>><%= HtmlUtil.escape(v) %></option>
            <%
              }
            %>
          </select>
        </div>

        <div class="col-12 col-md-3 mb-2">
          <label class="form-label">Status</label>
          <select class="form-control" name="<portlet:namespace/>status" id="<portlet:namespace/>status">
            <option value="">All</option>
            <%
              for (String v : statuses) {
            %>
              <option value="<%= HtmlUtil.escapeAttribute(v) %>" <%= v.equals(statusFilter) ? "selected" : "" %>><%= HtmlUtil.escape(v) %></option>
            <%
              }
            %>
          </select>
        </div>

        <div class="col-12 col-md-3 mb-2">
          <label class="form-label">Severity</label>
          <select class="form-control" name="<portlet:namespace/>severity" id="<portlet:namespace/>severity">
            <option value="">All</option>
            <%
              for (String v : severities) {
            %>
              <option value="<%= HtmlUtil.escapeAttribute(v) %>" <%= v.equals(severityFilter) ? "selected" : "" %>><%= HtmlUtil.escape(v) %></option>
            <%
              }
            %>
          </select>
        </div>

        <div class="col-12 col-md-3 mb-2">
          <label class="form-label">Step</label>
          <select class="form-control" name="<portlet:namespace/>step" id="<portlet:namespace/>step">
            <option value="">All</option>
            <%
              for (String v : steps) {
            %>
              <option value="<%= HtmlUtil.escapeAttribute(v) %>" <%= v.equals(stepFilter) ? "selected" : "" %>><%= HtmlUtil.escape(v) %></option>
            <%
              }
            %>
          </select>
        </div>

        <div class="col-6 col-md-2 mb-2">
          <label class="form-label">From</label>
          <input type="date" class="form-control" name="<portlet:namespace/>from" id="<portlet:namespace/>from"
                 value="<%= HtmlUtil.escapeAttribute(fromParam) %>">
        </div>
        <div class="col-6 col-md-2 mb-2">
          <label class="form-label">To</label>
          <input type="date" class="form-control" name="<portlet:namespace/>to" id="<portlet:namespace/>to"
                 value="<%= HtmlUtil.escapeAttribute(toParam) %>">
        </div>

        <div class="col-12 col-md-3 mb-2">
          <label class="form-label">Ref ID</label>
          <input type="number"
                 class="form-control"
                 id="<portlet:namespace/>ntucDTIdInput"
                 name="<portlet:namespace/>ntucDTId"
                 placeholder="e.g. 123456"
                 value="<%= HtmlUtil.escapeAttribute(ntucDtIdFilter) %>">
          <div class="audit-hint">Exact match. Use this when you have the Ref ID.</div>
        </div>

        <div class="col-12 col-md-5 mb-2">
          <label class="form-label">Search</label>
          <input type="text" class="form-control" id="<portlet:namespace/>qInput" name="<portlet:namespace/>q"
                 placeholder="message / courseCode / correlationId / jobRunId / errorCode"
                 value="<%= HtmlUtil.escapeAttribute(qFilter) %>">
          <div class="audit-hint">Press <b>Enter</b> or click <b>Apply</b> to search.</div>
        </div>
      </div>

      <div class="d-flex flex-wrap gap-2 mt-2">
        <div class="btn-group btn-group-sm mr-2 mb-2" role="group" id="<portlet:namespace/>presetGroup">
          <button type="button" class="btn btn-outline-secondary" data-preset="today">Today</button>
          <button type="button" class="btn btn-outline-secondary" data-preset="yesterday">Yesterday</button>
          <button type="button" class="btn btn-outline-secondary" data-preset="7d">Last 7 days</button>
        </div>

        <button class="btn btn-primary mb-2" type="submit" id="<portlet:namespace/>applyBtn">
          <span class="apply-label">Apply</span>
          <span class="apply-spinner d-none" aria-hidden="true">
            <span class="spinner-border spinner-border-sm ml-2" role="status"></span>
          </span>
        </button>

        <button class="btn btn-outline-secondary mb-2" type="button" id="<portlet:namespace/>clearBtn">Clear</button>
      </div>

      <div id="activeChips" class="mt-2"></div>
    </aui:form>
  </div>
</div>

<div class="card shadow-sm mb-3">
  <div class="card-body">
    <div class="table-responsive">
      <table id="<portlet:namespace/>auditLogTable" class="table table-hover table-striped w-100">
        <thead>
          <tr>
            <th>Start Time</th>
            <th>Severity</th>
            <th>Status</th>
            <th>Category</th>
            <th>Step</th>
            <th>Message</th>
            <th>Course Code</th>
            <th>Ref ID</th>
            <th>Correlation ID</th>
            <th>Duration (ms)</th>
            <th>Audit ID</th>
            <th>Details</th>
          </tr>
        </thead>
        <tbody></tbody>
      </table>
    </div>
  </div>
</div>

<div class="modal fade" id="<portlet:namespace/>auditDetailsModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Audit Details</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div id="<portlet:namespace/>auditDetailsMeta" class="mb-2"></div>
        <pre class="audit-json" id="<portlet:namespace/>auditDetailsPre">Loading...</pre>
      </div>
    </div>
  </div>
</div>

<script>
(function () {
  var NS = '<portlet:namespace />';
  var TABLE_URL = '<%= dataTablesURL %>';
  var DETAILS_URL = '<%= detailsURL %>';

  // ===== FIX: severity ALL vs default ERROR =====
  // If user selects "All" (blank), we must persist that choice across refresh.
  // We store a sentinel in URL so "no severity param" can still mean "first visit => default ERROR".
  var ALL_SENTINEL = '__ALL__';
  // ==============================================

  function ns(name) { return NS + name; }
  function getForm() { return document.querySelector('form[name="' + NS + 'fmFilters"]'); }

  function byName(name) { return document.querySelector('[name="' + ns(name) + '"]'); }
  function v(name) { var el = byName(name); return el ? el.value : ''; }
  function setVal(name, value) { var el = byName(name); if (el) el.value = value; }

  function esc(s) { if (s == null) return ''; return Liferay.Util.escapeHTML(String(s)); }

  function msToLocal(ms) {
    if (!ms) return '';
    try { return new Date(Number(ms)).toLocaleString(); } catch(e) { return ''; }
  }

  function truncate(s) {
    if (!s) return '';
    var t = String(s);
    return (t.length > 90) ? (t.substring(0, 90) + '…') : t;
  }

  function badgeSeverity(v) {
    if (!v) return '';
    var up = String(v).toUpperCase();
    var c = up.indexOf('ERROR') >= 0 ? 'danger'
          : up.indexOf('WARNING') >= 0 ? 'warning'
          : 'info';
    return '<span class="badge badge-' + c + '">' + esc(v) + '</span>';
  }

  function badgeStatus(v) {
    if (!v) return '';
    var up = String(v).toUpperCase();
    var c = up.indexOf('FAILED') >= 0 ? 'danger'
          : up.indexOf('SUCCESS') >= 0 ? 'success'
          : up.indexOf('PARTIAL') >= 0 ? 'warning'
          : 'info';
    return '<span class="badge badge-' + c + '">' + esc(v) + '</span>';
  }

  function fmtYYYYMMDD(dt) {
    var y = dt.getFullYear();
    var m = String(dt.getMonth() + 1).padStart(2, '0');
    var d = String(dt.getDate()).padStart(2, '0');
    return y + '-' + m + '-' + d;
  }

  function ymdToDmy(ymd) {
    if (!ymd) return '';
    var t = String(ymd).trim();
    var m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(t);
    if (!m) return t;
    return m[3] + '-' + m[2] + '-' + m[1];
  }

  function dateToFromMs(d) {
    if (!d) return '';
    try { return new Date(d + 'T00:00:00.000').getTime(); } catch(e) { return ''; }
  }
  function dateToToMs(d) {
    if (!d) return '';
    try { return new Date(d + 'T23:59:59.999').getTime(); } catch(e) { return ''; }
  }

  function applyPreset(preset) {
    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

    if (preset === 'today') {
      setVal('from', fmtYYYYMMDD(today));
      setVal('to', fmtYYYYMMDD(today));
      return;
    }
    if (preset === 'yesterday') {
      var y = new Date(today);
      y.setDate(y.getDate() - 1);
      setVal('from', fmtYYYYMMDD(y));
      setVal('to', fmtYYYYMMDD(y));
      return;
    }
    if (preset === '7d') {
      var start = new Date(today);
      start.setDate(start.getDate() - 6);
      setVal('from', fmtYYYYMMDD(start));
      setVal('to', fmtYYYYMMDD(today));
      return;
    }
  }

  function trySetDefaultSeverityError() {
    // Default only if *truly empty* AND caller decided it's allowed (first visit).
    if (v('severity')) return;
    var sevEl = byName('severity');
    if (!sevEl) return;

    for (var i = 0; i < sevEl.options.length; i++) {
      if (String(sevEl.options[i].value).toUpperCase() === 'ERROR') {
        setVal('severity', 'ERROR');
        return;
      }
    }
  }

  function setDefaultTodayDatesIfEmpty() {
    if (v('from') && v('to')) return;
    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    var t = fmtYYYYMMDD(today);
    if (!v('from')) setVal('from', t);
    if (!v('to')) setVal('to', t);
  }

  function setApplyLoading(isLoading) {
    var btn = document.getElementById(ns('applyBtn'));
    if (!btn) return;

    btn.disabled = !!isLoading;

    var sp = btn.querySelector('.apply-spinner');
    var label = btn.querySelector('.apply-label');

    if (sp) sp.classList.toggle('d-none', !isLoading);
    if (label) label.textContent = isLoading ? 'Applying...' : 'Apply';
  }

  // URL persistence (read/write)
  var URL_KEYS = ['category','status','severity','step','q','from','to','ntucDTId'];

  function readUrlParam(key) {
    try {
      var usp = new URLSearchParams(window.location.search);

      // ===== FIX: treat sentinel as "All" (blank) =====
      var val = usp.get(key) || '';
      if (key === 'severity' && val === ALL_SENTINEL) {
        return '';
      }
      return val;
      // ===============================================
    } catch (e) {
      return '';
    }
  }

  function hasUrlParam(key) {
    try {
      var usp = new URLSearchParams(window.location.search);
      return usp.has(key);
    } catch (e) {
      return false;
    }
  }

  function writeUrlFromApplied(applied) {
    try {
      var url = new URL(window.location.href);

      URL_KEYS.forEach(function (k) {
        var rawVal = (applied && applied[k] != null) ? String(applied[k]) : '';
        var val = rawVal;

        // ===== FIX: persist explicit "All" for severity =====
        if (k === 'severity' && !val) {
          val = ALL_SENTINEL;
        }
        // ===================================================

        if (val) url.searchParams.set(k, val);
        else url.searchParams.delete(k);
      });

      window.history.replaceState({}, document.title, url.toString());
    } catch (e) {
      // ignore
    }
  }

  // Applied snapshot + chips
  var applied = {
    severity: '',
    status: '',
    category: '',
    step: '',
    q: '',
    from: '',
    to: '',
    ntucDTId: ''
  };

  function snapshotCurrentIntoApplied() {
    applied.severity = v('severity');
    applied.status   = v('status');
    applied.category = v('category');
    applied.step     = v('step');
    applied.q        = v('q');
    applied.from     = v('from');
    applied.to       = v('to');
    applied.ntucDTId = v('ntucDTId');
  }

  function renderAppliedChips() {
    var wrap = document.getElementById('activeChips');
    if (!wrap) return;

    var chips = [];

    function addChip(k, valText) {
      if (!valText) return;
      chips.push(
        '<span class="audit-chip" title="' + esc(k + ': ' + valText) + '">' +
          '<span class="k">' + esc(k) + '</span>' +
          '<span class="v">' + esc(valText) + '</span>' +
        '</span>'
      );
    }

    function addRangeChip(fromYmd, toYmd) {
      if (!fromYmd && !toYmd) return;
      var f = ymdToDmy(fromYmd);
      var t = ymdToDmy(toYmd);

      chips.push(
        '<span class="audit-chip" title="' + esc('range: ' + f + ' -> ' + t) + '">' +
          '<span class="k">range</span>' +
          '<span class="v">' + esc(f) + ' &#8594; ' + esc(t) + '</span>' +
        '</span>'
      );
    }

    addChip('severity', applied.severity);
    addChip('status', applied.status);
    addChip('category', applied.category);
    addChip('step', applied.step);
    addChip('ref ID', applied.ntucDTId);
    addRangeChip(applied.from, applied.to);
    if (applied.q) addChip('search', applied.q);

    wrap.innerHTML = chips.join('') || '<span class="audit-hint">No filters applied.</span>';
  }

  function hydrateFormFromUrlThenDefaults() {
    var uCat = readUrlParam('category');
    var uSt  = readUrlParam('status');
    var uSev = readUrlParam('severity');
    var uStep= readUrlParam('step');
    var uQ   = readUrlParam('q');
    var uFrom= readUrlParam('from');
    var uTo  = readUrlParam('to');
    var uDt  = readUrlParam('ntucDTId');

    if (uCat) setVal('category', uCat);
    if (uSt)  setVal('status', uSt);
    if (uSev) setVal('severity', uSev); // uSev is '' when sentinel => leaves "All"
    if (uStep)setVal('step', uStep);
    if (uQ)   setVal('q', uQ);
    if (uFrom)setVal('from', uFrom);
    if (uTo)  setVal('to', uTo);
    if (uDt)  setVal('ntucDTId', uDt);

    setDefaultTodayDatesIfEmpty();

    // ===== FIX: default severity=ERROR only on first visit (no severity param at all) =====
    // If user chose All earlier, URL will have severity=__ALL__ (so hasUrlParam('severity') is true)
    if (!hasUrlParam('severity')) {
      trySetDefaultSeverityError();
    }
    // ===============================================================================
  }

  function setDefaultsIntoForm() {
    setVal('category', '');
    setVal('status', '');
    setVal('step', '');
    setVal('q', '');
    setVal('ntucDTId', '');

    var now = new Date();
    var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    var t = fmtYYYYMMDD(today);
    setVal('from', t);
    setVal('to', t);

    // Clear should mean "All", not "ERROR"
    setVal('severity', '');
  }

  function openDetails(auditId) {
    var meta = document.getElementById(ns('auditDetailsMeta'));
    var pre  = document.getElementById(ns('auditDetailsPre'));

    if (meta) meta.innerHTML = '<b>Audit ID:</b> ' + esc(auditId);
    if (pre) pre.textContent = 'Loading...';

    if (window.jQuery && jQuery.fn && jQuery.fn.modal) {
      jQuery('#' + ns('auditDetailsModal')).modal('show');
    } else {
      Liferay.Util.openToast({ message: 'Modal support not available; check theme/Bootstrap', type: 'warning' });
      return;
    }

    var url = DETAILS_URL;
    var sep = (url.indexOf('?') >= 0 ? '&' : '?');
    url += sep + 'auditId=' + encodeURIComponent(auditId);
    url += '&' + encodeURIComponent(ns('auditId')) + '=' + encodeURIComponent(auditId);

    var fetchFn = (Liferay.Util && Liferay.Util.fetch) ? Liferay.Util.fetch : window.fetch;
    if (!fetchFn) {
      if (pre) pre.textContent = 'Fetch not available in this browser.';
      return;
    }

    fetchFn(url, { method: 'GET', credentials: 'same-origin' })
      .then(function (r) { return r.text(); })
      .then(function (txt) {
        try {
          var obj = JSON.parse(txt);

          if (meta && obj && obj.ok) {
            var extra = '';
            if (obj.ntucDTId != null && String(obj.ntucDTId).trim() !== '') {
              extra += ' &nbsp;|&nbsp; <b>Ref ID:</b> ' + esc(obj.ntucDTId);
            }
            if (obj.courseCode) {
              extra += ' &nbsp;|&nbsp; <b>Course Code:</b> ' + esc(obj.courseCode);
            }
            meta.innerHTML = '<b>Audit ID:</b> ' + esc(auditId) + extra;
          }

          if (pre) pre.textContent = JSON.stringify(obj, null, 2);
        } catch (e) {
          if (pre) pre.textContent = txt;
        }
      })
      .catch(function (e) {
        if (pre) pre.textContent = 'Failed to load details: ' + (e && e.message ? e.message : String(e));
      });
  }

  if (!window.jQuery) {
    Liferay.Util.openToast({ message: 'Liferay jQuery is not available on this page', type: 'danger' });
    return;
  }
  if (!jQuery.fn || !jQuery.fn.DataTable) {
    Liferay.Util.openToast({ message: 'DataTables library is not loaded (CDN blocked / CSP?)', type: 'danger' });
    return;
  }

  var table = null;

  function init() {
    hydrateFormFromUrlThenDefaults();

    snapshotCurrentIntoApplied();
    writeUrlFromApplied(applied);
    renderAppliedChips();

    var tableSel = '#' + NS + 'auditLogTable';

    table = jQuery(tableSel).DataTable({
      processing: true,
      serverSide: true,
      searching: false,
      ordering: true,
      lengthChange: true,
      pageLength: 25,
      order: [[0, 'desc']],
      ajax: {
        url: TABLE_URL,
        type: 'POST',
        data: function (d) {
          // Canonical: send namespaced only (portlet resource contract)
          d[NS + 'draw'] = d.draw;
          d[NS + 'start'] = d.start;
          d[NS + 'length'] = d.length;

          // DataTables standard search key - keep it namespaced only (parser supports it)
          d[NS + 'search[value]'] = applied.q || '';

          if (d.order && d.order.length) {
            d[NS + 'order[0][column]'] = d.order[0].column;
            d[NS + 'order[0][dir]'] = d.order[0].dir;
          }

          // Filters - namespaced only (prevents INFO+ERROR collision)
          if (applied.severity) { d[NS + 'severity'] = applied.severity; }
          if (applied.status)   { d[NS + 'status']   = applied.status; }
          if (applied.category) { d[NS + 'category'] = applied.category; }
          if (applied.step)     { d[NS + 'step']     = applied.step; }

          if (applied.ntucDTId) { d[NS + 'ntucDTId'] = applied.ntucDTId; }

          var fromMs = dateToFromMs(applied.from);
          var toMs   = dateToToMs(applied.to);

          if (fromMs !== '' && fromMs != null) { d[NS + 'fromTimeMs'] = fromMs; }
          if (toMs !== '' && toMs != null)     { d[NS + 'toTimeMs']   = toMs;   }
        }
      },
      columns: [
        { data: 'startTimeMs', render: function (data) { return esc(msToLocal(data)); } },
        { data: 'severity', render: function (data) { return badgeSeverity(data); } },
        { data: 'status', render: function (data) { return badgeStatus(data); } },
        { data: 'category', render: function (data) { return esc(data); } },
        { data: 'step', render: function (data) { return esc(data); } },
        { data: 'message', render: function (data) { return '<span title="' + esc(data) + '">' + esc(truncate(data)) + '</span>'; } },
        { data: 'courseCode', render: function (data) { return esc(data); } },
        { data: 'ntucDTId', render: function (data) { return esc(data); } },
        { data: 'correlationId', render: function (data, type, row) {
            var txt = (data == null) ? '' : String(data);
            if (!txt) return '';
            var cidId = NS + 'cid-' + row.auditId;
            return '<span class="mr-2" id="' + cidId + '">' + esc(txt) + '</span>';
          }
        },
        { data: 'durationMs', render: function (data) { return esc(data); } },
        { data: 'auditId', render: function (data) { return esc(data); } },
        { data: null, orderable: false, render: function (data, type, row) {
            return '<button type="button" class="btn btn-outline-primary btn-sm show-details" data-audit-id="' + esc(row.auditId) + '">View</button>';
          }
        }
      ]
    });

    jQuery(tableSel)
      .on('preXhr.dt', function () { setApplyLoading(true); })
      .on('xhr.dt', function () { setApplyLoading(false); })
      .on('error.dt', function () { setApplyLoading(false); })
      .on('draw.dt', function () { setApplyLoading(false); });

    jQuery(tableSel).on('click', 'button.show-details', function () {
      var id = this.getAttribute('data-audit-id');
      if (!id) return;
      openDetails(id);
    });

    var form = getForm();
    if (form) {
      form.addEventListener('submit', function (e) {
        e.preventDefault();
        snapshotCurrentIntoApplied();
        writeUrlFromApplied(applied);
        renderAppliedChips();
        table.ajax.reload();
      });
    }

    var qInput = document.getElementById(ns('qInput'));
    if (qInput) {
      qInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
          e.preventDefault();
          if (form && form.requestSubmit) form.requestSubmit();
          else if (form) form.dispatchEvent(new Event('submit', { cancelable: true }));
        }
      });
    }

    var presetGroup = document.getElementById(ns('presetGroup'));
    if (presetGroup) {
      presetGroup.addEventListener('click', function (e) {
        var btn = e.target && e.target.closest ? e.target.closest('button[data-preset]') : null;
        if (!btn) return;
        applyPreset(btn.getAttribute('data-preset'));
      });
    }

    var clearBtn = document.getElementById(ns('clearBtn'));
    if (clearBtn) {
      clearBtn.addEventListener('click', function () {
        setDefaultsIntoForm();
        snapshotCurrentIntoApplied();
        writeUrlFromApplied(applied);
        renderAppliedChips();
        table.ajax.reload();
      });
    }
  }

  init();
})();
</script>
