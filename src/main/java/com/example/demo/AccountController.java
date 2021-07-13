package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {
	@Autowired
	HttpSession session;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ListRepository listRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RankRepository rankRepository;

	/**
	 * ログイン画面を表示
	 */
	@RequestMapping("/")
	public String login() {
		// セッション情報はクリアする
		session.invalidate();
		return "login";
	}

	/**
	 * ログインを実行
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView doLogin(
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			ModelAndView mv) {
		session.invalidate();
		// メールアドレスが空の場合にエラーとする
		if (email == null || email.length() == 0) {
			mv.addObject("message", "メールアドレスを入力してください");
			mv.setViewName("login");
			return mv;
		} else if (password == null || password.length() == 0) {
			//パスワードが空の場合エラーとする
			mv.addObject("message", "パスワードを入力してください");
			mv.setViewName("login");
			return mv;
		}

		Optional<User> user = userRepository.findByEmail(email);
		User u = user.get();

		//情報取得
		//メールアドレスが登録されているか確認する
		if (user.isEmpty()) {
			mv.addObject("message", "入力されたメールアドレスは登録されていません。");
			mv.setViewName("login");
			return mv;
		} else if (!u.getPassword().equals(password)) {
			//登録されたメールアドレスのパスワードと一致しているか確認する
			mv.addObject("message", "パスワードが正しくありません。");
			mv.setViewName("login");
			return mv;
		} else {
			//管理者の場合
			String manageremail = "manager@gmail.com";
			String managerpassword = "manager";
			if(email.equals(manageremail) && password.equals(managerpassword)) {
				//ユーザ情報を全件取得
				List<User> users = userRepository.findAll();
				session.setAttribute("users", users);

				//todolistを全件表示
				List<ToDoList> allList = listRepository.findAllByOrderByUseridAscCodeAsc();

				//ユーザを名前で表示
				List<User> userlist = userRepository.findAll();
				mv.addObject("userlist",userlist);

				//優先度を名前で表示
				List<Rank> ranklist = rankRepository.findAll();
				mv.addObject("ranklist", ranklist);

				//カテゴリーを名前で表示
				List<Category> categorylist = categoryRepository.findAll();
				mv.addObject("categorylist", categorylist);

				session.setAttribute("allList", allList);

				mv.setViewName("manager");
				return mv;
			}

			//一般ユーザの場合
			// セッションスコープにユーザ情報を格納する
			session.setAttribute("userInfo", user.get());
			session.setAttribute("category", categoryRepository.findAll());
			session.setAttribute("rank", rankRepository.findAll());

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

	/**
	 * ログアウトを実行
	 */
	@RequestMapping("/logout")
	public String logout() {
		// ログイン画面表示処理を実行するだけ
		return login();
	}
}
