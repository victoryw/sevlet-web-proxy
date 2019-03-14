package com.thoughtworks.picc.reverse.proxy;

import com.victoryw.picc.HelloServlet;
import com.victoryw.picc.LoginServelt;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReverseProxyFilter implements Filter {
    private FilterConfig config;

    private static final Log log = LogFactory.getLog(ReverseProxyFilter.class);
    private ProxyAddress proxyAddress;

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
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //filter non target request
        Boolean isProxy = isSetToProxy(httpServletRequest);
        if (!isProxy) {
            chain.doFilter(request, response);
        }

        //extract session

        completeRequest(httpServletRequest, httpServletResponse);
    }

    private void completeRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Request httpProxyRequest = createProxyRequest(httpServletRequest);
        ///TOOD: client should be static object shared between filter instance
        OkHttpClient client = new OkHttpClient();
        try (final Response proxyResponse = client.newCall(httpProxyRequest).execute()) {
            httpServletResponse.setStatus(proxyResponse.code());
            copyResponseHeaders(httpServletResponse, proxyResponse);

            if (proxyResponse.body() != null && proxyResponse.body().contentType() != null) {
                httpServletResponse.setContentType(proxyResponse.body().contentType().toString());
                httpServletResponse.getWriter().write(proxyResponse.body().string());
            }

        }
    }

    private void copyResponseHeaders(HttpServletResponse target, Response from) {
        final Headers proxyHeaders = from.headers();

        final List<String> noHeapHeaders = proxyHeaders.names().
                stream().
                filter(name -> !HOP_BY_HOP_HEADERS.contains(name)).
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

    /**
     * These are the "hop-by-hop" headers that should not be copied.
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html
     * I use an HttpClient HeaderGroup class instead of Set&lt;String&gt; because this
     * approach does case insensitive lookup faster.
     */
    private static final List<String> HOP_BY_HOP_HEADERS = Arrays.asList("Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization",
            "TE", "Trailers", "Transfer-Encoding", "Upgrade");

    private static final List<String> COOKIES_HEADER_NAMES = Arrays.asList(
            "Set-Cookie",
            "Set-Cookie2");

    private static final String LOCATION = "Location";

    private Boolean isSetToProxy(HttpServletRequest httpServletRequest) {
        return
                httpServletRequest.getRequestURI().endsWith(HelloServlet.class.getName()) ||
                httpServletRequest.getRequestURI().endsWith(LoginServelt.class.getName());
    }


    private Request createProxyRequest(HttpServletRequest httpServletRequest) {
//        <scheme>://<authority><path>?<query>#<fragment>
        HttpUrl httpUrl = HttpUrl.parse(httpServletRequest.getRequestURL().toString());
        assert httpUrl != null;

        final HttpUrl proxyUrl = new HttpUrl.Builder().
                scheme(this.proxyAddress.getScheme()).
                host(this.proxyAddress.getHost()).
                port(this.proxyAddress.getPort()).
                addEncodedPathSegments(String.join("/", httpUrl.encodedPathSegments())).
                query(httpUrl.query()).
                fragment(httpUrl.fragment()).
                build();
        return new Request.
                Builder().
                url(proxyUrl).
                method(httpServletRequest.getMethod(), null).
                build();
    }

    @Override
    public void destroy() {

    }
}
