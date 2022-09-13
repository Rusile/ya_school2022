package com.rusile.ya_school.dao;

import com.rusile.ya_school.entity.SystemItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SystemItemHistoryRepository extends JpaRepository<SystemItemHistory, Long> {
    List<SystemItemHistory> findAllByOwnerIdAndDateGreaterThanEqualAndDateLessThan(String id, Instant dateStart, Instant dateEnd);
    List<SystemItemHistory> findAllByOwnerId(String id);

}
