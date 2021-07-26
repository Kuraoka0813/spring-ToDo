package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class pokerController {
	@Autowired
	HttpSession session;

	@RequestMapping("/poker")
	public ModelAndView poker(ModelAndView mv) {
		session.invalidate();

		ArrayList<Integer> list = new ArrayList<Integer>();

		// listに値を入れる。この段階では昇順
		for (int i = 1; i <= 52; i++) {
			list.add(i);
		}

		// シャッフルして、順番を変える
		Collections.shuffle(list);

		session.setAttribute("list", list);

		//ハンド
		Integer h1 = list.get(1);
		Integer h2 = list.get(2);

		String suit1 = suit(h1);
		int num1 = num(h1);

		String suit2 = suit(h2);
		int num2 = num(h2);

		session.setAttribute("suit1", suit1);
		session.setAttribute("num1", num1);

		session.setAttribute("suit2", suit2);
		session.setAttribute("num2", num2);

		int x = 0;
		session.setAttribute("x", x);

		int y = 0;
		session.setAttribute("y", y);

		//CPUのBET
		Random random = new Random();
		int cpubet = (random.nextInt(9) + 1) * 10;
		session.setAttribute("cpubet", cpubet);

		session.setAttribute("pool", 0);

		//資金
		int total = 1000;
		session.setAttribute("total", total);

		mv.setViewName("poker");

		return mv;
	}

	@PostMapping("/frop")
	public ModelAndView frop(
			@RequestParam(name = "bet", defaultValue = "0") int bet,
			ModelAndView mv) {
		int cpubet2 = (int) session.getAttribute("cpubet");
		if (cpubet2 > bet) {
			bet = cpubet2;
		}

		if (cpubet2 < bet) {
			cpubet2 = bet;
		}

		int total = (int) session.getAttribute("total");
		total -= bet;
		session.setAttribute("total", total);

		//CPUのBET
		Random random = new Random();
		int cpubet = (random.nextInt(10)) * 10;
		session.setAttribute("cpubet", cpubet);

		int x = (int) session.getAttribute("x");
		x++;
		session.setAttribute("x", x);

		int pool = (int) session.getAttribute("pool");
		pool += bet * 2;
		session.setAttribute("pool", pool);

		ArrayList<Integer> list = new ArrayList<Integer>();

		list = (ArrayList<Integer>) session.getAttribute("list");

		if (x == 1) {
			//フロップ
			Integer h5 = list.get(5);
			Integer h6 = list.get(6);
			Integer h7 = list.get(7);

			String suit5 = suit(h5);
			int num5 = num(h5);

			String suit6 = suit(h6);
			int num6 = num(h6);

			String suit7 = suit(h7);
			int num7 = num(h7);

			session.setAttribute("suit5", suit5);
			session.setAttribute("num5", num5);

			session.setAttribute("suit6", suit6);
			session.setAttribute("num6", num6);

			session.setAttribute("suit7", suit7);
			session.setAttribute("num7", num7);
		} else if (x == 2) {
			Integer h8 = list.get(8);

			String suit8 = suit(h8);
			int num8 = num(h8);

			session.setAttribute("suit8", suit8);
			session.setAttribute("num8", num8);
		} else if (x == 3) {
			Integer h9 = list.get(9);

			String suit9 = suit(h9);
			int num9 = num(h9);

			session.setAttribute("suit9", suit9);
			session.setAttribute("num9", num9);
		}

		mv.setViewName("poker");

		return mv;
	}

	@RequestMapping("/open")
	public ModelAndView openhand(ModelAndView mv) {
		int x = (int) session.getAttribute("x");
		if (x < 3) {
			mv.setViewName("poker");
			return mv;
		}

		x++;
		session.setAttribute("x", x);

		int y = (int) session.getAttribute("y");
		y = 1;
		session.setAttribute("y", y);

		ArrayList<Integer> list = new ArrayList<Integer>();

		list = (ArrayList<Integer>) session.getAttribute("list");

		//ハンド
		Integer h1 = list.get(1);
		Integer h2 = list.get(2);

		String suit1 = suit(h1);
		int num1 = num(h1);

		String suit2 = suit(h2);
		int num2 = num(h2);

		//CPUハンド
		Integer h3 = list.get(3);
		Integer h4 = list.get(4);

		String suit3 = suit(h3);
		int num3 = num(h3);

		String suit4 = suit(h4);
		int num4 = num(h4);

		session.setAttribute("suit3", suit3);
		session.setAttribute("num3", num3);

		session.setAttribute("suit4", suit4);
		session.setAttribute("num4", num4);

		//フロップ
		Integer h5 = list.get(5);
		Integer h6 = list.get(6);
		Integer h7 = list.get(7);

		String suit5 = suit(h5);
		int num5 = num(h5);

		String suit6 = suit(h6);
		int num6 = num(h6);

		String suit7 = suit(h7);
		int num7 = num(h7);

		Integer h8 = list.get(8);

		String suit8 = suit(h8);
		int num8 = num(h8);

		Integer h9 = list.get(9);

		String suit9 = suit(h9);
		int num9 = num(h9);

		ArrayList<String> suitlist = new ArrayList<String>();
		suitlist.add(suit1);
		suitlist.add(suit2);
		suitlist.add(suit5);
		suitlist.add(suit6);
		suitlist.add(suit7);
		suitlist.add(suit8);
		suitlist.add(suit9);

		ArrayList<String> suitlist2 = new ArrayList<String>();
		suitlist2.add(suit3);
		suitlist2.add(suit4);
		suitlist2.add(suit5);
		suitlist2.add(suit6);
		suitlist2.add(suit7);
		suitlist2.add(suit8);
		suitlist2.add(suit9);

		int rank = 0;
		int ranksecond = 0;
		int rankthird = 0;
		int rank2 = 0;
		int ranksecond2 = 0;
		int rankthird2 = 0;

		ArrayList<Integer> numlist = new ArrayList<Integer>();
		numlist.add(num1);
		numlist.add(num2);
		numlist.add(num5);
		numlist.add(num6);
		numlist.add(num7);
		numlist.add(num8);
		numlist.add(num9);

		ArrayList<Integer> numlist2 = new ArrayList<Integer>();
		numlist2.add(num3);
		numlist2.add(num4);
		numlist2.add(num5);
		numlist2.add(num6);
		numlist2.add(num7);
		numlist2.add(num8);
		numlist2.add(num9);

		Collections.sort(numlist);
		Collections.sort(numlist2);

		//ワンペア・ツーペア・スリーカード・フルハウス
		for (var i = 0; i <= 5; i++) {
			if (numlist.get(i) == numlist.get(i + 1)) {
				if (rank == 2 && numlist.get(i) == numlist.get(i - 1)) {
					rank = 6;
					ranksecond = numlist.get(i);
				} else if (rank == 3) {
					rank = 6;
				} else if (rank == 0) {
					rank = 1;
					ranksecond = numlist.get(i);
					rankthird = numlist.get(i);
				} else if (rank != 6) {
					if (i >= 1) {
						if (numlist.get(i) == numlist.get(i - 1)) {
							rank = 3;
							ranksecond = numlist.get(i);
						} else {
							rank = 2;
							ranksecond = numlist.get(i);
						}
					}
				}
			}
		}

		for (var i = 0; i <= 5; i++) {
			if (numlist2.get(i) == numlist2.get(i + 1)) {
				if (rank2 == 2 && numlist2.get(i) == numlist2.get(i - 1)) {
					rank2 = 6;
					ranksecond2 = numlist2.get(i);
				} else if (rank2 == 3) {
					rank2 = 6;
				} else if (rank2 == 0) {
					rank2 = 1;
					ranksecond2 = numlist2.get(i);
					rankthird2 = numlist2.get(i);
				} else if (rank2 != 6) {
					if (i >= 1) {
						if (numlist2.get(i) == numlist2.get(i - 1)) {
							rank2 = 3;
							ranksecond2 = numlist2.get(i);
						} else {
							rank2 = 2;
							ranksecond2 = numlist2.get(i);
						}
					}
				}
			}
		}

		if (rank != 6) {
			//ストレート
			for (var i = 0; i <= 2; i++) {
				if (numlist.get(i) + 4 == numlist.get(i + 1) + 3
						&& numlist.get(i + 1) + 3 == numlist.get(i + 2) + 2
						&& numlist.get(i + 2) + 2 == numlist.get(i + 3) + 1
						&& numlist.get(i + 3) + 1 == numlist.get(i + 4)) {
					rank = 4;
					ranksecond = numlist.get(i + 4);
				}
			}
		}

		if (rank2 != 6) {
			for (var i = 0; i <= 2; i++) {
				if (numlist2.get(i) + 4 == numlist2.get(i + 1) + 3
						&& numlist2.get(i + 1) + 3 == numlist2.get(i + 2) + 2
						&& numlist2.get(i + 2) + 2 == numlist2.get(i + 3) + 1
						&& numlist2.get(i + 3) + 1 == numlist2.get(i + 4)) {
					rank2 = 4;
					ranksecond2 = numlist2.get(i + 4);
				}
			}
		}

		if (rank != 6) {
			//判定フラッシュかどうか
			for (var k = 0; k <= 6; k++) {
				for (var i = 0; i <= 6; i++) {
					int j = 0;
					if (suitlist.get(k).equals(suitlist.get(i))) {
						j++;
					}
					if (j >= 6) {
						ranksecond = numlist.get(i);
						rank = 5;
					}
				}
			}
		}

		if (rank2 != 6) {
			for (var k = 0; k <= 6; k++) {
				for (var i = 0; i <= 6; i++) {
					int j = 0;
					if (suitlist2.get(k).equals(suitlist2.get(i))) {
						j++;
					}
					if (j >= 6) {
						rank2 = 5;
						ranksecond2 = numlist2.get(i);
					}
				}
			}
		}

		if (rank == 4 && rank2 != 6) {

			//判定フラッシュかどうか
			for (var k = 0; k <= 6; k++) {
				for (var i = 0; i <= 6; i++) {
					int j = 0;
					if (suitlist.get(k).equals(suitlist.get(i))) {
						j++;
					}
					if (j >= 6) {
						ranksecond = numlist.get(i);
						rank = 7;
					}
				}
			}
		}

		String usermsg = "";
		if (rank == 0) {
			usermsg = "ぶたです。";
		} else if (rank == 1) {
			usermsg = "1ペアです。";
		} else if (rank == 2) {
			usermsg = "2ペアです。";
		} else if (rank == 3) {
			usermsg = "3カードです。";
		} else if (rank == 4) {
			usermsg = "ストレートです。";
		} else if (rank == 5) {
			usermsg = "フラッシュです。";
		} else if (rank == 6) {
			usermsg = "フルハウスです。";
		}
		session.setAttribute("usermsg", usermsg);

		String cpumsg = "";
		if (rank2 == 0) {
			cpumsg = "ぶたです。";
		} else if (rank2 == 1) {
			cpumsg = "1ペアです。";
		} else if (rank2 == 2) {
			cpumsg = "2ペアです。";
		} else if (rank2 == 3) {
			cpumsg = "3カードです。";
		} else if (rank2 == 4) {
			cpumsg = "ストレートです。";
		} else if (rank2 == 5) {
			cpumsg = "フラッシュです。";
		} else if (rank2 == 6) {
			cpumsg = "フルハウスです。";
		}
		session.setAttribute("cpumsg", cpumsg);

		if (rank == 0 && rank2 == 0) {
			for (var i = 6; i >= 0; i--) {
				if (numlist.get(i) > numlist2.get(i)) {
					rank++;
					break;
				} else if (numlist.get(i) < numlist2.get(i)) {
					rank2++;
					break;
				}
			}
		}

		int pool = (int) session.getAttribute("pool");
		int total = (int) session.getAttribute("total");

		String msg = "";
		if (rank > rank2) {
			msg = "あなたの勝ちです";
			total += pool;
		} else if (rank == rank2) {
			if (ranksecond > ranksecond2) {
				msg = "あなたの勝ちです";
				total += pool;
			} else if (ranksecond < ranksecond2) {
				msg = "あなたの負けです";
			} else if (rank == 1) {
				if (rank == 1 && rank2 == 1) {
					for (var i = 6; i >= 0; i--) {
						if (numlist.get(i) > numlist2.get(i)) {
							msg = "あなたの勝ちです";
							total += pool;
							break;
						} else if (numlist.get(i) < numlist2.get(i)) {
							msg = "あなたの負けです";
							break;
						}
					}
				} else if (rankthird > rankthird2) {
					msg = "あなたの勝ちです";
					total += pool;
				} else if (rankthird < rankthird2) {
					msg = "あなたの負けです";
				} else if (rank == 2 && rank2 == 2) {
					for (var i = 6; i >= 0; i--) {
						if (numlist.get(i) > numlist2.get(i)) {
							rank++;
							break;
						} else if (numlist.get(i) < numlist2.get(i)) {
							rank2++;
							break;
						}
					}

				}
			} else {
				msg = "引き分けです";
				total = total + pool / 2;
			}
		} else {
			msg = "あなたの負けです";
		}

		session.setAttribute("total", total);
		session.setAttribute("msg", msg);
		mv.setViewName("poker");

		return mv;
	}

	@RequestMapping("/top")
	public ModelAndView pokertop(ModelAndView mv) {

		ArrayList<Integer> list = new ArrayList<Integer>();

		// listに値を入れる。この段階では昇順
		for (int i = 1; i <= 52; i++) {
			list.add(i);
		}

		// シャッフルして、順番を変える
		Collections.shuffle(list);

		session.setAttribute("list", list);

		//ハンド
		Integer h1 = list.get(1);
		Integer h2 = list.get(2);

		String suit1 = suit(h1);
		int num1 = num(h1);

		String suit2 = suit(h2);
		int num2 = num(h2);

		session.setAttribute("suit1", suit1);
		session.setAttribute("num1", num1);

		session.setAttribute("suit2", suit2);
		session.setAttribute("num2", num2);

		int x = 0;
		session.setAttribute("x", x);

		int y = 0;
		session.setAttribute("y", y);

		//CPUのBET
		Random random = new Random();
		int cpubet = (random.nextInt(9) + 1) * 10;
		session.setAttribute("cpubet", cpubet);

		session.setAttribute("pool", 0);

		mv.setViewName("poker");

		return mv;
	}

	public String suit(int num) {
		String suit;

		if (num <= 13) {
			//suit = "&#x2666";
			suit = "&#x1f538;";
		} else if (num <= 26) {
			suit = "&#x2660";
		} else if (num <= 39) {
			//suit = "&#x2665";
			suit = "&#x1f9e1";
		} else {
			suit = "&#x2663";
		}
		return suit;

	}


	public int num(int num) {
		int number = num % 13 + 1;
		return number;
	}
}