package com.example.demo.services;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class CryptoService {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    
    // Gerar uma chave secreta para criptografia AES
    public String generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);  // Tamanho de chave AES de 256 bits
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
    
    // Gerar um vetor de inicialização aleatório
    public String generateIV() {
        byte[] iv = new byte[16]; // AES block size = 16 bytes
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }
    
    // Criptografar mensagem
    public String encrypt(String plainText, String keyString, String ivString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        byte[] ivBytes = Base64.getDecoder().decode(ivString);
        
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    // Descriptografar mensagem
    public String decrypt(String encryptedText, String keyString, String ivString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        byte[] ivBytes = Base64.getDecoder().decode(ivString);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }
}
