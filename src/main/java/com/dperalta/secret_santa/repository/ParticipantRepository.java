package com.dperalta.secret_santa.repository;

import com.dperalta.secret_santa.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByDrawId(Long drawId);

    Optional<Participant> findByDrawIdAndEmail(Long drawId, String email);

    boolean existsByDrawIdAndEmail(Long drawId, String email);

    List<Participant> findByDrawIdAndNotificationSentFalse(Long drawId);
}