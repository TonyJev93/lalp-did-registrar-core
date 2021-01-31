package net.lotte.lalpid.did.registrar.api.dto.request;

import lombok.Getter;
import lombok.ToString;
import net.lotte.lalpid.did.registrar.domain.DIDService;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.*;

@Getter
@ToString
public class ServiceDto {

    @NotEmpty(message = VALID_CHK_MSG_KEY_ID_IS_EMPTY)
    private String keyId;

    @NotEmpty(message = VALID_CHK_MSG_KEY_TYPE_IS_EMPTY)
    private String type;

    @NotEmpty(message = VALID_CHK_MSG_SERVICE_ENDPOINT_IS_EMPTY)
    private String serviceEndpoint;

    public static List<DIDService> toEntityList(List<ServiceDto> serviceDtoList) {
        return Optional.ofNullable(serviceDtoList).orElse(new ArrayList<>())
                .stream().map(serviceDto -> serviceDto.toEntity()).collect(Collectors.toList());
    }

    public static List<String> getKeyIdList(List<ServiceDto> serviceList) {
        return serviceList.stream().map(ServiceDto::getKeyId).collect(Collectors.toList());
    }

    public DIDService toEntity() {
        return DIDService.builder().keyId(this.keyId).type(this.type).serviceEndpoint(this.serviceEndpoint).build();
    }

}
