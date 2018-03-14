# Rx extension for Vert.x

[![Build Status](https://vertx.ci.cloudbees.com/buildStatus/icon?job=vert.x3-rx)](https://vertx.ci.cloudbees.com/view/vert.x-3/job/vert.x3-rx/)

Vert.x module adding support for Reactive Extensions (Rx) using the Rx libraries. This
allows Vert.x developers to use the Rx type-safe composable API to build Vert.x verticles.
This module provides helpers for adapting Vert.x stream and future constructs to Rx observables.

## Documentation

* [http://vertx.io/docs/vertx-rx/java/](RxJava Documentation)
* [http://vertx.io/docs/vertx-rx/java2/](RxJava 2 Documentation)
* [http://vertx.io/docs/vertx-rx/js/](RxJS Documentation)
* [http://vertx.io/docs/vertx-rx/groovy/](RxGroovy Documentation)

## Stack integration

RxJava and RxJava2 are integrated with the Vert.x stack.

Integration tests are placed here:

* [https://github.com/vert-x3/vertx-rx/tree/master/rx-java/src/test/java/io/vertx/it](RxJava)
* [https://github.com/vert-x3/vertx-rx/tree/master/rx-java2/src/test/java/io/vertx/it](RxJava 2)

Integration docs are placed here:

* [https://github.com/vert-x3/vertx-rx/tree/master/rx-java/src/main/asciidoc](RxJava)
* [https://github.com/vert-x3/vertx-rx/tree/master/rx-java2/src/main/asciidoc](RxJava 2)

The corresponding component uses an asciidoctor [https://asciidoctor.org/docs/user-manual/#include-directive](include) directive
to embed the doc snippet:

```
ifdef::java[]
include::override/rxjava.adoc[]
endif::[]
```

Vert.x Web Client example:

* https://github.com/vert-x3/vertx-web/blob/master/vertx-web-client/src/main/asciidoc/override/rxjava.adoc

is replaced by

* https://github.com/vert-x3/vertx-rx/blob/master/rx-java/src/main/asciidoc/vertx-web-client/java/override/rxjava.adoc
