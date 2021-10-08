package com.tiagobani.neaapi.dtos.feed.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class NeoItemResponse {

    @JsonProperty("neo_reference_id")
    private Long neoReferenceId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("close_approach_data")
    private List<CloseApproachDataResponse> closeApproachData;

    @JsonProperty("is_potentially_hazardous_asteroid")
    private Boolean isPotentiallyHazardousAsteroid;

}
