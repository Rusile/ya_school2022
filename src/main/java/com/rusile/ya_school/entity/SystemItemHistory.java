package com.rusile.ya_school.entity;

import com.rusile.ya_school.entity.enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Entity
@Table(name = "history")
public class SystemItemHistory {
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id()
    @Column(name = "changes_id")
    private long id;

    @Getter
    @Setter
    @Column(name = "id")
    private String ownerId;

    @Getter
    @Setter
    @ManyToOne()
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private SystemItem ownerItem;

    @Getter
    @Setter
    @Column(name = "url")
    private String url;


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

    public static SystemItemHistory valueOf(SystemItem systemItem) {
        SystemItemHistory systemItemHistory = new SystemItemHistory();
        systemItemHistory.setOwnerId(systemItem.getId());
        systemItemHistory.setUrl(systemItem.getUrl());
        systemItemHistory.setDate(systemItem.getDate());
        systemItemHistory.setParentId(systemItem.getParentId());
        systemItemHistory.setSize(systemItem.getSize());
        return systemItemHistory;
    }
}
