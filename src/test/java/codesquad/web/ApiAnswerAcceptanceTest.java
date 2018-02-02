package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.dto.AnswerDto;
import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(ApiAnswerAcceptanceTest.class);

	@Autowired
	AnswerRepository answerRepository;

	@Test
	public void create() {
		String questionLocation = createQuestion();
		QuestionDto questionDto = getResource(questionLocation, QuestionDto.class);

		AnswerDto answerDto = new AnswerDto("답변 컨텐츠다아아아아");
		String location = createResource(questionLocation + "/answers", answerDto);

		AnswerDto dbAnswer = getResource(location, AnswerDto.class);
		assertThat(dbAnswer.getContents(), is(answerDto.getContents()));
	}

	@Test
	public void show() {
	}

	@Test
	public void delete() {
	}

	private String createQuestion() {
		QuestionDto question = new QuestionDto("질문이다아아아", "질문이다아아아아아");
		return createResource("/api/questions", question);
	}
}
