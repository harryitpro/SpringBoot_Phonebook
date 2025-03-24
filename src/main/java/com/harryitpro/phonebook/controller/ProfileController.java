package com.harryitpro.phonebook.controller;

import com.harryitpro.phonebook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @GetMapping
    public ResponseEntity<String> getActiveProfiles() {
        String[] profiles = profileService.getActiveProfiles();
        return new ResponseEntity<>(Arrays.toString(profiles), HttpStatus.OK);
    }
}
