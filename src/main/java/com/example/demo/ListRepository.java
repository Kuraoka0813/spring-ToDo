package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<ToDoList, Integer>{
	//ユーザidごとの検索
	List<ToDoList> findByUserid(Integer userid);

	//単一検索
	Optional<ToDoList> findByCode(Integer code);

	//カテゴリーコード検索
	List<ToDoList> findByUseridAndCategoryCode(Integer userid, Integer categoryCode);

	//優先度のみでのソート
	List<ToDoList> findAllByOrderByRankAsc();

	//日付のみでのソート
	List<ToDoList> findAllByOrderByDateAsc();
}
