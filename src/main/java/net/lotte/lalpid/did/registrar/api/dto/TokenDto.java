package net.lotte.lalpid.did.registrar.api.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.Token;

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
//            this.rsaPrivateKey = Optional.ofNullable(token.getRsaPrivateKey().getEncoded()).orElse("".getBytes());
//            this.rsaPublicKey = Optional.ofNullable(token.getRsaPublicKey()).get().getEncoded();
        }

        public static Res of(Token token) {
            return TokenDto.Res.builder().token(token).build();
        }
    }

}
