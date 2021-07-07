package com.example.demo;

import java.util.Date;
import java.util.Optional;

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
	ListRepository listRepository;

	//
	@RequestMapping("/update/{code}")
	public ModelAndView update(
			@PathVariable(name="code") int code,
			ModelAndView mv) {
		Optional<List> record = listRepository.findById(code);

		mv.addObject("record", record);

		mv.setViewName("update");
		return mv;
	}


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
}
