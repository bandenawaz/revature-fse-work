package org.revature.bookstoreapi.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    private final String resourcename;
    private final Long resourceId;

    public ResourceNotFoundException(String resourcename, Long resourceId) {
        super(resourcename+" with ID "+resourceId+" was not found");
        this.resourcename = resourcename;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String resourcename, Long resourceId, String identifier) {
        super(resourcename+" with identifier "+identifier+" was not found! ");
        this.resourcename = resourcename;
        this.resourceId = null;
    }

    public String getResourcename() {
        return resourcename;
    }

    public Long getResourceId() {
        return resourceId;
    }
}
