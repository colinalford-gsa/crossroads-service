package gov.fbi.elabs.crossroads.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.fbi.elabs.crossroads.domain.Employee;
import gov.fbi.elabs.crossroads.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = CRSController.BasePath + "/v1/employee", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Employee", description = "Employee Operations")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Fetch Employee Details either by ids or emailIds")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "Provide Employee ids", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "exceptIds", value = "Provide Employee already selected as witness", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "emailIds", value = "Provide email ids to be retrieved", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "Provide status of the Transfer Type", dataType = "string", paramType = "query", allowableValues = "Everything,Active,Inactive", defaultValue = "Everything") })
	public ResponseEntity<Resources<Employee>> getEmployeeDetails(
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "exceptIds", required = false) String exceptIds,
			@RequestParam(value = "emailIds", required = false) String emailIds,
			@RequestParam(value = "status", required = false) String status) {

		if ((StringUtils.isNotEmpty(ids) && StringUtils.isNotEmpty(emailIds))
				|| (StringUtils.isNotEmpty(ids) && StringUtils.isNotEmpty(exceptIds))
				|| (StringUtils.isNotEmpty(emailIds) && StringUtils.isNotEmpty(exceptIds))) {
			logger.error("ids " + ids + " status");
			return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
		}

		List<Employee> employeeList = employeeService.getEmployees(ids, exceptIds, emailIds, status);
		for (Employee employee : employeeList) {
			employee.add(
					linkTo(methodOn(EmployeeController.class).getEmployeeDetails(employee.getEmployeeID().toString(),
							null, null, status)).withSelfRel().expand());
		}
		int results = employeeList != null ? employeeList.size() : 0;
		logger.info("No of employees returned " + results);

		Link selfLink = linkTo(methodOn(EmployeeController.class).getEmployeeDetails(ids, exceptIds, emailIds, status))
				.withSelfRel().expand();
		Resources<Employee> empResources = new Resources<>(employeeList, selfLink);
		return new ResponseEntity<Resources<Employee>>(empResources, HttpStatus.OK);
	}

}
