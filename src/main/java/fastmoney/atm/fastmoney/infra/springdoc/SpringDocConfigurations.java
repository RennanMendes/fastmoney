package fastmoney.atm.fastmoney.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Fastmoney API")
                        .description(" Esta API oferece operações bancárias seguras e convenientes, incluindo" +
                                "    depósitos, saques, transferências e consulta de extrato. Os usuários precisam" +
                                "    estar autenticados e têm permissão para apenas uma conta por CPF. As operações" +
                                "    financeiras requerem um PIN válido. Há restrições, como saldo suficiente para" +
                                "    saques e transferências, limites de R$ 500,00 para saques e transferências entre" +
                                "    20h e 5h. A recuperação de senha é possível por email, e os usuários recebem notificações" +
                                "    após cada operação. Esta API oferece uma solução completa e segura para as necessidades" +
                                "    bancárias dos usuários.")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("por email")
                                .email("amrennan@gmail.com"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://www.mit.edu/~amini/LICENSE.md"))

                );

    }

}


