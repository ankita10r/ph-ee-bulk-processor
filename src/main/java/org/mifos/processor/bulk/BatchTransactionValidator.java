package org.mifos.processor.bulk;


import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidatorBuilder;
import org.mifos.processor.bulk.BatchTransactionValidatorsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.mifos.connector.common.exception.PaymentHubError.ExtValidationError;

@Component
public class BatchTransactionValidator {

    private static final String RESOURCE = "batchTransactionValidator";
    private static final String REQUEST_ID = "requestId";
    private static final int EXPECTED_REQUEST_ID_LENGTH = 15;
    private static final String FILE_NAME = "fileName";
    private static final String PURPOSE = "purpose";
    private static final String TYPE = "type";
    private static final String TENANT = "tenant";
    private static final String REGISTERING_INSTITUTION_ID = "registeringInstitutionId";
    private static final String PROGRAM_ID = "programId";
    private static final String CALLBACK_URL = "callbackUrl";

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public PhErrorDTO validateBatchTransactions(String requestId, String fileName, String purpose, String type,
                                                String tenant, String registeringInstitutionId, String programId, String callbackUrl) {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();
        logger.info("Inside validation");

        // Check for requestId
        validatorBuilder.reset().resource(RESOURCE).parameter(REQUEST_ID).value(requestId)
                .isNullWithFailureCode(BatchTransactionValidatorsEnum.INVALID_REQUEST_ID)
                .validateFieldMaxLengthWithFailureCodeAndErrorParams(EXPECTED_REQUEST_ID_LENGTH,
                        BatchTransactionValidatorsEnum.INVALID_REQUEST_ID_LENGTH);

        // Check for fileName (optional)
        if (fileName != null) {
            validatorBuilder.reset().resource(RESOURCE).parameter(FILE_NAME).value(fileName)
                    .validateFieldMaxLengthWithFailureCodeAndErrorParams(EXPECTED_REQUEST_ID_LENGTH,
                            BatchTransactionValidatorsEnum.INVALID_FILE_NAME_LENGTH);
        }

        // Check for purpose
        validatorBuilder.reset().resource(RESOURCE).parameter(PURPOSE).value(purpose)
                .isNullWithFailureCode(BatchTransactionValidatorsEnum.INVALID_PURPOSE);

        // Check for type
        validatorBuilder.reset().resource(RESOURCE).parameter(TYPE).value(type)
                .isNullOrEmpty();

        // Check for tenant
        validatorBuilder.reset().resource(RESOURCE).parameter(TENANT).value(tenant)
                .isNullWithFailureCode(BatchTransactionValidatorsEnum.INVALID_TENANT);

        // Check for registeringInstitutionId (optional)
        if (registeringInstitutionId != null) {
            validatorBuilder.reset().resource(RESOURCE).parameter(REGISTERING_INSTITUTION_ID).value(registeringInstitutionId)
                    .validateFieldMaxLengthWithFailureCodeAndErrorParams(EXPECTED_REQUEST_ID_LENGTH,
                            BatchTransactionValidatorsEnum.INVALID_REGISTERING_INSTITUTION_ID_LENGTH);
        }

        // Check for programId (optional)
        if (programId != null) {
            validatorBuilder.reset().resource(RESOURCE).parameter(PROGRAM_ID).value(programId)
                    .validateFieldMaxLengthWithFailureCodeAndErrorParams(EXPECTED_REQUEST_ID_LENGTH,
                            BatchTransactionValidatorsEnum.INVALID_PROGRAM_ID_LENGTH);
        }

        // Check for callbackUrl (optional)
        if (callbackUrl != null) {
            validatorBuilder.reset().resource(RESOURCE).parameter(CALLBACK_URL).value(callbackUrl)
                    .isNullWithFailureCode(BatchTransactionValidatorsEnum.INVALID_CALLBACK_URL)
                    .validateFieldMaxLengthWithFailureCodeAndErrorParams(2048, BatchTransactionValidatorsEnum.INVALID_CALLBACK_URL);
        }

        // If errors exist, build and return PhErrorDTO
        if (validatorBuilder.hasError()) {
            logger.info("Found error");
            validatorBuilder.errorCategory(PaymentHubErrorCategory.Validation.toString())
                    .errorCode(BatchTransactionValidatorsEnum.BATCH_TRANSACTION_VALIDATION_ERROR.getCode())
                    .errorDescription(BatchTransactionValidatorsEnum.BATCH_TRANSACTION_VALIDATION_ERROR.getMessage())
                    .developerMessage(BatchTransactionValidatorsEnum.BATCH_TRANSACTION_VALIDATION_ERROR.getMessage())
                    .defaultUserMessage(BatchTransactionValidatorsEnum.BATCH_TRANSACTION_VALIDATION_ERROR.getMessage());

            PhErrorDTO.PhErrorDTOBuilder phErrorDTOBuilder = new PhErrorDTO.PhErrorDTOBuilder(ExtValidationError.getErrorCode());
            phErrorDTOBuilder.fromValidatorBuilder(validatorBuilder);
            return phErrorDTOBuilder.build();
        }

        return null;
    }
}
