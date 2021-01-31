package net.lotte.lalpid.did.registrar.api.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.infrastructure.util.Jwt;

public class TokenDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private String token;

        @Builder
        public Res(Jwt token) {
            this.token = token.getToken();
        }

        public static Res of(Jwt token) {
            return TokenDto.Res.builder().token(token).build();
        }
    }

}
