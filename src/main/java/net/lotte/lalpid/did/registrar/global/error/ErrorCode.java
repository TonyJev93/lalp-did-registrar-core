package net.lotte.lalpid.did.registrar.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    ENTITY_NOT_AVAILABLE(400, "C004", "Entity Not Available"),
    INTERNAL_SERVER_ERROR(500, "C005", "Server Error"),
    INVALID_TYPE_VALUE(400, "C006", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C007", "Access is Denied"),
    NULL_POINTER_EXCEPTION(500, "C008", "Null Pointer Excetion"),
    NOT_FOUND_HANDLE_EXCEPTION(500, "C009", "Not Found Handle Exception"),

    //  Error Verify
    FAIL_TO_VERIFY_SIGNATURE(400, "V001", "Fail to verify Signature"),
    NOT_FOUND_PUBLIC_KEY_IN_AUTHENTICATION(400, "V002", "The PublicKey isn't included in Authentications"),
    NOT_FOUND_PUBLIC_KEY_ID(400, "V003", "Not Found KeyID in PublicKey List"),
    NOT_FOUND_PUBLIC_KEY(400, "V004", "Not Found PublicKey in DIDDocument"),
    ALREADY_EXIST_KEY_ID(400, "V005", "Key ID already exist."),
    NOT_FOUND_KEY_ID(400, "V006", "Not Found KeyID."),
    NOT_FOUND_PUBLIC_KEY_VALUE(400, "V007", "PublicKey Value is essential when using the Key type"),
    DID_DOCUMENT_DEACTIVATED(410, "R002", "DID Document is deactivated."),

    //  Input Validation
    NOT_CORRECT_INPUT_KEY_ID(400, "I001", "Not correct input KeyId. Check your KeyID."),

    // Resolve Error
    OCCURRED_ERROR_RESOLVE_DID_DOCUMENT(400, "R001", "An error occurred while resolving the DID Document."),

    // Fabric
    FABRIC_PUT_CERTIFICATE_ERROR(400, "F001", "블록체인에 인증서 저장 실패"),
    FABRIC_GET_CERTIFICATE_ERROR(400, "F002", "블록체인에 인증서 조회 실패"),
    FABRIC_PUT_SIGNATURE_ERROR(400, "F003", "블록체인에 서명 저장 실패"),
    FABRIC_GET_SIGNATURE_ERROR(400, "F004", "블록체인에 서명 조회 실패"),

    // TOKEN
    GENERATE_TOKEN_ERROR(400, "T001", "토큰 발행에 문제가 발생하였습니다.");

    private int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}
