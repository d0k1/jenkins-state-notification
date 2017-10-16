/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.focusit.notification.notification;


import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import javax.xml.bind.DatatypeConverter;

import jenkins.model.Jenkins;


public enum Protocol {

    HTTP {
        @Override
        public void send(String url, byte[] data, int timeout, boolean isJson) throws IOException {
            URL targetUrl = new URL(url);
            if (!targetUrl.getProtocol().startsWith("http")) {
              throw new IllegalArgumentException("Not an http(s) url: " + url);
            }

            // Verifying if the HTTP_PROXY is available
            final String httpProxyUrl = System.getenv().get("http_proxy");
            URL proxyUrl = null;
            if (httpProxyUrl != null && httpProxyUrl.length() > 0) {
              proxyUrl = new URL(httpProxyUrl);
              if (!proxyUrl.getProtocol().startsWith("http")) {
                throw new IllegalArgumentException("Not an http(s) url: " + httpProxyUrl);
              }
            }

            Proxy proxy = Proxy.NO_PROXY;
            if (proxyUrl != null) {
              // Proxy connection to the address provided
              final int proxyPort = proxyUrl.getPort() > 0 ? proxyUrl.getPort() : 80;
              proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(), proxyPort));
            } else if (Jenkins.getInstance() != null && Jenkins.getInstance().proxy != null) {
              proxy = Jenkins.getInstance().proxy.createProxy(targetUrl.getHost());
            }

            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection(proxy);
            connection.setRequestProperty("Content-Type", String.format( "application/%s;charset=UTF-8", isJson ? "json" : "xml" ));
            String userInfo = targetUrl.getUserInfo();
            if (null != userInfo) {
              String b64UserInfo = DatatypeConverter.printBase64Binary(userInfo.getBytes());
              String authorizationHeader = "Basic " + b64UserInfo;
              connection.setRequestProperty("Authorization", authorizationHeader);
            }
            connection.setFixedLengthStreamingMode(data.length);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.connect();
            try {
              OutputStream output = connection.getOutputStream();
              try {
                output.write(data);
                output.flush();
              } finally {
                output.close();
              }
            } finally {
              // Follow an HTTP Temporary Redirect if we get one,
              //
              // NB: Normally using the HttpURLConnection interface, we'd call
              // connection.setInstanceFollowRedirects(true) to enable 307 redirect following but
              // since we have the connection in streaming mode this does not work and we instead
              // re-direct manually.
              if (307 == connection.getResponseCode()) {
                String location = connection.getHeaderField("Location");
                connection.disconnect();
                send(location, data,timeout, isJson);
              } else {
                connection.disconnect();
              }
            }
        }

        @Override
        public void validateUrl( String url ) {
            try {
                // noinspection ResultOfObjectAllocationIgnored
                new URL( url );
            } catch ( MalformedURLException e ) {
                throw new RuntimeException( String.format( "%sUse http://hostname:port/path for endpoint URL",
                                                           isEmpty ( url ) ? "" : "Invalid URL '" + url + "'. " ));
            }
        }
    };


    public abstract void send(String url, byte[] data, int timeout, boolean isJson) throws IOException;

    public void validateUrl(String url) {
        try {
            HostnamePort hnp = HostnamePort.parseUrl(url);
            if (hnp == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new RuntimeException( String.format( "%sUse hostname:port for endpoint URL",
                                                       isEmpty ( url ) ? "" : "Invalid URL '" + url + "'. " ));
        }
    }

    private static boolean isEmpty( String s ) {
        return (( s == null ) || ( s.trim().length() < 1 ));
    }
}
