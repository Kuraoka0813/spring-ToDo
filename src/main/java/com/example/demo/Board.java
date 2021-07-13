package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="board")
public class Board {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	private Integer listcode;
	@Column(name="date")
	private LocalDateTime datetime;
	private Integer userid;
	@Column(name="content")
	private String contents;
	@Transient
	private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	//コンストラクタ
	public Board() {

	}

	public Board(Integer listcode, Integer userid, String contents) {
		this.listcode = listcode;
		this.datetime = LocalDateTime.now();
		this.userid = userid;
		this.contents = contents;
	}

	//アクセッサメソッド
	public static DateTimeFormatter getFmt() {
		return fmt;
	}

	public static void setFmt(DateTimeFormatter fmt) {
		Board.fmt = fmt;
	}

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

	public String getDatetime() {
		return datetime.format(fmt);
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
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
