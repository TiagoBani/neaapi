package com.tiagobani.neaapi.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class FeedResponse {

    @JsonProperty("near_earth_objects")
    private Map<String, List<NeoItem>> neos;
}
//
//neo_reference_id,
//        name,
//        relative_velocity/kilometers_per_hour.