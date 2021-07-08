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

	//全リストを表示
	@RequestMapping("/list")
	public ModelAndView items(ModelAndView mv) {
		List<ToDoList> todoList = listRepository.findAll();
		session.setAttribute("todolists", todoList);

		mv.setViewName("list");
		return mv;
	}

	//指定したカテゴリコードのリストを表示
	@RequestMapping(value = "/list/{code}")
	public ModelAndView listByCode(
			@PathVariable(name = "code") int categoryCode,
			ModelAndView mv) {
		List<ToDoList> listByCategoryCode = listRepository.findByCategoryCode(categoryCode);
		session.setAttribute("todolists", listByCategoryCode);

		mv.setViewName("list");
		return mv;
	}

	//リストの並び替え
	@RequestMapping("/sort")
	public ModelAndView sort(ModelAndView mv) {
		List<ToDoList> todoList = listRepository.findAllByOrderByRankAsc();
		session.setAttribute("todolists", todoList);

		mv.setViewName("list");
		return mv;
	}

	//編集画面に
	@RequestMapping("/update/{code}")
	public ModelAndView update(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		Optional<ToDoList> record = listRepository.findByCode(code);
		ToDoList r = record.get();

		mv.addObject("record", record.get());
		mv.addObject("date", r.getDate());
		mv.addObject("category", r.getCategoryCode());
		mv.setViewName("update");
		return mv;
	}

	//編集
	@PostMapping("/update")
	public ModelAndView signupRegi(
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam("date") String Date,
			@RequestParam("category") Integer categoryCode,
			@RequestParam("rank") Integer rank,
			@RequestParam("code") Integer code,
			ModelAndView mv) {
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		Date date = java.sql.Date.valueOf(Date);

		// 編集情報をDBに格納する
		ToDoList list = new ToDoList(code, categoryCode, content, date, rank, userid, title);
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
			@RequestParam("category") Integer categoryCode,
			@RequestParam(name= "rank" , defaultValue = "1") Integer rank,
			@RequestParam("title") String title,
			ModelAndView mv) {
		//未入力項目があった場合
		if (content == null || content.length() == 0 ||
				Date == null || Date.length() == 0 ||
				rank == null ||
				title == null || title.length() == 0) {
			// 名前が空の場合にエラーとする
			mv.addObject("message", "未入力の項目があります");
			mv.addObject("content", content);
			mv.addObject("date", Date);
			mv.addObject("rank", rank);
			mv.addObject("title", title);
			mv.addObject("category", categoryCode);
			mv.setViewName("addList");
			return mv;
		}

		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		System.out.println(Date);

		Date date = java.sql.Date.valueOf(Date);

		// ToDoListをDBに格納する
		ToDoList list = new ToDoList(categoryCode, content, date, rank, userid, title);
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
