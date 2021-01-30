package net.lotte.lalpid.did.registrar.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DIDAssertionMethod extends DIDKey {

    @Builder
    public DIDAssertionMethod(String controller, String id, String type, String publicKeyPem, String keyId) {
        this.controller = controller;
        this.id = id;
        this.type = type;
        this.publicKeyPem = publicKeyPem;
        this.keyId = keyId;
    }


    public static List<Object> toObjectList(List<DIDAssertionMethod> didKeyList) {
        List<Object> resultList = new ArrayList<>();
        for (DIDAssertionMethod didKey : didKeyList) {
            // Key 완전체로 전달된 경우
            if (didKey.getType() != null) {
                resultList.add(didKey);
            }
            // Key ID만 전달된 경우
            else {
                resultList.add(didKey.getId());
            }
        }

        return resultList;
    }

}
