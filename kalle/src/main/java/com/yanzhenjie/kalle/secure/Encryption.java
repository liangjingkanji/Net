/*
 * Copyright © 2018 Zhenjie Yan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.kalle.secure;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created in 2016/4/10 11:27.
 */
public class Encryption {

    /**
     * Create a secret of encryption and decryption.
     */
    public static Secret createSecret(String key) {
        try {
            return new AESSecret(key);
        } catch (GeneralSecurityException e) {
            return new SafeSecret();
        }
    }

    /**
     * Byte array turn to hex string.
     */
    public static String byteArrayToHex(byte[] byteArray) {
        int len = byteArray.length;
        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            int intTmp = byteArray[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * Hex string turn to byte array.
     */
    public static byte[] hexToByteArray(String hexString) {
        byte[] byteArrayIn = hexString.getBytes();
        int iLen = byteArrayIn.length;
        byte[] byteArrayOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(byteArrayIn, i, 2);
            byteArrayOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return byteArrayOut;
    }

    /**
     * Get the md5 value of string.
     */
    public static String getMD5ForString(String content) {
        try {
            StringBuilder md5Builder = new StringBuilder();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] tempBytes = digest.digest(content.getBytes());
            int digital;
            for (byte tempByte : tempBytes) {
                digital = tempByte;
                if (digital < 0) {
                    digital += 256;
                }
                if (digital < 16) {
                    md5Builder.append("0");
                }
                md5Builder.append(Integer.toHexString(digital));
            }
            return md5Builder.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        return content;
    }

}
