package com.wardrumstudios.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLException;
import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;

public class WarHttp {
    private static boolean AddLineFeeds = false;
    private int timeoutSeconds = 3;

    protected WarHttp(WarBase activity) {
    }

    public void AddHttpGetLineFeeds(boolean value) {
        AddLineFeeds = value;
    }

    public void SetHttpTimeout(int seconds) {
    }

    public String HttpPost(String url) {
        Log.e("log_tag", "return blank string");
        return "";
    }

    public String HttpGet(String url) {

        Log.e("log_tag", "return blank string");
        return "";
    }

    public byte[] HttpGetData(String url) {

        Log.e("log_tag", "return blank string");
        return null;
    }


    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder("");
        while (true) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    sb.append(line);
                    if (AddLineFeeds) {
                        sb.append("\n");
                    }
                } else {
                    break;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                try {
                    is.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    is.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
        }
        is.close();
        Log.e("log_tag", "convertStreamToString " + sb.toString());
        return sb.toString();
    }

    class MyVerifier extends AbstractVerifier {
        private final X509HostnameVerifier delegate;

        public MyVerifier(X509HostnameVerifier delegate2) {
            this.delegate = delegate2;
        }

        public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            boolean ok = false;
            try {
                this.delegate.verify(host, cns, subjectAlts);
            } catch (SSLException e) {
                for (String cn : cns) {
                    if (cn.startsWith("*.")) {
                        try {
                            if (cn.substring(2).equals("onmodulus.net")) {
                            }
                            this.delegate.verify(host, new String[]{cn.substring(2)}, subjectAlts);
                            ok = true;
                        } catch (Exception e2) {
                        }
                    }
                }
                if (!ok) {
                    throw e;
                }
            }
        }
    }
}
