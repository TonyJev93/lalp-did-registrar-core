package net.lotte.lalpid.did.registrar.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import foundation.identity.did.DIDDocument;
import foundation.identity.did.DIDURL;
import foundation.identity.did.PublicKey;
import foundation.identity.did.parser.ParserException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;
import net.lotte.lalpid.did.registrar.infrastructure.util.Security;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// JWT 토큰 임시발행. 클라이언트단 token발행을 위해 수행
@Getter
@Setter
@Slf4j
public class Token {

    private String token;
    private RSAPublicKey rsaPublicKey;
    private RSAPrivateKey rsaPrivateKey;

    public static DIDURL getDidUrl(String token) {
        // 토큰 디코딩
        DecodedJWT decodedJWT = JWT.decode(token);

        // DID URL 추출
        String didUrl = decodedJWT.getKeyId(); // Fragment 포함된 DID URL

        return DIDUrl.fromString(didUrl);
    }

    public static Token toJwt(Map<String, Object> operation) {
        Token tokenReturn = new Token();
        try {
            RSAPrivateKey privateKey;
            String privateKeyPem = Optional.ofNullable(operation.get("privateKeyPem")).orElse("").toString();
            KeyPair keyPair;
            if (privateKeyPem.isEmpty()) {


                keyPair = Security.generateKeyPair("RSA", 2048);

                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                tokenReturn.setRsaPublicKey(publicKey);

                privateKey = (RSAPrivateKey) keyPair.getPrivate();


            } else {

                JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
                StringReader reader = new StringReader(privateKeyPem);
                PEMParser pr = new PEMParser(reader);

                Object pemContent = pr.readObject();
                if (pemContent instanceof PEMKeyPair) {
                    PEMKeyPair pemKeyPair = (PEMKeyPair) pemContent;
                    keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
                    privateKey = (RSAPrivateKey) keyPair.getPrivate();
                } else if (pemContent instanceof PrivateKeyInfo) {
                    PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemContent;
                    privateKey = (RSAPrivateKey) jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
                } else {
                    throw new IllegalArgumentException(
                            "Unsupported private key format '" + pemContent.getClass().getSimpleName() + '"');
                }

                operation.remove("privateKeyPem");
            }

            DIDURL didUrl = DIDURL.fromString(operation.get("verificationMethod").toString());

            // 본문을 이용한 토큰을 발행한다.
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create().withIssuer(didUrl.getDid().getDidString()).withKeyId(didUrl.getDidUrlString())
                    .withClaim("operation", operation).sign(algorithm);

            tokenReturn.setRsaPrivateKey(privateKey);
            tokenReturn.setToken(token);
            return tokenReturn;

        } catch (NoSuchAlgorithmException | IOException | ParserException e) {
            throw new BusinessException("Generate KeyPair error", ErrorCode.GENERATE_TOKEN_ERROR);
        }
    }


    public static boolean verify(String token, DIDDocument didDocument) {

        DIDURL didUrl = Token.getDidUrl(token);
        String keyId = didUrl.getFragment();

        List<PublicKey> publicKeyList = DIDPublicKey.getPublicKeyList(didDocument);

        PublicKey selectedPublicKey = DIDPublicKey.selectPublicKey(publicKeyList, keyId);


        if (didDocument.getAuthentications().stream().noneMatch(authentication -> authentication.getId().equals(selectedPublicKey.getId()))) {
            throw new BusinessException("Authentication에 해당 ID의 PublicKey가 존재하지 않습니다.", ErrorCode.NOT_FOUND_PUBLIC_KEY_IN_AUTHENTICATION);
        }

        // RSA PublicKey로 형변환
        RSAPublicKey publicKey = (RSAPublicKey) Security.publicKeyConverter(selectedPublicKey.getPublicKeyPem());

        try {
            // JWT 검증 수행
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
            verifier.verify(token);
            log.info("'{}' JWT 검증 완료", didUrl.getDidUrlString());
        } catch (JWTVerificationException exception) {
            // 검증 오류 예외처리
            throw new BusinessException("token 검증 error", ErrorCode.FAIL_TO_VERIFY_SIGNATURE);
        }

        return true;
    }
}
