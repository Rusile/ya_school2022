package com.rusile.ya_school.http_classes;

import com.rusile.ya_school.entity.SystemItem;
import com.rusile.ya_school.entity.SystemItemHistory;
import com.rusile.ya_school.entity.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
public class SystemItemHistoryUnit {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String parentId;

    @Getter
    @Setter
    private Type type;

    @Getter
    @Setter
    private long size;

    @Getter
    @Setter
    private Instant date;

    public static SystemItemHistoryUnit valueOf(SystemItem systemItem) {
        SystemItemHistoryUnit unit = new SystemItemHistoryUnit();
        unit.setId(systemItem.getId());
        unit.setUrl(systemItem.getUrl());
        unit.setParentId(systemItem.getParentId());
        unit.setType(systemItem.getType());
        unit.setSize(systemItem.getSize());
        unit.setDate(systemItem.getDate());
        return unit;
    }

    public static SystemItemHistoryUnit valueOf(SystemItemHistory itemHistory) {
        SystemItemHistoryUnit unit = new SystemItemHistoryUnit();
        unit.setId(itemHistory.getOwnerId());
        unit.setUrl(itemHistory.getUrl());
        unit.setParentId(itemHistory.getParentId());
        unit.setType(itemHistory.getOwnerItem().getType());
        unit.setSize(itemHistory.getSize());
        unit.setDate(itemHistory.getDate());
        return unit;
    }
}
