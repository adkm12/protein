package gachon.protein;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@SpringBootApplication
public class ProteinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProteinApplication.class, args);
	}
	/*@GetMapping(value = "/")
	public String HelloWorld(){
		return "Hello World";
	}*/
}
