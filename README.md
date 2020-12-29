# Template
This is a template for building an android application.
It contains some functionality already built:
- 1)Model View ViewModel architecture
- 2)Sign In/Login functionality
- 3)Registration functionality
- 4)Make remote API requests
- 5)Local storage (Room DB)
- 6)Progress indicators
- 7)Alert Dialogs

To get started using this template:
https://www.youtube.com/watch?v=Z0MTZrZujt8&ab_channel=MangoBurrito
- 1)Download or clone the project
- 2)Start Android Studio & open existing project, select the Template project you downloaded in the previous step
- 2)Enter an application id of your choosing in build.gradle < plugins { applicationId "com.android.myprojectname" } >
- 3)Enter that same applicationId in the package name in Manifest.xml
- 4)Refactor your package names according to the app id(build.gradle) & package name(Manifest.xml) you specified
- 5)Update the App Name
- 6)add your base url the the AuthenticationService.kt file
