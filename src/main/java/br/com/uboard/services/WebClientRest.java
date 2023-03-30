package br.com.uboard.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class WebClientRest {

	private RestTemplate restTemplate;

	public WebClientRest() {

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
		messageConverters.add(gsonHttpMessageConverter);
		this.restTemplate = new RestTemplate(messageConverters);
	}

	public HttpEntity<Object> getHttpEntity(Map<String, String> newHeaders) {
		HttpHeaders httpHeaders = this.getHttpHeaders(newHeaders);
		return new HttpEntity<>(null, httpHeaders);
	}

	private HttpHeaders getDefaultHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private HttpHeaders getHttpHeaders(Map<String, String> newHeaders) {
		HttpHeaders headers = getDefaultHeaders();
		for (Map.Entry<String, String> entry : newHeaders.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}

		return headers;
	}

	private HttpEntity<Object> getHttpEntity() {
		return new HttpEntity<>(null, getDefaultHeaders());
	}

	private <U> HttpEntity<?> getHttpEntity(U body) {
		return new HttpEntity<>(body, getDefaultHeaders());
	}

	public <T> ResponseEntity<T> get(String uri, Class<T> responseType) {
		return this.restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(), responseType);
	}

	public <T> ResponseEntity<T> get(String uri, HttpEntity<Object> httpEntity, Class<T> responseType) {
		return this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, responseType);
	}

	public <T> ResponseEntity<T> get(String uri, ParameterizedTypeReference<T> responseType) {
		return this.restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(), responseType);
	}

	public <T, U> ResponseEntity<T> post(String uri, Class<T> model, U body) {
		return this.restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(body), model);
	}

	public <T, U> ResponseEntity<T> put(String uri, Class<T> model, U body) {
		return this.restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(body), model);
	}

	public <T> ResponseEntity<T> delete(String uri, Class<T> responseType) {
		return this.restTemplate.exchange(uri, HttpMethod.DELETE, getHttpEntity(), responseType);
	}
}
