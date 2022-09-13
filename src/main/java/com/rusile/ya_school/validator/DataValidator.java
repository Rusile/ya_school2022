package com.rusile.ya_school.validator;

import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.entity.enums.Type;
import com.rusile.ya_school.exception.BadSizeIdException;
import com.rusile.ya_school.exception.IllegalDateIntervalException;
import com.rusile.ya_school.exception.ValidationException;
import com.rusile.ya_school.exception.messages.ExceptionMessageEnum;
import com.rusile.ya_school.http_classes.SystemItemImport;
import com.rusile.ya_school.http_classes.SystemItemImportRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class DataValidator {

    private final Map<Integer, List<String>> errorsMap = new HashMap<>();

    private void checkUniqueId(List<SystemItemImport> imports) {
        Set<String> uniqueIds = new HashSet<>();
        imports.forEach(p -> uniqueIds.add(p.getId()));
        if (uniqueIds.size() != imports.size()) {
            errorsMap.put(0, Collections.singletonList(ExceptionMessageEnum.ID_NOT_UNIQUE.getMessage()));
        }
    }

    private String checkSize(SystemItemImport itemImport) {
        if (itemImport.getType().equals(Type.FOLDER) && itemImport.getSize() != 0) {
            return ExceptionMessageEnum.FOLDER_SIZE.getMessage();
        }
        if (itemImport.getType().equals(Type.FILE) && itemImport.getSize() == 0) {
            return ExceptionMessageEnum.FILE_SIZE.getMessage();
        }
        return null;
    }

    private String checkUrlSize(SystemItemImport itemImport) {
        if (itemImport.getUrl().length() > 255) {
            return ExceptionMessageEnum.URL_SIZE.getMessage();
        }

        return null;
    }

    private String checkUrl(SystemItemImport itemImport) {
        if (itemImport.getType().equals(Type.FOLDER) && itemImport.getUrl() != null) {
            return ExceptionMessageEnum.URL_NOT_NULL.getMessage();
        }
        if (itemImport.getType().equals(Type.FILE) && itemImport.getUrl() == null) {
            return ExceptionMessageEnum.URL_NULL.getMessage();
        }

        if (itemImport.getType().equals(Type.FILE)) {
            return checkUrlSize(itemImport);
        }
        return null;
    }

    private String checkParentExistence(SystemItemImport itemImport, Map<String, SystemItem> parents, List<SystemItemImport> itemsFromRequest) {
        if (itemImport.getParentId() == null) {
            return null;
        }
        SystemItemImport parentInRequest = null;
        Optional<SystemItemImport> parentInRequestOpt = itemsFromRequest.stream().filter(p -> p.getId().equals(itemImport.getParentId())).findFirst();
        if (parentInRequestOpt.isPresent()) {
           parentInRequest = parentInRequestOpt.get();
        }

        SystemItem parentInDB = parents.get(itemImport.getParentId());
        if (parentInDB == null && parentInRequest == null) {
            return ExceptionMessageEnum.PARENT_NOT_EXISTS.getMessage();
        }

        if ((parentInDB != null && parentInDB.getType().equals(Type.FILE)) || (parentInRequest != null && parentInRequest.getType().equals(Type.FILE))) {
            return ExceptionMessageEnum.PARENT_IS_FILE.getMessage();
        }

        return null;
    }

    private String checkCast(SystemItemImport itemImport, Map<String, SystemItem> updates) {
        String itemId = itemImport.getId();

        if (!updates.containsKey(itemId)) {
            return null;
        }

        Type itemType = itemImport.getType();
        Type dbType = updates.get(itemId).getType();

        if (!itemType.equals(dbType)) {
            return ExceptionMessageEnum.CAST_NOT_ALLOWED.getMessage();
        }

        return null;
    }

    public void validateRequest(SystemItemImportRequest request, Map<String, SystemItem> parents, Map<String, SystemItem> updates) {
        errorsMap.clear();
        List<SystemItemImport> imports = request.getItems();
        checkUniqueId(imports);

        List<String> mesList = new ArrayList<>();

        for (int i = 0; i < imports.size(); i++) {
            mesList.clear();

            SystemItemImport currentItemImport = imports.get(i);

            String error = checkSize(currentItemImport);
            if (error != null) {
                mesList.add(error);
            }

            error = checkCast(currentItemImport, updates);
            if (error != null) {
                mesList.add(error);
            }

            error = checkParentExistence(currentItemImport, parents, request.getItems());
            if (error != null) {
                mesList.add(error);
            }

            error = checkUrl(currentItemImport);
            if (error != null) {
                mesList.add(error);
            }
            if (!mesList.isEmpty()) {
                errorsMap.put(i + 1, mesList);
            }
        }
        if (!errorsMap.isEmpty()) {
            throw new ValidationException(errorsMap);
        }

    }

    public void validateId(String id) {
        if (id.length() > 255) {
            throw new BadSizeIdException();
        }
    }

    public void validateDates(Instant dateStart, Instant dateEnd) {
        if (!dateStart.isBefore(dateEnd)) {
            throw new IllegalDateIntervalException();
        }
    }
}
