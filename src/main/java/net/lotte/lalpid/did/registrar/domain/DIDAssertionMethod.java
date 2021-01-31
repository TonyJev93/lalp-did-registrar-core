package net.lotte.lalpid.did.registrar.domain;

import foundation.identity.did.AssertionMethod;
import foundation.identity.did.Authentication;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;
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
        for (DIDAssertionMethod assertionMethod : didKeyList) {
            // Key 완전체로 전달된 경우
            if (assertionMethod.getType() != null) {
                resultList.add(assertionMethod.toAssertionMethod());
            }
            // Key ID만 전달된 경우
            else {
                resultList.add(assertionMethod.getId());
            }
        }

        return resultList;
    }

    private Object toAssertionMethod() {
        AssertionMethod assertionMethod = AssertionMethod.builder().id(URI.create(this.id)).type(this.type).build();
        assertionMethod.setJsonObjectKeyValue("publicKeyPem", this.publicKeyPem);
        assertionMethod.setJsonObjectKeyValue("controller", this.controller);
        return assertionMethod;
    }

}
