package com.harryitpro.phonebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private Environment environment;

    public String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }
}
