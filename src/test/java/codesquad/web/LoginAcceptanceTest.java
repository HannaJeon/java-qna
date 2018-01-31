package codesquad.web;

import codesquad.service.UserService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

	@Test
	public void 로그인_화면() {
		ResponseEntity<String> response = template().getForEntity("/users/login", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}

	@Test
	public void 로그인_성공() {
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("userId", "javajigi");
		params.add("password", "test");
		params.add("name", "자바지기");
		params.add("email", "javajigi@slipp.net");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
		assertThat(response.getHeaders().getLocation().getPath(), is("/users"));
	}

	@Test
	public void 로그인_실패() {
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("userId", "test");
		params.add("password", "test");
		params.add("name", "test");
		params.add("email", "test");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
}
