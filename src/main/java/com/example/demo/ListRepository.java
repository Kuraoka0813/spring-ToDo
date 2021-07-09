package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListRepository extends JpaRepository<ToDoList, Integer> {
	//ユーザid昇順
	List<ToDoList> findByOrderByUseridAsc();

	//ユーザidごとの検索
	List<ToDoList> findByUserid(Integer userid);

	//単一検索
	Optional<ToDoList> findByCode(Integer code);

	//カテゴリーコード検索
	List<ToDoList> findByUseridAndCategoryCode(Integer userid, Integer categoryCode);

	//優先度のみでのソート
	List<ToDoList> findByUseridOrderByRankAsc(Integer userid);

	//カテゴリーコードでの優先度でのソート
	List<ToDoList> findByUseridAndCategoryCodeOrderByRankAsc(Integer userid, Integer categoryCode);

	//日付のみでのソート
	List<ToDoList> findByUseridOrderByDateAsc(Integer userid);

	//カテゴリーコードでの日付のみでのソート
	List<ToDoList> findByUseridAndCategoryCodeOrderByDateAsc(Integer userid, Integer categoryCode);
}
