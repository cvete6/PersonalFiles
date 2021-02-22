package com.example.demo.Repository;

import com.example.demo.DomainModel.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Manipulate with person data in database
 */
public interface PersonJpaRepository extends JpaRepository<Person, Integer> {

    Page<Person> findAll(Pageable pageable);

    Optional<Person> findBySocialNumber(String socialNumber);

}
