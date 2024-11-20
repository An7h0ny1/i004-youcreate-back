package com.igrowker.miniproject.Collaborator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.igrowker.miniproject.Collaborator.Model.Collaborator;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    Optional<Collaborator> findByName(String name);
}
