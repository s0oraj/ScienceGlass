package blog.cosmos.home.scienceglass;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import blog.cosmos.home.scienceglass.MainActivity;
import static blog.cosmos.home.scienceglass.MainActivity.TAG;


/**
 * This class does the actual background work of the app
 * It uses Volley library to fetch data from WordPress Blog in the Background thread
 * and returns an {@link ArrayList} list of {@link Post} in the Main Ui thread.
 * Extends base {@link Loader} class to implement Loader operations
 **/

public class PostsLoader extends Loader<List<Post>> {

    // String url of wordpress api from where ScienceGlass website is being fetched
    private final String mUrl;
    // Context reference of MainActivity of app
    private final Context mainActivityContext;
    // ArrayList of Post which we want to return after JSON Parsing using Volley
    private  List<Post> mPosts;

    private JsonObjectRequest request;

    // Number of posts to be shown in main UI Screen. Length of List<Post> will depend on this variable.
    private int mPostCount;


    /**
     * Constructor when PostLoader object is first created
     * Called from onCreateLoader() callback in the MainActivity
     * @param context used to retrieve the application context.
     * @param url Url to get Wordpress API JSON of ScienceGlass blog
     * @param postCount number of recent posts user wants to see in the app
     */
    public PostsLoader(@NonNull Context context, String url, int postCount) {
        super(context);
        //Initialize member variables
        mUrl = url;
        mainActivityContext= context;
        mPostCount = postCount;

        // Create a new arraylist of posts
        mPosts = new ArrayList<>();

    }

    /**
     * Called when the loader is first created and then started
     * **/
    @Override
    protected void onStartLoading() {

        // If list of posts is not empty and already contains some data,
        // deliever result immediately to onLoadFinished() callback in MainActivity
        if(!mPosts.isEmpty()){
            deliverResult(mPosts);
        }
        else{
            // If list of posts is empty, start fetching new JSON data from the WORDPRESS API
            forceLoad();
        }

    }

    /**
     * Triggered when forceload method is called in the onStartLoading() callback
     * **/
    @Override
    protected void onForceLoad() {
        // Start extracting json from Wordpress API using Volley in a background thread
      extractJson();
    }


    /**
     * Cancel JSON request once onStopLoading is called
     * **/
    @Override
    protected void onStopLoading() {
        request.cancel();
    }

    /**
     * Helper method to start extracting json from Wordpress API using Volley in a background thread
     * once the JSON data is fetched, onResponse() method is triggered in the main thread.
     * onResponse() method extracts the JSON data {@link JSONObject} into  List {@link ArrayList} of Posts {@link Post}
     * and puts this List into delieverRequest() method when is then recieved by onLoadFinished() method in MainAcitivy {@link MainActivity}
     * **/
    public void extractJson(){

        //Use of volley to extract data
        RequestQueue queue = Volley.newRequestQueue(mainActivityContext);

        // Create a new JSON request
         request = new JsonObjectRequest
                (Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject websiteResponse) {

                        //On response start extracting JSONObject into a List of Posts
                        JSONArray response = null;
                        try {
                            response = websiteResponse.getJSONArray("posts");

                            // Toast message to show the length of array of response of JSONObject i.e websiteResponse variable
                            Toast.makeText(mainActivityContext, "length of responsearray "+response.length(), Toast.LENGTH_SHORT).show();

                            // If mPostCount is larger than the total number of posts ScienceGlass has, then
                            // put total number of posts i.e response.length into mPostCount value
                            if(mPostCount>response.length()){

                                mPostCount = response.length();
                            }

                            // JSON Parsing of response array
                            for(int i = 0; i < mPostCount;i++){

                                Post p = new Post();

                                JSONObject jsonObjectData = response.getJSONObject(i);

                                // extract the date of a single post
                                p.setDate(jsonObjectData.getString("date"));


                                // extract the title of a single post
                                String unformattedTitle= jsonObjectData.getString("title");
                                String title = Html.fromHtml(unformattedTitle).toString();
                                p.setTitle(title);


                                // extract the content of a single post
                                p.setContent(jsonObjectData.getString("content"));


                                //extract the excerpt
                                p.setExcerpt(jsonObjectData.getString("excerpt"));


                                // extract feature image of a single post
                                p.setFeature_image(jsonObjectData.getString("featured_image"));
                                //   Log.d(TAG, "featured image "+p.getFeature_image());


                                // add post to the list of posts
                                mPosts.add(p);

                                //Repeat this forloop till the value of i reaches mPostCount i.e number of post user desires to see

                            }


                        } catch (JSONException e) {
                            //Catch a json exception
                            e.printStackTrace();
                            Toast.makeText(mainActivityContext, "JsonException occured while parsing, check StackTrace",Toast.LENGTH_SHORT).show();
                        }

                        // Once list of posts i.e mPosts is extracted, deliever this data to MainActivitys onLoadFinished() method
                        deliverResult(mPosts);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mainActivityContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

         // add request to the queue
        queue.add(request);
    }


}
