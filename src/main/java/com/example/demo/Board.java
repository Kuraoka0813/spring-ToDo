package com.example.demo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="board")
public class Board {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	private Integer listcode;
	@Column(name="date")
	private LocalDateTime date;
	private LocalDateTime time;
	private Integer userid;
	@Column(name="content")
	private String contents;

	//コンストラクタ
	public Board() {

	}

	public Board(Integer listcode, Integer userid, String contents) {
		this.listcode = listcode;
		this.date = LocalDateTime.now();
		this.time = LocalDateTime.now();
		this.userid = userid;
		this.contents = contents;
	}

	//アクセッサメソッド
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getListcode() {
		return listcode;
	}

	public void setListcode(Integer listcode) {
		this.listcode = listcode;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
