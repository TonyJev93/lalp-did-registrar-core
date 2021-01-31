package net.lotte.lalpid.did.registrar.domain;

import foundation.identity.did.DIDDocument;
import foundation.identity.did.PublicKey;
import lombok.*;
import net.lotte.lalpid.did.registrar.infrastructure.util.DID.DIDDocumentUtil;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DIDPublicKey extends DIDKey {

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
        return DIDDocumentUtil.getPublicKeyList(didDocument).stream().map(publicKey -> fromPublicKey(publicKey)).collect(Collectors.toList());
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


    public PublicKey toPublicKey() {
        PublicKey publicKey = PublicKey.builder().id(URI.create(this.id)).type(this.type).publicKeyPem(this.publicKeyPem).build();
        publicKey.setJsonObjectKeyValue("controller", this.controller);
        return publicKey;
    }

}
