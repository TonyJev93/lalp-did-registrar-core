package net.lotte.lalpid.did.registrar.api.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.Token;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class TokenDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private String token;
        private byte[] rsaPrivateKey;
        private byte[] rsaPublicKey;

        @Builder
        public Res(Token token) {
            this.token = token.getToken();
            this.rsaPrivateKey = token.getRsaPrivateKey().getEncoded();
            this.rsaPublicKey = token.getRsaPublicKey().getEncoded();
        }

        public static Res of(Token token) {
            return TokenDto.Res.builder().token(token).build();
        }
    }

}
