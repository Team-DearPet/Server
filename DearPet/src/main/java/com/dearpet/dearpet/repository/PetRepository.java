package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
