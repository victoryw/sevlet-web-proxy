package com.thoughtworks.picc.reverse.proxy;

import com.victoryw.picc.HelloServlet;
import javafx.util.Pair;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReverseProxyFilter implements Filter {
    private FilterConfig config;

    private static final Log log = LogFactory.getLog(ReverseProxyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        proxyAddress = new ProxyAddress("http://localhost:9100");
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        //filter non http request
        final boolean isInstanceOfHttpServletResponse = response instanceof HttpServletResponse;
        final boolean isInstanceOfHttpServletRequest = request instanceof HttpServletRequest;
        if (!isInstanceOfHttpServletRequest || !isInstanceOfHttpServletResponse) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //filter non target request
        Boolean isProxy = isSetToProxy(httpServletRequest);
        if (!isProxy) {
            chain.doFilter(request, response);
            return;
        }

        final List<Pair<String, String>> extensionHeaders = new ArrayList<>();
        //extract session
        final HttpSession session = httpServletRequest.getSession(false);
        if(session != null) {
            final String sessionValue = HttpSessionHeadConverter.convertSessionAttributeToString(session);
            extensionHeaders.add(new Pair<>(X_SESSION_HEADER_NAME, sessionValue));
        }

        completeRequest(httpServletRequest, httpServletResponse, extensionHeaders);
    }

    @Override
    public void destroy() {

    }

    //--------------- proxy----------------//
    private ProxyAddress proxyAddress;
    private static final String CONTENT_LENGTH = "Content-Length";
    /**
     * These are the "hop-by-hop" headers that should not be copied.
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html
     * I use an HttpClient HeaderGroup class instead of Set&lt;String&gt; because this
     * approach does case insensitive lookup faster.
     */
    private static final List<String> HOP_BY_HOP_HEADERS = Arrays.asList("CONNECTION", "KEEP-ALIVE", "PROXY-AUTHENTICATE", "PROXY-AUTHORIZATION",
            "TE", "TRAILERS", "TRANSFER-ENCODING", "UPGRADE");

    private static final List<String> COOKIES_HEADER_NAMES = Arrays.asList(
            "SET-COOKIE",
            "SET-COOKIE2");

    private static final List<String> IGNORE_REQUEST_HEADERS = Arrays.asList("HOST");

    private static final String LOCATION = "Location";

    private static final String X_SESSION_HEADER_NAME = "X-Session-Attribute";

    private void completeRequest(HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse,
                                 List<Pair<String, String>> extensionHeaders) throws IOException {
        Request httpProxyRequest = createProxyRequest(httpServletRequest, extensionHeaders);
        ///TOOD: client should be static object shared between filter instance
        OkHttpClient client = new OkHttpClient();
        try (final Response proxyResponse = client.newCall(httpProxyRequest).execute()) {
            httpServletResponse.setStatus(proxyResponse.code());
            copyResponseHeaders(httpServletResponse, proxyResponse);

            if (proxyResponse.body() != null && proxyResponse.body().contentType() != null) {
                httpServletResponse.setContentType(Objects.requireNonNull(proxyResponse.body().contentType()).toString());
                httpServletResponse.getWriter().write(proxyResponse.body().string());
            }

        }
    }

    private void copyResponseHeaders(HttpServletResponse target, Response from) {
        final Headers proxyHeaders = from.headers();

        final List<String> noHeapHeaders = proxyHeaders.names().
                stream().
                filter(name -> !HOP_BY_HOP_HEADERS.contains(name.toUpperCase())).
                filter(name -> !COOKIES_HEADER_NAMES.contains(name.toUpperCase())).
                collect(Collectors.toList());

        noHeapHeaders.
                forEach(name -> {
                    final String value = String.join(",", proxyHeaders.values(name));
                    if (name.equalsIgnoreCase(LOCATION)) {
                        ///TODO: extract the location from the proxy to here
                    } else {
                        target.addHeader(name, value);
                    }
                });
    }



    private Boolean isSetToProxy(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRequestURI().endsWith(
                HelloServlet.class.getName()) ;
    }


    private Request createProxyRequest(final HttpServletRequest httpServletRequest,
                                       List<Pair<String, String>> extensionHeaders) throws IOException {
        final HttpUrl proxyUrl = createProxyUrl(httpServletRequest);
        final Request.Builder requestBuilder = new Request.
                Builder().
                url(proxyUrl);

        //headers
        final Enumeration<String> headerNames = httpServletRequest.getHeaderNames();


        final List<Pair<String, String>> headers = Collections.list(headerNames).
                stream().
                filter(name -> !IGNORE_REQUEST_HEADERS.contains(name.toUpperCase())).
                map(name -> new Pair<>(name, httpServletRequest.getHeader(name))).
                collect(Collectors.toList());

        //setXForwardedForHeader
        headers.add(new Pair<>("X-Forwarded-For", httpServletRequest.getRemoteAddr()));
        headers.add(new Pair<>("X-Forwarded-Proto", httpServletRequest.getScheme()));
        headers.add(new Pair<>("X-Proxy-With-User", "true"));

        //setExtensionHeader
        headers.addAll(extensionHeaders);

        headers.forEach(pair -> requestBuilder.header(pair.getKey(), pair.getValue()));

        RequestBody body = null;
        if(httpServletRequest.getHeader(CONTENT_LENGTH) != null) {
            final MediaType contentType = MediaType.get(httpServletRequest.getContentType());
            final ServletInputStream bodyStream = httpServletRequest.getInputStream();
            final byte[]  content = new byte[bodyStream.available()];
            bodyStream.read(content);
            body = RequestBody.create(contentType, content);
        }

        return requestBuilder.method(httpServletRequest.getMethod(), body).build();
    }

    private HttpUrl createProxyUrl(HttpServletRequest httpServletRequest) {
        HttpUrl httpUrl = HttpUrl.parse(httpServletRequest.getRequestURL().toString());
        assert httpUrl != null;
        return new HttpUrl.Builder().
                scheme(this.proxyAddress.getScheme()).
                host(this.proxyAddress.getHost()).
                port(this.proxyAddress.getPort()).
                addEncodedPathSegments(String.join("/", httpUrl.encodedPathSegments())).
                query(httpServletRequest.getQueryString()).
                build();
    }


}

