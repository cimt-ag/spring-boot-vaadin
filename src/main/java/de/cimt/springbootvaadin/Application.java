package de.cimt.springbootvaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
@Theme(value = "cimt")
@PWA(name = "Spring Boot Vaadin", shortName = "cimt", offlinePath = "offline.html", offlineResources = { "images/offline.png" })
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        log.info("Starting Spring Boot Vaadin application");
        SpringApplication.run(Application.class);
    }
}

