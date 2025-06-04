package com.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meal.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserIdAndDeletedFalse(Long userId);


    Optional<Address> findByIdAndDeletedFalse(Long id);
}
