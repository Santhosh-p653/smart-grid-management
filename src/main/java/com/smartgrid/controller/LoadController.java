package com.smartgrid.controller;

import com.smartgrid.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/load")
public class LoadController {

    @Autowired
    private LoadService loadService;

    @GetMapping("/analysis")
    public ResponseEntity<List<Map<String, Object>>> analyzeLoad() {
        return ResponseEntity.ok(loadService.analyzeLoad());
    }
}
