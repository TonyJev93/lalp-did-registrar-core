package net.lotte.lalpid.did.registrar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.net.URI;

@Getter
public class DIDKey {
    protected String controller;
    protected String id;
    protected String type;
    protected String publicKeyPem;

    @JsonIgnore
    protected String keyId;

    public void mappingDid(URI did) {
        this.id = did.toString() + "#" + this.keyId;
        this.controller = did.toString();
    }

}
