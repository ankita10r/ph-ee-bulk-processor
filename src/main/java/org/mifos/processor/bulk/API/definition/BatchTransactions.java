package org.mifos.processor.bulk.api.definition;


import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mifos.processor.bulk.camel.config.CamelProperties.*;
import static org.mifos.processor.bulk.camel.config.CamelProperties.HEADER_CLIENT_CORRELATION_ID;
import static org.mifos.processor.bulk.camel.config.CamelProperties.HEADER_PLATFORM_TENANT_ID;
import static org.mifos.processor.bulk.zeebe.ZeebeVariables.*;

public interface BatchTransactions {

    @PostMapping(value = "/batchtransactions", produces = "application/json")
    String batchTransactions(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestHeader(value = HEADER_CLIENT_CORRELATION_ID) String requestId,
            @RequestHeader(value = FILE_NAME, required = false) String fileName,
            @RequestHeader(value = PURPOSE) String purpose,
            @RequestParam(value = HEADER_TYPE) String type,
            @RequestHeader(value = HEADER_PLATFORM_TENANT_ID) String tenant,
            @RequestHeader(value = CALLBACK, required = false) String callbackUrl,
            @RequestHeader(value = HEADER_REGISTERING_INSTITUTE_ID, required = false) String registeringInstitutionId,
            @RequestHeader(value = HEADER_PROGRAM_ID, required = false) String programId) throws IOException;

}
