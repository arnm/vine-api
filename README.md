# vine-api

A Clojure library designed to wrap the [Unofficial Private Vine API](https://github.com/VineAPI/VineAPI/blob/master/endpoints.md). This library was built using [http-kit](https://github.com/http-kit/http-kit). All of the API functions defined in this library are light wrappers around the [http-kit client functions](http://www.http-kit.org/client.html).

## Usage

[![Clojars Project](http://clojars.org/com.alexeinunez/vine-api/latest-version.svg)](http://clojars.org/com.alexeinunez/vine-api)

### Function Naming Convention

All supported API functions can be found in [here](https://github.com/arnm/vine-api/blob/master/src/vine/api.clj).

The function names are generated based on the resource path given to the ```def-vine-endpoint``` macro.

For example, when the resource path ```posts/search/<query>``` is passed to the macro it generates this function: 
``` clojure
vine.api/posts-search-query
([method query & [opts callback]] [method query & [callback]])
  Requests https://vine.co/api/posts/search/<query>.
```

which can be called like this:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

@(v/post-search-query :get "my query")
```

Learning from the [unit tests](https://github.com/arnm/vine-api/blob/master/test/vine/test/api.clj) is highly recommended, as they will demostrate how most functions can be used.

### Authentication

A good portion of the Vine API requires for requests to be authenticated for a user. This can be done by setting the ```vine-session-id``` request header which should have the value of the ```key``` field provided when a user logs on.

Logging in a user:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

@(v/users-authenticate :post
                    {:form-params
                      {:username "user-email"
                       :password "user-password"}})
```

This will return a map with a ```:body``` key which contains a string with the JSON response:
``` json
{
    "code": "",
    "data": {
        "username": "username",
        "edition": "US",
        "userId": 1234859230,
        "key": "34720342-59834-2-23-49kl",
        "avatarUrl": "http://v.cdn.vine.co/avatars/default.png"
    },
    "success": true,
    "error": ""
}
```

The ```key``` field value is what you want to set the ```vine-session-id``` header to.

Here is an example of making an authenticated request:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

;; ...
;; login was done previously

@(v/timelines-graph :get {:headers {"vine-session-id" "34720342-59834-2-23-49kl"}})
```


### Synchronous Calls

Use futures to make synchronous requests:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

(println @(v/timelines-popular :get {:query-params {:size 1}}))
```
Prints out response map from request to https://vine.co/api/timelines/popular?size=1

### Asynchronous Calls

Use callbacks to make asynchronous requests:
``` clojure
(ns sample.core
  (:require [vine.api :as v]))

(v/venues-search-query :get "texas" (fn [r] (println (:status r))))
```
Prints out status code of request to https://vine.co/api/venues/search/texas

## Development

If you want work on the library you will have to run Leiningen with the development profile specified in the ```project.clj```:

``` bash
# run the REPL
lein with-profile +d repl
# run the tests
lein with-profile +d test
```

You will also have to specify a local ```profiles.clj``` inside of the repo root so that you can specify your test data. This functionality is added by the [Environ](https://github.com/weavejester/environ) Leiningen plugin.

Here is a sample ```profiles.clj``` which has all valid data except for the user login credentials:

``` clojure
{:dev  {:env  {:username "bob"
               :email "a@b.com"
               :password "pass"
               :user-id  "955681242773340160"
               :post-id "1178768651474640896"
               :channel-id "1070175184667013120"
               :venue-id "4e72b98a62e1a77aac80bb04"}}}
```

You repo directory should look something similar to this:
```
↳ tree
.
├── LICENSE
├── README.md
├── doc
│   └── intro.md
├── pom.xml
├── pom.xml.asc
├── profiles.clj
├── project.clj
├── src
│   └── vine
│       ├── api.clj
│       └── util
│           └── http.clj
└── test
    └── vine
        └── test
            └── api.clj

7 directories, 10 files
```
