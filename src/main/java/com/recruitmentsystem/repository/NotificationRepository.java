package com.recruitmentsystem.repository;

import com.recruitmentsystem.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    int countByAccountId(Integer id);

    @Query("""
            select count(*) from Notifications n
            where n.isRead = false 
            and n.account.id = :id
            """)
    int countNewByAccountId(Integer id);

    @Query("""
            select n from Notifications n
            where n.account.id = :id
            """)
    List<Notifications> getAccountNotification(Integer id);

    @Query("""
            select n from Notifications n
            where n.account.id = :id
            """)
    Page<Notifications> getAccountNotificationPaging(Integer id, Pageable paging);
}
