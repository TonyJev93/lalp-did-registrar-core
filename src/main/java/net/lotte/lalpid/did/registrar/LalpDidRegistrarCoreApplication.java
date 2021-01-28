package net.lotte.lalpid.did.registrar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ldcc.lalp.did.fabric.client", "net.lotte.lalpid.did.registrar"})
public class LalpDidRegistrarCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(LalpDidRegistrarCoreApplication.class, args);
    }
}
