package net.lotte.lalpid.did.registrar.api;

import lombok.RequiredArgsConstructor;
import net.lotte.lalpid.did.registrar.api.dto.RegistrarDto;
import net.lotte.lalpid.did.registrar.api.dto.validator.UpdateValidator;
import net.lotte.lalpid.did.registrar.api.dto.validator.markInterface.Update;
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
    private final UpdateValidator updateValidator;

    @PostMapping
    public ResponseEntity<RegistrarDto.Res> registerDid(@RequestBody @Valid RegistrarDto.RegisterReq request) {
        return new ResponseEntity<RegistrarDto.Res>(RegistrarDto.Res.builder().lalpDIDDocument(registrarService.register(request.toEntity())).build(), HttpStatus.CREATED);
    }

    @PutMapping("/{didUrl}")
    public ResponseEntity<RegistrarDto.Res> updateDid(@RequestBody @Validated(Update.class) RegistrarDto.UpdateReq request
            , @PathVariable("didUrl") @Validated(Update.class) @Pattern(regexp = VALID_CHK_REGEXP_LALP_DID_URL, message = VALID_CHK_MSG_DID_URL_PATTERN_ERROR) String didUrl
            , BindingResult bindingResult) throws BindException {

        updateValidator.validate(request, didUrl, bindingResult);
        return new ResponseEntity<RegistrarDto.Res>(RegistrarDto.Res.builder().lalpDIDDocument(registrarService.update(request.getToken())).build(), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<RegistrarDto.Res> deleteDid(@RequestBody @Valid RegistrarDto.DeleteReq request) {
        return null;
    }

}

