package com.vagabond.LearnRestTemplate.service;

import com.vagabond.LearnRestTemplate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Service
public class RestService {

    private RestTemplate restTemplate;

    @Autowired
    public RestService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    private String url = "http://91.241.64.178:7081/api/users";
    private String urlWithId = "http://91.241.64.178:7081/api/users/3";
    private String cookies;

    public User[] getUsersWithCookies() {

        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);

        cookies = response.getHeaders().get("Set-Cookie").get(0);

        return response.getBody();
    }

    public String createUser() throws URISyntaxException {

        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        User user = new User((long) 3, "James", "Brown", (byte) 73);

        // build the request
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.print("You code is: ");
        System.out.print(response.getBody());

        // check response status code
        if(response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public String updateUser() {

        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        // create user object
        User user = new User((long) 3,"Thomas", "Shelby", (byte)50);

        // build the request
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        // send PUT request to update post with `id` 3
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class, 3
        );

        System.out.print(response.getBody());

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public String deleteUser() {

        // create headers
        HttpHeaders headers = new HttpHeaders();

        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);

        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set `cookie` header
        headers.set("Cookie", cookies);

        // build the request
        HttpEntity<User> entity = new HttpEntity<>(headers);

        // send DELETE request to delete user with `id` 3
        ResponseEntity<String> response = restTemplate.exchange(
                urlWithId, HttpMethod.DELETE, entity, String.class
        );

        System.out.println(response.getBody());

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
