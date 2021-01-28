package net.lotte.lalpid.did.registrar.infrastructure.didResolver;

import foundation.identity.did.DIDDocument;
import net.lotte.lalpid.did.registrar.domain.infra.ResolverRepository;
import net.lotte.lalpid.did.registrar.infrastructure.http.HttpHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@PropertySource(value = {"classpath:/lalpid.properties"})
public class LalpResolverRepository implements ResolverRepository {
    // 요청마다 신규 Session 발행하는 RestTemplate 사용

    private final RestTemplate restTemplate;

    @Value("${lalpid.resolver.url}")
    String resolverUrl;

    public LalpResolverRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public DIDDocument getDIDDocument(String did) {
        String requestUrl = HttpHelper.AddPath(resolverUrl, did);
        // Execute Http Method
        ResponseEntity<DIDDocument> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<DIDDocument>() {
                });

        return response.getBody();
    }
}
