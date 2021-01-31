package net.lotte.lalpid.did.registrar.application;

import foundation.identity.did.DIDDocument;
import lombok.AllArgsConstructor;
import net.lotte.lalpid.did.registrar.application.dao.DIDUpdateDao;
import net.lotte.lalpid.did.registrar.domain.DIDToken;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;
import net.lotte.lalpid.did.registrar.domain.infra.RegistrarRepository;
import net.lotte.lalpid.did.registrar.domain.infra.ResolverRepository;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RegistrarServiceImpl implements RegistrarService {

    private final RegistrarRepository registrarRepository;
    private final ResolverRepository resolverRepository;

    @Override
    public LalpDIDDocument register(LalpDIDDocument lalpDIDDocument) {

        // DID Document init - DID 신규 발번 및 맵핑
        lalpDIDDocument.init();
        if (!registrarRepository.saveDIDDocument(lalpDIDDocument.toDIDDocument())) {
            throw new BusinessException("Occur Error while registering DID Document in Fabric", ErrorCode.FABRIC_NETWORK_ERROR);
        }

        return lalpDIDDocument;
    }

    @Override
    public boolean update(String token, String did) {
        // Transfer Token to Update Request Map
        DIDUpdateDao didUpdateDao = DIDToken.of(token).toUpdateDto();

        // get DID Document from resolver
        DIDDocument targetDidDocument = resolverRepository.getDIDDocument(did);

        // To DIDDocument Entity
        DIDDocument updatedDIDDocument = didUpdateDao.toDIDDocument(targetDidDocument);

        // TODO : DID Document validation check ( duplicated check )

        return registrarRepository.saveDIDDocument(updatedDIDDocument);
    }

    @Override
    public boolean delete(String did) {

        // get DID Document from resolver
        DIDDocument didDocument = resolverRepository.getDIDDocument(did);

        didDocument.setJsonObjectKeyValue("publicKey", "");
        didDocument.setJsonObjectKeyValue("authentication", "");
        didDocument.setJsonObjectKeyValue("assertionMethod", "");

        return registrarRepository.saveDIDDocument(didDocument);
    }
}
