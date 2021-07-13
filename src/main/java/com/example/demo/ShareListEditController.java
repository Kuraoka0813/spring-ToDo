package com.example.demo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("share")
public class ShareListEditController {
	@Autowired
	HttpSession session;

	@Autowired
	ListRepository listRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	LogRepository logRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RankRepository rankRepository;

	@Autowired
	ShareListRepository sharelistRepository;
	//共有画面での編集
	@PostMapping(value = "edit", params = "regi")
	public ModelAndView shareupdate(
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam("date") String Date,
			@RequestParam("category") Integer categoryCode,
			@RequestParam("rank") Integer rank,
			@RequestParam("code") Integer code,
			ModelAndView mv) {
		//未入力項目があった場合
		if (content == null || content.length() == 0 ||
				Date == null || Date.length() == 0 ||
				rank == null ||
				title == null || title.length() == 0) {
			// 名前が空の場合にエラーとする
			mv.addObject("message", "未入力の項目があります");

			//共有のデータの単一検索、
			Optional<ShareList> record = sharelistRepository.findById(code);
			ShareList r = record.get();

			mv.addObject("record", record.get());
			mv.addObject("date", r.getDate());
			mv.addObject("category", r.getCategoryCode());
			mv.addObject("rank", r.getRank());

			mv.setViewName("shareupdate");
			return mv;
		}

		//編集の対象のテータの取得
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		Date date = java.sql.Date.valueOf(Date);

		// 編集情報をDBに格納する
		ShareList list = new ShareList(code, categoryCode, content, date, rank, userid, title);
		sharelistRepository.saveAndFlush(list);

		//Listの中身を取得
		List<ShareList> record = sharelistRepository.findAllByOrderByUseridAscCodeAsc();

		//表示
		session.setAttribute("shareList", record);

		//ユーザを名前で表示
		List<User> userlist = userRepository.findAll();
		mv.addObject("userlist", userlist);

		//優先度を名前で表示
		List<Rank> ranklist = rankRepository.findAll();
		mv.addObject("ranklist", ranklist);

		//カテゴリーを名前で表示
		List<Category> categorylist = categoryRepository.findAll();
		mv.addObject("categorylist", categorylist);

		mv.setViewName("shareList");

		return mv;
	}

	//共有の取り消し
	@PostMapping(value = "edit", params = "rele")
	public ModelAndView removeshare(
			@RequestParam("code") Integer code,
			ModelAndView mv) {
		//共有のデータの検索
		Optional<ShareList> shareList = sharelistRepository.findById(code);
		ShareList s = shareList.get();
		Integer categoryCode = s.getCategoryCode();
		String content = s.getContent();
		Date date = s.getDate();
		Integer rank = s.getRank();
		Integer userid = s.getUserId();
		String title = s.getTitle();

		ToDoList list = new ToDoList(categoryCode, content, date, rank, userid, title);

		//共有用のデータベースに格納
		listRepository.saveAndFlush(list);

		//登録した共有データの削除
		sharelistRepository.deleteById(code);

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

}
