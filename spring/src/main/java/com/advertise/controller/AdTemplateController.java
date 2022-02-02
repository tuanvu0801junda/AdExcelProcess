package com.advertise.controller;

import com.advertise.entity.Advertisement;
import com.advertise.entity.Campaign;
import com.advertise.entity.ErrorExcel;
import com.advertise.service.AdExcelService;
import com.advertise.service.CampaignExcelService;
import com.advertise.service.ErrorExcelService;
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
import java.util.HashMap;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@CrossOrigin(origins = "http://localhost:3333")
@RestController
@RequestMapping(path = "/file")
public class AdTemplateController {

    //define a location to store files
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads";

    @Autowired
    private AdExcelService adExcelService;

    @Autowired
    private CampaignExcelService campaignExcelService;

    // upload files --> create --> post [CHECKED!]
    @PostMapping("/upload")
    public HashMap<String, Integer> uploadFiles(@RequestParam("excelFile") MultipartFile uploadedFile) throws IOException {
        HashMap<String,Integer> record = new HashMap<>();

        // Copy a file to Storage, replace if there was a same-name file
        String filename = uploadedFile.getOriginalFilename();
        Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
        copy(uploadedFile.getInputStream(), fileStorage, REPLACE_EXISTING);

        // Read sheet "Ad"
        List<Advertisement> ads = this.adExcelService.readAdFromExcel(filename);
        ArrayList<ErrorExcel> errAd = this.adExcelService.getErrList();

        // Read sheet "Campaign"
        List<Campaign> campaigns = this.campaignExcelService.readCampaignFromExcel(filename);
        ArrayList<ErrorExcel> errCampaign = this.campaignExcelService.getErrList();

        // Write error.xlsx if error happened
        ArrayList<ErrorExcel> both = new ArrayList<>();
        if (errCampaign.size() != 0) both.addAll(errCampaign);
        if (errAd.size() != 0) both.addAll(errAd);
        if (both.size() != 0) {
            ErrorExcelService errorExcelService = new ErrorExcelService();
            errorExcelService.writeErrorExcel(both);
            record.put("status", 500);
            return record;
        }

        //No error --> save to database
        this.adExcelService.insertNewAdsIntoDB(ads);
        this.campaignExcelService.insertCampaignsIntoDB(campaigns);

        // Return if no error
        record.put("status",200);
        return record;
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

//    public AdTemplateController(AdExcelService adExcelService, CampaignExcelService campaignExcelService){
//        this.adExcelService = adExcelService;
//        this.campaignExcelService = campaignExcelService;
//    }
}
