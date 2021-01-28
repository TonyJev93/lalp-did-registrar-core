package net.lotte.lalpid.did.registrar.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import java.io.StringReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class Security {

    // key 알고리즘, size에 맞는 Key pair 반환
    public static KeyPair generateKeyPair(String keyFactoryAlgorithm, int keySize) throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance(keyFactoryAlgorithm);

        generator.initialize(keySize, new SecureRandom());

        KeyPair keyPair = generator.generateKeyPair();

        return keyPair;
    }

    /**
     * Public Key로 RSA 암호화를 수행합니다.
     *
     * @param plainText 암호화할 평문입니다.
     * @param publicKey 공개키 입니다.
     * @return
     */
    public static String encryptRSA(String keyFactoryAlgorithm, String plainText, PublicKey publicKey) {
        String encrypted = "";

        try {
            Cipher cipher = Cipher.getInstance(keyFactoryAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytePlain = cipher.doFinal(plainText.getBytes());
            encrypted = Base64.getEncoder().encodeToString(bytePlain);
        } catch (Exception ex) {

        }
        return encrypted;
    }

    /**
     * Private Key로 RAS 복호화를 수행합니다.
     *
     * @param encrypted  암호화된 이진데이터를 base64 인코딩한 문자열 입니다.
     * @param privateKey 복호화를 위한 개인키 입니다.
     * @return
     * @throws Exception
     */
    public static String decryptRSA(String keyFactoryAlgorithm, String encrypted, PrivateKey privateKey) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance(keyFactoryAlgorithm);
            byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytePlain = cipher.doFinal(byteEncrypted);
            decrypted = new String(bytePlain, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public static String sign(String signatureAlgorithm, String plainText, PrivateKey privateKey) {
        byte[] signature = null;
        try {
            Signature privateSignature = Signature.getInstance(signatureAlgorithm);
            privateSignature.initSign(privateKey);
            String charset = "utf-8";
            privateSignature.update(plainText.getBytes(charset));
            signature = privateSignature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String signatureAlgorithm, String plainText, String signature, PublicKey publicKey) {
        try {
            return Security.verifySignature(signatureAlgorithm, plainText, signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean verifySignature(String signatureAlgorithm, String plainText, String signature, PublicKey publicKey) {
        Signature sig;

        try {
            sig = Signature.getInstance(signatureAlgorithm);
            sig.initVerify(publicKey);
            sig.update(plainText.getBytes());
            if (!sig.verify(Base64.getDecoder().decode(signature))) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // STRING -> PUBLIC KEY
    public static PublicKey publicKeyConverter(String stringPublicKey) {

        PublicKey publicKey = null;
        StringReader reader = new StringReader(stringPublicKey);
        PemReader pr = new PemReader(reader);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pr.readPemObject().getContent());

            publicKey = keyFactory.generatePublic(publicKeySpec);

        } catch (Exception ex) {
            log.debug("Convert publicKey key(DB -> Program) ERROR / DB publicKey key : %s", stringPublicKey);
            ex.printStackTrace();
        }

        return publicKey;
    }

    // STRING -> PRIVATE KEY
    public static PrivateKey PrivateKeyConverter(String stringPrivateKey) {

        PrivateKey privateKey = null;
        StringReader reader = new StringReader(stringPrivateKey);
        PemReader pr = new PemReader(reader);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pr.readPemObject().getContent());
            privateKey = keyFactory.generatePrivate(privateKeySpec);

        } catch (Exception ex) {
            log.debug("Convert private key(DB -> Program) ERROR / DB private key : %s", stringPrivateKey);
            throw new RuntimeException();
        }

        return privateKey;
    }
}
