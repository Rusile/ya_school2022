package com.rusile.ya_school.http_classes;


import com.rusile.ya_school.entity.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@NoArgsConstructor
public class SystemItemImport {

    @Getter
    @Setter
    @NotNull
    private String id;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String parentId;

    @Getter
    @Setter
    @NotNull
    private Type type;

    @Getter
    @Setter
    private long size;
}
