package com.tiagobani.neaapi.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tiagobani.neaapi.dtos.feed.request.FeedRequest;
import com.tiagobani.neaapi.models.NearEarthObject;
import com.tiagobani.neaapi.services.NeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NeaResource {

    private final NeaService service;

    @GetMapping(value = "/feed")
    public ResponseEntity<Map<String, List<NearEarthObject>>> getFeed(@Valid FeedRequest feedRequest) throws JsonProcessingException {

        var neoItems = service.getFeedsByStartDateAndEndDate(feedRequest);

        Map<String, List<NearEarthObject>> result = new HashMap<>();
        for (String key: neoItems.getNeos().keySet()) {
            var neoItemsFiltered = service.filterValidNeoItem(neoItems.getNeos().get(key));
            result.put(key, neoItemsFiltered);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/feed/{requestDate}")
    public ResponseEntity<List<NearEarthObject>> getFeed(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @PathVariable( name = "requestDate") LocalDate requestDate
    ) throws JsonProcessingException {

        var neoItems = service.getFeedsByStartDateAndEndDate(new FeedRequest(requestDate, requestDate));
        var neoItemsFiltered = neoItems.getNeos().get(requestDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).parallelStream()
                .filter(service::validateNeoItem)
                .filter(neoItemResponse -> service.validateCloseApproachData(neoItemResponse.getCloseApproachData()))
                .map(service::mapToModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(neoItemsFiltered);
    }
}

