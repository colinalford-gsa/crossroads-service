package gov.fbi.elabs.crossroads.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.fbi.elabs.crossroads.domain.Location;
import gov.fbi.elabs.crossroads.exception.BaseApplicationException;
import gov.fbi.elabs.crossroads.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = CRSController.BasePath+"/v1/location", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Location", description = "Location Operations")
public class LocationController {

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Fetch all workflow")
	public List<Location> getAllLocations() throws BaseApplicationException{
		return locationService.getAllLocations();
	}
	
	
}
