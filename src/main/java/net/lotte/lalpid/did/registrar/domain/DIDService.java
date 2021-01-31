package net.lotte.lalpid.did.registrar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import foundation.identity.did.Service;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DIDService {
    private String id;
    private String type;
    private String serviceEndpoint;

    @JsonIgnore
    protected String keyId;

    @Builder
    public DIDService(String id, String type, String serviceEndpoint, String keyId) {
        this.id = id;
        this.type = type;
        this.serviceEndpoint = serviceEndpoint;
        this.keyId = keyId;
    }

    public static List<Service> toServiceList(List<DIDService> didServiceList) {
        return didServiceList.stream().map(didService -> didService.toService()).collect(Collectors.toList());
    }

    public static List<DIDService> fromServiceList(List<Service> serviceList) {
        return serviceList.stream().map(service -> DIDService.fromService(service)).collect(Collectors.toList());
    }

    public static DIDService fromService(Service service) {
        return DIDService.builder()
                .id(service.getId().toString())
                .serviceEndpoint(service.getServiceEndpoint())
                .type(service.getType()).build();
    }

    public Service toService() {
        return Service.builder().id(URI.create(this.id)).type(this.type).serviceEndpoint(this.serviceEndpoint).build();
    }

    public void mappingDid(URI did) {
        this.id = did.toString() + "#" + this.keyId;
    }
}
