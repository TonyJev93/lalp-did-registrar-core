package net.lotte.lalpid.did.registrar.api.dto.request;

import lombok.Getter;
import lombok.ToString;
import net.lotte.lalpid.did.registrar.domain.DIDAssertionMethod;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_MSG_KEY_ID_IS_EMPTY;


@Getter
@ToString
public class AssertionMethodDto {

    @NotEmpty(message = VALID_CHK_MSG_KEY_ID_IS_EMPTY)
    protected String keyId;

    protected String type;

    protected String publicKeyValue;


    public static List<DIDAssertionMethod> toEntityList(List<AssertionMethodDto> assertionMethodDtoList) {

        return Optional.ofNullable(assertionMethodDtoList).orElse(new ArrayList<>()).stream()
                .map(assertionMethodDto ->
                        DIDAssertionMethod.builder()
                                .keyId(assertionMethodDto.getKeyId())
                                .type(assertionMethodDto.getType())
                                .publicKeyPem(assertionMethodDto.getPublicKeyValue())
                                .build())
                .collect(Collectors.toList());
    }

    public static List<AssertionMethodDto> getKeyListWithType(List<AssertionMethodDto> authenticationList) {
        return authenticationList.stream()
                .filter(assertionMethodDto -> assertionMethodDto.getType() != null && !"".equals(assertionMethodDto.getType()))
                .collect(Collectors.toList());
    }

    public static List<AssertionMethodDto> getKeyListNotWithType(List<AssertionMethodDto> authenticationList) {
        return authenticationList.stream()
                .filter(assertionMethodDto -> assertionMethodDto.getType() == null || "".equals(assertionMethodDto.getType()))
                .collect(Collectors.toList());
    }

    public static List<String> getKeyIdListWithType(List<AssertionMethodDto> assertionMethodList) {
        return getKeyListWithType(assertionMethodList).stream().map(AssertionMethodDto::getKeyId).collect(Collectors.toList());
    }

    public static List<String> getKeyIdListWithoutType(List<AssertionMethodDto> assertionMethodDtoList) {
        return getKeyListNotWithType(assertionMethodDtoList).stream().map(AssertionMethodDto::getKeyId).collect(Collectors.toList());
    }
}
