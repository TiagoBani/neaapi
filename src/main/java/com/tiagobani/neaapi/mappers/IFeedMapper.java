package com.tiagobani.neaapi.mappers;

import com.tiagobani.neaapi.dtos.feed.response.NeoItemResponse;
import com.tiagobani.neaapi.models.NearEarthObject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IFeedMapper {

    NearEarthObject sourceToDestination(NeoItemResponse neoItemResponse);

}
