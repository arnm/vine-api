# vine-api

A Clojure library designed to wrap the [Unofficial Private Vine API](https://github.com/VineAPI/VineAPI/blob/master/endpoints.md). This library was built using [http-kit](https://github.com/http-kit/http-kit). All of the API functions defined in this library are light wrappers around the [http-kit client functions](http://www.http-kit.org/client.html).

## Usage

[![Clojars Project](http://clojars.org/com.alexeinunez/vine-api/latest-version.svg)](http://clojars.org/com.alexeinunez/vine-api)

Currently only GET requests which do not require authentication are supported.

All supported API functions can be found in [here](https://github.com/arnm/vine-api/blob/master/src/vine/api.clj).

### Synchronous

Use futures to make synchronous requests:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

(println @(v/timelines-popular {:query-params {:size 1}}))
```
Prints out response map from request to https://vine.co/api/timelines/popular?size=1

### Asynchronous

Use callbacks to make asynchronous requests:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

(v/venues-search-query "texas" (fn [r] (println (:status r))))
```
Prints out status code of request to https://vine.co/api/venues/search/texas



