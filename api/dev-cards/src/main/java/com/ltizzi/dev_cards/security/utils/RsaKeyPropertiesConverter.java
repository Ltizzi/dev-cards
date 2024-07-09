package com.ltizzi.dev_cards.security.utils;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Leonardo Terlizzi
 */
@Component
@ConfigurationPropertiesBinding
public class RsaKeyPropertiesConverter {

    @Component
    public static class RsaPublicKeyConverter implements Converter<String, RSAPublicKey>{
        @Override
        public RSAPublicKey convert(String source){
            try{
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(source));
                return (RSAPublicKey) keyFactory.generatePublic(keySpec);
            }catch (Exception e){
                throw new IllegalArgumentException("Failted to convert public key", e);
            }
        }
    }
    @Component
    public static class RSAPrivateKeyConverter implements Converter<String, RSAPrivateKey>{
        @Override
        public RSAPrivateKey convert(String source){
            try{
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(source));
                return  (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

            }catch (Exception e){
                throw new IllegalArgumentException("Failed to convert private key", e);

            }
        }
    }
}
