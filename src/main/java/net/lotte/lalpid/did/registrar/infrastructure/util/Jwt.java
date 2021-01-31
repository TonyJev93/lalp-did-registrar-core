package net.lotte.lalpid.did.registrar.infrastructure.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import foundation.identity.did.DIDURL;
import foundation.identity.did.parser.ParserException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.Optional;

// JWT 토큰 임시발행. 클라이언트단 token발행을 위해 수행
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Slf4j
public class Jwt {

    private String token;

    public static Jwt generateToken(Map<String, Object> operation) {
        Jwt jwt = new Jwt();
        try {
            RSAPrivateKey privateKey;
            String privateKeyPem = Optional.ofNullable(operation.get("privateKeyPem")).orElse("").toString();
            KeyPair keyPair;

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

            DIDURL didUrl = DIDURL.fromString(operation.get("verificationMethod").toString());

            // 본문을 이용한 토큰을 발행한다.
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create().withIssuer(didUrl.getDid().getDidString()).withKeyId(didUrl.getDidUrlString())
                    .withClaim("operation", operation).sign(algorithm);

            jwt.setToken(token);
            return jwt;

        } catch (IOException | ParserException e) {
            throw new BusinessException("Generate KeyPair error", ErrorCode.GENERATE_TOKEN_ERROR);
        }
    }

}
