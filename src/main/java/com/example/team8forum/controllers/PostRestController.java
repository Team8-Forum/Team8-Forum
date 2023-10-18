package com.example.team8forum.controllers;

import com.example.team8forum.helpers.PostMapper;
import com.example.team8forum.services.contracts.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService service;

    private final PostMapper postMapper;
}
