package com.example.BookVault;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;


class BookVaultApplicationTests {
    @BeforeEach
    void setup() {
    }


    @Test
    void contextLoads() {

        ApplicationModules applicationModules = ApplicationModules.of(BookVaultApplication.class);

        applicationModules.verify();

        new Documenter(applicationModules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();

    }

}
