package net.lotte.lalpid.did.registrar.domain.infra;

import foundation.identity.did.DIDDocument;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;

public interface RegistrarRepository {
    void saveDIDDocument(DIDDocument didDocument);
    void updateDIDDocument();
    void deleteDIDDocument();
}
