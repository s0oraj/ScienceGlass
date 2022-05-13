package blog.cosmos.home.scienceglass.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import blog.cosmos.home.scienceglass.R;


/**
 *This activity provides information about what is ScienceGlass website all about.
 *This app is the app version of the WordPress Blog ScienceGlass https://cosmos.home.blog
 **/
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        // Set action bars title to "About"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setTitle("About");

    }

    /** Works in conjunction with  this code written in onCreate method
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     Calls onBackPressed() method when user presses backbutton
     This redirects user back to the mainActivity**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}