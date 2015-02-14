# vine-api

A Clojure library designed to wrap the [Unofficial Private Vine API](https://github.com/VineAPI/VineAPI/blob/master/endpoints.md). This library was built using [http-kit](https://github.com/http-kit/http-kit). All of the API functions defined in this library are light wrappers around the [http-kit client functions](http://www.http-kit.org/client.html).

## Usage

[![Clojars Project](http://clojars.org/vine-api/latest-version.svg)](http://clojars.org/vine-api)

Currently only GET requests which do not require authentication are supported.

``` clojure
(ns sample.core
    (:require [vine.api :as v]))

(println @(v/timelines-popular {:query-params {:size 1}}))
```

Returns a string containing the JSON response to https://vine.co/api/timelines/popular?size=1

