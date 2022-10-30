 
 https://user-images.githubusercontent.com/42529024/198877730-7ba06c5d-4146-49b5-9b54-30dc7ab9c05f.png
 
 
 
 
 
 
 # ScienceGlass

 A blogging application based on a WordPress Blog website called ScienceGlass.

 Install & try the app: [Download APK](https://drive.google.com/file/d/1Ysg01UcOXfticLrtStf2CNlqX8CaMk4P/view?usp=sharingg)

 Blog's Official Website: https://cosmos.home.blog/

 * This app displays recent posts of the blog in its main ui screen.
 * It fetches JSON Data by using [Public API](https://developer.wordpress.com/docs/api/) of WordPress.
 * The JSON received is then extracted into relevant information and shown in the main user-interface of the app.


 ## Screenshots


  
 <a href="https://user-images.githubusercontent.com/42529024/168333688-391859dd-49fd-4e84-b91d-8fe348639413.png" target="_blank">
  <img src="https://user-images.githubusercontent.com/42529024/168333688-391859dd-49fd-4e84-b91d-8fe348639413.png" width="22%" />
 <span>&nbsp;</span>
 <a href="https://raw.githubusercontent.com/s0oraj/ScienceGlass/master/illustration_gif_two.gif" target="_blank">
  <img src="https://raw.githubusercontent.com/s0oraj/ScienceGlass/master/illustration_gif_two.gif" width="22%" />
</a>
<span>&nbsp;</span>
<a href="https://raw.githubusercontent.com/s0oraj/ScienceGlass/master/illustration_gif_three.gif" target="_blank">
  <img src="https://raw.githubusercontent.com/s0oraj/ScienceGlass/master/illustration_gif_three.gif" width="22%" />
</a>
<span>&nbsp;</span>
<a href="https://raw.githubusercontent.com/s0oraj/ScienceGlass/master/illustration_gif_one.gif" target="_blank">
  <img src="https://raw.githubusercontent.com/s0oraj/ScienceGlass/master/illustration_gif_one.gif" width="22%" />
</a>


 ## How this app works

 - This android app displays recent posts of a WordPress Blog called ScienceGlass.
 - It fetches JSON Data from WordPress [Public API](https://developer.wordpress.com/docs/api/) by using [Volley](https://github.com/google/volley) library. Volley helps in fetching JSON data in a seperate background thread.
 - After this, the recieved JSON data is being parsed in the main ui thread. After JSON parsing the data is shown to the main user-interface of the app.
 - For displaying featured image of a post, [Picasso](https://github.com/square/picasso) library is used which takes image from url and loads it to the screen.
 - And finally, Androids [WebView](https://developer.android.com/reference/android/webkit/WebView) library is used to convert html content into relevant data to be loaded on the screen.
 
 ## Libraries used

 * [Volley] (https://github.com/google/volley)
 * [Picasso] (https://github.com/square/picasso)


 ## Features

 -  Users can set the number of recent post they want to see. For example, if a user sets his preference to three posts then the user is only shown three recent posts from the ScienceGlass blog
 -  Swapping and Deleting of Data: Users can tap on a post and swipe right or left to remove it from screen. Users can also tap on a post hold on it and change its location by moving it up or down the list of available posts. Refresh button will restore all data back to its original configuration.
 -  Change layout: Users can customize what layout they want to see their posts in. Two layouts are provided: LinearLayout and GridLayout.
 
 
## API Reference

#### Get a list of matching posts.

```http
  GET /sites/$site/posts/
```

| Method | URL    | Requires authentication?     |
| :-------- | :------- | :------------------------- |
| `GET` | `https://public-api.wordpress.com/rest/v1.1/sites/$site/posts/` | No |

#### Method Parameters


| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `$site`      | `(int string)` | Site ID or domain |
 
 Our Website (to be replaced by $site) : https://cosmos.home.blog




