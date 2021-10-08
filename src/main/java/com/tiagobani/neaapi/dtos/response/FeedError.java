package com.tiagobani.neaapi.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FeedError {

    @JsonProperty("code")
    private Integer status;

    @JsonProperty("http_error")
    private String errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

}