package com.example.demo;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListController {
	@Autowired
	HttpSession session;

	@Autowired
	ListRepository listRepository;

	@Autowired
	UserRepository userRepository;

	//編集画面に
	@RequestMapping("/update/{code}")
	public ModelAndView update(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		Optional<ToDoList> record = listRepository.findByCode(code);

		mv.addObject("record", record.get());

		mv.setViewName("update");
		return mv;
	}

	//編集
	@PostMapping("/update")
	public ModelAndView signupRegi(
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam("date") String Date,
			@RequestParam("category_code") Integer category_code,
			@RequestParam("rank") Integer rank,
			@RequestParam("code") Integer code,
			ModelAndView mv) {
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		Date date = java.sql.Date.valueOf(Date);

		// 編集情報をDBに格納する
		ToDoList list = new ToDoList(code, category_code, content, date, rank, userid, title);
		listRepository.saveAndFlush(list);

		mv.setViewName("list");

		return mv;
	}

	//追加登録画面に
	@RequestMapping("/addList")
	public ModelAndView addList(ModelAndView mv) {

		mv.setViewName("addList");
		return mv;
	}

	//追加機能
	@PostMapping("/addList")
	public ModelAndView addRegi(
			@RequestParam("content") String content,
			@RequestParam("date") String Date,
			@RequestParam("category_code") Integer category_code,
			@RequestParam("rank") Integer rank,
			@RequestParam("title") String title,
			ModelAndView mv) {
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		Date date = java.sql.Date.valueOf(Date);

		// ToDoListをDBに格納する
		ToDoList list = new ToDoList(category_code, content, date, rank, userid, title);
		listRepository.saveAndFlush(list);

		mv.setViewName("list");

		return mv;
	}
}
