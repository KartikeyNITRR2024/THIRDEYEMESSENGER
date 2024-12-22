package com.thirdeye.thirdeyemessenger.externalcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.thirdeye.thirdeyemessenger.utils.AllMicroservicesData;

@Service
public class ThirdEye_Purse_Connection {
	@Value("${authenticationMicroserviceName}")
    private String authenticationMicroserviceName;
    
    @Autowired
	AllMicroservicesData allMicroservicesData;

    @Autowired
    private RestTemplate restTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(ThirdEye_Purse_Connection.class);
    
	public boolean authNonAdminUser(String token) throws Exception {
    	try {
    	    HttpHeaders headers = new HttpHeaders();
    	    headers.set("Content-Type", "application/json");
    	    headers.set("token", token);
    	    HttpEntity<Void> request = new HttpEntity<>(headers);
    	    
    	    String microserviceUrl = allMicroservicesData.allMicroservices.get(authenticationMicroserviceName).getMicroserviceUrl();
    	    Integer microserviceUniqueId = allMicroservicesData.allMicroservices.get(authenticationMicroserviceName).getMicroserviceUniqueId();
    	    Integer currentUniqueId = allMicroservicesData.current.getMicroserviceUniqueId();
    	    String url = microserviceUrl + "api/authmicroservices/" + microserviceUniqueId + "/nonadminuser";
    	    
    	    logger.info("Url to auth non-admin user {} : ", url);
    	    
    	    ResponseEntity<Boolean> response = restTemplate.exchange(
    	        url,
    	        HttpMethod.GET,
    	        request,
    	        Boolean.class
    	    );
    	    
    	    return response.getBody();
    	    
    	} catch (RestClientResponseException e) {
    	     String errorMessage = e.getMessage();
    	     logger.error("Received {} response: {}", e.getRawStatusCode(), errorMessage);
    	     throw new Exception(errorMessage);
    	} catch (Exception e) {
    	    logger.error("Unexpected error occurred: ", e);
    	    throw new Exception(e.getMessage());
    	}

    }
	
	public boolean authAdminUser(String token) throws Exception {
    	try {
    	    HttpHeaders headers = new HttpHeaders();
    	    headers.set("Content-Type", "application/json");
    	    headers.set("token", token);
    	    HttpEntity<Void> request = new HttpEntity<>(headers);
    	    
     	    String microserviceUrl = allMicroservicesData.allMicroservices.get(authenticationMicroserviceName).getMicroserviceUrl();
    	    Integer microserviceUniqueId = allMicroservicesData.allMicroservices.get(authenticationMicroserviceName).getMicroserviceUniqueId();
    	    Integer currentUniqueId = allMicroservicesData.current.getMicroserviceUniqueId();
    	    String url = microserviceUrl + "api/authmicroservices/" + microserviceUniqueId + "/adminuser";
    	    
    	    logger.info("Url to auth non-admin user {} : ", url);
    	    
    	    ResponseEntity<Boolean> response = restTemplate.exchange(
    	        url,
    	        HttpMethod.GET,
    	        request,
    	        Boolean.class
    	    );
    	    
    	    return response.getBody();
    	    
    	} catch (RestClientResponseException e) {
    	     String errorMessage = e.getMessage();
    	     logger.error("Received {} response: {}", e.getRawStatusCode(), errorMessage);
    	     throw new Exception(errorMessage);
    	} catch (Exception e) {
    	    logger.error("Unexpected error occurred: ", e);
    	    throw new Exception(e.getMessage());
    	}

    }
}



