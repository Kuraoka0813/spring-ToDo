package com.example.demo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="todolist")
public class List {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	private Integer category_code;
	private String content;
	private Date date;
	private Integer rank;
	private Integer id;
	private String title;

	//コンストラクタ
	public List() {

	}
	public List(Integer code, Integer category_code, String content, Date date) {
		this.code = code;
		this.category_code = category_code;
		this.content = content;
		this.date = date;
	}

	public List(Integer category_code, String content, Date date, Integer rank, Integer id,
			String title) {
		this.category_code = category_code;
		this.content = content;
		this.date = date;
		this.rank = rank;
		this.id = id;
		this.title = title;
	}

	//アクセッサメソッド
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getCategory_code() {
		return category_code;
	}
	public void setCategory_code(Integer category_code) {
		this.category_code = category_code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
