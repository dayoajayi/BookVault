package com.example.BookVault;

import java.util.Optional;

public class TestOptional {
    public static void main(String[] args) {
        Optional<String> optionalString = Optional.of("Test");
        String result = optionalString.map(String::toUpperCase).orElse("default");
        System.out.println(result);  // Should print "TEST"
    }
}
