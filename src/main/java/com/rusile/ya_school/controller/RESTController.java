package com.rusile.ya_school.controller;

import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.http_classes.SystemItemHistoryResponse;
import com.rusile.ya_school.service.SystemItemServiceImpl;
import com.rusile.ya_school.http_classes.SystemItemImportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.rusile.ya_school.http_classes.ResponseStatus;

import java.time.Instant;

@RestController
public class RESTController {


    private final SystemItemServiceImpl systemItemServiceImpl;

    @Autowired
    public RESTController(SystemItemServiceImpl systemItemServiceImpl) {
        this.systemItemServiceImpl = systemItemServiceImpl;
    }

    @PostMapping("/imports")
    public ResponseStatus imports(@RequestBody @Validated SystemItemImportRequest request) {
        systemItemServiceImpl.imports(request);
        return new ResponseStatus(200, "Successfully imported");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseStatus delete(@PathVariable String id, @RequestParam Instant date) {
        systemItemServiceImpl.delete(id, date);
        return new ResponseStatus(200, "Successfully deleted");
    }

    @GetMapping("/nodes/{id}")
    public SystemItem getById(@PathVariable String id) {
        return systemItemServiceImpl.getById(id);
    }

    @GetMapping("/updates")
    public SystemItemHistoryResponse updates(@RequestParam Instant date) {
        return systemItemServiceImpl.updates(date);
    }
}
