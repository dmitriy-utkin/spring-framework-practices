package com.example.fourth.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApiDescription() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local host url");

        Contact developerContact = new Contact();
        developerContact.setEmail("dm.utkin@icloud.com");
        developerContact.setName("Dmitriy UTKIN");
        developerContact.setUrl("https://github.com/dmitriy-utkin");

        Info info = new Info();
        info.setTitle("Spring course: 4th practice, API: News service");
        info.setVersion("1.0");
        info.setContact(developerContact);
        info.setDescription("Api to manage news (with link to author, comments, topics). " +
                "Was implemented validation of user`s privilege by Aspect, " +
                "implemented smth like authorisation with help of application config and setting th user name. \nEnjoy!");

        return new OpenAPI().info(info).servers(List.of(localServer));
    }
}
