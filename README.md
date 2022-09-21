# JWT Login 



##### flavors:
- mock
- prod

###### mock :
the application not call server
the application use mock class to work (local implemented)

when user login with **"invalid"** *username* or **"invalid"** *password* -> Invalid Credentials error
when user login with **"error"** *username* or **"error"** *password* -> Unexpected error
when user try autologin with **"expired"** *RefreshToken*" ->Token Expired error

when user login other *username* and *password* -> navigate to home screen
when user login with *RefreshToken*-> navigate to home screen

###### prod:
the application call server url
the server  not implemnted yet

##### Test
**Test run with mock flavor**
- check login credentials
- check jwt params
- check login button enable/disable
- check invalid credentials on login form
- check unexpected error on login form
- check success login and check home screen content
