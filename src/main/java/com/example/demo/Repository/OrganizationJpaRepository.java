package com.example.demo.Repository;

import com.example.demo.DomainModel.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Manipulate with Organization data in database
 */
public interface OrganizationJpaRepository extends JpaRepository<Organization, Integer> {
}
