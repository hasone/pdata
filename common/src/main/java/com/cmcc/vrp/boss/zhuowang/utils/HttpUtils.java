package com.cmcc.vrp.boss.zhuowang.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * <p>Title: </p> <p>Description: </p>
 *
 * @author lgk8023
 * @date 2017年1月22日 上午11:12:04
 */
public class HttpUtils {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String METHOD_POST = "POST";

    /**
     * @param url
     * @param params
     * @param charset
     * @param connectTimeout
     * @param readTimeout
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params,
                                String charset, int connectTimeout, int readTimeout,
                                Map<String, String> headerMap) throws IOException {
        return _doPostWithFile(url, params, charset, connectTimeout,
                readTimeout, headerMap);
    }

    private static String _doPostWithFile(String url,
                                          Map<String, String> params, String charset, int connectTimeout,
                                          int readTimeout, Map<String, String> headerMap) throws IOException {
        String boundary = System.currentTimeMillis() + "";
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                String ctype = "multipart/form-data;charset=" + charset
                        + ";boundary=" + boundary;
                conn = getConnection(new URL(url), "POST", ctype, headerMap);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
            }
            try {
                if (conn == null) {
                    return null;
                }
                out = conn.getOutputStream();

                byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n")
                        .getBytes(charset);

                byte[] xmlheadBytes = getTextEntry("xmlhead",
                        (String) params.get("xmlhead"), charset);
                out.write(entryBoundaryBytes);
                out.write(xmlheadBytes);

                byte[] xmlbodyBytes = getTextEntry("xmlbody",
                        (String) params.get("xmlbody"), charset);
                out.write(entryBoundaryBytes);
                out.write(xmlbodyBytes);

                byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n")
                        .getBytes(charset);
                out.write(endBoundaryBytes);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                throw e;
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if ((value == null) || ((strLen = value.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static byte[] getTextEntry(String fieldName, String fieldValue,
                                       String charset) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\"\r\nContent-Type:text/plain; charset=UTF-8\r\n\r\n");
        entry.append(fieldValue);
        return entry.toString().getBytes(charset);
    }

    private static HttpURLConnection getConnection(URL url, String method,
                                                   String ctype, Map<String, String> headerMap) throws IOException {
        HttpURLConnection conn = null;
        if ("https".equals(url.getProtocol())) {
            SSLContext ctx = null;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0],
                        new TrustManager[]{new DefaultTrustManager()},
                        new SecureRandom());
            } catch (Exception e) {
                throw new IOException(e);
            }
            HttpsURLConnection connHttps = (HttpsURLConnection) url
                    .openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn = connHttps;
        } else {

            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestProperty("connection", "Keep-Alive");

        conn.setRequestProperty("Content-Type", ctype);
        if (headerMap != null) {
            for (String key : headerMap.keySet()) {
                conn.setRequestProperty(key, headerMap.get(key));
            }
        }
        return conn;
    }

    protected static String getResponseAsString(HttpURLConnection conn)
            throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        }
        String msg = getStreamAsString(es, charset);
        if (isEmpty(msg)) {
            throw new IOException(conn.getResponseCode() + ":"
                    + conn.getResponseMessage());
        }
        throw new IOException(msg);
    }

    private static String getStreamAsString(InputStream stream, String charset)
            throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();

            char[] buff = new char['?'];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }
            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = "UTF-8";
        if (!isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if ((pair.length != 2) || (isEmpty(pair[1]))) {
                        break;
                    }
                    charset = pair[1].trim();
                    break;
                }
            }
        }
        return charset;
    }

    /**
     * <p>Title: </p> <p>Description: </p>
     *
     * @author lgk8023
     * @date 2017年1月22日 上午11:12:40
     */
    public static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        /**
         * (non-Javadoc)
         *
         * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[],
         * java.lang.String)
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        /**
         * (non-Javadoc)
         *
         * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[],
         * java.lang.String)
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }

}
