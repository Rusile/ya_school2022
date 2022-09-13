package com.rusile.ya_school.http_classes;

import com.rusile.ya_school.entity.SystemItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
public class SystemItemHistoryResponse {
    @Getter
    @Setter
    Set<SystemItemHistoryUnit> items;
}
