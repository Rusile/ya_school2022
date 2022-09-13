package com.rusile.ya_school.controller;

import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.http_classes.SystemItemHistoryResponse;
import com.rusile.ya_school.service.SystemItemHistoryServiceImpl;
import com.rusile.ya_school.service.SystemItemServiceImpl;
import com.rusile.ya_school.http_classes.SystemItemImportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.rusile.ya_school.http_classes.ResponseStatus;

import java.time.Instant;

@RestController
public class RESTController {


    private final SystemItemServiceImpl systemItemService;
    private final SystemItemHistoryServiceImpl systemItemHistoryService;


    @Autowired
    public RESTController(SystemItemServiceImpl systemItemServiceImpl, SystemItemHistoryServiceImpl systemItemHistoryService) {
        this.systemItemService = systemItemServiceImpl;
        this.systemItemHistoryService = systemItemHistoryService;
    }

    @PostMapping("/imports")
    public ResponseStatus imports(@RequestBody @Validated SystemItemImportRequest request) {
        systemItemService.imports(request);
        return new ResponseStatus(200, "Successfully imported");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseStatus delete(@PathVariable String id, @RequestParam Instant date) {
        systemItemService.delete(id, date);
        return new ResponseStatus(200, "Successfully deleted");
    }

    @GetMapping("/nodes/{id}")
    public SystemItem getById(@PathVariable String id) {
        return systemItemService.getById(id);
    }

    @GetMapping("/updates")
    public SystemItemHistoryResponse updates(@RequestParam Instant date) {
        return systemItemService.updates(date);
    }

    @GetMapping("/node/{id}/history")
    public SystemItemHistoryResponse history(@PathVariable String id, @RequestParam Instant dateStart, @RequestParam Instant dateEnd) {
        return systemItemHistoryService.history(id, dateStart, dateEnd);
    }
}
