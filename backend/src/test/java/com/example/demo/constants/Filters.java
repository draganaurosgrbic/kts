package com.example.demo.constants;

import org.springframework.stereotype.Component;

import com.example.demo.dto.FilterParamsDTO;

@Component
public class Filters {

	public FilterParamsDTO filtersEmpty() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(MainConstants.FILTER_ALL);
		filters.setLocation(MainConstants.FILTER_ALL);
		filters.setType(MainConstants.FILTER_ALL);
		return filters;
	}
	
	public FilterParamsDTO filtersAll() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(CulturalOfferConstants.FILTER_NAMES_ALL);
		filters.setLocation(CulturalOfferConstants.FILTER_LOCATIONS_ALL);
		filters.setType(CulturalOfferConstants.FILTER_TYPES_ALL);
		return filters;
	}
		
	public FilterParamsDTO filtersOne() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(MainConstants.FILTER_ONE);
		filters.setLocation(MainConstants.FILTER_ONE);
		filters.setType(MainConstants.FILTER_ONE);
		return filters;
	}
	
	public FilterParamsDTO filtersOneName() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(MainConstants.FILTER_ONE);
		filters.setLocation(MainConstants.FILTER_ALL);
		filters.setType(MainConstants.FILTER_ALL);
		return filters;
	}
	
	public FilterParamsDTO filtersOneLocation() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(MainConstants.FILTER_ALL);
		filters.setLocation(MainConstants.FILTER_ONE);
		filters.setType(MainConstants.FILTER_ALL);
		return filters;
	}
	
	public FilterParamsDTO filtersOneType() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(MainConstants.FILTER_ALL);
		filters.setLocation(MainConstants.FILTER_ALL);
		filters.setType(MainConstants.FILTER_ONE);
		return filters;
	}
	
	public FilterParamsDTO filtersNone() {
		FilterParamsDTO filters = new FilterParamsDTO();
		filters.setName(MainConstants.FILTER_ALL);
		filters.setLocation(MainConstants.FILTER_ALL);
		filters.setType(MainConstants.FILTER_NONE);
		return filters;
	}
	
}
