package videoquotes;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
public class Application {
    public static void main(String[] args) {	
	
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

}
