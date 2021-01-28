package net.lotte.lalpid.did.registrar.api.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.DIDPublicKey;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Secret {
    List<DIDPublicKey> keys;

    @Builder
    public Secret(List<DIDPublicKey> keys){
        this.keys = keys;
    }
}
