package net.lotte.lalpid.did.registrar.infrastructure.util.DID;

import foundation.identity.did.DIDURL;
import foundation.identity.did.parser.ParserException;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;

import java.net.URI;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_MSG_DID_URL_PARSING_ERROR;

public class DIDUrlUtil {

    public static DIDURL fromString(String didUrl) {
        try {
            return DIDURL.fromString(didUrl);
        } catch (ParserException e) {
            // DID Parsing Error
            throw new BusinessException(VALID_CHK_MSG_DID_URL_PARSING_ERROR, ErrorCode.INVALID_TYPE_VALUE);
        }
    }

    public static DIDURL fromUri(URI didUrl) {
        try {
            return DIDURL.fromUri(didUrl);
        } catch (ParserException e) {
            // DID Parsing Error
            throw new BusinessException(VALID_CHK_MSG_DID_URL_PARSING_ERROR, ErrorCode.INVALID_TYPE_VALUE);
        }
    }

    public static String getFragment(String didUrl) {
        return fromString(didUrl).getFragment();
    }
}
