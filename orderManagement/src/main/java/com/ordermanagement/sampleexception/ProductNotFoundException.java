package com.ordermanagement.sampleexception;

public class ProductNotFoundException extends Exception {
	public ProductNotFoundException(String message) {
        super(message);
    }
}
