package com.hansol.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	@Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("API Docs")
                        .description("Scheduler Project :: API")
                        .version("0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Scheduler Project :: WEB")
                        .url("http://localhost:8080"));
    }
	
	@Bean
	public GroupedOpenApi buildGroupedOpenApi() {
		return GroupedOpenApi.builder()
			.group("group")
			.addOpenApiCustomiser(buildSecurityOpenApi())
			.build();
	}
	
	private OpenApiCustomiser buildSecurityOpenApi() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .getComponents().addSecuritySchemes("JWT", securityScheme);
    }
}