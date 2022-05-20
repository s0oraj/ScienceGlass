
package blog.cosmos.home.scienceglass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blog.cosmos.home.scienceglass.navigation.AboutActivity;
import blog.cosmos.home.scienceglass.navigation.HelpActivity;
import blog.cosmos.home.scienceglass.navigation.SettingsActivity;
import blog.cosmos.home.scienceglass.navigation.SuggestionActivity;



/**
 * This is main screen of the app.
 * It displays list of posts that were posted on the official ScienceGlass blog website https://cosmos.home.blog
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Post>>
    , NavigationView.OnNavigationItemSelectedListener
    , SharedPreferences.OnSharedPreferenceChangeListener
{
    public static final String TAG = "TAG";

    //Variables for navigation bar
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    // Variables for recyclerview, post and postadapter
    private RecyclerView postRecyclerView;
    private List<Post> posts;
    private PostsAdapter adapter;
    private View mEmptyStateTextView;
    private LoaderManager loaderManager;
    private View loadingIndicator;

    /** Boolean flag that keeps track of whether the user chose linearlayout or gridlayout to view posts,
     * has been edited true if linearlayout else false if gridlayout
     * Uses shared preferences to store this data
     * Default value is true, unless user changes it later*/
    private boolean isLinearLayout = true;

    /** Identifier for the post data loader */
    private static final int POST_LOADER = 1;


    /** Keeps track of number of posts user wants to see ordered by most recent posts.
     * Uses shared preferences to store this data
     * Default value is 6, unless user changes it later*/
    private int mPostCount=6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup our custom toolbar on which navigation menu can be given, on left side of main screen.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set onclick listener to FAB button which is a refresh button for this app
        FloatingActionButton refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAdapter();
            }
        });

        //This block of code creates navigation drawer in the main activity screen of the app to the left side
        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        navigationView = findViewById(R.id.nav_view);
        toggle.setDrawerIndicatorEnabled(true); //enable hamburger sign on top left
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        /*
        * Next block of code creates a lit of posts and sets a recyclerview
        * which will populate this list of posts to the screen using an adapter
        */
        // Create an empty list of posts
        posts = new ArrayList<>();
        // Find reference to recyclerview
        postRecyclerView = findViewById(R.id.post_list);
        //Obtain a reference to the sharedPreferences file for this app.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of the preference change
        prefs.registerOnSharedPreferenceChangeListener(this);
        //Check users preference of layout using preferences, whatsoever the user has preferred,
        // set layout accordingly. If user opens app for first time then set layout to default linearlayout.
        //
        isLinearLayout = prefs.getBoolean("is_linear_layout",true);
        if(isLinearLayout) {
            //Set linearlayout
            postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            //Set GridLayout
            postRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        // Find reference to adapter and set adapter to the recycler view
        adapter = new PostsAdapter(posts);
        postRecyclerView.setAdapter(adapter);

        // Find empty view, which will be shown when there is no internet connection
        // Find loading indicator progressbar,
        // it will be shown on screen until the app retrieves information back from internet.
        mEmptyStateTextView = findViewById(R.id.empty_view);
        loadingIndicator = findViewById(R.id.loading_indicator);

        //Get reference to loadermanager
        loaderManager
                = getSupportLoaderManager();
        // Check if device is connected to the internet.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()){
            //if connected then kickoff the loader
            loaderManager.initLoader(1, null, this);
        } else {
            //if not connected then this block of code shows empty view displaying "No internet"
            loadingIndicator.setVisibility(View.GONE);
            postRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

        // Helper class for creating swipe to dismiss, drag and drop functionality to posts in the app
        // Because of this, users can drag,swap and drop posts in main screen.
        // Clicking on Refresh FAB button will refresh and restore all data
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // Get the from and to positions.
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                // Swap the items and notify the adapter.
                Collections.swap(adapter.allPosts,from,to);
                adapter.notifyItemMoved(from,to);
                return true;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Remove the item from the data
                adapter.allPosts.remove(viewHolder.getAdapterPosition());
                //notify the adapter
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        // Attach the helper to the RecyclerView
        itemTouchHelper.attachToRecyclerView(postRecyclerView);
    }





    /**
     * Callback method. This called when an option from navigation menu is selected. Navigation menu is situated at the left side of the main screen.
    */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
              switch (item.getItemId()) {

                  case R.id.visit_website:
                      visitWebsite(getResources().getString(R.string.website_url));
                      break;

                  case R.id.about: { // Remember to provide id to menu items in xml for
                      Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                      this.startActivity(intent);
                      break;
                  }

                  case R.id.suggestion: {
                      Intent intent = new Intent(MainActivity.this, SuggestionActivity.class);
                      this.startActivity(intent);
                      break;
                  }

                  case R.id.facebook_page:
                      visitWebsite(getResources().getString(R.string.facebook_url));
                      break;

                  case R.id.author_details:
                      visitWebsite(getResources().getString(R.string.author_url));
                      break;

              }
              //close navigation drawer once an option has been selected.
              // So when user comes back to the main screen, navigation menu
              // is not showing but hidden to the left side of screen.
              drawer.closeDrawer(GravityCompat.START);

              return true;
    }


    /**
     * Helper method when user clicks on website option in navigation menu.
     * Redirects user to a browser app which can open the website page.
     *
     * @param url Url of the main website of ScienceGlass which gets passed from onNavigationItemSelected() method
     * */
    public void visitWebsite( String url){
              Uri websiteUri = Uri.parse(url);
              Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
              websiteIntent.setData(websiteUri);
              startActivity(websiteIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
              getMenuInflater().inflate(R.menu.main, menu);
              return true;
    }



    /**
     * Callback when menu item of menu from right side of the main screen, is called.
     * **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
              switch (item.getItemId()) {

                  case R.id.settings_menu_item: {
                      Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                      this.startActivity(intent);
                       return true;
                  }
                  case R.id.change_layout:{
                      changeLayout();
                      return true;
                  }

                  case R.id.help_menu_item: {
                      Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                      this.startActivity(intent);
                      return true;
                  }
              }
              return super.onOptionsItemSelected(item);
    }


    /**
     * Helper method which gets called when user presses on changeLayout button from menu on right side of the main screen.
     * This method changes arrangement of posts from gridlayout to linearlayout and vice versa, depending on which layout is currently set.
     *
     * This method uses SharedPreferences to saved users choice of layout, when user opens app after some time.
     **/
    public void changeLayout (){

              //Check if there is a stable internet connection
              ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
              NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


              // If User clicks changelayout button,
              //but there is no internet and user has also swiped all posts i.e list of posts is empty
              if(adapter.allPosts.isEmpty() && (networkInfo ==null)){
                  //Not connected AND user has swiped every post. Therefore, show emptystate view
                  loadingIndicator.setVisibility(View.GONE);
                  postRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                  mEmptyStateTextView.setVisibility(View.VISIBLE);

                  // Return early, no layout will be changed
                  return;
              }
              else {
                  // if connected AND not everypost is swiped
                  // then set visiblity of emptystateview to gone
                  mEmptyStateTextView.setVisibility(View.GONE);
              }

              if(isLinearLayout){
                  // if linearlayout is present, then this block of code changes layout to gridlayout
                  postRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

                  // restart the loader
                   getSupportLoaderManager().restartLoader(1,null,this);

                  //update the is_linear_layout value present in shared preference to false, because now user has preferred gridlayout
                  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                  SharedPreferences.Editor editor = prefs.edit();
                  editor.putBoolean("is_linear_layout",false);
                  editor.apply();

                  //update isLinearLayout value to current shared preference
                  isLinearLayout =  prefs.getBoolean("is_linear_layout",true);
              } else {
                  // if Gridlayout is present, then change to linearlayout
                  postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                  // restart the loader
                   getSupportLoaderManager().restartLoader(1,null,this);


                  //update the is_linear_layout value present in shared preference to true, because now user has preferred linearlayout
                  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                  SharedPreferences.Editor editor = prefs.edit();
                  editor.putBoolean("is_linear_layout",true);
                  editor.apply();

                  //update isLinearLayout value to current shared preference
                  isLinearLayout = prefs.getBoolean("is_linear_layout",true);

              }
    }




    /**
     * Called when a loader is first created
     * */
    @NonNull
    @Override
    public Loader<List<Post>> onCreateLoader(int id, @Nullable Bundle args) {

              // Get the current value of post count which user has saved, by using SharedPreferences.
        //    // Post count is total number of recent posts which user wants to see in the app.
              SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
              String numberOfPosts = sharedPrefs.getString(
                      getString(R.string.settings_postcount_key),
                      getString(R.string.settings_postcount_default));

              mPostCount= Integer.parseInt(numberOfPosts);

              // Start the loader by proving context, url and no of posts into its constructor
              return new PostsLoader(this,getResources().getString(R.string.json_url),mPostCount);
    }




    /**
     * Called when loader finishes loading data and is ready to give back results of what it loaded.
     **/
    @Override
    public void onLoadFinished(@NonNull Loader<List<Post>> loader, List<Post> data) {
             // Hide the progressbar/loadingIndicator because now we have got back data
             // from internet which will be shown on screen
              View loadingIndicator = findViewById(R.id.loading_indicator);
              loadingIndicator.setVisibility(View.GONE);


              // If no data is returned then show EmptyStateView showing "No posts received"
              if(data.isEmpty()){

                  postRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));

               TextView emptyTitle = mEmptyStateTextView.findViewById(R.id.empty_title_text);
               emptyTitle.setText(R.string.no_posts);

                TextView emptySubtitle = mEmptyStateTextView.findViewById(R.id.empty_subtitle_text);
                emptySubtitle.setText(R.string.try_again);

              mEmptyStateTextView.setVisibility(View.VISIBLE);
              }

             // If data is found, update the adapter with new data which will be shown in the screen via recyclerview and layoutmanager.

             if (data != null && !data.isEmpty()) {
                  adapter.addAll(data);
              }
          }



          /**
           * Called when loader is being reset. This method is used to clear out existing adapter's data,
           * so the adapter can start populating new fresh data to the RecyclerView
           * **/
          @Override
          public void onLoaderReset(@NonNull Loader loader) {
              // clear the adapter
              adapter.clear();
          }


          /**
           * This callback method is called when there is a change in value of users existing preferences for the app.
           * **/
          @Override
          public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

             if(key.equals(getString(R.string.settings_postcount_key))){
                 // Clear the recyclerview as a new query will be kicked off
                 adapter.clear();

                 // Hide the empty state text view as the loading indicator will be displayed
                 mEmptyStateTextView.setVisibility(View.GONE);

                 // Show the loading indicator while new data is being fetched
                 loadingIndicator = findViewById(R.id.loading_indicator);
                 loadingIndicator.setVisibility(View.VISIBLE);

                 // Restart the loader to requery the internet as user settings have been updated
                 getSupportLoaderManager().restartLoader(1,null,MainActivity.this);
             }
          }



          /**
           * Helper method which is called when FAB button is clicked.
           * Refreshes adapters data and adapter starts filling data again to recyclerview.
           **/
          public void resetAdapter (){

             //reset the adapter
              adapter.clear();
              // Hide the empty state text view as the loading indicator will be displayed
              mEmptyStateTextView.setVisibility(View.GONE);

              // Show the loading indicator while new data is being fetched
              loadingIndicator = findViewById(R.id.loading_indicator);
              loadingIndicator.setVisibility(View.VISIBLE);

              // Check connectivity to the internet.
              ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
              NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
              if (networkInfo!=null && networkInfo.isConnected()){
                   //If connected, restart loader
                  getSupportLoaderManager().restartLoader(1,null,this);
              } else {
                  // if not connected, show emptyview
                  loadingIndicator.setVisibility(View.GONE);
                  postRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));
                  mEmptyStateTextView.setVisibility(View.VISIBLE);
              }
          }



}