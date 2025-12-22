package com.atguigu.spzx.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP 请求工具类（Apache HttpClient 4.5+）
 */
public final class HttpUtils {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 5000;

    /* ========================= GET ========================= */

    public static HttpResponse doGet(String host,
                                     String path,
                                     Map<String, String> headers,
                                     Map<String, String> querys) throws Exception {
        try (CloseableHttpClient client = wrapClient(host)) {
            URIBuilder builder = new URIBuilder(buildUrl(host, path));
            addQuery(builder, querys);

            HttpGet request = new HttpGet(builder.build());
            setHeaders(request, headers);
            setConfig(request);
            return client.execute(request);
        }
    }

    /* ========================= POST ========================= */

    /** POST 表单 */
    public static HttpResponse doPost(String host,
                                      String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      Map<String, String> bodys) throws Exception {
        try (CloseableHttpClient client = wrapClient(host)) {
            URIBuilder builder = new URIBuilder(buildUrl(host, path));
            addQuery(builder, querys);

            HttpPost request = new HttpPost(builder.build());
            setHeaders(request, headers);
            setConfig(request);

            if (bodys != null && !bodys.isEmpty()) {
                List<BasicNameValuePair> pairs = new ArrayList<>();
                for (Map.Entry<String, String> e : bodys.entrySet()) {
                    pairs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
                }
                request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            }
            return client.execute(request);
        }
    }

    /** POST JSON */
    public static HttpResponse doPost(String host,
                                      String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body) throws Exception {
        try (CloseableHttpClient client = wrapClient(host)) {
            URIBuilder builder = new URIBuilder(buildUrl(host, path));
            addQuery(builder, querys);

            HttpPost request = new HttpPost(builder.build());
            setHeaders(request, headers);
            setConfig(request);

            if (StringUtils.isNotBlank(body)) {
                request.setEntity(new StringEntity(body, "UTF-8"));
            }
            return client.execute(request);
        }
    }

    /** POST 字节流 */
    public static HttpResponse doPost(String host,
                                      String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      byte[] body) throws Exception {
        try (CloseableHttpClient client = wrapClient(host)) {
            URIBuilder builder = new URIBuilder(buildUrl(host, path));
            addQuery(builder, querys);

            HttpPost request = new HttpPost(builder.build());
            setHeaders(request, headers);
            setConfig(request);

            if (body != null) {
                request.setEntity(new ByteArrayEntity(body));
            }
            return client.execute(request);
        }
    }

    /* ========================= PUT ========================= */

    public static HttpResponse doPut(String host,
                                     String path,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     String body) throws Exception {
        try (CloseableHttpClient client = wrapClient(host)) {
            URIBuilder builder = new URIBuilder(buildUrl(host, path));
            addQuery(builder, querys);

            HttpPut request = new HttpPut(builder.build());
            setHeaders(request, headers);
            setConfig(request);

            if (StringUtils.isNotBlank(body)) {
                request.setEntity(new StringEntity(body, "UTF-8"));
            }
            return client.execute(request);
        }
    }

    /* ========================= DELETE ========================= */

    public static HttpResponse doDelete(String host,
                                        String path,
                                        Map<String, String> headers,
                                        Map<String, String> querys) throws Exception {
        try (CloseableHttpClient client = wrapClient(host)) {
            URIBuilder builder = new URIBuilder(buildUrl(host, path));
            addQuery(builder, querys);

            HttpDelete request = new HttpDelete(builder.build());
            setHeaders(request, headers);
            setConfig(request);
            return client.execute(request);
        }
    }

    /* ========================= 内部工具 ========================= */

    private static String buildUrl(String host, String path) {
        StringBuilder sb = new StringBuilder(host);
        if (StringUtils.isNotBlank(path) && !path.startsWith("/")) {
            sb.append('/');
        }
        if (StringUtils.isNotBlank(path)) {
            sb.append(path);
        }
        return sb.toString();
    }

    private static void addQuery(URIBuilder builder, Map<String, String> querys) {
        if (querys != null) {
            for (Map.Entry<String, String> e : querys.entrySet()) {
                builder.addParameter(e.getKey(), e.getValue());
            }
        }
    }

    private static void setHeaders(HttpRequestBase request, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        }
    }

    private static void setConfig(HttpRequestBase request) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        request.setConfig(config);
    }

    /** HTTPS 忽略证书 */
    private static CloseableHttpClient wrapClient(String host) {
        if (host != null && host.startsWith("https://")) {
            try {
                SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, (chain, authType) -> true)
                        .build();
                SSLConnectionSocketFactory sslFactory =
                        new SSLConnectionSocketFactory(sslContext, (h, s) -> true);
                return HttpClients.custom()
                        .setSSLSocketFactory(sslFactory)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("创建 HTTPS 客户端失败", e);
            }
        }
        return HttpClients.createDefault();
    }

    private HttpUtils() {}
}
