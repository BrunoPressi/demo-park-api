package com.compass.demo_park_api.repository;

import com.compass.demo_park_api.entity.Customer;
import com.compass.demo_park_api.repository.projection.CustomerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c")
    Page<CustomerProjection> findAllPageable(Pageable pageable);

    Customer findByUserId(Long id);

    Optional<Customer> findByCpf(String cpf);
}
