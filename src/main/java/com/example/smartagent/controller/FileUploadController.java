package com.example.smartagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @PostMapping("/upload")
    public void uploadFile(String fileName) {
        // Simulate file upload process
        logger.info("Uploading file: {}", fileName);
        // File upload logic goes here
        logger.info("File {} uploaded successfully.", fileName);
    }

}