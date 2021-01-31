package net.lotte.lalpid.did.registrar.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.identity.did.DIDDocument;
import foundation.identity.did.DIDURL;
import foundation.identity.did.PublicKey;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lotte.lalpid.did.registrar.application.dao.DIDUpdateDao;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;
import net.lotte.lalpid.did.registrar.infrastructure.util.DID.DIDDocumentUtil;
import net.lotte.lalpid.did.registrar.infrastructure.util.DID.DIDUrlUtil;
import net.lotte.lalpid.did.registrar.infrastructure.util.Security;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

// JWT 토큰 임시발행. 클라이언트단 token발행을 위해 수행
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class DIDToken {

    private String token;

    @Builder
    public DIDToken(String token) {
        this.token = token;
    }

    public static DIDToken of(String token) {
        return DIDToken.builder().token(token).build();
    }

    public DIDURL getDidUrl() {
        // 토큰 디코딩
        DecodedJWT decodedJWT = JWT.decode(this.token);

        // DID URL 추출
        String didUrl = decodedJWT.getKeyId(); // Fragment 포함된 DID URL

        return DIDUrlUtil.fromString(didUrl);
    }

    public String getFragment() {
        DIDURL didUrl = this.getDidUrl();
        String keyId = didUrl.getFragment();
        return keyId;
    }

    public boolean verify(DIDDocument didDocument) {

        String keyId = this.getFragment();

        List<PublicKey> publicKeyList = DIDDocumentUtil.getPublicKeyList(didDocument);
        PublicKey selectedPublicKey = DIDDocumentUtil.selectPublicKey(publicKeyList, keyId);

        if (((List<Object>) didDocument.getJsonObject().get("publicKey")).size() == 0) {
            throw new BusinessException("해당 DID는 폐기되었습니다.", ErrorCode.DID_DOCUMENT_DEACTIVATED);
        }

        if (didDocument.getAuthentications().stream().noneMatch(authentication -> authentication.getId().equals(selectedPublicKey.getId()))) {
            throw new BusinessException("Authentication에 해당 ID의 PublicKey가 존재하지 않습니다.", ErrorCode.NOT_FOUND_PUBLIC_KEY_IN_AUTHENTICATION);
        }

        // RSA PublicKey로 형변환
        RSAPublicKey publicKey = (RSAPublicKey) Security.publicKeyConverter(selectedPublicKey.getPublicKeyPem());

        try {
            // JWT 검증 수행
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
            verifier.verify(this.token);
            log.info("'{}' JWT 검증 완료", this.getDidUrl().getDidUrlString());
        } catch (JWTVerificationException exception) {
            // 검증 오류 예외처리
            throw new BusinessException("token 검증 error", ErrorCode.FAIL_TO_VERIFY_SIGNATURE);
        }

        return true;
    }

    public Map<String, String> getClaim() {
        try {
            // get update request claim
            return (Map<String, String>) JWT.decode(this.token).getClaim("operation").asMap().get("operation");

        } catch (Exception e) {
            throw new BusinessException("Not valid token type", ErrorCode.NOT_VALID_TOKEN_TYPE);
        }
    }

    public DIDUpdateDao toUpdateDto() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jwtString = objectMapper.writeValueAsString(this.getClaim());
            DIDUpdateDao didUpdateDao = objectMapper.readValue(jwtString, DIDUpdateDao.class);

            return didUpdateDao;
        } catch (JsonProcessingException e) {
            throw new BusinessException("Parsing Error", ErrorCode.NOT_VALID_TOKEN_TYPE);
        }
    }

    public DIDUpdateDao toDeleteDto() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jwtString = objectMapper.writeValueAsString(this.getClaim());
            DIDUpdateDao didUpdateDao = objectMapper.readValue(jwtString, DIDUpdateDao.class);

            return didUpdateDao;
        } catch (JsonProcessingException e) {
            throw new BusinessException("Parsing Error", ErrorCode.NOT_VALID_TOKEN_TYPE);
        }
    }
}
