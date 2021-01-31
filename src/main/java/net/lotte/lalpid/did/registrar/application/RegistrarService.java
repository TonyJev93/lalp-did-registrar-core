package net.lotte.lalpid.did.registrar.application;

import foundation.identity.did.DIDDocument;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RegistrarService {

    LalpDIDDocument register(LalpDIDDocument lalpDIDDocument);

    boolean update(String token, String did);

    boolean delete(String did);

}
