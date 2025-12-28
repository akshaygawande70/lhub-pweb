<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.liferay.portal.kernel.security.auth.AuthTokenUtil" %>
<%@ page import="com.liferay.portal.kernel.theme.ThemeDisplay" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.UnicodeFormatter" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.ntuc.notification.configuration.Dt5EmailTemplateConfiguration" %>
<%@ page import="com.ntuc.notification.audit.api.constants.AlertEmailToken" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/clay" prefix="clay" %>

<%@ include file="/init.jsp" %>

<%!
private String L(javax.servlet.http.HttpServletRequest request, String key, String deflt) {
    String v = com.liferay.portal.kernel.language.LanguageUtil.get(request, key);
    return (v == null || v.equals(key)) ? deflt : v;
}
%>

<%
    Dt5EmailTemplateConfiguration configuration =
        (Dt5EmailTemplateConfiguration) request.getAttribute(Dt5EmailTemplateConfiguration.class.getName());

    String dt5Subject = configuration != null ? GetterUtil.getString(configuration.dt5FailureSubject()) : "";
    String dt5Body    = configuration != null ? GetterUtil.getString(configuration.dt5FailureBody())    : "";

    String clsSubject = configuration != null ? GetterUtil.getString(configuration.clsFailureSubject()) : "";
    String clsBody    = configuration != null ? GetterUtil.getString(configuration.clsFailureBody())    : "";

    String jaSubject  = configuration != null ? GetterUtil.getString(configuration.jaFailureSubject())  : "";
    String jaBody     = configuration != null ? GetterUtil.getString(configuration.jaFailureBody())     : "";

    String redirect = ParamUtil.getString(request, "redirect");

    ThemeDisplay td = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

    // OSGi Http Whiteboard servlet:
    String saveUrl = td.getPortalURL() + td.getPathContext() + "/o/dt5-email-templates/save";

    // CSRF token
    String pAuth = AuthTokenUtil.getToken(request);
%>

<style>
/* Compact / professional density */
.dt5-wrap { font-size: 12.5px; }
.dt5-wrap h2 { font-size: 18px; }
.dt5-wrap .h5 { font-size: 14px; }

.dt5-wrap .card { border-radius: 10px; }
.dt5-wrap .card-header { padding: .55rem .75rem; }
.dt5-wrap .card-body { padding: .75rem; }

.dt5-muted { color: #6b6c7e; }
.dt5-mono {
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
}

/* section toggling */
.template-section { display:none; }
.template-section.active { display:block; }

/* selector row tighter */
.dt5-selector-row { gap: .5rem; }
.dt5-selector-row .form-group { margin-bottom: 0; }

/* token panel: sticky + internal scroll */
.dt5-token-card {
  position: sticky;
  top: .75rem;
  height: calc(100vh - 1.5rem);
  display: flex;
  flex-direction: column;
}
.dt5-token-body {
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: .6rem;
}
.dt5-token-scroll {
  min-height: 0;
  overflow: auto;
  padding-top: .4rem;
  border-top: 1px solid #e7e7ed;
}

/* compact search */
.dt5-search .form-control { height: 32px; font-size: 12.5px; }
.dt5-search .input-group-text { padding: 0 .55rem; }

/* token grid (dense) */
.dt5-token-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: .25rem;
}

/* token row */
.dt5-token-item {
  display:flex;
  align-items:center;
  justify-content:space-between;
  gap:.4rem;
  padding: .28rem .28rem;
  border-radius: 8px;
}
.dt5-token-item:hover { background:#f7f8f9; }

/* token chip (insert) */
.dt5-token-chip {
  border: 1px solid #e7e7ed;
  background: #fff;
  padding: .18rem .45rem;
  border-radius: 8px;
  font-size: 12px;
  line-height: 1.2;
  cursor:pointer;
  max-width: 100%;
  overflow:hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.dt5-token-chip:hover { border-color:#cfd2dc; }

/* tiny icon buttons */
.dt5-icon-btn {
  border: 1px solid transparent;
  background: transparent;
  padding: .12rem .25rem;
  border-radius: 6px;
  cursor:pointer;
  color:#6b6c7e;
}
.dt5-icon-btn:hover { background:#eef0f5; color:#272833; }

/* footer: compact and sticky */
.dt5-footer {
  position: sticky;
  bottom: 0;
  background: #fff;
  border-top: 1px solid #e7e7ed;
  padding: .55rem .75rem;
  z-index: 5;
}

/* CKEditor dropdown panels above sticky stuff */
.cke_panel { z-index: 99999 !important; }

/* Fix search input + icon vertical alignment (Bootstrap + Clay) */
.dt5-search .input-group {
  align-items: stretch;
}

.dt5-search .input-group-text,
.dt5-search .form-control {
  height: 30px;
  font-size: 12.5px;
}

.dt5-search .input-group-text {
  padding: 0 .55rem;
  display: flex;
  align-items: center;
}

.dt5-search .input-group-text svg {
  width: 14px;
  height: 14px;
}

</style>

<aui:script sandbox="false">
function <portlet:namespace />initDt5Body() { return "<%= UnicodeFormatter.toString(dt5Body) %>"; }
function <portlet:namespace />initClsBody() { return "<%= UnicodeFormatter.toString(clsBody) %>"; }
function <portlet:namespace />initJaBody()  { return "<%= UnicodeFormatter.toString(jaBody) %>"; }
</aui:script>

<div class="container-fluid p-3 dt5-wrap">
  <div class="d-flex align-items-center justify-content-between mb-2">
    <div class="d-flex align-items-center">
      <clay:icon symbol="envelope-closed" />
      <h2 class="ml-2 mb-0"><%= L(request,"email-templates-heading","Email Templates") %></h2>
    </div>
  </div>

  <aui:form action="<%= saveUrl %>" method="post" name="fm" cssClass="needs-validation" novalidate="novalidate">
    <aui:input name="redirect" type="hidden" value="<%= GetterUtil.getString(redirect) %>" />
    <aui:input name="p_auth" type="hidden" value="<%= pAuth %>" />

    <div class="row">
      <!-- LEFT -->
      <div class="col-lg-8">
        <!-- selector -->
        <div class="card shadow-sm mb-3">
          <div class="card-header d-flex align-items-center justify-content-between">
            <strong>Template Selector</strong>
            <small class="dt5-muted d-none d-md-inline">
              <clay:icon symbol="info-circle-open" />&nbsp;Pick a category, then edit Subject &amp; Body.
            </small>
          </div>

          <div class="card-body">
            <div class="d-flex flex-wrap dt5-selector-row align-items-end">
              <div style="min-width: 280px; flex: 1;">
                <aui:select
                  name="templateType"
                  label="<%= L(request, "choose-template-type", "Choose template type") %>"
                  cssClass="form-control"
                >
                  <aui:option value="dt5" selected="true">DT5 Failure</aui:option>
                  <aui:option value="cls">CLS Failure</aui:option>
                  <aui:option value="ja">JA Failure</aui:option>
                </aui:select>
                <small class="dt5-muted">Switching type reveals the respective editor below.</small>
              </div>
            </div>
          </div>
        </div>

        <!-- DT5 -->
        <section class="template-section active" data-type="dt5" aria-labelledby="<portlet:namespace />dt5Heading">
          <div class="card shadow-sm mb-3">
            <div class="card-header d-flex align-items-center justify-content-between">
              <h3 id="<portlet:namespace />dt5Heading" class="h5 mb-0">DT5 Failure</h3>
              <span class="badge badge-primary">DT5_FAILURE</span>
            </div>

            <div class="card-body">
              <aui:input name="dt5FailureSubject" type="text" label="Subject"
                         value="<%= dt5Subject %>" maxLength="180" required="true" />
              <div class="d-flex justify-content-end">
                <small class="dt5-muted"><span id="<portlet:namespace />dt5SubjectCount">0</span>/180</small>
              </div>

              <div class="mt-3">
                <aui:field-wrapper label="Body">
                  <div class="border rounded p-2 bg-white">
                    <liferay-ui:input-editor name="dt5FailureBody" initMethod="initDt5Body" editorName="ckeditor" />
                  </div>
                </aui:field-wrapper>
              </div>
            </div>

            <div class="dt5-footer d-flex justify-content-end align-items-center">
              <div>
                <aui:button type="button" cssClass="btn btn-outline-secondary btn-sm mr-2"
                  value="<%= L(request,"cancel","Cancel") %>"
                  onClick="<%= "location.href='" + GetterUtil.getString(redirect).replace(\"'\",\"\\'\") + "'" %>" />
                <aui:button type="submit" cssClass="btn btn-primary btn-sm"
                  value="<%= L(request,"save","Save") %>" id="<portlet:namespace />saveBtn" />
              </div>
            </div>
          </div>
        </section>

        <!-- CLS -->
        <section class="template-section" data-type="cls" aria-labelledby="<portlet:namespace />clsHeading">
          <div class="card shadow-sm mb-3">
            <div class="card-header d-flex align-items-center justify-content-between">
              <h3 id="<portlet:namespace />clsHeading" class="h5 mb-0">CLS Failure</h3>
              <span class="badge badge-warning">CLS_FAILURE</span>
            </div>

            <div class="card-body">
              <aui:input name="clsFailureSubject" type="text" label="Subject"
                         value="<%= clsSubject %>" maxLength="180" required="true" />
              <div class="d-flex justify-content-end">
                <small class="dt5-muted"><span id="<portlet:namespace />clsSubjectCount">0</span>/180</small>
              </div>

              <div class="mt-3">
                <aui:field-wrapper label="Body">
                  <div class="border rounded p-2 bg-white">
                    <liferay-ui:input-editor name="clsFailureBody" initMethod="initClsBody" editorName="ckeditor" />
                  </div>
                </aui:field-wrapper>
              </div>
            </div>

            <div class="dt5-footer d-flex justify-content-end align-items-center">
              <div>
                <aui:button type="button" cssClass="btn btn-outline-secondary btn-sm mr-2"
                  value="<%= L(request,"cancel","Cancel") %>"
                  onClick="<%= "location.href='" + GetterUtil.getString(redirect).replace(\"'\",\"\\'\") + "'" %>" />
                <aui:button type="submit" cssClass="btn btn-primary btn-sm"
                  value="<%= L(request,"save","Save") %>" id="<portlet:namespace />saveBtn2" />
              </div>
            </div>
          </div>
        </section>

        <!-- JA -->
        <section class="template-section" data-type="ja" aria-labelledby="<portlet:namespace />jaHeading">
          <div class="card shadow-sm mb-3">
            <div class="card-header d-flex align-items-center justify-content-between">
              <h3 id="<portlet:namespace />jaHeading" class="h5 mb-0">JA Failure</h3>
              <span class="badge badge-success">JA_FAILURE</span>
            </div>

            <div class="card-body">
              <aui:input name="jaFailureSubject" type="text" label="Subject"
                         value="<%= jaSubject %>" maxLength="180" required="true" />
              <div class="d-flex justify-content-end">
                <small class="dt5-muted"><span id="<portlet:namespace />jaSubjectCount">0</span>/180</small>
              </div>

              <div class="mt-3">
                <aui:field-wrapper label="Body">
                  <div class="border rounded p-2 bg-white">
                    <liferay-ui:input-editor name="jaFailureBody" initMethod="initJaBody" editorName="ckeditor" />
                  </div>
                </aui:field-wrapper>
              </div>
            </div>

            <div class="dt5-footer d-flex justify-content-end align-items-center">
              <div>
                <aui:button type="button" cssClass="btn btn-outline-secondary btn-sm mr-2"
                  value="<%= L(request,"cancel","Cancel") %>"
                  onClick="<%= "location.href='" + GetterUtil.getString(redirect).replace(\"'\",\"\\'\") + "'" %>" />
                <aui:button type="submit" cssClass="btn btn-primary btn-sm"
                  value="<%= L(request,"save","Save") %>" id="<portlet:namespace />saveBtn3" />
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- RIGHT: Tokens -->
      <div class="col-lg-4 mt-3 mt-lg-0">
        <div class="card shadow-sm dt5-token-card">
          <div class="card-header d-flex align-items-center justify-content-between">
            <strong>Tokens</strong>
            <small class="dt5-muted">Insert</small>
          </div>

          <div class="dt5-token-body">
            <div class="dt5-search">
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text"><clay:icon symbol="search" /></span>
                </div>
                <input id="<portlet:namespace />tokenSearch" type="text" class="form-control"
                  placeholder="Search…" autocomplete="off" />
              </div>
            </div>

            <div class="dt5-token-scroll" id="<portlet:namespace />tokenScroll">
              <div class="dt5-token-grid" id="<portlet:namespace />tokensGrid">
                <%
                  AlertEmailToken[] tokens = AlertEmailToken.values();
                  for (int i = 0; i < tokens.length; i++) {
                    AlertEmailToken t = tokens[i];
                %>
                <div class="dt5-token-item" data-token="<%= t.token() %>" data-desc="<%= t.description() %>">
                  <div class="dt5-token-chip dt5-mono" data-action="insert" title="<%= t.description() %>">
                    <%= t.token() %>
                  </div>
                  <div class="d-flex align-items-center">
                    <button type="button" class="dt5-icon-btn" data-action="copy" title="Copy">
                      <clay:icon symbol="copy" />
                    </button>
                  </div>
                </div>
                <%
                  }
                %>
              </div>

              <div class="pt-2">
                <small class="dt5-muted">Never insert secrets or raw payloads.</small>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </aui:form>
</div>

<aui:script sandbox="false">
(function () {
  var ns = '<portlet:namespace />';
  var redirectUrl = '<%= GetterUtil.getString(redirect).replace("'", "\\'") %>';

  function showToast(type, message) {
    // Liferay DXP 7.3: prefer openToast if present
    try {
      if (window.Liferay && Liferay.Util && typeof Liferay.Util.openToast === 'function') {
        Liferay.Util.openToast({
          message: message,
          type: type, // 'success' | 'danger' | 'warning' | 'info'
          autoClose: 4000
        });
        return;
      }
    } catch (e) {}
    // fallback
    alert(message);
  }

  // Switch sections
  var selector = document.getElementById(ns + 'templateType');
  function showSection(type) {
    var sections = document.querySelectorAll('.template-section');
    for (var i = 0; i < sections.length; i++) {
      var s = sections[i];
      s.classList.toggle('active', s.getAttribute('data-type') === type);
    }
  }
  if (selector) {
    selector.addEventListener('change', function () { showSection(selector.value); });
    showSection(selector.value || 'dt5');
  }

  // Counters
  function bindCounter(idInput, idCounter){
    var el = document.getElementById(idInput);
    var c  = document.getElementById(idCounter);
    if(!el || !c) return;
    var upd = function(){ c.textContent = (el.value||'').length; };
    el.addEventListener('input', upd); upd();
  }
  bindCounter(ns + 'dt5FailureSubject', ns + 'dt5SubjectCount');
  bindCounter(ns + 'clsFailureSubject', ns + 'clsSubjectCount');
  bindCounter(ns + 'jaFailureSubject',  ns + 'jaSubjectCount');

  // Active editor instance
  function getActiveEditorName() {
    var type = selector ? selector.value : 'dt5';
    if (type === 'cls') return ns + 'clsFailureBody';
    if (type === 'ja')  return ns + 'jaFailureBody';
    return ns + 'dt5FailureBody';
  }

  function insertToken(token) {
    var editorName = getActiveEditorName();
    if (window.CKEDITOR && CKEDITOR.instances && CKEDITOR.instances[editorName]) {
      try { CKEDITOR.instances[editorName].insertText(token); CKEDITOR.instances[editorName].focus(); return; } catch(e){}
    }
  }

  // Token search + actions
  var tokenSearch = document.getElementById(ns + 'tokenSearch');
  var tokensGrid  = document.getElementById(ns + 'tokensGrid');

  function filterTokens(q) {
  q = (q || '').toLowerCase().trim();
  if (!tokensGrid) return;

  var rows = tokensGrid.querySelectorAll('.dt5-token-item');

  for (var i = 0; i < rows.length; i++) {
    var row = rows[i];
    var token = (row.getAttribute('data-token') || '').toLowerCase();
    var desc  = (row.getAttribute('data-desc') || '').toLowerCase();

    var show = false;

    if (!q) {
      show = true;
    }
    // 1️ Strong match: token itself
    else if (token.indexOf(q) >= 0) {
      show = true;
    }
    // 2️ Weak match: description ONLY if token didn't match
    // else if (desc.indexOf(q) >= 0) {
    //   show = true;
    // }

    row.style.display = show ? '' : 'none';
  }
}


  if (tokenSearch) {
    tokenSearch.addEventListener('input', function () { filterTokens(tokenSearch.value); });
  }

  if (tokensGrid) {
    tokensGrid.addEventListener('click', function (e) {
      var row = e.target.closest('.dt5-token-item');
      if (!row) return;

      var token = (row.getAttribute('data-token') || '').trim();
      if (!token) return;

      var actionEl = e.target.closest('[data-action]');
      var action = actionEl ? actionEl.getAttribute('data-action') : null;

      if (action === 'copy') {
        if (navigator.clipboard && navigator.clipboard.writeText) navigator.clipboard.writeText(token);
        return;
      }

      // default: insert
      insertToken(token);
    });
  }

  // Save handling: toast + refresh
  var form = document.querySelector('form.needs-validation');
  var saveBtn1 = document.getElementById(ns + 'saveBtn');
  var saveBtn2 = document.getElementById(ns + 'saveBtn2');
  var saveBtn3 = document.getElementById(ns + 'saveBtn3');

  function setSaving(isSaving) {
    var btns = [saveBtn1, saveBtn2, saveBtn3];
    for (var i = 0; i < btns.length; i++) {
      var b = btns[i];
      if (!b) continue;
      if (isSaving) {
        b.disabled = true;
        b.dataset.prevText = b.innerText;
        b.innerText = 'Saving…';
      } else {
        b.disabled = false;
        if (b.dataset.prevText) b.innerText = b.dataset.prevText;
      }
    }
  }

  function syncCkEditors() {
    try {
      if (window.CKEDITOR && CKEDITOR.instances) {
        for (var k in CKEDITOR.instances) {
          if (!CKEDITOR.instances.hasOwnProperty(k)) continue;
          try { CKEDITOR.instances[k].updateElement(); } catch (e) {}
        }
      }
    } catch (e) {}
  }

  if (form) {
    form.addEventListener('submit', function (event) {
      event.preventDefault();
      event.stopPropagation();

      if (!form.checkValidity()) {
        form.classList.add('was-validated');
        showToast('danger', 'Please fix validation errors before saving.');
        return;
      }

      // Ensure editor values are pushed into their underlying inputs
      syncCkEditors();

      setSaving(true);

      var url = form.getAttribute('action');
      var fd = new FormData(form);

      fetch(url, {
        method: 'POST',
        body: fd,
        credentials: 'same-origin'
      })
      .then(function (res) {
        if (res.ok) {
          showToast('success', 'Templates saved successfully.');

			setTimeout(function () {
			  if (redirectUrl && redirectUrl.length > 0) {
			    window.location.href = redirectUrl;
			  } else {
			    window.location.reload(true);
			  }
			}, 1000);
			
			return;

        }

        // Try to read response text for debugging-friendly toast (but keep it short)
        return res.text().then(function (t) {
          var msg = 'Save failed (HTTP ' + res.status + ').';
          if (t) {
            t = (t + '').replace(/\s+/g, ' ').trim();
            if (t.length > 140) t = t.substring(0, 140) + '…';
            msg += ' ' + t;
          }
          showToast('danger', msg);
          setSaving(false);
        });
      })
      .catch(function () {
        showToast('danger', 'Save failed due to network/server error.');
        setSaving(false);
      })
      .finally(function () {
        form.classList.add('was-validated');
      });
    }, false);
  }
})();
</aui:script>
