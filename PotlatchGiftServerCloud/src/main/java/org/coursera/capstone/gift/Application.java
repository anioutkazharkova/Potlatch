package org.coursera.capstone.gift;

import javax.servlet.MultipartConfigElement;

import org.coursera.capstone.gift.auth.OAuth2SecurityConfiguration;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableAutoConfiguration
//Tell Spring to automatically create a JPA implementation of our
//VideoRepository
@EnableDynamoDBRepositories
//Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
//so that requests can be routed to our Controllers)
@EnableWebMvc
//Tell Spring that this object represents a Configuration for the
//application
@Configuration
//Tell Spring to go and scan our controller package (and all sub packages) to
//find any Controllers or other components that are part of our applciation.
//Any class in this package that is annotated with @Controller is going to be
//automatically discovered and connected to the DispatcherServlet.
@ComponentScan
@Import(OAuth2SecurityConfiguration.class)
public class Application extends RepositoryRestMvcConfiguration {

	private static final String MAX_REQUEST_SIZE = "150MB";
	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
	// Tell Spring to launch our app!
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
    public MultipartConfigElement multipartConfigElement() {
		// Setup the application container to be accept multipart requests
		final MultiPartConfigFactory factory = new MultiPartConfigFactory();
		// Place upper bounds on the size of the requests to ensure that
		// clients don't abuse the web container by sending huge requests
		factory.setMaxFileSize(MAX_REQUEST_SIZE);
		factory.setMaxRequestSize(MAX_REQUEST_SIZE);

		// Return the configuration to setup multipart in the container
		return factory.createMultipartConfig();
	}
	// We are overriding the bean that RepositoryRestMvcConfiguration 
	// is using to convert our objects into JSON so that we can control
	// the format. The Spring dependency injection will inject our instance
	// of ObjectMapper in all of the spring data rest classes that rely
	// on the ObjectMapper. This is an example of how Spring dependency
	// injection allows us to easily configure dependencies in code that
	// we don't have easy control over otherwise.
	@Override
	public ObjectMapper halObjectMapper(){
		return new ResourcesMapper();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
    @Value("${AWS_ACCESS_KEY_ID}")
    private String amazonAWSAccessKey;

  @Value("${AWS_SECRET_KEY}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(
                amazonAWSCredentials());
       amazonDynamoDB.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

}
