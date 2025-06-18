package com.wardrumstudios.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MediaVault {
    private String secret;

    public MediaVault(String secret2) {
        this.secret = secret2;
    }

    public String compute() {
        return compute((MediaVaultRequest) null);
    }

    public String compute(MediaVaultRequest options) throws IllegalArgumentException {
        if (options == null) {
            throw new IllegalArgumentException("Invalid 'options' parameter.");
        } else if (options.getMediaURL() == null || options.getMediaURL().length() == 0) {
            throw new IllegalArgumentException("options.getMediaURL() is required.");
        } else if (this.secret == null || this.secret.length() == 0) {
            throw new IllegalArgumentException("MediaVault.getSecret() is null.");
        } else {
            StringBuilder result = new StringBuilder(options.getMediaURL());
            String urlParams = "";
            String hashParams = "";
            if (options != null) {
                urlParams = options.getURLParamers();
                hashParams = options.getHashParameters();
            }
            if (urlParams.length() > 0) {
                if (result.indexOf("?") > -1) {
                    result.append("&" + urlParams);
                } else {
                    result.append("?" + urlParams);
                }
            }
            String hash = getMD5Hash(this.secret + hashParams + result);
            if (result.indexOf("?") > -1) {
                result.append("&h=" + hash);
            } else {
                result.append("?h=" + hash);
            }
            return result.toString();
        }
    }

    private String getMD5Hash(String hashParams) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hashParams.getBytes());
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 255) < 16) {
                sb.append("0");
            }
            sb.append(Long.toString((long) (b[i] & 255), 16));
        }
        return sb.toString();
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret2) {
        this.secret = secret2;
    }
}
