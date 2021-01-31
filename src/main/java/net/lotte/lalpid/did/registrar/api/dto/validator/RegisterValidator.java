package net.lotte.lalpid.did.registrar.api.dto.validator;

import net.lotte.lalpid.did.registrar.api.dto.RegistrarDto;
import net.lotte.lalpid.did.registrar.api.dto.request.AssertionMethodDto;
import net.lotte.lalpid.did.registrar.api.dto.request.AuthenticationDto;
import net.lotte.lalpid.did.registrar.api.dto.request.PublicKeyDto;
import net.lotte.lalpid.did.registrar.api.dto.request.ServiceDto;
import net.lotte.lalpid.did.registrar.api.dto.validator.annotation.RegisterInputCheck;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.lotte.lalpid.did.registrar.global.error.ErrorCode.*;

@Component
public class RegisterValidator implements ConstraintValidator<RegisterInputCheck, RegistrarDto.RegisterReq> {
    @Override
    public void initialize(RegisterInputCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrarDto.RegisterReq req, ConstraintValidatorContext constraintValidatorContext) {
        int invalidCount = 0;

        /* ************************************************ 0. 변수 선언 ********************************************************* */
        List<PublicKeyDto> publicKeyList = Optional.ofNullable(req.getAddPublicKeys()).orElse(new ArrayList<>());
        List<AuthenticationDto> authenticationList = Optional.ofNullable(req.getAddAuthentications()).orElse(new ArrayList<>());
        List<AssertionMethodDto> assertionMethodList = Optional.ofNullable(req.getAddAssertionMethods()).orElse(new ArrayList<>());
        List<ServiceDto> serviceList = Optional.ofNullable(req.getAddServices()).orElse(new ArrayList<>());

        /* 1. PublicKey */
        // Key ID List
        List<String> publicKeyIdList = PublicKeyDto.getKeyIdList(publicKeyList);

        /* 2. Authentication */
        // (Type 포함 O) Auth Key List
        List<AuthenticationDto> authKeyListWithType = AuthenticationDto.getKeyListWithType(authenticationList);

        // (Type 포함 O) Auth Key ID List
        List<String> authKeyIdListWithType = AuthenticationDto.getKeyIdListWithType(authenticationList);

        // (Type 포함 X) Auth Key ID List
        List<String> authKeyIdListWithoutType = AuthenticationDto.getKeyIdListWithoutType(authenticationList);

        // (Type 포함 O) Assertion Key List
        List<AssertionMethodDto> assertionKeyListWithType = AssertionMethodDto.getKeyListWithType(assertionMethodList);

        /* 3. AssertionMethod */
        // (Type 포함 O) Assertion Key ID List
        List<String> assertionKeyIdListWithType = AssertionMethodDto.getKeyIdListWithType(assertionKeyListWithType);

        // (Type 포함 X) Assertion Key ID List
        List<String> assertionKeyIdListWithoutType = AssertionMethodDto.getKeyIdListWithoutType(assertionMethodList);

        /* 4. Service */
        // Key ID List
        List<String> serviceKeyIdList = ServiceDto.getKeyIdList(serviceList);
        /* ************************************************************************************************************************ */

        /* ************************************************ 1. publicKey 유효성 검증 ************************************************ */
        // 1.1. KEY ID 중복여부 체크
        if (publicKeyList.stream().map(PublicKeyDto::getKeyId).distinct().count() != publicKeyList.size()) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, ALREADY_EXIST_KEY_ID.getMessage(), "addPublicKeys");
        }
        /* ************************************************************************************************************************ */


        /* ************************************************ 2. authentication 유효성 검증 ******************************************* */
        // 2.1. KEY ID PublicKey 내 존재여부(key Type이 없는 경우에만)
        if (!publicKeyIdList.containsAll(authKeyIdListWithoutType)) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, NOT_FOUND_PUBLIC_KEY_ID.getMessage(), "addAuthentications");
        }

        // 2.2. KEY ID 중복여부 체크(key Type이 있는 경우에만)
        if (authKeyIdListWithType.stream().distinct().count() != authKeyIdListWithType.size()
                || authKeyIdListWithType.stream().anyMatch(authKeyId -> publicKeyIdList.contains(authKeyId) || assertionKeyIdListWithType.contains(authKeyId) || serviceKeyIdList.contains(authKeyId))
        ) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, ALREADY_EXIST_KEY_ID.getMessage(), "addAuthentications");
        }

        // 2.3. KEY value 존재여부 체크(key Type이 있는 경우에만)
        if (authKeyListWithType.stream().anyMatch(addAuthentication -> this.isEmpty(addAuthentication.getPublicKeyValue()))) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, NOT_FOUND_PUBLIC_KEY_VALUE.getMessage(), "addAuthentications");
        }
        /* ************************************************************************************************************************ */

        /* ************************************************ 3. assertionMethod 유효성 검증 ****************************************** */
        // 3.1. KEY ID PublicKey 내 존재여부 (key Type이 없는 경우에만)
        if (!publicKeyIdList.containsAll(assertionKeyIdListWithoutType)) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, NOT_FOUND_PUBLIC_KEY_ID.getMessage(), "addAssertionMethods");
        }

        // 3.2. KEY ID 중복여부 체크(key Type이 있는 경우에만)
        if (assertionKeyIdListWithType.stream().distinct().count() != assertionKeyIdListWithType.size()
                || assertionKeyIdListWithType.stream().anyMatch(assertionKeyId -> publicKeyIdList.contains(assertionKeyId) || authKeyIdListWithType.contains(assertionKeyId) || serviceKeyIdList.contains(assertionKeyId))) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, ALREADY_EXIST_KEY_ID.getMessage(), "addAssertionMethods");
        }

        // 3.3. KEY value 존재여부 체크(key Type이 있는 경우에만)
        if (assertionKeyListWithType.stream().anyMatch(assertionMethod -> this.isEmpty(assertionMethod.getPublicKeyValue()))) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, NOT_FOUND_PUBLIC_KEY_VALUE.getMessage(), "addAuthentications");
        }
        /* ************************************************************************************************************************ */

        /* ************************************************ 4. service 유효성 검증 ****************************************** */
        // 4.1. Key ID 중복여부 체크
        if (serviceKeyIdList.stream().distinct().count() != serviceKeyIdList.size()
                || serviceKeyIdList.stream().anyMatch(serviceKeyId -> publicKeyIdList.contains(serviceKeyId) || authKeyIdListWithType.contains(serviceKeyId) || assertionKeyIdListWithType.contains(serviceKeyId))) {
            invalidCount++;
            addConstraintViolation(constraintValidatorContext, ALREADY_EXIST_KEY_ID.getMessage(), "addServices");
        }
        /* ************************************************************************************************************************ */
        return invalidCount == 0;
    }

    private boolean isEmpty(String str) {
        return "".equals(Optional.ofNullable(str).orElse(""));
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage, String firstNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(firstNode)
                .addConstraintViolation();
    }

}
