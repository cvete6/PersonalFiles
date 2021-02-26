package com.example.demo.Repository;

import com.example.demo.DomainModel.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Manipulate with Organization data in database
 */
public interface OrganizationJpaRepository extends JpaRepository<Organization, Integer> {
    Page<Organization> findAll(Pageable pageable);

    Optional<Organization> findByEmail(String email);
}
