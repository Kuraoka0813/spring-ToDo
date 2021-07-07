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

	//編集画面に
	@RequestMapping("/update/{code}")
	public ModelAndView update(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		Optional<List> record = listRepository.findByCode(code);

		mv.addObject("record", record.get());

		mv.setViewName("update");
		return mv;
	}

	//編集
	@PostMapping("/update")
	public ModelAndView signupRegi(
			@RequestParam("content") String content,
			@RequestParam("date") Date date,
			@RequestParam("category_code") Integer category_code,
			@RequestParam("code") Integer code,
			ModelAndView mv) {
		// お客様情報をDBに格納する
		List list = new List(code, category_code, content, date);
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
			@RequestParam("date") Date date,
			@RequestParam("category_code") Integer category_code,
			@RequestParam("title") String title,
			ModelAndView mv) {
		User user = (User) session.getAttribute("todolists");
		Integer id = user.getId();

		// ToDoListをDBに格納する
		List list = new List(category_code, content, date, id, title);
		listRepository.saveAndFlush(list);

		mv.setViewName("list");

		return mv;
	}
}
