/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.secure;

import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Zhenjie Yan on 2018/2/11.
 */
public class AESSecret implements Secret {

    private Cipher encrypt;
    private Cipher decrypt;

    public AESSecret(String key) throws GeneralSecurityException {
        Key cryptKey = getKey(key.getBytes());
        encrypt = Cipher.getInstance("AES");
        encrypt.init(Cipher.ENCRYPT_MODE, cryptKey);
        decrypt = Cipher.getInstance("AES");
        decrypt.init(Cipher.DECRYPT_MODE, cryptKey);
    }

    @Override
    public String encrypt(String data) throws GeneralSecurityException {
        return Encryption.byteArrayToHex(encrypt(data.getBytes()));
    }

    @Override
    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        return encrypt.doFinal(data);
    }

    @Override
    public String decrypt(String data) throws GeneralSecurityException {
        return new String(decrypt(Encryption.hexToByteArray(data)));
    }

    @Override
    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        return decrypt.doFinal(data);
    }

    private Key getKey(byte[] keyData) {
        byte[] arrB = new byte[8];
        for (int i = 0; i < keyData.length && i < arrB.length; i++) {
            arrB[i] = keyData[i];
        }
        return new SecretKeySpec(arrB, "AES");
    }
}