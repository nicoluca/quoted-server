package org.nico.quotedserver.util;

import org.springframework.http.ResponseEntity;

public class EntitiyNotFoundException extends RuntimeException {
        public EntitiyNotFoundException(String message) {
            super(message);
        }

        public static ResponseEntity<String> notFoundResponse(String message) {
            return new ResponseEntity<>(message, org.springframework.http.HttpStatus.NOT_FOUND);
        }
}
