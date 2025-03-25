package com.green.jobdone.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
          title = "JobDone"
        , description = "잡던"
        , version = "v2"
    ),
    servers ={@Server(url="https://job-done.r-e.kr:52340")}
    , security = @SecurityRequirement(name = "Authorization")
)

@SecurityScheme(
          type = SecuritySchemeType.HTTP
        , name = "Authorization"
        , in = SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "Bearer"
)


public class SwaggerConfiguration {



}
