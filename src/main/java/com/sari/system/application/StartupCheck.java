package com.sari.system.application;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupCheck {

    @PostConstruct
    public void checkLibreOffice() {

        try {

            Process process =
                    new ProcessBuilder(
                            "which",
                            "soffice"
                    ).start();

            String output =
                    new String(
                            process.getInputStream()
                                    .readAllBytes()
                    );

            System.out.println(
                    "LibreOffice = " + output
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}

