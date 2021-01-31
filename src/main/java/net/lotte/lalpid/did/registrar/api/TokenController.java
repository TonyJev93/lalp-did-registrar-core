package net.lotte.lalpid.did.registrar.api;

import lombok.RequiredArgsConstructor;
import net.lotte.lalpid.did.registrar.api.dto.TokenDto;
import net.lotte.lalpid.did.registrar.infrastructure.util.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0/register/token")
@Validated
public class TokenController {
    @PostMapping("/jwt")
    public ResponseEntity<TokenDto.Res> getJwt(@RequestBody Map<String, Object> operation) {
        Jwt jwtToken = Jwt.generateToken(operation);
        return new ResponseEntity<TokenDto.Res>(TokenDto.Res.of(jwtToken), HttpStatus.OK);
    }
}
