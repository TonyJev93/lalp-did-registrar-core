package net.lotte.lalpid.did.registrar.api.dto.request;

import lombok.Getter;
import lombok.ToString;
import net.lotte.lalpid.did.registrar.domain.DIDPublicKey;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.*;

@Getter
@ToString
public class PublicKeyDto {

    @NotEmpty(message = VALID_CHK_MSG_KEY_ID_IS_EMPTY)
    protected String keyId;

    @NotEmpty(message = VALID_CHK_MSG_KEY_VALUE_IS_EMPTY)
    protected String publicKeyValue;

    @NotEmpty(message = VALID_CHK_MSG_KEY_TYPE_IS_EMPTY)
    protected String type;

    public static List<DIDPublicKey> toEntityList(List<PublicKeyDto> publicKeyDtoList) {
        return Optional.ofNullable(publicKeyDtoList).orElse(new ArrayList<>()).stream()
                .map(publicKeyDto ->
                        DIDPublicKey.builder()
                                .keyId(publicKeyDto.getKeyId())
                                .type(publicKeyDto.getType())
                                .publicKeyPem(publicKeyDto.getPublicKeyValue())
                                .build())
                .collect(Collectors.toList());
    }

    public static List<String> getKeyIdList(List<PublicKeyDto> publicKeyDtoList) {
        return publicKeyDtoList.stream().map(PublicKeyDto::getKeyId).collect(Collectors.toList());
    }
}
