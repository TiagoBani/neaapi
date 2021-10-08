package com.tiagobani.neaapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagobani.neaapi.dtos.feed.request.FeedRequest;
import com.tiagobani.neaapi.dtos.feed.response.FeedResponse;
import com.tiagobani.neaapi.exceptions.NeaFeedException;
import com.tiagobani.neaapi.gateways.NeaGateway;
import com.tiagobani.neaapi.utils.FileUtil;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NeaServiceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private NeaGateway neaGateway;
    @Autowired
    private NeaService service;

    @Test
    public void shouldCanFilterValidNeoItem() throws IOException {
        String file = FileUtil.loadEmployeesWithSpringInternalClass("feed_one_date.json");
        var feedResponse = objectMapper.readValue(file, FeedResponse.class);
        var neoItemResponseList = feedResponse.getNeos().get("2020-12-12");

        var result = service.filterValidNeoItem(neoItemResponseList);

        assertNotNull(result);
        assertNotEquals(neoItemResponseList.size(), result.size());
        assertEquals(1, result.size());
        assertEquals("481817 (2008 UL90)", result.get(0).getName());
    }

    @Test
    public void shouldCanRequestNeasWithDateRangeValid() throws IOException {
        String startDateString = "2020-12-18";
        LocalDate startDate = LocalDate.parse(startDateString);
        FeedRequest feedRequest = new FeedRequest(startDate, startDate);
        String file = FileUtil.loadEmployeesWithSpringInternalClass("feed_range.json");
        var feedResponse = objectMapper.readValue(file, FeedResponse.class);
        when(neaGateway.getFeeds(anyString(), anyString())).thenReturn(feedResponse);

        var result = service.getFeedsByStartDateAndEndDate(feedRequest);

        assertNotNull(result);
        assertNotNull(result.getNeos());
        assertEquals(feedResponse.getNeos().size(), result.getNeos().size());
        assertEquals(22, result.getNeos().get(startDateString).size());
        assertEquals("417201 (2005 XM4)", result.getNeos().get(startDateString).get(0).getName());
        assertEquals(2417201, result.getNeos().get(startDateString).get(0).getNeoReferenceId());
        assertNotNull(result.getNeos().get(startDateString).get(0).getCloseApproachData());
        assertEquals(1, result.getNeos().get(startDateString).get(0).getCloseApproachData().size());
        assertNotNull(result.getNeos().get(startDateString).get(0).getCloseApproachData().get(0).getRelativeVelocity());
        assertEquals("65423.4227473554", result.getNeos().get(startDateString).get(0).getCloseApproachData().get(0).getRelativeVelocity().getKilometersPerHour());
    }

    @Test
    public void shouldCanNotRequestNeasWithNasaException() throws IOException {
        String nasaDateError = FileUtil.loadEmployeesWithSpringInternalClass("nasa_date_error.json");
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(HttpStatus.BAD_REQUEST.value());
        when(feignException.contentUTF8()).thenReturn(nasaDateError);
        when(neaGateway.getFeeds(anyString(), anyString())).thenThrow(feignException);

        assertThrows(NeaFeedException.class, () -> service.getFeedsByStartDateAndEndDate(new FeedRequest()));
    }

    @Test
    public void shouldCanNotRequestNeasWithDateRangeInvalid() {
        FeedRequest feedRequest = new FeedRequest(LocalDate.now(), LocalDate.now().plusMonths(1));

        assertThrows(NeaFeedException.class, () -> service.getFeedsByStartDateAndEndDate(feedRequest));
    }
}
