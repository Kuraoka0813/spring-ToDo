package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<List, Integer>{
	Optional<List> findById(Integer id);
	Optional<List> findByCode(Integer code);
}
