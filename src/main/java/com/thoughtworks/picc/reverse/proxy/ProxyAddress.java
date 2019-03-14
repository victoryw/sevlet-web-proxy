package com.thoughtworks.picc.reverse.proxy;

import okhttp3.HttpUrl;

class ProxyAddress {

    private HttpUrl targetUrl;

    ProxyAddress(String url) {
        targetUrl = HttpUrl.parse(url);
    }

    String getScheme() {
        return targetUrl.scheme();
    }

    String getHost() {
        return targetUrl.host();
    }

    int getPort() {
        return targetUrl.port();
    }
}
