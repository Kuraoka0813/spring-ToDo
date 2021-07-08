package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<ToDoList, Integer>{
	List<ToDoList> findByUserid(Integer userid);
	Optional<ToDoList> findByCode(Integer code);
	//List<ToDoList> findByCategorycode(Integer categorycode);
}
