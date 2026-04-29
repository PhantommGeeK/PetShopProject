package com.cg.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	 
    private final String resourceName;
    private final Object resourceId;
 
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(resourceName + " not found with identifier: " + resourceId);
        this.resourceName = resourceName;
        this.resourceId   = resourceId;
    }
 
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = "Resource";
        this.resourceId   = "N/A";
    }
 
    public String getResourceName() { return resourceName; }
    public Object getResourceId()   { return resourceId;   }
}