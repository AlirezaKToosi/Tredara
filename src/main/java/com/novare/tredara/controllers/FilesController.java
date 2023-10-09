package com.novare.tredara.controllers;

import com.novare.tredara.services.IStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@Log4j2
public class FilesController {
    private static IStorageService iStorageService;

    public FilesController(IStorageService iStorageService) {

        FilesController.iStorageService = iStorageService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename, HttpServletRequest request) {
        Resource file = iStorageService.loadAsResource(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.error("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        log.info("Image file processed");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
