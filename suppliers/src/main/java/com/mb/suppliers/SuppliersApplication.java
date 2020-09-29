package com.mb.suppliers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
class Supplier{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;

}
@RepositoryRestResource
interface SupplierRepository extends JpaRepository<Supplier,Long>{

}
@Configuration
class KeycloakConfig {

	@Bean
	KeycloakSpringBootConfigResolver configResolver(){
		return new KeycloakSpringBootConfigResolver();
	}
}

@Configuration
class CorsFilter {

	@Bean
	public WebMvcConfigurer corsConfiguration() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods(HttpMethod.GET.toString(), HttpMethod.POST.toString(),
								HttpMethod.PUT.toString(), HttpMethod.DELETE.toString(), HttpMethod.OPTIONS.toString())
						.allowedOrigins("*");
			}
		};
	}
}
@KeycloakConfiguration
class KeycloakSpringSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(keycloakAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.authorizeRequests().antMatchers("/suppliers/**").hasAuthority("app-manager");
	}
}
@SpringBootApplication
public class SuppliersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuppliersApplication.class, args);
	}

	@Bean
	CommandLineRunner start(SupplierRepository supplierRepository, RepositoryRestConfiguration repositoryRestConfiguration){

		return args -> {
			repositoryRestConfiguration.exposeIdsFor(Supplier.class);
			supplierRepository.save(new Supplier(null,"Kendy shop", "kendy.shop@kendy.com"));
			supplierRepository.save(new Supplier(null,"Amazon", "amazon@amazon.com"));
			supplierRepository.save(new Supplier(null,"Namo Technology", "namo.tech@namo.com"));

			supplierRepository.findAll().forEach(s-> System.out.println(s.toString()));
		};
	}
}
