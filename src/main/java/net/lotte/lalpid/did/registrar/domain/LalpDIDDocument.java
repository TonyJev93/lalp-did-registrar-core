package net.lotte.lalpid.did.registrar.domain;

import foundation.identity.did.DIDDocument;
import lombok.*;
import net.lotte.lalpid.did.registrar.infrastructure.util.Uuid;

import java.net.URI;
import java.util.List;

import static net.lotte.lalpid.did.registrar.infrastructure.util.Time.getCurrentDateTimeForW3CFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LalpDIDDocument {

    private String context;

    private URI did;

    private List<DIDService> serviceList;

    private List<DIDAssertionMethod> assertionMethodList;

    private List<DIDAuthentication> authenticationList;

    private List<DIDPublicKey> publicKeyList;

    private String created;

    private String updated;

    @Builder
    public LalpDIDDocument(URI did, String context, List<DIDService> serviceList, List<DIDAssertionMethod> assertionMethodList, List<DIDAuthentication> authenticationList, List<DIDPublicKey> publicKeyList) {
        this.did = did;
        this.context = context;
        this.serviceList = serviceList;
        this.assertionMethodList = assertionMethodList;
        this.authenticationList = authenticationList;
        this.publicKeyList = publicKeyList;
    }

    public static LalpDIDDocument fromDIDDocument(DIDDocument didDocument) {
        return LalpDIDDocument.builder()
                .did(didDocument.getId())
                .context(didDocument.getContexts().get(0).toString())
                .serviceList(DIDService.fromServiceList(didDocument.getServices()))
                .publicKeyList(DIDPublicKey.fromDIDDocument(didDocument))
                .authenticationList(DIDAuthentication.fromAuthenticationList(didDocument.getAuthentications()))
//                .assertionMethodList(DIDAssertionMethod.fromAssertionMethodList(didDocument.getAssertionMethods()))
                .build();
    }

    public void init() {
        // 신규 DID 발번
        this.did = Uuid.generateDid();

        // Context 맵핑
        this.context = DIDDocument.builder().build().getContexts().get(0).toString();

        this.mappingDID(this.did);

        // 생성일시
        this.created = getCurrentDateTimeForW3CFormat();

        // 수정일시
        this.updated = getCurrentDateTimeForW3CFormat();
    }

    public void mappingDID(URI did) {
        // PublicKey - DID 맵핑
        this.publicKeyList.forEach(didPublicKey -> didPublicKey.mappingDid(did));

        // Authentication - DID 맵핑
        this.authenticationList.forEach(didAuthentication -> didAuthentication.mappingDid(did));

        // AssertionMethod - DID 맵핑
        this.assertionMethodList.forEach(didAssertionMethod -> didAssertionMethod.mappingDid(did));

        // Service - DID 맵핑
        this.serviceList.forEach(service -> service.mappingDid(did));

    }

    public DIDDocument toDIDDocument() {

        DIDDocument didDocument = DIDDocument.builder().id(this.did).build();
        didDocument.setJsonObjectKeyValue("publicKey", DIDPublicKey.toPublicKeyList(this.publicKeyList));
        didDocument.setJsonObjectKeyValue("service", DIDService.toServiceList(this.serviceList));
        didDocument.setJsonObjectKeyValue("authentication", DIDAuthentication.toObjectList(this.authenticationList));
        didDocument.setJsonObjectKeyValue("assertionMethod", DIDAssertionMethod.toObjectList(this.assertionMethodList));
        didDocument.setJsonObjectKeyValue("created", this.created);
        didDocument.setJsonObjectKeyValue("updated", this.updated);

        return didDocument;
    }
}
