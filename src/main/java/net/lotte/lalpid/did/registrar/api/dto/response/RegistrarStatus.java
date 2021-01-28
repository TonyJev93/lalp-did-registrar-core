package net.lotte.lalpid.did.registrar.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegistrarStatus {
    FINISHED("finished");

    private final String value;
}
