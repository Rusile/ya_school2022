package com.rusile.ya_school.service;

import com.rusile.ya_school.dao.SystemItemRepository;
import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.entity.SystemItemHistory;
import com.rusile.ya_school.entity.enums.Type;
import com.rusile.ya_school.exception.NotFoundElementException;
import com.rusile.ya_school.http_classes.SystemItemHistoryResponse;
import com.rusile.ya_school.http_classes.SystemItemHistoryUnit;
import com.rusile.ya_school.http_classes.SystemItemImport;
import com.rusile.ya_school.http_classes.SystemItemImportRequest;
import com.rusile.ya_school.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SystemItemServiceImpl implements SystemItemService {

    private final SystemItemRepository systemItemRepository;

    private final DataValidator validator;

    private final SystemItemHistoryServiceImpl systemItemHistoryService;

    @Autowired
    public SystemItemServiceImpl(SystemItemRepository systemItemRepository, DataValidator validator, SystemItemHistoryServiceImpl systemItemHistoryService) {
        this.systemItemRepository = systemItemRepository;
        this.validator = validator;
        this.systemItemHistoryService = systemItemHistoryService;
    }

    @Override
    public SystemItem getById(String id) {
        validator.validateId(id);

        Optional<SystemItem> optional = systemItemRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundElementException();
        }

        return optional.get();
    }

    @Override
    public void imports(SystemItemImportRequest request) {
        Map<String, SystemItem> parents = systemItemRepository
                .findAllById(request.getItems()
                        .stream()
                        .map(SystemItemImport::getParentId)
                        .toList())
                .stream()
                .collect(Collectors.toMap(SystemItem::getId, Function.identity()));
        Map<String, SystemItem> updates = systemItemRepository
                .findAllById(request.getItems()
                        .stream()
                        .map(SystemItemImport::getId)
                        .toList())
                .stream()
                .collect(Collectors.toMap(SystemItem::getId, Function.identity()));
        validator.validateRequest(request, parents, updates);

        Set<SystemItem> requestSystemItems = toSystemItems(request);

        Instant date = request.getUpdateDate();

        addNewFolders(requestSystemItems, date, updates);

        addRemainingFilesAndFolders(requestSystemItems, updates, date);

        systemItemHistoryService.save(systemItemRepository.findAllByDate(date).stream()
                .map(SystemItemHistory::valueOf)
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(String id, Instant date) {
        validator.validateId(id);
        Optional<SystemItem> optional = systemItemRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundElementException();
        }
        SystemItem itemToDelete = optional.get();
        systemItemRepository.deleteById(id);
        long size = itemToDelete.getSize();
        String parentId = itemToDelete.getParentId();

        recursivelyUpdateNodes(parentId, -size, date);
    }

    @Override
    public SystemItemHistoryResponse updates(Instant date) {
        List<SystemItem> items = systemItemRepository.findAllByDateBetween(date.minus(24, ChronoUnit.HOURS), date);
        SystemItemHistoryResponse response = new SystemItemHistoryResponse();
        Set<SystemItemHistoryUnit> units = new HashSet<>();
        items.forEach(p -> units.add(SystemItemHistoryUnit.valueOf(p)));
        response.setItems(units);
        return response;
    }

    private void addRemainingFilesAndFolders(Set<SystemItem> requestSystemItems, Map<String, SystemItem> updates, Instant date) {
        for (SystemItem item : requestSystemItems) {
            String id = item.getId();
            long newSize = item.getSize();
            String newParentID = item.getParentId();

            if (updates.containsKey(id)) {
                SystemItem oldItem = updates.get(id);
                String oldParentID = oldItem.getParentId();
                long oldSize = oldItem.getSize();
                if (item.getType().equals(Type.FOLDER)) {
                    item.setSize(oldSize);
                    newSize=oldSize;
                }
                if (!Objects.equals(newParentID, oldParentID)) {
                    if (newParentID != null) recursivelyUpdateNodes(newParentID, newSize, date);
                    if (oldParentID != null) recursivelyUpdateNodes(oldParentID, -oldSize, date);
                //if (newParentID != null && !newParentID.equals(oldParentID) || (newParentID == null && oldParentID != null)) {
                } else {
                    long offset = newSize - oldSize;
                    recursivelyUpdateNodes(newParentID, offset, date);
                }
            } else {
                if (item.getType().equals(Type.FILE))
                    recursivelyUpdateNodes(newParentID, newSize, date);
            }
            if (item.getType().equals(Type.FILE) || updates.containsKey(id))
                systemItemRepository.save(item);
        }
    }


    private void addNewFolders(Set<SystemItem> requestItems, Instant date, Map<String, SystemItem> updates) {
        for (SystemItem item : requestItems) {
            if (item.getType() == Type.FILE || updates.containsKey(item.getId())) {
                continue;
            }

            systemItemRepository.save(item);
            String parentId = item.getParentId();
            while (parentId != null) {

                Optional<SystemItem> opt = systemItemRepository.findById(parentId);
                if (opt.isEmpty()) {
                    break;
                }
                SystemItem itemToUpdate = opt.get();
                itemToUpdate.setDate(date);
                systemItemRepository.save(itemToUpdate);

                parentId = itemToUpdate.getParentId();
            }

        }
    }

    private void recursivelyUpdateNodes(String parentId, long offset, Instant date) {
        while (parentId != null) {
            Optional<SystemItem> opt = systemItemRepository.findById(parentId);
            if (opt.isEmpty()) {
                break;
            }
            SystemItem itemToUpdate = opt.get();
            itemToUpdate.setDate(date);
            itemToUpdate.setSize(itemToUpdate.getSize() + offset);
            systemItemRepository.save(itemToUpdate);

            parentId = itemToUpdate.getParentId();
        }
    }


    private Set<SystemItem> toSystemItems(SystemItemImportRequest request) {
        Set<SystemItem> systemItems = new HashSet<>();
        Instant date = request.getUpdateDate();
        request.getItems().forEach(p -> {
            SystemItem item = new SystemItem();
            item.setId(p.getId());
            item.setUrl(p.getUrl());
            item.setParentId(p.getParentId());
            item.setType(p.getType());
            item.setSize(p.getSize());
            item.setDate(date);
            systemItems.add(item);
        });
        return systemItems;
    }

}
