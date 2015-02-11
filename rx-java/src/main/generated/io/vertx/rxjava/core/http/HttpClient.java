/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.core.http;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.MultiMap;
import io.vertx.core.http.WebsocketVersion;
import io.vertx.rxjava.core.metrics.Measured;
import io.vertx.core.http.HttpMethod;
import java.util.Map;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Handler;

/**
 * An asynchronous HTTP client.
 * <p>
 * It allows you to make requests to HTTP servers, and a single client can make requests to any server.
 * <p>
 * It also allows you to open WebSockets to servers.
 * <p>
 * The client can also pool HTTP connections.
 * <p>
 * For pooling to occur, keep-alive must be true on the {@link io.vertx.core.http.HttpClientOptions} (default is true).
 * In this case connections will be pooled and re-used if there are pending HTTP requests waiting to get a connection,
 * otherwise they will be closed.
 * <p>
 * This gives the benefits of keep alive when the client is loaded but means we don't keep connections hanging around
 * unnecessarily when there would be no benefits anyway.
 * <p>
 * The client also supports pipe-lining of requests. Pipe-lining means another request is sent on the same connection
 * before the response from the preceeding one has returned. Pipe-lining is not appropriate for all requests.
 * <p>
 * To enable pipe-lining, it must be enabled on the {@link io.vertx.core.http.HttpClientOptions} (default is false).
 * <p>
 * When pipe-lining is enabled the connection will be automatically closed when all in-flight responses have returned
 * and there are no outstanding pending requests to write.
 * <p>
 * The client is designed to be reused between requests.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class HttpClient implements Measured {

  final io.vertx.core.http.HttpClient delegate;

  public HttpClient(io.vertx.core.http.HttpClient delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The metric base name
   *
   * @return the metric base name
   */
  public String metricBaseName() {
    String ret = this.delegate.metricBaseName();
    return ret;
  }

  /**
   * Will return the metrics that correspond with this measured object.
   *
   * @return the map of metrics where the key is the name of the metric (excluding the base name) and the value is
   * the json data representing that metric
   */
  public Map<String,JsonObject> metrics() {
    Map<String,JsonObject> ret = this.delegate.metrics();
;
    return ret;
  }

  /**
   * Create an HTTP request to send to the server at the specified host and port.
   * @param method  the HTTP method
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest request(HttpMethod method, int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.request(method, port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server at the specified host and default port.
   * @param method  the HTTP method
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest request(HttpMethod method, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.request(method, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param method  the HTTP method
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest request(HttpMethod method, int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.request(method, port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param method  the HTTP method
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest request(HttpMethod method, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.request(method, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server at the default host and port.
   * @param method  the HTTP method
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest request(HttpMethod method, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.request(method, requestURI));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param method  the HTTP method
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest request(HttpMethod method, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.request(method, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server using an absolute URI
   * @param method  the HTTP method
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest requestAbs(HttpMethod method, String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.requestAbs(method, absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param method  the HTTP method
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest requestAbs(HttpMethod method, String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.requestAbs(method, absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server at the specified host and port.
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest get(int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.get(port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server at the specified host and default port.
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest get(String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.get(host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest get(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.get(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest get(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.get(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server at the default host and port.
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest get(String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.get(requestURI));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest get(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.get(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server using an absolute URI
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest getAbs(String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.getAbs(absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP GET request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest getAbs(String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.getAbs(absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP GET request to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient getNow(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.getNow(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP GET request to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient getNow(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.getNow(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP GET request  to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient getNow(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.getNow(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server at the specified host and port.
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest post(int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.post(port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server at the specified host and default port.
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest post(String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.post(host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest post(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.post(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest post(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.post(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server at the default host and port.
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest post(String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.post(requestURI));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest post(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.post(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server using an absolute URI
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest postAbs(String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.postAbs(absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP POST request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest postAbs(String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.postAbs(absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server at the specified host and port.
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest head(int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.head(port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server at the specified host and default port.
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest head(String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.head(host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest head(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.head(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest head(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.head(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server at the default host and port.
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest head(String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.head(requestURI));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest head(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.head(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server using an absolute URI
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest headAbs(String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.headAbs(absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP HEAD request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest headAbs(String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.headAbs(absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP HEAD request to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient headNow(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.headNow(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP HEAD request to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient headNow(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.headNow(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP HEAD request  to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient headNow(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.headNow(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server at the specified host and port.
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest options(int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.options(port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server at the specified host and default port.
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest options(String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.options(host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest options(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.options(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest options(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.options(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server at the default host and port.
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest options(String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.options(requestURI));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest options(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.options(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server using an absolute URI
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest optionsAbs(String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.optionsAbs(absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP OPTIONS request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest optionsAbs(String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.optionsAbs(absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP OPTIONS request to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient optionsNow(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.optionsNow(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP OPTIONS request to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient optionsNow(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.optionsNow(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Sends an HTTP OPTIONS request  to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient optionsNow(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClient ret= HttpClient.newInstance(this.delegate.optionsNow(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server at the specified host and port.
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest put(int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.put(port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server at the specified host and default port.
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest put(String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.put(host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest put(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.put(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest put(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.put(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server at the default host and port.
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest put(String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.put(requestURI));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest put(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.put(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server using an absolute URI
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest putAbs(String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.putAbs(absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP PUT request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest putAbs(String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.putAbs(absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server at the specified host and port.
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest delete(int port, String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.delete(port, host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server at the specified host and default port.
   * @param host  the host
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest delete(String host, String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.delete(host, requestURI));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server at the specified host and port, specifying a response handler to receive
   * the response
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest delete(int port, String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.delete(port, host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server at the specified host and default port, specifying a response handler to receive
   * the response
   * @param host  the host
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest delete(String host, String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.delete(host, requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server at the default host and port.
   * @param requestURI  the relative URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest delete(String requestURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.delete(requestURI));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server at the default host and port, specifying a response handler to receive
   * the response
   * @param requestURI  the relative URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest delete(String requestURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.delete(requestURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server using an absolute URI
   * @param absoluteURI  the absolute URI
   * @return  an HTTP client request object
   */
  public HttpClientRequest deleteAbs(String absoluteURI) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.deleteAbs(absoluteURI));
    return ret;
  }

  /**
   * Create an HTTP DELETE request to send to the server using an absolute URI, specifying a response handler to receive
   * the response
   * @param absoluteURI  the absolute URI
   * @param responseHandler  the response handler
   * @return  an HTTP client request object
   */
  public HttpClientRequest deleteAbs(String absoluteURI, Handler<HttpClientResponse> responseHandler) {
    HttpClientRequest ret= HttpClientRequest.newInstance(this.delegate.deleteAbs(absoluteURI, new Handler<io.vertx.core.http.HttpClientResponse>() {
      public void handle(io.vertx.core.http.HttpClientResponse event) {
        responseHandler.handle(new HttpClientResponse(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified port, host and relative request URI
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(int port, String host, String requestURI, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(port, host, requestURI, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the host and relative request URI and default port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String host, String requestURI, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(host, requestURI, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified port, host and relative request URI, and with the specified headers
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(int port, String host, String requestURI, MultiMap headers, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(port, host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified host,relative request UR, and default port and with the specified headers
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String host, String requestURI, MultiMap headers, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified port, host and relative request URI, with the specified headers and using
   * the specified version of WebSockets
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(int port, String host, String requestURI, MultiMap headers, WebsocketVersion version, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(port, host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified host, relative request URI and default port with the specified headers and using
   * the specified version of WebSockets
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String host, String requestURI, MultiMap headers, WebsocketVersion version, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified port, host and relative request URI, with the specified headers, using
   * the specified version of WebSockets, and the specified websocket sub protocols
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param subProtocols  the subprotocols to use
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(int port, String host, String requestURI, MultiMap headers, WebsocketVersion version, String subProtocols, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(port, host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, subProtocols, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket to the specified host, relative request URI and default port, with the specified headers, using
   * the specified version of WebSockets, and the specified websocket sub protocols
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param subProtocols  the subprotocols to use
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String host, String requestURI, MultiMap headers, WebsocketVersion version, String subProtocols, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, subProtocols, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket at the relative request URI using the default host and port
   * @param requestURI  the relative URI
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String requestURI, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(requestURI, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket at the relative request URI using the default host and port and the specified headers
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String requestURI, MultiMap headers, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket at the relative request URI using the default host and port, the specified headers and the
   * specified version of WebSockets
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String requestURI, MultiMap headers, WebsocketVersion version, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Connect a WebSocket at the relative request URI using the default host and port, the specified headers, the
   * specified version of WebSockets and the specified sub protocols
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param subProtocols  the subprotocols
   * @param wsConnect  handler that will be called with the websocket when connected
   * @return a reference to this, so the API can be used fluently
   */
  public HttpClient websocket(String requestURI, MultiMap headers, WebsocketVersion version, String subProtocols, Handler<WebSocket> wsConnect) {
    HttpClient ret= HttpClient.newInstance(this.delegate.websocket(requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, subProtocols, new Handler<io.vertx.core.http.WebSocket>() {
      public void handle(io.vertx.core.http.WebSocket event) {
        wsConnect.handle(new WebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified port, host and relative request URI
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(int port, String host, String requestURI) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(port, host, requestURI));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified host, relative request URI and default port
   * @param host  the host
   * @param requestURI  the relative URI
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String host, String requestURI) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(host, requestURI));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified port, host and relative request URI, and with the specified headers
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(int port, String host, String requestURI, MultiMap headers) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(port, host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate()));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified host, relative request URI and default port and with the specified headers
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String host, String requestURI, MultiMap headers) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate()));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified port, host and relative request URI, with the specified headers and using
   * the specified version of WebSockets
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(int port, String host, String requestURI, MultiMap headers, WebsocketVersion version) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(port, host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified host, relative request URI and default port and with the specified headers and using
   * the specified version of WebSockets
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String host, String requestURI, MultiMap headers, WebsocketVersion version) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified port, host and relative request URI, with the specified headers, using
   * the specified version of WebSockets, and the specified websocket sub protocols
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param subProtocols  the subprotocols to use
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(int port, String host, String requestURI, MultiMap headers, WebsocketVersion version, String subProtocols) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(port, host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, subProtocols));
    return ret;
  }

  /**
   * Create a WebSocket stream to the specified host, relative request URI and default port, with the specified headers, using
   * the specified version of WebSockets, and the specified websocket sub protocols
   * @param host  the host
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param subProtocols  the subprotocols to use
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String host, String requestURI, MultiMap headers, WebsocketVersion version, String subProtocols) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(host, requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, subProtocols));
    return ret;
  }

  /**
   * Create a WebSocket stream at the relative request URI using the default host and port and the specified headers
   * @param requestURI  the relative URI
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String requestURI) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(requestURI));
    return ret;
  }

  /**
   * Create a WebSocket stream at the relative request URI using the default host and port and the specified headers
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String requestURI, MultiMap headers) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(requestURI, (io.vertx.core.MultiMap) headers.getDelegate()));
    return ret;
  }

  /**
   * Create a WebSocket stream at the relative request URI using the default host and port, the specified headers and the
   * specified version of WebSockets
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String requestURI, MultiMap headers, WebsocketVersion version) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version));
    return ret;
  }

  /**
   * Create a WebSocket stream at the relative request URI using the default host and port, the specified headers, the
   * specified version of WebSockets and the specified sub protocols
   * @param requestURI  the relative URI
   * @param headers  the headers
   * @param version  the websocket version
   * @param subProtocols  the subprotocols
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketStream websocketStream(String requestURI, MultiMap headers, WebsocketVersion version, String subProtocols) {
    WebSocketStream ret= WebSocketStream.newInstance(this.delegate.websocketStream(requestURI, (io.vertx.core.MultiMap) headers.getDelegate(), version, subProtocols));
    return ret;
  }

  /**
   * Close the client. Closing will close down any pooled connections.
   * Clients should always be closed after use.
   */
  public void close() {
    this.delegate.close();
  }


  public static HttpClient newInstance(io.vertx.core.http.HttpClient arg) {
    return new HttpClient(arg);
  }
}
