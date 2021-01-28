package net.lotte.lalpid.did.registrar.domain.infra;

import foundation.identity.did.DIDDocument;

public interface ResolverRepository {
    DIDDocument getDIDDocument(String did);
}
