package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.dto.UserDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

	@Autowired
	private QuestionRepository questionRepository;

	private final QuestionDto question = new QuestionDto("질문이다아아아", "질문이다아아아아아");

	@Test
	public void create() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String location = createResource("/api/questions", question, basicAuthTemplate());

		QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);

		assertThat(dbQuestion.getTitle(), is(question.getTitle()));
		assertThat(dbQuestion.getContents(), is(question.getContents()));
		assertThat(location, is("/api/questions/" + dbQuestion.getId()));
	}

	@Test
	public void show() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String location = createResource("/api/questions", question, basicAuthTemplate());

		ResponseEntity<String> response = basicAuthTemplate().getForEntity(location, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}

	@Test
	public void update_owner() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String location = createResource("/api/questions", question, basicAuthTemplate());

		QuestionDto updateQuestion = new QuestionDto("바뀌었다", "바뀐질문이다아");
		basicAuthTemplate().put(location, updateQuestion);

		QuestionDto dbQuestion = getResource(location, QuestionDto.class, defaultUser());

		assertThat(dbQuestion.getTitle(), is(updateQuestion.getTitle()));
		assertThat(dbQuestion.getContents(), is(updateQuestion.getContents()));
	}

	@Test
	public void update_not_owner() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String location = createResource("/api/questions", question, basicAuthTemplate());

		QuestionDto updateQuestion = new QuestionDto("바뀌었다", "바뀐질문이다아");
		User user = new User("test", "test", "test", "test");
		basicAuthTemplate().put(location, QuestionDto.class, user);

		QuestionDto dbQuestion = getResource(location, QuestionDto.class, defaultUser());
		assertNotEquals(dbQuestion.getTitle(), updateQuestion.getTitle());
	}
}
