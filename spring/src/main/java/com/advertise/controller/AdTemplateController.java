package com.advertise.controller;

import com.advertise.service.AdExcelHandler;
import com.advertise.service.CampaignExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping(path = "/file")
public class AdTemplateController {

    //define a location to store files
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads";

    // upload files --> create --> post [CHECKED!]
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String filename = file.getOriginalFilename();
            Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            // copy a file to Storage, replace if there was a same-name file
            filenames.add(filename);
        }
        return ResponseEntity.ok().body(filenames);
    }

    // download --> take --> get [CHECKED!]
    @GetMapping("download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " not found on server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    @Autowired
    private AdExcelHandler adExcelHandler;
    @Autowired
    private CampaignExcelHandler campaignExcelHandler;

    public AdTemplateController(AdExcelHandler adExcelHandler, CampaignExcelHandler campaignExcelHandler) {
        this.adExcelHandler = adExcelHandler;
        this.campaignExcelHandler = campaignExcelHandler;
    }
}
