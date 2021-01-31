package net.lotte.lalpid.did.registrar.application.dao;

import foundation.identity.did.DIDDocument;
import foundation.identity.did.PublicKey;
import foundation.identity.did.Service;
import foundation.identity.jsonld.JsonLDObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.api.dto.request.AssertionMethodDto;
import net.lotte.lalpid.did.registrar.api.dto.request.AuthenticationDto;
import net.lotte.lalpid.did.registrar.api.dto.request.PublicKeyDto;
import net.lotte.lalpid.did.registrar.api.dto.request.ServiceDto;
import net.lotte.lalpid.did.registrar.domain.DIDAssertionMethod;
import net.lotte.lalpid.did.registrar.domain.DIDAuthentication;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;
import net.lotte.lalpid.did.registrar.infrastructure.util.DID.DIDUrlUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DIDUpdateDao {
    private List<PublicKeyDto> addPublicKeys;

    private List<AuthenticationDto> addAuthentications;

    private List<AssertionMethodDto> addAssertionMethods;

    private List<ServiceDto> addServices;

    private List<RemoveKeyId> removePublicKeys;

    private List<RemoveKeyId> removeAuthentications;

    private List<RemoveKeyId> removeAssertionMethods;

    private List<RemoveKeyId> removeServices;

    private static boolean findKey(JsonLDObject targetKey, List<DIDUpdateDao.RemoveKeyId> removeKeyList) {

        return removeKeyList.stream().anyMatch(removeKey -> removeKey.getKeyId().equals(targetKey.getId().getFragment()));

    }

    public String getKeyId(Object obj) {
        if (obj.getClass().equals(String.class)) {
            return DIDUrlUtil.getFragment((String) obj);
        } else {
            return DIDUrlUtil.getFragment(((Map<String, String>) obj).get("id"));
        }
    }

    public DIDDocument toDIDDocument(DIDDocument targetDidDocument) {

        List<Map<String, Object>> publicKeyMapList = (List<Map<String, Object>>) targetDidDocument.getJsonObject().get("publicKey");
        List<PublicKey> publicKeyList = publicKeyMapList.stream().map(publicKey -> PublicKey.fromJsonObject(publicKey)).collect(Collectors.toList());
        List<Object> authenticationList = (List<Object>) targetDidDocument.getJsonObject().get("authentication");
        List<Object> assertionMethodList = (List<Object>) targetDidDocument.getJsonObject().get("assertionMethod");
        List<Service> serviceList = targetDidDocument.getServices();

        publicKeyList.removeIf(target -> findKey(target, this.removePublicKeys));
        authenticationList.removeIf(o -> this.removeAuthentications.stream().map(RemoveKeyId::getKeyId).collect(Collectors.toList()).contains(getKeyId(o)));
        assertionMethodList.removeIf(o -> this.removeAssertionMethods.stream().map(RemoveKeyId::getKeyId).collect(Collectors.toList()).contains(getKeyId(o)));
        serviceList.removeIf(target -> findKey(target, this.removeServices));

        LalpDIDDocument lalpDIDDocument = this.toEntity();
        lalpDIDDocument.mappingDID(targetDidDocument.getId());

        lalpDIDDocument.getPublicKeyList().forEach(didPublicKey -> publicKeyList.add(didPublicKey.toPublicKey()));
        lalpDIDDocument.getServiceList().forEach(service -> serviceList.add(service.toService()));

        List<Object> authList = DIDAuthentication.toObjectList(lalpDIDDocument.getAuthenticationList());
        authList.addAll(authenticationList);

        List<Object> assertionList = DIDAssertionMethod.toObjectList(lalpDIDDocument.getAssertionMethodList());
        assertionList.addAll(assertionMethodList);

        targetDidDocument.setJsonObjectKeyValue("publicKey", publicKeyList);
        targetDidDocument.setJsonObjectKeyValue("authentication", authList);
        targetDidDocument.setJsonObjectKeyValue("assertionMethod", assertionList);
        targetDidDocument.setJsonObjectKeyValue("service", serviceList);

        return targetDidDocument;

    }

    public LalpDIDDocument toEntity() {
        return LalpDIDDocument.builder()
                .publicKeyList(PublicKeyDto.toEntityList(this.addPublicKeys))
                .authenticationList(AuthenticationDto.toEntityList(this.addAuthentications))
                .assertionMethodList(AssertionMethodDto.toEntityList(this.addAssertionMethods))
                .serviceList(ServiceDto.toEntityList(this.addServices))
                .build();
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RemoveKeyId {
        private String keyId;
    }

}
