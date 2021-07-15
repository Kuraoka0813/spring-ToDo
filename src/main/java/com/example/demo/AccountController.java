package com.example.demo;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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

	@Autowired
	ShareListRepository sharelistRepository;

	/**
	 * ログイン画面を表示
	 */
	@RequestMapping("/")
	public String login() {
		// セッション情報はクリアする
		session.invalidate();

		//格言情報の登録
		String[] proverblist = {
				"そのことはできる、それをやる、と決断せよ。それからその方法を見つけるのだ。エイブラハム・リンカーン",
				"最も重要なことから始めよ。ピーター・ドラッカー",
				"何事であれ、最終的には自分で考える覚悟がないと、情報の山に埋もれるだけである。羽生善治",
				"我々は、最初から苦しむ方向をとったから、あとは楽になった。真似をして楽をしたものは、その後に苦しむことになる。本田宗一郎",
				"一方はこれで十分だと考えるが、もう一方はまだ足りないかもしれないと考える。そうしたいわば紙一枚の差が、大きな成果の違いを生む。松下幸之助",
				"最も重要な決定とは、何をするかではなく、何をしないかを決めることだ。スティーブ・ジョブズ",
				"考えなさい。調査し、探究し、問いかけ、熟考するのです。ウォルト・ディズニー",
				"目標を達成するためには、思い切って今の自分から脱皮しなくちゃ。バーバラ・ブラハム",
				"忙しさにこれで十分ということはない。蟻も忙しいのだ。問題は、何にそんなに忙しいのかということである。ソロー",
				"とにかく、とりかかれば心が燃え上がるし、続けていれば仕事は完成する。ゲーテ",
				"結果が出ないとき、どういう自分でいられるか。決してあきらめない姿勢が何かを生み出すきっかけをつくる。イチロー"};

		//ランダムで取得
		Random random = new Random();
		int r = random.nextInt(11);

		//ランダムで検索
		String proverb = proverblist[r];

		session.setAttribute("proverb", proverb);
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

		//DBからユーザ情報の検索
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
			mv.addObject("message", "パスワードは正しくありません。");
			mv.setViewName("login");
			return mv;
		} else {
			//管理者の場合
			String manageremail = "manager@gmail.com";
			String managerpassword = "manager";
			if(email.equals(manageremail) && password.equals(managerpassword)) {
				// セッションスコープにユーザ情報を格納する
				session.setAttribute("userInfo", user.get());
				session.setAttribute("category", categoryRepository.findAll());
				session.setAttribute("rank", rankRepository.findAll());

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

				//共有のデータの全件検索、
				List<ShareList> sharelist = sharelistRepository.findAllByOrderByUseridAscCodeAsc();


				//表示
				session.setAttribute("shareList", sharelist);


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

			//カテゴリー検索に使う
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
