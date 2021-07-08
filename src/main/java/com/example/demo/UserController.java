package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@Autowired
	UserRepository userRepository;

	//会員登録画面に遷移
	@RequestMapping("/signup")
	public ModelAndView signup(ModelAndView mv) {
		mv.setViewName("signup");
		return mv;
	}

	//新規登録
	@PostMapping("/signup")
	public ModelAndView signupRegi(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			ModelAndView mv) {
		//すべての項目に入力されているかチェックする

		if (name == null || name.length() == 0) {
			// 名前が空の場合にエラーとする
			mv.addObject("message", "名前を入力してください");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.setViewName("signup");
			return mv;
		}else if (email == null || email.length() == 0) {
			//メールアドレスが空の場合エラーとする
			mv.addObject("message", "メールアドレスを入力してください");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.setViewName("signup");
			return mv;
		} else if (password == null || password.length() == 0) {
			//パスワードが空の場合エラーとする
			mv.addObject("message", "パスワードを入力してください");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.setViewName("signup");
			return mv;
		}

		//既に登録されているメールアドレスか判定する
		//一致するメールアドレスがあるか検索する
		Optional<User> User = userRepository.findByEmail(email);

		if (User.isPresent()) {
			//もしあったら
			mv.addObject("message", "入力されたメールアドレスは既に登録されています。");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.setViewName("signup");
			return mv;
		} else {
			// もしなかったらユーザ情報をDBに格納する
			User user = new User(name, email, password);
			userRepository.saveAndFlush(user);

			mv.addObject("message", "ユーザ登録されました。");
			mv.setViewName("login");

			return mv;
		}
	}
}
