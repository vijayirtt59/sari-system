package com.sari.system.application;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class StartupCheck {

    @PostConstruct
    public void verify() {

        File soffice = new File("/usr/bin/soffice");

        System.out.println(
                "LibreOffice exists = "
                        + soffice.exists()
        );
    }
}

