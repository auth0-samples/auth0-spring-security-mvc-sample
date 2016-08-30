# Rules

This example shows how to add work with `Auth0` rules, which are very usefull to extend functionality.

You can read a quickstart for this sample [here](https://auth0.com/docs/quickstart/webapp/java-spring-security-mvc/06-rules). 

## Prerequisites

In order to run this example you will need to have Java 7+ and Maven installed.

Check that your maven version is 3.0.x or above:

```sh
mvn -v
```

## Important Snippets

### Create an Auth0 Rule

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


### Update configuration information

Enter your:

`auth0.domain`, `auth0.issuer`, `auth0.clientId`, and `auth0.clientSecret` information into `src/main/resources/auth0.properties`.

Note:

`auth0.issuer` should have the value `https://YOUR_DOMAIN.auth0.com/` (the trailing slash is important).
For example, if your `auth0.domain` is `example.auth0.com` then `auth0.issuer` should have value `https://example.auth0.com/`.

 
There are two properties in `auth0.properties` that you do not need to touch. Leave values as `false`

`auth0.servletFilterEnabled: false` - this ensures we don't autowire the ServletFilter defined in an Auth0 dependency
library.

`auth0.defaultAuth0WebSecurityEnabled: false` - this ensures we do not autowire the default configuration file
provided with the `auth0-spring-security-mvc` library itself. That is a default configuration suitable only for
simpler applications seeking to have an out of the box secured endpoint URL - similar to `auth0-servlet` library.

For details on the other settings, please check the README for the library this sample depends on  [Auth0 Spring Security MVC](https://github.com/auth0/auth0-spring-security-mvc).
In particular, [this section on defalut configuration](https://github.com/auth0/auth0-spring-security-mvc#default-configuration) which lists each property together with a description on its purpose.

## Build and Run

In order to build and run the project execute:

```sh
mvn spring-boot:run
```

Then, go to [http://localhost:3099/login](http://localhost:3099/login). 

Shut it down manually with Ctrl-C.
