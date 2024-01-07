package com.gordonplumb.watchlist.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/list")
public class ListController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("blah");
    }
}
