package com.tiagobani.neaapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagobani.neaapi.dtos.request.FeedRequest;
import com.tiagobani.neaapi.dtos.response.CloseApproachData;
import com.tiagobani.neaapi.dtos.response.FeedError;
import com.tiagobani.neaapi.dtos.response.FeedResponse;
import com.tiagobani.neaapi.dtos.response.NeoItem;
import com.tiagobani.neaapi.exceptions.NeaFeedException;
import com.tiagobani.neaapi.gateways.NeaGateway;
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

    private final ObjectMapper mapper;
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
            var error = mapper.readValue(ex.contentUTF8(), FeedError.class);
            throw new NeaFeedException(error.getErrorMessage());
        }
    }

    public List<NeoItem> filterValidNeoItem(List<NeoItem> neoItems){
        return Optional.ofNullable(neoItems).orElseGet(ArrayList::new).parallelStream()
                .filter(this::validateNeoItem)
                .filter(neoItem -> validateCloseApproachData(neoItem.getCloseApproachData()))
                .map(neoItem -> {

                    neoItem.setIsPotentiallyHazardousAsteroid(null);
                    neoItem.getCloseApproachData().get(0).setOrbitingBody(null);

                    return neoItem;
                })
                .collect(Collectors.toList());
    }

    public Boolean validateNeoItem(NeoItem neoItem){
        return Objects.requireNonNullElse(neoItem.getIsPotentiallyHazardousAsteroid(), false);
    }
    public Boolean validateCloseApproachData(List<CloseApproachData> closeApproachData){
        return Optional.ofNullable(closeApproachData).orElseGet(ArrayList::new).parallelStream()
                .map(CloseApproachData::getOrbitingBody)
                .anyMatch(orbitingBody -> orbitingBody.equalsIgnoreCase("Earth"));
    }
}
