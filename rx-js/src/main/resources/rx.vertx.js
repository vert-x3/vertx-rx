;(function (factory) {
    var objectTypes = {
        'boolean': false,
        'function': true,
        'object': true,
        'number': false,
        'string': false,
        'undefined': false
    };

    var root = (objectTypes[typeof window] && window) || this,
        freeExports = objectTypes[typeof exports] && exports && !exports.nodeType && exports,
        freeModule = objectTypes[typeof module] && module && !module.nodeType && module,
        moduleExports = freeModule && freeModule.exports === freeExports && freeExports,
        freeGlobal = objectTypes[typeof global] && global;

    if (freeGlobal && (freeGlobal.global === freeGlobal || freeGlobal.window === freeGlobal)) {
        root = freeGlobal;
    }

    // Because of build optimizers
    if (typeof define === 'function' && define.amd) {
        define(['rx.binding', 'exports'], function (Rx, exports) {
            root.Rx = factory(root, exports, Rx);
            return root.Rx;
        });
    } else if (typeof module === 'object' && module && module.exports === freeExports) {
        module.exports = factory(root, module.exports, require('./rx'));
    } else {
        root.Rx = factory(root, {}, root.Rx);
    }
}.call(this, function (root, exp, Rx, undefined) {

  // Aliases
  var Observable = Rx.Observable,
    Observer = Rx.Observer,
    observerProto = Observer.prototype;

  Observable.fromReadStream = function () {
    if (arguments.length === 1 && typeof arguments[0] === 'object') {
      var readStream = arguments[0];
      var subscribed = false;
      return Rx.Observable.create(function(observer) {
        if (subscribed) {
          throw new Error('ReadStream observable support only a single subscription');
        }
        subscribed = true;
        readStream.exceptionHandler(function(err) { observer.onError(err); });
        readStream.endHandler(function() { observer.onCompleted(); });
        readStream.handler(function(event) { observer.onNext(event); });
        return function() {
          readStream._jdel.endHandler(null);
          readStream._jdel.exceptionHandler(null);
          readStream._jdel.handler(null); // This may call the endHandler
        }
      })
    } else {
      throw new TypeError('function invoked with invalid arguments');
    }
  };

  function toFuture(observer) {
    return function(result, cause) {
      if (result != null) {
        observer.onNext(result);
        observer.onCompleted();
      } else {
        observer.onError(cause);
      }
    };
  }

  observerProto.toFuture = function() {
    return toFuture(this);
  };

  Rx.observableFuture = function() {
    var subject = new Rx.Subject();
    subject.toHandler = function() {
      return toFuture(subject);
    };
    return subject;
  };

  function toHandler(observer) {
    return function(result) {
      observer.onNext(result);
    };
  }

  observerProto.toHandler = function() {
    return toHandler(this);
  };

  Rx.observableHandler = function() {
    var subject = new Rx.Subject();
    subject.toHandler = function() {
      return toHandler(subject);
    };
    return subject;
  };

  return Rx;
}));
