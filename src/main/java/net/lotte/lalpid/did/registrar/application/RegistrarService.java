package net.lotte.lalpid.did.registrar.application;

import foundation.identity.did.DIDDocument;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;
import org.springframework.stereotype.Service;

@Service
public interface RegistrarService {

    LalpDIDDocument register(LalpDIDDocument registerRequest);

    LalpDIDDocument update(String token);

    LalpDIDDocument delete();

}
