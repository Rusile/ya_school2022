package com.rusile.ya_school.service;

import com.rusile.ya_school.entity.SystemItemHistory;
import com.rusile.ya_school.http_classes.SystemItemHistoryResponse;

import java.time.Instant;
import java.util.List;

public interface SystemItemHistoryService {
    void save(List<SystemItemHistory> historyList);
    SystemItemHistoryResponse history(String id, Instant dateStart, Instant dateEnd);
}
