package com.ntuc.notification.audit.internal.core;

public interface AuditLogRepository {

    void persist(AuditRecord record);
}
