package org.mifos.processor.bulk;

import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidationCodeType;
import org.springframework.stereotype.Component;

public enum BatchTransactionValidatorsEnum implements ValidationCodeType {

    BATCH_TRANSACTION_VALIDATION_ERROR("error.msg.batch.transaction.validation.errors", "Batch transaction validation failed"),
    INVALID_REQUEST_ID("error.msg.schema.request.id.cannot.be.null.or.empty", "Request ID cannot be null or empty"),
    INVALID_REQUEST_ID_LENGTH("error.msg.schema.request.id.length.is.invalid", "Request ID length is invalid"),
    INVALID_FILE_NAME_LENGTH("error.msg.schema.file.name.length.is.invalid", "File name length is invalid"),
    INVALID_PURPOSE("error.msg.schema.purpose.cannot.be.null.or.empty", "Purpose cannot be null or empty"),
    INVALID_TYPE("error.msg.schema.type.cannot.be.null.or.empty", "Type cannot be null or empty"),
    INVALID_TENANT("error.msg.schema.tenant.cannot.be.null.or.empty", "Tenant cannot be null or empty"),
    INVALID_REGISTERING_INSTITUTION_ID_LENGTH("error.msg.schema.registering.institution.id.length.is.invalid", "Registering Institution ID length is invalid"),
    INVALID_PROGRAM_ID_LENGTH("error.msg.schema.program.id.length.is.invalid", "Program ID length is invalid"),
    INVALID_CALLBACK_URL("error.msg.schema.callback.url.cannot.be.null.or.empty", "Callback URL cannot be null or empty"),
    INVALID_CALLBACK_URL_LENGTH("error.msg.schema.callback.url.length.is.invalid", "Callback URL length is invalid");

    private final String code;
    private final String category;
    private final String message;

    BatchTransactionValidatorsEnum(String code, String message) {
        this.code = code;
        this.category = PaymentHubErrorCategory.Validation.toString();
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getCategory() {
        return this.category;
    }

    public String getMessage() {
        return message;
    }
}


