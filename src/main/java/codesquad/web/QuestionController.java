package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

	@Resource(name = "qnaService")
	private QnaService qnaService;

	@GetMapping("")
	public String showAll() {
		return "home";
	}

	@GetMapping("/form")
	public String form() {
		return "/qna/form";
	}

	@PostMapping("")
	public String create(@LoginUser User user, String title, String contents) {
		Question question = new Question(title, contents);
		qnaService.create(user, question);
		return "redirect:/questions";
	}

	@GetMapping("/{id}")
	public String show() {
		return "/qna/show";
	}

	@PutMapping("/{id}")
	public String update() {
		return "redirect:/questions";
	}

	@DeleteMapping("/{id}")
	public String delete() {
		return "redirect:/questions";
	}

}
