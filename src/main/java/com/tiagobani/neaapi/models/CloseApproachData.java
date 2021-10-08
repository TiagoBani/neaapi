package com.tiagobani.neaapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CloseApproachData {

    @JsonProperty("relative_velocity")
    private RelativeVelocity relativeVelocity;

}
