package com.rusile.ya_school.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rusile.ya_school.entity.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "system_item")
public class SystemItem {

    @Getter
    @Setter
    @Id
    @Column(name = "id")
    private String id;

    @Getter
    @Setter
    @Column(name = "url")
    private String url;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Getter
    @Setter
    @Column(name = "parent_id")
    private String parentId;

    @Getter
    @Setter
    @Column(name = "last_update_date", nullable = false)
    private Instant date;

    @Getter
    @Setter
    @Column(name = "size")
    private long size;

    @Setter
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    private Set<SystemItem> children;

    @JsonIgnore
    @Getter
    @Setter
    @OneToMany(mappedBy = "ownerItem", cascade = CascadeType.ALL)
    private Set<SystemItemHistory> history;

    public Set<SystemItem> getChildren() {
        if (type == Type.FILE) {
            return null;
        }
        return children;
    }
}
