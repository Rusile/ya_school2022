package com.rusile.ya_school.dao;

import com.rusile.ya_school.entity.SystemItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SystemItemRepository extends JpaRepository<SystemItem, String> {
    List<SystemItem> findAllByDateBetween(Instant dateStart, Instant dateEnd);
    List<SystemItem> findAllByDate(Instant date);
}
