package com.tiagobani.neaapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagobani.neaapi.dtos.feed.request.FeedRequest;
import com.tiagobani.neaapi.dtos.feed.response.CloseApproachDataResponse;
import com.tiagobani.neaapi.dtos.feed.response.FeedErrorResponse;
import com.tiagobani.neaapi.dtos.feed.response.FeedResponse;
import com.tiagobani.neaapi.dtos.feed.response.NeoItemResponse;
import com.tiagobani.neaapi.exceptions.NeaFeedException;
import com.tiagobani.neaapi.gateways.NeaGateway;
import com.tiagobani.neaapi.mappers.IFeedMapper;
import com.tiagobani.neaapi.models.NearEarthObject;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NeaService {

    private final ObjectMapper objectMapper;
    private final IFeedMapper feedMapper;
    private final NeaGateway neaGateway;

    public FeedResponse getFeedsByStartDateAndEndDate(FeedRequest request) throws JsonProcessingException {
        LocalDate startDate = Optional.ofNullable(request.getStartDate()).orElse(LocalDate.now());
        LocalDate endDate = Optional.ofNullable(request.getEndDate()).orElse(startDate);
        if(Period.between(startDate, endDate).getDays() > 7)
            throw new NeaFeedException("The Feed date limit is only 7 Days");

        String startDateParsed = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String endDateParsed = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            return neaGateway.getFeeds(startDateParsed, endDateParsed);
        }catch (FeignException ex){
            log.info(ex.contentUTF8());
            var error = objectMapper.readValue(ex.contentUTF8(), FeedErrorResponse.class);
            throw new NeaFeedException(error.getErrorMessage());
        }
    }

    public List<NearEarthObject> filterValidNeoItem(List<NeoItemResponse> neoItemResponses){
        return Optional.ofNullable(neoItemResponses).orElseGet(ArrayList::new).parallelStream()
                .filter(this::validateNeoItem)
                .filter(neoItemResponse -> validateCloseApproachData(neoItemResponse.getCloseApproachData()))
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    public Boolean validateNeoItem(NeoItemResponse neoItemResponse){
        return Objects.requireNonNullElse(neoItemResponse.getIsPotentiallyHazardousAsteroid(), false);
    }

    public Boolean validateCloseApproachData(List<CloseApproachDataResponse> closeApproachDatumResponses){
        return Optional.ofNullable(closeApproachDatumResponses).orElseGet(ArrayList::new).parallelStream()
                .map(CloseApproachDataResponse::getOrbitingBody)
                .anyMatch(orbitingBody -> orbitingBody.equalsIgnoreCase("Earth"));
    }

    public NearEarthObject mapToModel(NeoItemResponse neoItemResponse){
        return feedMapper.sourceToDestination(neoItemResponse);
    }
}
