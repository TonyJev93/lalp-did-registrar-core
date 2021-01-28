package net.lotte.lalpid.did.registrar.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.identity.did.DIDDocument;
import foundation.identity.did.DIDURL;
import lombok.AllArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;
import net.lotte.lalpid.did.registrar.domain.Token;
import net.lotte.lalpid.did.registrar.domain.infra.RegistrarRepository;
import net.lotte.lalpid.did.registrar.domain.infra.ResolverRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegistrarServiceImpl implements RegistrarService {

    private final RegistrarRepository registrarRepository;
    private final ResolverRepository resolverRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public LalpDIDDocument register(LalpDIDDocument lalpDIDDocument) {

        // DID Document init - DID 신규 발번 및 맵핑
        lalpDIDDocument.init();
        registrarRepository.saveDIDDocument(lalpDIDDocument.toDIDDocument());

        return lalpDIDDocument;
    }

    @Override
    public LalpDIDDocument update(String token) {

        // get DID URL from token
        DIDURL didUrl = Token.getDidUrl(token);

        // get DID Document from resolver
        DIDDocument didDocument = resolverRepository.getDIDDocument(didUrl.getDid().getDidString());

        // verify token
        Token.verify(token, didDocument);

        // update did document

        return LalpDIDDocument.fromDIDDocument(didDocument);
    }


    @Override
    public LalpDIDDocument delete() {
        return null;
    }
}
