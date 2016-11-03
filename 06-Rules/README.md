# Rules

This example shows how to add work with `Auth0` rules, which are very usefull to extend functionality.

You can read a quickstart for this sample [here](https://auth0.com/docs/quickstart/webapp/java-spring-security-mvc/06-rules). 

## Prerequisites

In order to run this example you will need to have Java 7+ and Maven installed.

Check that your maven version is 3.0.x or above:

```sh
mvn -v
```

## Create an Auth0 Rule

Since this sample applies Role based authorization on the Home Page (defaults to requiring `ROLE_ADMIN`), go to `Rules`
and create the following new Rule:

```
function (user, context, callback) {
  user.app_metadata = user.app_metadata || {};
  // You can add a Role based on what you want
  // Here, we simply check domain
  var addRolesToUser = function(user, cb) {
    if (user.email.indexOf('@gmail.com') > -1) {
      cb(null, ['ROLE_ADMIN']);
   } else if (user.email.indexOf('@auth0.com') > -1) {
      cb(null, ['ROLE_ADMIN']);
    } else {
      cb(null, ['ROLE_USER']);
    }
  };

  addRolesToUser(user, function(err, roles) {
    if (err) {
      callback(err);
    } else {
      user.app_metadata.roles = roles;
      auth0.users.updateAppMetadata(user.user_id, user.app_metadata)
        .then(function(){
          callback(null, user, context);
        })
        .catch(function(err){
          callback(err);
        });
    }
  });
}
```

In our simple Rule above, we add `ROLE_ADMIN` to any user profiles whose email addresses are `gmail.com` and `auth0.com` domains.
Otherwise, we only provide `ROLE_USER` role. Our Spring Security Sample app will read this information from the UserProfile and apply
the granted authorities when checking authorization access to secured endpoints configured with Role based permissions

Here is our sample `AppConfig` entry where we specify the endpoints security settings - defined in AppConfig.java


```
  // Apply the Authentication and Authorization Strategies your application endpoints require
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**", "/login").permitAll()
                .antMatchers("/portal/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                .antMatchers("/portal/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(getSecuredRoute()).authenticated();
```

Here, we only allow users with `ROLE_USER` or `ROLE_ADMIN` to access the home page.


## Configure your Spring Security app

Your Spring Security app needs some information in order to authenticate against your Auth0 account. We have created a file for you but you need to update some of the entries with the valid values for your Client. The file is `/src/main/resources/auth0.properties.example` and it contains the following:

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
auth0.defaultAuth0WebSecurityEnabled: false
auth0.connection: {CONNECTION}
auth0.customLogin: true
auth0.signingAlgorithm: HS256
#auth0.signingAlgorithm: RS256
#auth0.publicKeyPath: /WEB-INF/certificate/cert.pem
```

Rename the file to `auth0.properties` and change the following values:
- `auth0.domain`:	Your auth0 domain. You can find the correct value on the Settings tab of your client on the dashboard.
- `auth0.issuer`:	The issuer of the JWT Token. This is typically your auth0 domain with a `https://` prefix and a `/` suffix. For example, if your `auth0.domain` is `example.auth0.com` then the `auth0.issuer` should be set to `https://example.auth0.com/` (the trailing slash is important!).
- `auth0.clientId`:	The unique identifier for your client. You can find the correct value on the Settings tab of your client on the dashboard. 
- `auth0.clientSecret`:	The secret used to sign and validate the tokens that will be used in the different authentication flows. You can find the correct value on the Settings tab of your client on the dashboard.
- `auth0.connection`: The name of the database connection you created, for example `custom-login-DB`.
- `auth0.customLogin`: Set to `true` to enable custom login instead of Lock.

If you download the seed from our [Quickstart](https://auth0.com/docs/quickstart/webapp/java-spring-security-mvc/06-rules) then the `domain`, `clientId` and `clientSecret` attributes will be populated for you, unless you are not logged in or you do not have at least one registered client. In any case you should verify that the values are correct if you have multiple clients in your account and you might want to use another than the one we set the information for.

## Build and Run

In order to build and run the project execute:

```sh
mvn spring-boot:run
```

Then, go to [http://localhost:3099/login](http://localhost:3099/login). 

Shut it down manually with Ctrl-C.
