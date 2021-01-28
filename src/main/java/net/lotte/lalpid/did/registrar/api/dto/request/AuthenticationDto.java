package net.lotte.lalpid.did.registrar.api.dto.request;

import lombok.Getter;
import lombok.ToString;
import net.lotte.lalpid.did.registrar.domain.DIDAuthentication;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_MSG_KEY_ID_IS_EMPTY;

@Getter
@ToString
public class AuthenticationDto {

    @NotEmpty(message = VALID_CHK_MSG_KEY_ID_IS_EMPTY)
    protected String keyId;

    protected String type;

    protected String publicKeyValue;

    public static List<DIDAuthentication> toEntityList(List<AuthenticationDto> authenticationDtoList) {
        return Optional.ofNullable(authenticationDtoList).orElse(new ArrayList<>()).stream()
                .map(authenticationDto ->
                        DIDAuthentication.builder()
                                .keyId(authenticationDto.keyId)
                                .type(authenticationDto.type)
                                .publicKeyPem(authenticationDto.publicKeyValue)
                                .build())
                .collect(Collectors.toList());
    }

    public static List<AuthenticationDto> getKeyListWithType(List<AuthenticationDto> authenticationDtoList) {
        return authenticationDtoList.stream()
                .filter(authenticationDto -> authenticationDto.getType() != null && !"".equals(authenticationDto.getType()))
                .collect(Collectors.toList());
    }

    public static List<AuthenticationDto> getKeyListNotWithType(List<AuthenticationDto> authenticationDtoList) {
        return authenticationDtoList.stream()
                .filter(authenticationDto -> authenticationDto.getType() == null || "".equals(authenticationDto.getType()))
                .collect(Collectors.toList());
    }

    public static List<String> getKeyIdListWithType(List<AuthenticationDto> authenticationDtoList) {
        return getKeyListWithType(authenticationDtoList).stream().map(AuthenticationDto::getKeyId).collect(Collectors.toList());
    }

    public static List<String> getKeyIdListWithoutType(List<AuthenticationDto> authenticationDtoList) {
        return getKeyListNotWithType(authenticationDtoList).stream().map(AuthenticationDto::getKeyId).collect(Collectors.toList());
    }
}
