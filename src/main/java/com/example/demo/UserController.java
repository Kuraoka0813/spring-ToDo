package com.example.demo;

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
		// お客様情報をDBに格納する
		User user = new User(name, email, password);
		userRepository.saveAndFlush(user);

		mv.setViewName("login");

		return mv;
	}
}
