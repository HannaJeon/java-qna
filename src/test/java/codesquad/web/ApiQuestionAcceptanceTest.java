package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.dto.QuestionDto;
import codesquad.dto.UserDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import javax.xml.ws.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

	@Test
	public void create() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		QuestionDto newQuestion = new QuestionDto("질문1", "질문입니다");
		ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);

		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		String location = response.getHeaders().getLocation().getPath();

		QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);

		assertThat(dbQuestion.getTitle(), is(newQuestion.getTitle()));
		assertThat(dbQuestion.getContents(), is(newQuestion.getContents()));
		assertThat(location, is("/api/questions/" + dbQuestion.getId()));
	}

	@Test
	public void show() {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		QuestionDto newQuestion = new QuestionDto("질문이다", "질문이다아");
		ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);

		String location = response.getHeaders().getLocation().getPath();

		response = basicAuthTemplate().getForEntity(location, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}

//	@Test
//	public void update() throws Exception {
//		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
//
//	}
}
