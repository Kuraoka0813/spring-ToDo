package com.example.demo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="log")
public class Log {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	@Column(name="categorycode")
	private Integer categoryCode;
	private String content;
	private Date date;
	private Integer rank;
	private Integer userid;
	private String title;

	//コンストラクタ
	public Log() {

	}

	public Log(Integer categoryCode, String content, Date date, Integer rank, Integer userid,
			String title) {
		this.categoryCode = categoryCode;
		this.content = content;
		this.date = date;
		this.rank = rank;
		this.userid = userid;
		this.title = title;
	}

	public Log(Integer code, Integer categoryCode, String content, Date date, Integer rank, Integer userid,
			String title) {
		super();
		this.code = code;
		this.categoryCode = categoryCode;
		this.content = content;
		this.date = date;
		this.rank = rank;
		this.userid = userid;
		this.title = title;
	}
	//アクセッサメソッド
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
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
	public Integer getUserId() {
		return userid;
	}
	public void setUserId(Integer userid) {
		this.userid = userid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
