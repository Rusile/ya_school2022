package com.rusile.ya_school.service;

import com.rusile.ya_school.dao.SystemItemHistoryRepository;
import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.entity.SystemItemHistory;
import com.rusile.ya_school.exception.NotFoundElementException;
import com.rusile.ya_school.http_classes.SystemItemHistoryResponse;
import com.rusile.ya_school.http_classes.SystemItemHistoryUnit;
import com.rusile.ya_school.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SystemItemHistoryServiceImpl implements SystemItemHistoryService {

    private final SystemItemHistoryRepository systemItemHistoryRepository;

    private final DataValidator validator;

    @Autowired
    public SystemItemHistoryServiceImpl(SystemItemHistoryRepository systemItemHistoryRepository, DataValidator validator) {
        this.systemItemHistoryRepository = systemItemHistoryRepository;
        this.validator = validator;
    }


    @Override
    public void save(List<SystemItemHistory> historyList) {
        systemItemHistoryRepository.saveAll(historyList);
    }

    @Override
    public SystemItemHistoryResponse history(String id, Instant dateStart, Instant dateEnd) {
        validator.validateId(id);
        validator.validateDates(dateStart, dateEnd);

        if (systemItemHistoryRepository.findAllByOwnerId(id).isEmpty()) {
            throw new NotFoundElementException();
        }

        List<SystemItemHistory> systemItemHistoriesList = systemItemHistoryRepository.findAllByOwnerIdAndDateGreaterThanEqualAndDateLessThan(id, dateStart, dateEnd);
        SystemItemHistoryResponse response = new SystemItemHistoryResponse();
        Set<SystemItemHistoryUnit> units = new HashSet<>();
        systemItemHistoriesList.forEach(p -> units.add(SystemItemHistoryUnit.valueOf(p)));
        response.setItems(units);
        return response;
    }
}
