package net.lotte.lalpid.did.registrar.infrastructure.util;

import java.net.URI;
import java.util.UUID;

public class Uuid {

    public static String generateJobId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static URI generateDid() {
        return URI.create("did:lalp:" + UUID.randomUUID().toString());
    }

}
