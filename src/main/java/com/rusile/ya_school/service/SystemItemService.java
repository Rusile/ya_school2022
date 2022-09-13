package com.rusile.ya_school.service;

import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.http_classes.SystemItemHistoryResponse;
import com.rusile.ya_school.http_classes.SystemItemImportRequest;

import java.time.Instant;

public interface SystemItemService {
    SystemItem getById(String id);

    void imports(SystemItemImportRequest request);

    void delete(String id, Instant date);

    SystemItemHistoryResponse updates(Instant date);
}
