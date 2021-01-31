package net.lotte.lalpid.did.registrar.api;

import lombok.RequiredArgsConstructor;
import net.lotte.lalpid.did.registrar.api.dto.RegistrarDto;
import net.lotte.lalpid.did.registrar.api.dto.validator.markInterface.Delete;
import net.lotte.lalpid.did.registrar.api.dto.validator.markInterface.Update;
import net.lotte.lalpid.did.registrar.application.DIDTokenService;
import net.lotte.lalpid.did.registrar.application.RegistrarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_MSG_DID_URL_PATTERN_ERROR;
import static net.lotte.lalpid.did.registrar.api.dto.validator.ValidationMsg.VALID_CHK_REGEXP_LALP_DID_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0/register")
@Validated
public class RegistrarController {

    private final RegistrarService registrarService;
    private final DIDTokenService didTokenService;


    @PostMapping
    public ResponseEntity<RegistrarDto.RegisterRes> registerDid(@RequestBody @Valid RegistrarDto.RegisterReq request) {
        return new ResponseEntity<RegistrarDto.RegisterRes>(RegistrarDto.RegisterRes.builder().lalpDIDDocument(registrarService.register(request.toEntity())).build(), HttpStatus.CREATED);
    }

    @PutMapping("/{did}")
    public ResponseEntity<RegistrarDto.UpdateRes> updateDid(@RequestBody @Validated(Update.class) RegistrarDto.UpdateReq request
            , @PathVariable("did") @Validated(Update.class) @Pattern(regexp = VALID_CHK_REGEXP_LALP_DID_URL, message = VALID_CHK_MSG_DID_URL_PATTERN_ERROR) String did
            , BindingResult bindingResult) throws BindException {

        String token = request.getToken();
        didTokenService.verify(token, did, bindingResult);

        if (!registrarService.update(token, did)) {
            return new ResponseEntity<RegistrarDto.UpdateRes>(RegistrarDto.UpdateRes.builder().msg("fail to update DID document").result(false).build(), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<RegistrarDto.UpdateRes>(RegistrarDto.UpdateRes.builder().msg("success to update DID document.").result(true).build(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{did}")
    public ResponseEntity<RegistrarDto.UpdateRes> deleteDid(@RequestBody @Validated(Delete.class) RegistrarDto.DeleteReq request
            , @PathVariable("did") @Validated(Delete.class) @Pattern(regexp = VALID_CHK_REGEXP_LALP_DID_URL, message = VALID_CHK_MSG_DID_URL_PATTERN_ERROR) String did
            , BindingResult bindingResult) throws BindException {

        String token = request.getToken();
        didTokenService.verify(token, did, bindingResult);

        registrarService.delete(did);
        return new ResponseEntity<RegistrarDto.UpdateRes>(RegistrarDto.UpdateRes.builder().msg("success to delete DID document.").result(true).build(), HttpStatus.ACCEPTED);
    }

}

