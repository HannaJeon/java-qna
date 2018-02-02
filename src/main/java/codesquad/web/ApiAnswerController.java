package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions/{questionid}/answers")
public class ApiAnswerController {
	private static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

	@Resource(name = "qnaService")
	private QnaService qnaService;

	@PostMapping("")
	public ResponseEntity<Void> create(@LoginUser User user, @PathVariable long questionid, @Valid @RequestBody Answer answer) {
		Answer saveAnswer = qnaService.addAnswer(user, questionid, answer);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(saveAnswer.generateUrl()));
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public AnswerDto show(@PathVariable long questionId, @PathVariable long id) {
		log.debug("가나다: {}");
		Answer answer = qnaService.findByIdAnswer(id);
		return answer.toAnswerDto();
	}
}
