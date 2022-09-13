package com.rusile.ya_school.http_classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
public class SystemItemImportRequest {

    @Getter
    @Setter
    @NotNull
    @Size(min = 1)
    @Valid
    private List<SystemItemImport> items;

    @Getter
    @Setter
    @NotNull
    private Instant updateDate;
}
