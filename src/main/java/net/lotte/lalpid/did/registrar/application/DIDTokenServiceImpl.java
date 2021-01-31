package net.lotte.lalpid.did.registrar.application;

import foundation.identity.did.DIDDocument;
import lombok.AllArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.DIDToken;
import net.lotte.lalpid.did.registrar.api.dto.validator.TokenValidator;
import net.lotte.lalpid.did.registrar.domain.infra.ResolverRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@Service
public class DIDTokenServiceImpl implements DIDTokenService {

    private final TokenValidator tokenValidator;
    private final ResolverRepository resolverRepository;

    @Override
    public void verify(String token, String did, BindingResult bindingResult) {

        tokenValidator.validate(token, did, bindingResult);

        // get DID Document from resolver
        DIDDocument didDocument = resolverRepository.getDIDDocument(did);

        // get DID Token Object
        DIDToken didToken = DIDToken.of(token);

        // verify token
        didToken.verify(didDocument);
    }
}
