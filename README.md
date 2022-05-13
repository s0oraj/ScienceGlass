
# ScienceGlass

A simple blog app which is based on a WordPress Blog website called ScienceGlass.

Install & try the app :[Download APK](https://drive.google.com/file/d/1t6S_fNJWltUb-kXMFivFwFjI0jX4oMOK/view?usp=sharing)

Blog's Offical Website : https://cosmos.home.blog/

* This app displays recent posts of the blog in its main ui screen.
* It fetches JSON Data by using [Public API](https://developer.wordpress.com/docs/api/) of WordPress.
* The JSON recieved is then extracted into relevant information and shown in the main user-interface of the app.


## Screenshots

<a href="https://user-images.githubusercontent.com/42529024/168290528-0f992359-105c-4ac7-85f1-e6d257c46c49.png" target="_blank">
  <img src="https://user-images.githubusercontent.com/42529024/168290528-0f992359-105c-4ac7-85f1-e6d257c46c49.png" width="31%" />
</a>

<a href="https://user-images.githubusercontent.com/42529024/168290010-b56b3edc-6dfd-47ba-a300-ff8b28a54dc5.png" target="_blank">
  <img src="https://user-images.githubusercontent.com/42529024/168290010-b56b3edc-6dfd-47ba-a300-ff8b28a54dc5.png" width="31%" />
</a>

<a href="https://user-images.githubusercontent.com/42529024/168290330-30f56689-d2c2-4c2a-bcae-37259ef6ad74.png" target="_blank">
  <img src="https://user-images.githubusercontent.com/42529024/168290330-30f56689-d2c2-4c2a-bcae-37259ef6ad74.png" width="31%" />
</a>

# Libraries used

* [Volley] (https://github.com/google/volley)
* [Picasso] (https://github.com/square/picasso)

## How this app works

- This android app displays recent posts of a WordPress Blog called ScienceGlass.
- It fetches JSON Data from WordPress [Public API](https://developer.wordpress.com/docs/api/) by using Volley library.
- After this, it then parses the recieved data and shows it in main user-interface of the app.
- For displaying featured image of a post, picasso library is used which takes image from url and loads it to the screen.
- And finally, Androids WebView library is used to convert html content into relevant data to be loaded on the screen.




## Features

-  Users can set the number of recent post they want to see. If a user sets his preference to three posts then the user is only shown three recent posts from the ScienceGlass blog
-  Swapping and Deleting of Data: Users can tap on a post and swipe right to remove it from screen. And users can also tap on a post and change its location by moving it up or down the list of posts fom the website. Refresh button will restore all data back to its original configuration.
-  Change layout: Users can coustomize what layout they want to see their posts in. Two layouts are provided: LinearLayout and GridLayout.


<a href="https://user-images.githubusercontent.com/42529024/168299190-8b371bb0-7d1f-4088-92b4-f9f6d0851241.png" target="_blank">
  <img src="https://user-images.githubusercontent.com/42529024/168299190-8b371bb0-7d1f-4088-92b4-f9f6d0851241.png" width="31%" />
</a>
<a href="https://user-images.githubusercontent.com/42529024/168299449-7b03c6b3-dbbe-48e6-938c-de9a04d9cfac.png" target="_blank">
  <img src="https://user-images.githubusercontent.com/42529024/168299449-7b03c6b3-dbbe-48e6-938c-de9a04d9cfac.png" width="31%" />
</a>


