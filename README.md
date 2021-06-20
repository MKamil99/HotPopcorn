# HotPopcorn
### Table of contents
* [Project description](#project-description)
* [Used technologies](#used-technologies)
* [How to compile it?](#how-to-compile-it)
* [How to use it?](#how-to-use-it)
* [Project status](#project-status)

### Project description
**Hot Popcorn** is a remastered version of [Popcorn](https://github.com/xlimiii/Popcorn) application. 
Similarly to previous one, it  uses **The Movie Database API** to present information **about movies, people and TV Shows**.
You can **search for** specific title / person or check what is popular nowadays. Each movie and TV Show is presented really 
precisely - you can get to know what is it about, when and where it was produced, which languages are used in it, how long it lasts, 
which genres it represents, who plays in it, who produced it and what rating it has. You can also check when and where specific person
was born, when he/she died, with which movies and TV shows is connected and read his/her biography.

This time to store the data, the application uses two **Firebase** modules: **Authentication** and **Realtime Database**. 
Thanks to them, you can have your own account, login on any device you want and save interesting titles just for you. 
Another differences between Hot Popcorn and Popcorn are mainly in design (eg. orange color, tabs with pages, FAB) 
and in code structure (eg. another way of binding layouts, some abstract classes to inherit from, public getters in ViewModels).

### Used technologies
* Kotlin 1.5.10 - langauge in which the project has been written,
* Retrofit 2.9.0 - library responsible for communicating with API,
* Moshi 2.4.0 - library with JSON converter used by Retrofit,
* Glide 4.11.0 - library responsible for displaying pictures with given url,
* Material 1.3.0 - library responsible for using Material Design components,
* Navigation 2.3.5 - library responsible for moving between the fragments,
* Firebase Database 20.0.0 - library responsible for storing data in Firebase,
* Firebase Auth 21.0.1 - library responsible for authentication in Firebase.

### How to compile it?
Hot Popcorn uses Firebase and TMDB REST API. Both of them need API keys, which means that you'll 
have to generate your own ones as they are not contained in this repository. At first, 
follow the steps from this [instruction](https://developers.themoviedb.org/3/getting-started/introduction) to **create a developer account in TMDB service**. 
After you get the Key, place it in the **line 78 of model/api/ApiRequest.kt file**:
```
private const val apiKey: String = "YOUR_API_KEY"    // PLACE YOUR API KEY HERE
```
Then, follow this [instruction](https://firebase.google.com/docs/android/setup) to **register your application in the Firebase**. 
During this step you'll have to download your own ```google-services.json``` file and place it in the ```app``` directory.
Also don't forget to **enable Firebase Database and Firebase Authentication** in your Firebase Console. If you do it correctly, 
you'll be able to use all functionalities of the application.

### How to use it?
Before you get access to main content, you have to **login** or **register**. For now, you can do it only by your e-mail. 
If you do it once, your email and password will be remembered so after launching the application in the future, the fields will be field.
What is more, if you don't log out, you will be still logged in after relaunching the app.

<p align="center">
<img src="https://user-images.githubusercontent.com/43967269/118283405-9e2e5a80-b4cf-11eb-851d-7d7e259c2b6f.png" alt="Login Screen">
<img src="https://user-images.githubusercontent.com/43967269/118283468-adada380-b4cf-11eb-94a9-36151e0413b1.png" alt="Register Screen">
</p>

Main content consists of two tabs: **Explore** and **Library**. In **first tab**, which represents data from API, there are three pages: 
**Movies**, **People** and **TV Shows**. By default, in each of them there are displayed **popular movies, popular people and popular TV Shows**, 
but you can use search view placed at the top of the screen to **browse items by their names**.

<p align="center">
<img src="https://user-images.githubusercontent.com/43967269/118284328-991ddb00-b4d0-11eb-885a-5893d5a42a61.png" alt="MoviesFragment">
<img src="https://user-images.githubusercontent.com/43967269/118284362-a1761600-b4d0-11eb-8e51-bfd6ee7fd624.png" alt="TVShowsFragment">
<img src="https://user-images.githubusercontent.com/43967269/118284375-a4710680-b4d0-11eb-8dbb-c583ba60dfe1.png" alt="PeopleFragment">
</p>

In **second tab**, which represents data from Firebase, there are two pages: **To Watch** and **Watched**. By default, in each of them there are displayed full lists,
but you can - again - use search view placed at the top of the screen to **browse items by their titles**. This search view is the same search view as previous, 
so using it causes in searching in all five lists at the same time.

<p align="center">
<img src="https://user-images.githubusercontent.com/43967269/118284827-23663f00-b4d1-11eb-80c1-11e37fab54a5.png" alt="ToWatchFragment">
<img src="https://user-images.githubusercontent.com/43967269/118284834-26612f80-b4d1-11eb-9b12-8316d873b0ff.png" alt="WatchedFragment">
</p>

If you click a row or tile with specific movie / TV Show / person, you'll go to the screen presenting **it's details**. Information which is displayed here 
has been already mentioned in [Project description](#project-description). You can also click the name of company responsible for this title and check 
the details of the company. As you can see there is also **Floating Action Button in the left lower corner**. By default, it is gray. If you click it once,
it will become red, which means that current movie / TV Show is in your "To Watch" list. If you click it again, it will change its color to green, which means
that current movie / TV Show is in your "Watched" list. Clicking it again will bring it to default, gray color - the movie / TV Show is not in your lists.

<p align="center">
<img src="https://user-images.githubusercontent.com/43967269/118285518-d0d95280-b4d1-11eb-8d0c-b29eff3c6e4d.png" alt="MovieDetailsFragment">
<img src="https://user-images.githubusercontent.com/43967269/118285537-d6cf3380-b4d1-11eb-8ae6-732c3fd5befc.png" alt="PersonDetailsFragment">
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/43967269/118285528-d3d44300-b4d1-11eb-9bfe-11f5d4189a1d.png" alt="TVShowDetailsFragment">
<img src="https://user-images.githubusercontent.com/43967269/118285544-d9318d80-b4d1-11eb-9fb2-f4e5d95708b7.png" alt="CompanyDetailsFragment">
</p>

### Project status
The project is useful and almost complete, but there are still some **features that can be added**:
* **possibility to work offline** - the application doesn't work without access to Internet or API,
* **possibility to use Google account** or any other social media account, which is much more convenient than using the email,
* **possibility to remind the password** if user forgets it.
