package net.lotte.lalpid.did.registrar.domain;

import foundation.identity.did.Authentication;
import foundation.identity.did.DIDDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DIDAuthentication extends DIDKey {

    @Builder
    public DIDAuthentication(String controller, String id, String type, String publicKeyPem, String keyId) {
        this.controller = controller;
        this.id = id;
        this.type = type;
        this.publicKeyPem = publicKeyPem;
        this.keyId = keyId;
    }


    public static List<Object> toObjectList(List<DIDAuthentication> authenticationList) {
        List<Object> resultList = new ArrayList<>();
        for (DIDAuthentication authentication : authenticationList) {
            // Key 완전체로 전달된 경우
            if (authentication.getType() != null) {
                resultList.add(authentication);
            }
            // Key ID만 전달된 경우
            else {
                resultList.add(authentication.getId());
            }
        }

        return resultList;
    }

    public static List<DIDAuthentication> fromAuthenticationList(List<Authentication> authenticationList) {
        return authenticationList.stream().map(authentication -> DIDAuthentication.fromAuthentication(authentication)).collect(Collectors.toList());
    }

    private static DIDAuthentication fromAuthentication(Authentication authentication) {

        return null;
    }
}
