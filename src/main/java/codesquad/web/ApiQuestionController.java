package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.HttpSessionUtils;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionController.class);

	@Resource(name = "qnaService")
	private QnaService qnaService;

	@PostMapping("")
	public ResponseEntity<Void> create(@Valid @RequestBody Question question, @LoginUser User user) {
		Question saveQuestion = qnaService.create(user, question);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create("/api/questions/" + saveQuestion.getId()));
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public QuestionDto show(@PathVariable long id) {
		Question question = qnaService.findById(id);
		return question.toQuestionDto();
	}

}
