package com.tiagobani.neaapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    @Builder.Default
    private Date timestamp = new Date();
    private String message;
    private String path;
    private String details;
}
