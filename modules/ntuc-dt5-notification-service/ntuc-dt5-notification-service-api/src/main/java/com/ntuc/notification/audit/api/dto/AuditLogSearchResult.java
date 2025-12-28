package com.ntuc.notification.audit.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DataTables-compatible result payload.
 *
 * Fields match DataTables server-side protocol:
 * - draw: echo back request draw
 * - recordsTotal: total rows (baseline scope: company+group)
 * - recordsFiltered: total rows AFTER filters/search
 * - data: page rows (safe subset for grid)
 */
public class AuditLogSearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int draw;
    private long recordsTotal;
    private long recordsFiltered;

    private final List<AuditLogRowDto> data = new ArrayList<>();

    public int getDraw() { return draw; }
    public void setDraw(int draw) { this.draw = draw; }

    public long getRecordsTotal() { return recordsTotal; }
    public void setRecordsTotal(long recordsTotal) { this.recordsTotal = recordsTotal; }

    public long getRecordsFiltered() { return recordsFiltered; }
    public void setRecordsFiltered(long recordsFiltered) { this.recordsFiltered = recordsFiltered; }

    public List<AuditLogRowDto> getData() { return Collections.unmodifiableList(data); }

    public void addRow(AuditLogRowDto row) {
        if (row != null) {
            data.add(row);
        }
    }

    public void addAll(List<AuditLogRowDto> rows) {
        if (rows != null) {
            for (AuditLogRowDto r : rows) {
                addRow(r);
            }
        }
    }
}
