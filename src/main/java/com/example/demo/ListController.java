package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

	@Autowired
	LogRepository logRepository;

	//全リストを表示
	@RequestMapping("/list")
	public ModelAndView items(ModelAndView mv) {
		//ユーザ情報の取得
		User u = (User) session.getAttribute("userInfo");

		//ToDoListの中身をとる。
		Integer Userid = u.getId();
		List<ToDoList> record = listRepository.findByUserid(Userid);

		//ToDoListの中身をセッションスコープに格納する
		session.setAttribute("todolists", record);

		session.setAttribute("categoryCode", 0);

		mv.setViewName("list");
		return mv;
	}

	//指定したカテゴリコードのリストを表示
	@RequestMapping(value = "/list/{code}")
	public ModelAndView listByCode(
			@PathVariable(name = "code") int categoryCode,
			ModelAndView mv) {
		//ユーザ情報の取得
		User u = (User) session.getAttribute("userInfo");

		//ToDoListの中身をとる。
		Integer Userid = u.getId();
		List<ToDoList> listByCategoryCode = listRepository.findByUseridAndCategoryCode(Userid, categoryCode);
		session.setAttribute("todolists", listByCategoryCode);

		session.setAttribute("categoryCode", categoryCode);
		mv.setViewName("list");
		return mv;
	}

	//リストの並び替え
	@PostMapping("/sort")
	public ModelAndView sort(
			@RequestParam(name = "sort", defaultValue = "") String sort,
			ModelAndView mv) {
		//ユーザ情報の取得
		User u = (User) session.getAttribute("userInfo");

		//ToDoListの中身をとる。
		Integer Userid = u.getId();

		int categoryCode = (int) session.getAttribute("categoryCode");

		if (categoryCode == 0) {
			if (sort.equals("asc")) {
				//優先度のみでのソート
				List<ToDoList> todoList = listRepository.findByUseridOrderByRankAsc(Userid);
				session.setAttribute("todolists", todoList);
			} else if (sort.equals("until")) {
				//日付のみでのソート
				List<ToDoList> todoList = listRepository.findByUseridOrderByDateAsc(Userid);
				session.setAttribute("todolists", todoList);
			}
		} else {
			if (sort.equals("asc")) {
				//優先度のみでのソート
				List<ToDoList> todoList = listRepository.findByUseridAndCategoryCodeOrderByRankAsc(Userid,
						categoryCode);
				session.setAttribute("todolists", todoList);
			} else if (sort.equals("until")) {
				//日付のみでのソート
				List<ToDoList> todoList = listRepository.findByUseridAndCategoryCodeOrderByDateAsc(Userid,
						categoryCode);
				session.setAttribute("todolists", todoList);
			}
		}
		mv.setViewName("list");
		return mv;
	}

	//編集画面に
	@RequestMapping("/update/{code}")
	public ModelAndView update(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		Optional<ToDoList> record = listRepository.findById(code);
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
			@RequestParam(name = "rank", defaultValue = "1") Integer rank,
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
		// セッションスコープからユーザの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		//セッションスコープからリストから削除する項目を読み取る
		Optional<ToDoList> logList = listRepository.findByCode(code);
		ToDoList l = logList.get();
		Integer categoryCode = l.getCategoryCode();
		String content = l.getContent();
		Date date = l.getDate();
		Integer rank = l.getRank();
		String title = l.getTitle();

		Log list = new Log(categoryCode, content, date, rank, userid, title);

		//削除履歴に登録する
		logRepository.saveAndFlush(list);

		// データベースから削除
		listRepository.deleteById(code);

		//Listの中身を取得
		List<ToDoList> record = listRepository.findByUserid(userid);

		//ToDoListの中身をセッションスコープに格納する
		session.setAttribute("todolists", record);

		mv.setViewName("list");
		return mv;
	}

	//選択項目の削除
	@GetMapping("/selectdelete")
	public ModelAndView selectAdd(
			@RequestParam(name = "check", defaultValue = "") ArrayList<String> check,
			ModelAndView mv) {
		// セッションスコープからユーザの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		if (check != null || check.size() != 0) {
			for (int y = 0; y < check.size(); y++) {
				int x = Integer.parseInt(check.get(y));

				listRepository.deleteById(x);
			}
		}
		List<ToDoList> record = listRepository.findByUserid(userid);

		session.setAttribute("todolists", record);
		mv.setViewName("list");
		return mv;
	}
}
