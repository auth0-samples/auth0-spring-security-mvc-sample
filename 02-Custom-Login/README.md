# Custom Login

The `01-Login` sample explains how to login but with the [Lock widget](https://auth0.com/docs/libraries/lock). Lock is completely optional so you can build an application with Auth0 using your custom design without having to include it. You just need to use the [Auth0.js library](https://github.com/auth0/auth0.js).

In our example we will configure a custom database connection to use with our custom login. We will also keep on building on our previous example, the one using Lock. We will add a flag that when set the custom login will be used, while when unset Lock will be used. This is completely optional of course, you can configure your own web app to use only custom login if this is what you want.

You can read a quickstart for this sample [here](https://auth0.com/docs/quickstart/webapp/java-spring-security-mvc/01-login). 

## Prerequisites

In order to run this example you will need to have Java 7+ and Maven installed.

Check that your maven version is 3.0.x or above:

```sh
mvn -v
```

## Configure callback URLs

Callback URLs are URLs that Auth0 invokes after the authentication process. Auth0 routes your application back to this URL and attaches some details to it including a token. Callback URLs can be manipulated on the fly and that could be harmful. For security reasons, you will need to add your application's URL in the client's `Allowed Callback URLs`. This will enable Auth0 to recognize the URLs as valid. If omitted, authentication will not be successful for the app instance.

The values you must configure are:
- Allowed Callback URL: `http://localhost:3099/callback`
- Allowed Logout URLs: `http://localhost:3099/logout`

## Create a database connection

First we will create a new database connection and we will name it `custom-login-DB`. We will use Auth0 database infrastructure to store our users.

**NOTE:** If you have an existing user store, or wish to store user credentials on your own server, see the custom database connection tutorial at [Authenticate Users with Username and Password using a Custom Database](https://auth0.com/docs/connections/database/mysql) for detailed steps on how to setup and configure it.

Log into Auth0, and select the **Connections > Database** menu option. 

Click the **Create DB Connection** button and provide a name for the database. You will be navigated to the connection's settings. 

At the **Clients Using This Connection** section, enable the connection for your app.

Now let's create a user. 

Select the Users menu option, click the **Create User** button and fill in the email, password, and the database at which the user will be created. Use an email address you have access to since creating the user will trigger a verification email to be sent. Click **Save**.

Head back to **Connections > Database** and select the **Try** button on your new database so we can verify that our user can log in.

**NOTE:** You can add also social connections. To do so you need to create the relevant button in your login form and the javascript to specify which connection to use, for example `google-oauth2`, `github`, etc. You can find details and some sample code on the [auth0.js](https://auth0.com/docs/libraries/auth0js#login) document.

## Configure your Spring Security app

Your Spring Security app needs some information in order to authenticate against your Auth0 account. We have created a file for you but you need to update some of the entries with the valid values for your Client. The file is `/src/main/resources/auth0.properties` and it contains the following:

```
auth0.domain: {DOMAIN}
auth0.issuer: {ISSUER}
auth0.clientId: {CLIENT_ID}
auth0.clientSecret: {CLIENT_SECRET}
auth0.onLogoutRedirectTo: /login
auth0.securedRoute: /portal/*
auth0.loginCallback: /callback
auth0.loginRedirectOnSuccess: /portal/home
auth0.loginRedirectOnFail: /login
auth0.base64EncodedSecret: true
auth0.authorityStrategy: ROLES
auth0.servletFilterEnabled: false
auth0.defaultAuth0WebSecurityEnabled: false
auth0.connection: {CONNECTION}
auth0.customLogin: true
auth0.signingAlgorithm: HS256
#auth0.signingAlgorithm: RS256
#auth0.publicKeyPath: /WEB-INF/certificate/cert.pem
```

You need to change the following values:
- `auth0.domain`:	Your auth0 domain. You can find the correct value on the Settings tab of your client on the dashboard.
- `auth0.issuer`:	The issuer of the JWT Token. This is typically your auth0 domain with a `https://` prefix and a `/` suffix. For example, if your `auth0.domain` is `example.auth0.com` then the `auth0.issuer` should be set to `https://example.auth0.com/` (the trailing slash is important!).
- `auth0.clientId`:	The unique identifier for your client. You can find the correct value on the Settings tab of your client on the dashboard. 
- `auth0.clientSecret`:	The secret used to sign and validate the tokens that will be used in the different authentication flows. You can find the correct value on the Settings tab of your client on the dashboard.
- `auth0.connection`: The name of the database connection you created, for example `custom-login-DB`.
- `auth0.customLogin`: Set to `true` to enable custom login instead of Lock.

If you download the seed from our [Quickstart](https://auth0.com/docs/quickstart/webapp/java-spring-security-mvc/01-login) then the `domain`, `clientId` and `clientSecret` attributes will be populated for you, unless you are not logged in or you do not have at least one registered client. In any case you should verify that the values are correct if you have multiple clients in your account and you might want to use another than the one we set the information for. Do not forget to manually set the `issuer` attribute!

## Build and Run

In order to build and run the project execute:

```sh
mvn spring-boot:run
```

Then, go to [http://localhost:3099/login](http://localhost:3099/login). 

Shut it down manually with Ctrl-C.
