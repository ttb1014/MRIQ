package com.vervyle.mriqBackend.controllers;

import com.vervyle.mriqBackend.DataLoader;
import com.vervyle.mriqBackend.exception.InvalidDirectoryNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/frames")
public class FrameController {

    private final DataLoader dataLoader;

    @GetMapping("/{frameName}")
    public ResponseEntity<Resource> getFrames(@PathVariable String frameName) {
        var pathExists = dataLoader.directoryExists(frameName);
        if (!pathExists) {
            throw new InvalidDirectoryNameException("Directory with this specified name was not found.");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + frameName + ".zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dataLoader.getImages(frameName));
    }

}
