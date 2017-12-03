package com.dolores.chief.api.controllers;

import com.dolores.chief.api.dto.UserPostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public ResponseEntity<Void> createUser(
        @RequestBody UserPostDto userPostDto
    ) {
        //TODO: implement
        return null;
    }
}
