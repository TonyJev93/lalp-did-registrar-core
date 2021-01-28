package net.lotte.lalpid.did.registrar.api.dto.response;

import foundation.identity.did.DIDDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.DIDPublicKey;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DidState {
    private String identifier;
    private String state;
    private Secret secret;

    @Builder
    public DidState(LalpDIDDocument lalpDIDDocument) {
        this.identifier = lalpDIDDocument.getDid().toString();
        this.state = RegistrarStatus.FINISHED.getValue();
        this.secret = Secret.builder().keys(lalpDIDDocument.getPublicKeyList()).build();
    }

}
