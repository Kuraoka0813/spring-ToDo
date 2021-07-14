package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Autowired
	LogRepository logRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RankRepository rankRepository;

	@Autowired
	ShareListRepository sharelistRepository;

	@Autowired
	BoardRepository boardRepository;

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

		//現在表示しているカテゴリの情報
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
		//編集の対象のデータの取得
		Optional<ToDoList> record = listRepository.findById(code);
		ToDoList r = record.get();

		mv.addObject("record", record.get());
		mv.addObject("date", r.getDate());
		mv.addObject("category", r.getCategoryCode());
		mv.addObject("rank", r.getRank());
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
			mv.setViewName("update");
			return mv;
		}

		//編集の対象のテータの取得
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
	@PostMapping("/selectdelete")
	public ModelAndView selectAdd(
			@RequestParam(name = "check", defaultValue = "") ArrayList<String> check,
			ModelAndView mv) {
		// セッションスコープからユーザの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		//選択されている箇所の判定、削除、履歴への追加
		if (check != null || check.size() != 0) {
			for (int y = 0; y < check.size(); y++) {
				int x = Integer.parseInt(check.get(y));

				//セッションスコープからリストから削除する項目を読み取る
				Optional<ToDoList> logList = listRepository.findByCode(x);
				ToDoList l = logList.get();
				Integer categoryCode = l.getCategoryCode();
				String content = l.getContent();
				Date date = l.getDate();
				Integer rank = l.getRank();
				String title = l.getTitle();

				Log list = new Log(categoryCode, content, date, rank, userid, title);

				//削除履歴に登録する
				logRepository.saveAndFlush(list);

				//削除する
				listRepository.deleteById(x);
			}
		}
		List<ToDoList> record = listRepository.findByUserid(userid);

		session.setAttribute("todolists", record);
		mv.setViewName("list");
		return mv;
	}

	//削除履歴へ
	@RequestMapping("/log")
	public ModelAndView logList(ModelAndView mv) {
		// セッションスコープからユーザの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		//リストの検索
		List<Log> record = logRepository.findByUserid(userid);

		session.setAttribute("todolists", record);
		mv.setViewName("log");
		return mv;
	}

	//完全削除削除
	@RequestMapping("/deletelog/{code}")
	public ModelAndView deletelog(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		// セッションスコープからユーザの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		// データベースから削除
		logRepository.deleteById(code);

		//Listの中身を取得
		List<Log> record = logRepository.findByUserid(userid);

		//ToDoListの中身をセッションスコープに格納する
		session.setAttribute("todolists", record);

		mv.setViewName("log");
		return mv;
	}

	//選択して完全削除削除
	@RequestMapping("/selectdeletelog")
	public ModelAndView selectdeletelog(
			@RequestParam(name = "check", defaultValue = "") ArrayList<String> check,
			ModelAndView mv) {
		// セッションスコープからユーザの情報を取得する
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();

		//選択されている箇所の判定、削除
		if (check != null || check.size() != 0) {
			for (int y = 0; y < check.size(); y++) {
				int x = Integer.parseInt(check.get(y));

				//削除する
				logRepository.deleteById(x);
			}
		}
		List<Log> record = logRepository.findByUserid(userid);

		session.setAttribute("todolists", record);
		mv.setViewName("log");
		return mv;
	}

	//削除履歴の詳細画面に
	@RequestMapping("/detail/{code}")
	public ModelAndView deletedetail(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		//対象の履歴を取得
		Optional<Log> record = logRepository.findById(code);
		Log r = record.get();

		//カテゴリーの取得
		Optional<Category> c = categoryRepository.findById(r.getCategoryCode());
		Category _c = c.get();
		String categoty = _c.getName();

		//ランクの取得
		Optional<Rank> ra = rankRepository.findById(r.getRank());
		Rank _ra = ra.get();
		String rank = _ra.getName();

		//表示
		mv.addObject("record", record.get());
		mv.addObject("date", r.getDate());
		mv.addObject("category", categoty);
		mv.addObject("rank", rank);

		mv.setViewName("detail");
		return mv;
	}

	//共有への登録
	@RequestMapping("/share/{code}")
	public ModelAndView addShareList(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		//共有のデータの検索
		Optional<ToDoList> shareList = listRepository.findById(code);
		ToDoList s = shareList.get();
		Integer categoryCode = s.getCategoryCode();
		String content = s.getContent();
		Date date = s.getDate();
		Integer rank = s.getRank();
		Integer userid = s.getUserId();
		String title = s.getTitle();

		ShareList list = new ShareList(categoryCode, content, date, rank, userid, title);

		//共有用のデータベースに格納
		sharelistRepository.saveAndFlush(list);

		//登録した共有データの削除
		listRepository.deleteById(code);

		//共有後のリストの取得
		List<ToDoList> record = listRepository.findByUserid(userid);

		session.setAttribute("todolists", record);

		mv.setViewName("list");
		return mv;
	}

	//共有リスト確認画面に
	@RequestMapping("/shareList")
	public ModelAndView shareList(ModelAndView mv) {
		//共有のデータの全件検索、
		List<ShareList> sharelist = sharelistRepository.findAllByOrderByUseridAscCodeAsc();

		//ユーザを名前で表示
		List<User> userlist = userRepository.findAll();
		mv.addObject("userlist", userlist);

		//優先度を名前で表示
		List<Rank> ranklist = rankRepository.findAll();
		mv.addObject("ranklist", ranklist);

		//カテゴリーを名前で表示
		List<Category> categorylist = categoryRepository.findAll();
		mv.addObject("categorylist", categorylist);

		//表示
		session.setAttribute("shareList", sharelist);

		mv.setViewName("shareList");
		return mv;
	}

	//共有リストの詳細画面に
	@RequestMapping("/shareDetail/{code}")
	public ModelAndView shareListDetail(
			@PathVariable(name = "code") int code,
			ModelAndView mv) {
		//共有のデータの単一検索、
		Optional<ShareList> record = sharelistRepository.findById(code);
		ShareList r = record.get();

		mv.addObject("record", record.get());
		mv.addObject("date", r.getDate());
		mv.addObject("category", r.getCategoryCode());
		mv.addObject("rank", r.getRank());

		//掲示板の情報の取得
		List<Board> allContents = boardRepository.findByListcodeOrderByCodeDesc(code);
		mv.addObject("allContents", allContents);

		//ユーザを名前で表示
		List<User> userlist = userRepository.findAll();
		mv.addObject("userlist", userlist);

		//優先度を名前で表示
		List<Rank> ranklist = rankRepository.findAll();
		mv.addObject("ranklist", ranklist);

		//カテゴリーを名前で表示
		List<Category> categorylist = categoryRepository.findAll();
		mv.addObject("categorylist", categorylist);

		//投稿者なら編集できる、そうでなければ閲覧のみ
		//ユーザ情報取得
		User u = (User) session.getAttribute("userInfo");
		Integer userid = u.getId();
		//もし投稿者なら
		if (userid.equals(r.getUserId())) {
			mv.setViewName("shareupdate");
		} else {
			mv.setViewName("sharedetail");
		}

		//リストのコードの登録
		session.setAttribute("listcode", code);

		return mv;
	}

	@PostMapping(value = "/apply/update")
	public ModelAndView applyupdate(
			@RequestParam("contents") String contents,
			ModelAndView mv) {
		//表示しているリストのコードの取得
		Integer listcode = (Integer) session.getAttribute("listcode");

		if (contents == "") {
			String ErrorMsg = "書き込みを入力してください";
			mv.addObject("ErrorMsg", ErrorMsg);
		}

		//投稿したユーザidを取得
		User user = (User) session.getAttribute("userInfo");
		Integer userid = user.getId();

		//ユーザを名前で表示
		List<User> userlist = userRepository.findAll();
		mv.addObject("userlist", userlist);

		//正規表現パターンを指定
		Pattern p = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
		//対象の文字列を指定
		Matcher m = p.matcher(contents);
		String URL = "";
		while (m.find()) {
			//マッチした文字列を取得
			System.out.println(m.group());
			URL = m.group();
		}

		int result = contents.indexOf(URL);
		if (result != -1) {
			String start = contents.substring(0, result);
			String end = contents.substring(result + URL.length());
			String frontTag = "<a href='";
			String centerTag = "'>";
			String behindTag = "</a>";
			contents = start + frontTag + URL + centerTag + URL + behindTag + end;
		}

		if (contents != "") {
			//入力情報のDB登録
			Board record = new Board(listcode, userid, contents);
			boardRepository.saveAndFlush(record);

			//掲示板の情報の取得
			List<Board> allContents = boardRepository.findByListcodeOrderByCodeDesc(listcode);

			mv.addObject("allContents", allContents);
		}

		//共有のデータの単一検索、
		Optional<ShareList> record = sharelistRepository.findById(listcode);
		ShareList r = record.get();

		mv.addObject("record", record.get());
		mv.addObject("date", r.getDate());
		mv.addObject("category", r.getCategoryCode());
		mv.addObject("rank", r.getRank());

		mv.setViewName("shareupdate");
		return mv;
	}

	@PostMapping(value = "/apply/detail")
	public ModelAndView applydetail(
			@RequestParam("contents") String contents,
			ModelAndView mv) {
		//表示しているリストのコードの取得
		Integer listcode = (Integer) session.getAttribute("listcode");

		if (contents == "") {
			String ErrorMsg = "書き込みを入力してください";
			mv.addObject("ErrorMsg", ErrorMsg);
		}

		//投稿したユーザidを取得
		User user = (User) session.getAttribute("userInfo");
		Integer userid = user.getId();

		//ユーザを名前で表示
		List<User> userlist = userRepository.findAll();
		mv.addObject("userlist", userlist);

		//優先度を名前で表示
		List<Rank> ranklist = rankRepository.findAll();
		mv.addObject("ranklist", ranklist);

		//カテゴリーを名前で表示
		List<Category> categorylist = categoryRepository.findAll();
		mv.addObject("categorylist", categorylist);

		//正規表現パターンを指定
		Pattern p = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
		//対象の文字列を指定
		Matcher m = p.matcher(contents);
		String URL = "";
		while (m.find()) {
			//マッチした文字列を取得
			System.out.println(m.group());
			URL = m.group();
		}

		int result = contents.indexOf(URL);
		if (result != -1) {
			String start = contents.substring(0, result);
			String end = contents.substring(result + URL.length());
			String frontTag = "<a href='";
			String centerTag = "'>";
			String behindTag = "</a>";
			contents = start + frontTag + URL + centerTag + URL + behindTag + end;
		}

		if (contents != "") {
			//入力情報のDB登録
			Board record = new Board(listcode, userid, contents);
			boardRepository.saveAndFlush(record);

			//掲示板の情報の取得
			List<Board> allContents = boardRepository.findByListcodeOrderByCodeDesc(listcode);

			mv.addObject("allContents", allContents);
		}

		//共有のデータの単一検索、
		Optional<ShareList> record = sharelistRepository.findById(listcode);
		ShareList r = record.get();

		mv.addObject("record", r);

		mv.setViewName("sharedetail");
		return mv;
	}

}
