package net.lotte.lalpid.did.registrar.api.dto.validator;

public class ValidationMsg {
    public static final String VALID_CHK_MSG_KEY_ID_IS_EMPTY = "keyID is empty";
    public static final String VALID_CHK_MSG_KEY_TYPE_IS_EMPTY = "keyType is empty";
    public static final String VALID_CHK_MSG_KEY_VALUE_IS_EMPTY = "publicKeyValue is empty";
    public static final String VALID_CHK_MSG_ADD_PUBLIC_KEY_IS_EMPTY = "addPublicKeys is empty";
    public static final String VALID_CHK_MSG_ADD_AUTHENTICATION_IS_EMPTY = "authentication is empty";
    public static final String VALID_CHK_MSG_SERVICE_ENDPOINT_IS_EMPTY = "serviceEndpoint is empty";
    public static final String VALID_CHK_MSG_TOKEN_IS_EMPTY = "token is empty";
    public static final String VALID_CHK_MSG_DID_URL_PATTERN_ERROR = "PathVariable(DID URL) Pattern should be [did:lalp:{UUID}]";
    public static final String VALID_CHK_MSG_DID_URL_PARSING_ERROR = "Parsing Error, DID URL 형식이 올바르지 않습니다.";
    public static final String VALID_CHK_MSG_TOKEN_DID_NOT_EQUALS_WITH_PATH_DID = "token 내 DID가 요청한 DID와 일치하지 않습니다.";
    public static final String VALID_CHK_REGEXP_LALP_DID_URL = "did:lalp:([a-zA-z0-9-]*)$";


}
