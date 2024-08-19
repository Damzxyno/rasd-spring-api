package com.damzxyno.rasdspringapi.api;

import com.damzxyno.rasdspringapi.core.services.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthorizationReportingWebMvcResource {
    private final ReportingService reportingService;

    @Autowired
    public AuthorizationReportingWebMvcResource(ReportingService reportingService){
        this.reportingService = reportingService;
    }

    @GetMapping(
            value = {"${authorisation-report.api-docs.path:#{T(com.damzxyno.rasdspringapi.utils.Constants).DEFAULT_API_DOCS_URL}}"},
            produces = {"application/json"}
    )
    public String authorizationReportJson(HttpServletRequest request) {
        return reportingService.getAuthorisationReport(request);
    }
}
