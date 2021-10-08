package com.tiagobani.neaapi.dtos.feed.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CloseApproachDataResponse {

    @JsonProperty("relative_velocity")
    private RelativeVelocityResponse relativeVelocity;

    @JsonProperty("orbiting_body")
    private String orbitingBody;

}
