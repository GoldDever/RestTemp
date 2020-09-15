package com.vagabond.LearnRestTemplate.service;

import com.vagabond.LearnRestTemplate.model.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestService {
    private final RestTemplate restTemplate;

    private String url = "http://91.241.64.178:7081/api/users";
    private String urlWithId = "http://91.241.64.178:7081/api/users/{id}";
    private String cookies;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(500))
                .setReadTimeout(Duration.ofSeconds(500))
                .build();
    } 

    public String getUsersPlainJSON() {
        return this.restTemplate.getForObject(url, String.class);
    }

    public User[] getUsersAsObject() {
        return this.restTemplate.getForObject(url, User[].class);
    }

    public User[] getUsersWithCookies() {
        // create headers
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<User[]> response = this.restTemplate.getForEntity(url, User[].class);

        cookies = response.getHeaders().get("Set-Cookie").get(0);

        System.out.println(cookies);

        return response.getBody();
    }

    public User getUserWithUrlParameters() {
        return this.restTemplate.getForObject(urlWithId, User.class, 1);
    }

    public User getUserWithResponseHandling() {
        ResponseEntity<User> response = this.restTemplate.getForEntity(urlWithId, User.class, 1);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public User createUser() {

        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("name", "James");
        map.put("lastName", "Brown");
        map.put("age", 73);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<User> response = this.restTemplate.postForEntity(url, entity, User.class);

        // check response status code
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public User createUserWithObject() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        // create a post object
        User user = new User((long) 3,"James", "Brown", (byte)73);

        // build the request
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        System.out.println("Created user: " + user);

        // send POST request
        return restTemplate.postForObject(url, entity, User.class);
    }

    public void updateUser() {

        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        // create a user object
        User user = new User("Thomas", "Shelby", (byte)50);

        // build the request
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        // send PUT request to update user with `id` 3
        this.restTemplate.put(urlWithId, entity, 3);
    }

    public User updateUserWithResponse() {

        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        // create user object
        User user = new User("Thomas", "Shelby", (byte)50);

        // build the request
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        // send PUT request to update post with `id` 10
        ResponseEntity<User> response = this.restTemplate.exchange(
                urlWithId, HttpMethod.PUT, entity, User.class, 3
        );

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public void deleteUser() {

        // send DELETE request to delete user with `id` 3
        this.restTemplate.delete(urlWithId, 3);
    }
}
