package blog.cosmos.home.scienceglass.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import blog.cosmos.home.scienceglass.R;

/**
 * This activity provides detailed guide on how to use this app, for users.
 * **/
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Set action bars title to "Help"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help");


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
