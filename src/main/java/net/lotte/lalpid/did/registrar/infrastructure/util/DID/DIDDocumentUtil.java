package net.lotte.lalpid.did.registrar.infrastructure.util.DID;

import foundation.identity.did.DIDDocument;
import foundation.identity.did.PublicKey;
import net.lotte.lalpid.did.registrar.global.error.ErrorCode;
import net.lotte.lalpid.did.registrar.global.error.exception.BusinessException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DIDDocumentUtil {
    public static List<PublicKey> getPublicKeyList(DIDDocument didDocument) {
        return ((List<Map<String, Object>>) didDocument.getJsonObject().get("publicKey"))
                .stream().map(publicKeyMap -> PublicKey.fromJsonObject(publicKeyMap)).collect(Collectors.toList());
    }

    public static PublicKey selectPublicKey(List<PublicKey> publicKeyList, String keyId) {
        List<PublicKey> selectedPublicKeyList = publicKeyList.stream()
                .filter(publicKey -> keyId.equals(DIDUrlUtil.fromUri(publicKey.getId()).getFragment())).collect(Collectors.toList());

        if (Optional.ofNullable(selectedPublicKeyList).get().size() == 0) {
            throw new BusinessException("서명한 Key ID에 해당하는 PublicKey를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_PUBLIC_KEY_ID);
        }

        return selectedPublicKeyList.get(0);

    }
}
