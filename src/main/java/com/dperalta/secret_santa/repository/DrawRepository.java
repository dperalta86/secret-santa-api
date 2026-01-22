package com.dperalta.secret_santa.repository;

import com.dperalta.secret_santa.model.Draw;
import com.dperalta.secret_santa.model.DrawStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

    Optional<Draw> findByCode(String code);

    boolean existsByCode(String code);

    List<Draw> findByStatus(DrawStatus status);

    @Query("SELECT d FROM Draw d LEFT JOIN FETCH d.participants WHERE d.code = :code")
    Optional<Draw> findByCodeWithParticipants(String code);
}