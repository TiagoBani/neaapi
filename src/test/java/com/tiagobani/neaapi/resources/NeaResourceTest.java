package com.tiagobani.neaapi.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagobani.neaapi.dtos.feed.response.FeedResponse;
import com.tiagobani.neaapi.gateways.NeaGateway;
import com.tiagobani.neaapi.utils.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NeaResourceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;// = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @MockBean
    private NeaGateway neaGateway;
//    @Autowired
//    private MockMvc mockMvc;

    private String baseUrl;

    @BeforeEach
    public void setup(){
        this.baseUrl = "http://localhost:" + port + "/nea/";
    }

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {

        String responseBody = this.restTemplate.getForObject(this.baseUrl + "/actuator/health", String.class);

        assertEquals(responseBody,"{\"status\":\"UP\"}");
    }

    @Test
    public void shouldCanProcessWithDateValid() throws Exception {
        String file = FileUtil.loadEmployeesWithSpringInternalClass("feed_range.json");
        var feedResponse = objectMapper.readValue(file, FeedResponse.class);
        when(neaGateway.getFeeds(anyString(), anyString())).thenReturn(feedResponse);
        String expectedBody = FileUtil.loadEmployeesWithSpringInternalClass("response_feed_one_date.json");

        var responseBody = this.restTemplate.getForObject(this.baseUrl+"/feed/2020-12-18", String.class);

        assertEquals(expectedBody, responseBody);
    }

    @Test
    public void shouldCanProcessWithDateRangeValid() throws Exception {
        String file = FileUtil.loadEmployeesWithSpringInternalClass("feed_range.json");
        var feedResponse = objectMapper.readValue(file, FeedResponse.class);
        when(neaGateway.getFeeds(anyString(), anyString())).thenReturn(feedResponse);
        String expectedBody = FileUtil.loadEmployeesWithSpringInternalClass("response_feed_range_date.json");

        var responseBody = this.restTemplate.getForObject(this.baseUrl+"/feed?startDate=2020-12-12&endDate=2020-12-19", String.class);

        assertEquals(expectedBody, responseBody);
    }
}
