package com.tiagobani.neaapi.dtos.feed.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {

    @JsonProperty("near_earth_objects")
    private Map<String, List<NeoItemResponse>> neos;
}
