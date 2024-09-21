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

## Test

In order to run the tests.

``` shell
$ npx shadow-cljs compile test
```

## Deploy

Ensure that you have built the release build for the azure-function target ([build](#build)).

Login to your azure account.

``` shell
$ az login
```

If you don't have one already, create a resource group for the function app (replace the name and the region as required).

``` shell
$ az group create --name AzureFunctionsQuickstart-rg --location northeurope
```

Then create a storage account for the function app.

``` shell
$ az storage account create --name <storage acc name> --location northeurope --resource-group AzureFunctionsQuickstart-rg --sku Standard_LRS --allow-blob-public-access false
```

Storage account name must be between 3 and 24 characters in length and use numbers and lower-case letters only. Note:
this can't include hyphens (dashes) or underscores.

Then you should be able to create your Azure function app.

``` shell
$ az functionapp create --resource-group AzureFunctionsQuickstart-rg --consumption-plan-location northeurope --runtime
node --runtime-version 18 --functions-version 4 --name <functionapp name> --storage-account <storage acc name>
```

After that you need to run the following from the root directory of this project.

``` shell
$ func azure functionapp publish <functionapp name>
```


