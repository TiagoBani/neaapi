package com.tiagobani.neaapi.dtos.feed.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelativeVelocityResponse {

    @JsonProperty("kilometers_per_hour")
    private String kilometersPerHour;
}
