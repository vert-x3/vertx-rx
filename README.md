# Rx extension for Vert.x

[![Build Status](https://travis-ci.org/vert-x3/vertx-rx.svg?branch=master)](https://travis-ci.org/vert-x3/vertx-rx)

Vert.x module adding support for Reactive Extensions (Rx) using the Rx libraries. This
allows Vert.x developers to use the Rx type-safe composable API to build Vert.x verticles.
This module provides helpers for adapting Vert.x stream and future constructs to Rx observables.

## Documentation

* [RxJava Documentation](http://vertx.io/docs/vertx-rx/java/)
* [RxJava 2 Documentation](http://vertx.io/docs/vertx-rx/java2/)
* [RxJS Documentation](http://vertx.io/docs/vertx-rx/js/)
* [RxGroovy Documentation](http://vertx.io/docs/vertx-rx/groovy/)

## Stack integration

RxJava and RxJava2 are integrated with the Vert.x stack.

### Integration tests

Integration tests are placed here:

* [RxJava](https://github.com/vert-x3/vertx-rx/tree/master/rx-java/src/test/java/io/vertx/it)
* [RxJava 2](https://github.com/vert-x3/vertx-rx/tree/master/rx-java2/src/test/java/io/vertx/it)

### Integration docs

Integration docs are placed here:

* [RxJava 2](https://github.com/vert-x3/vertx-rx/tree/master/rx-java2/src/main/asciidoc)

The corresponding component uses an asciidoctor [https://asciidoctor.org/docs/user-manual/#include-directive](include) directive
to embed the doc snippet:

```
ifeval::["$lang" == "java"]
include::override/rxjava2.adoc[]
endif::[]
```

Vert.x Web Client example:

* https://github.com/vert-x3/vertx-web/blob/master/vertx-web-client/src/main/asciidoc/override/rxjava2.adoc

is replaced by

* https://github.com/vert-x3/vertx-rx/blob/master/rx-java2/src/main/asciidoc/vertx-web-client/java/override/rxjava2.adoc

The final aggregation happens in the [stack generation](https://github.com/vert-x3/vertx-stack/tree/master/stack-docs) module.
