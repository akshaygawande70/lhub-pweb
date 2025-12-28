package com.ntuc.notification.audit.api.constants;

/**
 * STRICT email alert categories.
 *
 * <p>Rules:</p>
 * <ul>
 *   <li>Templates are keyed by this enum only (no free text).</li>
 *   <li>Service-layer email sending chooses template based on this enum only.</li>
 *   <li>UI may show friendly names but must persist subject/body by this enum mapping.</li>
 * </ul>
 */
public enum AlertEmailCategory {
    CLS_FAILURE,
    JA_FAILURE,
    DT5_FAILURE
}
