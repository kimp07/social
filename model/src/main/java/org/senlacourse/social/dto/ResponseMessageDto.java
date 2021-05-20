package org.senlacourse.social.dto;

import lombok.Data;

@Data
public class ResponseMessageDto {

    private final String message;

    public ResponseMessageDto() {
        this.message = "Operation complete";
    }

    public ResponseMessageDto(String message) {
        this.message = message;
    }
}
