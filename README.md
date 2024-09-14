# Clojurescript Azure Function

This is an Hello, World azure function app written in Clojurescript.

## Build

In order to build the function for use in Azure run:

``` shell
$ npx shadow-cljs release azure-function
```

This will build the JS in the azure-function/ directory.

## Test as an Azure Function locally

Install the Azure Core Function tools
(https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=linux%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-javascript
)

``` shell
$ func init --worker-runtime node --language javascript
$ func start
```

In order to test the function use the following curl commands.

### This invokes the query passing the name in the query string of the url.

``` shell
$ curl http://localhost:7071/api/hello\?name\=name%20in%20query
Hello, name in query!% 
```

### This invokes the query passing the name in the body of the url.

``` shell
$ curl -X POST -d 'Name in body' http://localhost:7071/api/hello
Hello, Name in body!%
```

### This invokes the query defaulting the name parameter.

``` shell
$ curl http://localhost:7071/api/hello
Hello, World!%
```



