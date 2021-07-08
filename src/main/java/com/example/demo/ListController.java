package com.example.demo;

import java.util.Date;
import java.util.List;
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


//	//指定したカテゴリコードの商品を表示
//	@RequestMapping(value="/list/{code}")
//	public ModelAndView listByCode(
//			@PathVariable(name="code") int categoryCode,
//			ModelAndView mv
//	) {
//		List<ToDoList> listByCategory_Code = listRepository.findByCategorycode(categoryCode);
//		session.setAttribute("todolists", listByCategory_Code);
//
//		mv.setViewName("list");
//		return mv;
//	}

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

		//Listの中身を取得
		List<ToDoList> record = listRepository.findByUserid(userid);

		//ToDoListの中身をセッションスコープに格納する
		session.setAttribute("todolists", record);

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

		System.out.println(Date);

		Date date = java.sql.Date.valueOf(Date);

		// ToDoListをDBに格納する
		ToDoList list = new ToDoList(category_code, content, date, rank, userid, title);
		listRepository.saveAndFlush(list);

		//Listの中身を取得
		List<ToDoList> record = listRepository.findByUserid(userid);

		//ToDoListの中身をセッションスコープに格納する
		session.setAttribute("todolists", record);

		mv.setViewName("list");

		return mv;
	}

	//削除
	@RequestMapping("/delete/{code}")
	public ModelAndView deleteList(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		// セッションスコープからリストの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		// データベースから削除
		listRepository.deleteById(code);

		//Listの中身を取得
		List<ToDoList> record = listRepository.findByUserid(userid);

		//ToDoListの中身をセッションスコープに格納する
		session.setAttribute("todolists", record);

		mv.setViewName("list");
		return mv;
	}
}
