package net.lotte.lalpid.did.registrar.application;

import org.springframework.validation.BindingResult;

public interface DIDTokenService {
    void verify(String token, String did, BindingResult bindingResult);
}
