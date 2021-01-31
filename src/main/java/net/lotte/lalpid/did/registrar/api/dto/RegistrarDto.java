package net.lotte.lalpid.did.registrar.api.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.lotte.lalpid.did.registrar.api.dto.request.AssertionMethodDto;
import net.lotte.lalpid.did.registrar.api.dto.request.AuthenticationDto;
import net.lotte.lalpid.did.registrar.api.dto.request.PublicKeyDto;
import net.lotte.lalpid.did.registrar.api.dto.request.ServiceDto;
import net.lotte.lalpid.did.registrar.api.dto.response.DidState;
import net.lotte.lalpid.did.registrar.api.dto.validator.annotation.RegisterInputCheck;
import net.lotte.lalpid.did.registrar.api.dto.validator.markInterface.Update;
import net.lotte.lalpid.did.registrar.domain.LalpDIDDocument;
import net.lotte.lalpid.did.registrar.infrastructure.util.Uuid;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.*;

public class RegistrarDto {

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @RegisterInputCheck
    @Getter
    public static class RegisterReq {

        @Valid
        @NotEmpty(message = VALID_CHK_MSG_ADD_PUBLIC_KEY_IS_EMPTY)
        private List<PublicKeyDto> addPublicKeys;

        @Valid
        @NotEmpty(message = VALID_CHK_MSG_ADD_AUTHENTICATION_IS_EMPTY) // DID 수정/삭제를 위해 Authentication 무조건 하나 이상 필요
        private List<AuthenticationDto> addAuthentications;

        @Valid
        private List<AssertionMethodDto> addAssertionMethods;

        @Valid
        private List<ServiceDto> addServices;

        @Builder
        public RegisterReq(List<PublicKeyDto> addPublicKeys, List<AuthenticationDto> addAuthentications, List<AssertionMethodDto> addAssertionMethods, List<ServiceDto> addServices) {
            this.addPublicKeys = addPublicKeys;
            this.addAuthentications = addAuthentications;
            this.addAssertionMethods = addAssertionMethods;
            this.addServices = addServices;
        }

        public LalpDIDDocument toEntity() {
            return LalpDIDDocument.builder()
                    .publicKeyList(PublicKeyDto.toEntityList(this.addPublicKeys))
                    .authenticationList(AuthenticationDto.toEntityList(this.addAuthentications))
                    .assertionMethodList(AssertionMethodDto.toEntityList(this.addAssertionMethods))
                    .serviceList(ServiceDto.toEntityList(this.addServices))
                    .build();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class UpdateReq {
        @Valid
        @NotEmpty(message = VALID_CHK_MSG_TOKEN_IS_EMPTY, groups = Update.class)
        private String token;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class DeleteReq {
        @Valid
        @NotEmpty(message = VALID_CHK_MSG_TOKEN_IS_EMPTY, groups = Update.class)
        private String token;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegisterRes {
        private String jobId;
        private DidState didState;

        @Builder
        public RegisterRes(LalpDIDDocument lalpDIDDocument) {
            this.jobId = Uuid.generateJobId();
            this.didState = DidState.builder().lalpDIDDocument(lalpDIDDocument).build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRes {
        private boolean result;
        private String msg;

        @Builder
        public UpdateRes(boolean result, String msg) {
            this.result = result;
            this.msg = msg;
        }
    }


}
