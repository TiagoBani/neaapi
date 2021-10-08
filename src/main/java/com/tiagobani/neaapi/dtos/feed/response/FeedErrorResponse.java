package com.tiagobani.neaapi.dtos.feed.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FeedErrorResponse {

    @JsonProperty("code")
    private Integer status;

    @JsonProperty("http_error")
    private String errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

}