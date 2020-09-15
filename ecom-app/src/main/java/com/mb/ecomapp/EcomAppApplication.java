package com.mb.ecomapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.facade.SimpleHttpFacade;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String designation;
	private double price;

}

@Data
@ToString
class Supplier{

	private Long id;
	private String name;
	private String email;

}

interface  ProductRepository extends JpaRepository<Product,Long> {

}

@Controller
class  ProductController{

	final ProductRepository productRepository;
	final AdapterDeploymentContext adapterDeploymentContext;
	final KeycloakRestTemplate keycloakRestTemplate;

	public ProductController(ProductRepository productRepository, AdapterDeploymentContext adapterDeploymentContext, KeycloakRestTemplate keycloakRestTemplate) {
		this.productRepository = productRepository;
		this.adapterDeploymentContext=adapterDeploymentContext;
		this.keycloakRestTemplate=keycloakRestTemplate;
	}

	@GetMapping(value = "/")
	public String index(){
		return "index";
	}

	@GetMapping(value = "/products")
	public String products(Model model){
		model.addAttribute("products", productRepository.findAll());
		return "products";
	}

	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request) throws ServletException {
       request.logout();
       return "redirect:/";
	}

	@GetMapping(value = "/changePassword")
	public String changePassword(RedirectAttributes atributes, HttpServletRequest request, HttpServletResponse response){
		HttpFacade facade = new SimpleHttpFacade(request,response);
		KeycloakDeployment deployment = adapterDeploymentContext.resolveDeployment(facade);
		atributes.addAttribute("referrer", deployment.getResourceName());
		return "redirect:"+ deployment.getAccountUrl() +"/password";
	}

	@GetMapping("/suppliers")
	public String suppliers(Model model){
		ResponseEntity<PagedModel<Supplier>> response;
		response = keycloakRestTemplate.exchange("http://localhost:8083/suppliers", HttpMethod.GET, null, new ParameterizedTypeReference<PagedModel<Supplier>>() {});
		model.addAttribute("supliers", response.getBody().getContent());
		return "suppliers";
	}
}

@Configuration
class KeycloakConfig {

	@Bean
	KeycloakSpringBootConfigResolver configResolver(){
		return new KeycloakSpringBootConfigResolver();
	}

	@Bean
	KeycloakRestTemplate  keycloakRestTemplate(KeycloakClientRequestFactory keycloakClientRequestFactory){
		return  new KeycloakRestTemplate(keycloakClientRequestFactory);
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
		http.authorizeRequests().antMatchers("/products/**").authenticated();
	}
}

@SpringBootApplication
public class EcomAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomAppApplication.class, args);
	}

	@Bean
	CommandLineRunner start(ProductRepository productRepository){
		return args -> {
			productRepository.save(new Product(null, "Macbook Pro", 5000));
			productRepository.save(new Product(null, "Iphone X", 6500));
			productRepository.save(new Product(null, "Huawei Mate ", 1200));

			productRepository.findAll().forEach(p-> System.out.println(p.toString()));


		};
	}
}
