package net.lotte.lalpid.did.registrar.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.identity.did.DIDDocument;
import foundation.identity.did.PublicKey;
import lombok.*;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class DIDPublicKey extends DIDKey {

    ObjectMapper objectMapper = new ObjectMapper();

    @Builder
    public DIDPublicKey(String controller, String id, String type, String publicKeyPem, String keyId) {
        this.controller = controller;
        this.id = id;
        this.type = type;
        this.publicKeyPem = publicKeyPem;
        this.keyId = keyId;
    }

    public static List<PublicKey> toPublicKeyList(List<DIDPublicKey> didPublicKeyList) {
        return didPublicKeyList.stream().map(didPk -> didPk.toPublicKey()).collect(Collectors.toList());
    }

    public static List<DIDPublicKey> fromDIDDocument(DIDDocument didDocument) {
        return getPublicKeyList(didDocument).stream().map(publicKey -> fromPublicKey(publicKey)).collect(Collectors.toList());
    }

    public static DIDPublicKey fromPublicKey(PublicKey publicKey) {
        DIDPublicKey didPublicKey =
                DIDPublicKey.builder()
                        .id(publicKey.getId().toString())
                        .type(publicKey.getType())
                        .publicKeyPem(publicKey.getPublicKeyPem())
                        .controller(Optional.ofNullable(publicKey.getJsonObject().get("controller").toString()).orElse(""))
                        .build();

        return didPublicKey;
    }

    public static List<PublicKey> getPublicKeyList(DIDDocument didDocument) {
        return ((List<Map<String, Object>>) didDocument.getJsonObject().get("publicKey"))
                .stream().map(publicKeyMap -> PublicKey.fromJsonObject(publicKeyMap)).collect(Collectors.toList());
    }

    public static PublicKey selectPublicKey(List<PublicKey> publicKeyList, String keyId) {
        return Optional.ofNullable(publicKeyList.stream()
                .filter(publicKey -> keyId.equals(DIDUrl.fromUri(publicKey.getId()).getFragment()))
                .collect(Collectors.toList()))
                .orElseThrow(() -> new BusinessException("서명한 Key ID에 해당하는 PublicKey를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_PUBLIC_KEY_ID))
                .get(0);
    }

    public PublicKey toPublicKey() {
        PublicKey publicKey = PublicKey.builder().id(URI.create(this.id)).type(this.type).publicKeyPem(this.publicKeyPem).build();
        publicKey.setJsonObjectKeyValue("controller", this.controller);
        return publicKey;
    }

}
