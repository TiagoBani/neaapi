package com.tiagobani.neaapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class NearEarthObject {

    @JsonProperty("neo_reference_id")
    private Long neoReferenceId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("close_approach_data")
    private List<CloseApproachData> closeApproachData;

}
