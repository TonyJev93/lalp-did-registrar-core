package net.lotte.lalpid.did.registrar.infrastructure.fabric;

import com.ldcc.lalp.did.fabric.client.FabricService;
import foundation.identity.did.DIDDocument;
import lombok.AllArgsConstructor;
import net.lotte.lalpid.did.registrar.domain.infra.RegistrarRepository;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class FabricRegistrarRepository implements RegistrarRepository {

    private final FabricService fabricService;

    @Override
    public boolean saveDIDDocument(DIDDocument didDocument) {

        try {
            System.out.println(didDocument.toJson(true));

//            fabricService.executeChaincodeForPutState("registerDidDocument", didDocument.toJson(true);

            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public void updateDIDDocument() {

    }

    @Override
    public void deleteDIDDocument() {

    }
}
