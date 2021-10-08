package com.tiagobani.neaapi.gateways;

import com.tiagobani.neaapi.dtos.response.FeedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "neaGateway", url = "${nea-gateway.url}")
public interface NeaGateway {

    @RequestMapping(path = "/neo/rest/v1/feed?api_key=${nea-gateway.api_key}")
    FeedResponse getFeeds(@RequestParam("start_date") String startDate,
                          @RequestParam("end_date") String endDate);
}
