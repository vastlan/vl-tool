package com.vast.vl_tool.security;

import com.vast.vl_tool.exception.PropertyException;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

/**
 * @author vastlan
 * @description
 * @created 2022/2/24 17:36
 */

public class RSAGenerator {

  private static final KeyPair keyPair;

  private static final String KEY_ALGORITHM  = "RSA";

  private static final Integer KEY_SIZE  = 1024;

  private static final String base64PrivateKey;

  public static final String base64PublicKey;

  static {
    try {

      // 获取对象 keyPairGenerator 参数 RSA 1024byte
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
      SecureRandom secureRandom = new SecureRandom();
      keyPairGenerator.initialize(KEY_SIZE, secureRandom);

      // 通过对象 keyPairGenerator 获取对象 keyPair
      keyPair = keyPairGenerator.generateKeyPair();

      // 通过对象 keyPair 获取 RSA 公秘钥对象
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

      byte[] publicKeyBytes = publicKey.getEncoded();
      byte[] privateKeyBytes = privateKey.getEncoded();

      Base64.Encoder encoder = Base64.getEncoder();

      base64PublicKey = encoder.encodeToString(publicKeyBytes);
      base64PrivateKey = encoder.encodeToString(privateKeyBytes);

    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new PropertyException("RSAGenerator 加解密异常");
    }
  }

  public static String getPublicKey() {
    return base64PublicKey;
  }

  public static String getPrivateKey() {
    return base64PublicKey;
  }

  /**
   * 加密
   * @param password
   * @return
   */
  public static String encrypt(String password) {
    if (!StringUtils.hasLength(password)) {
      throw new PropertyException("密码不合法");
    }

    byte[] bytes = password.getBytes(StandardCharsets.UTF_8);

    try {

      // 得到Cipher对象来实现对源数据的RSA加密
      Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, RSAGenerator.keyPair.getPublic());

      // 执行加密操作
      byte[] finalBytes = cipher.doFinal(bytes);

      return Base64.getEncoder().encodeToString(finalBytes);

    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new PropertyException("RSAGenerator 加密异常");
    }
  }

  /**
   * 解密
   * @param password
   * @return
   */
  public static String decrypt(String password) {
    if (!StringUtils.hasLength(password)) {
      throw new PropertyException("密码不合法");
    }

    byte[] bytes = Base64.getDecoder().decode(password);

    try {

      // 得到Cipher对象对已用公钥加密的数据进行RSA解密
      Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, RSAGenerator.keyPair.getPrivate());

      // 执行解密操作
      byte[] finalBytes = cipher.doFinal(bytes);

      return new String(finalBytes, StandardCharsets.UTF_8);

    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new PropertyException("RSAGenerator 解密异常");
    }

  }

}
