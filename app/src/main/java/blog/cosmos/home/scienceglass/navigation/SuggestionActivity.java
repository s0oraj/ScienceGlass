package blog.cosmos.home.scienceglass.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import blog.cosmos.home.scienceglass.R;


/**
 * This class sends users suggestion to the author via intents.
 * When submit button is clicked sendEmail method is triggered which sends email
 * intent to be handled by an email app in users phone.
 * **/
public class SuggestionActivity extends AppCompatActivity {

    // Variables for input text fields and String variables to store them
   private EditText nameEditText;
   private EditText lastNameEditText;
   private EditText commentEditText;
   private String name;
   private String comment;
   private String lastName;


    /** Boolean flag that keeps track of whether the user has clicked on at least one of textviews (true)
     *  or has not clicked on anything (false) (concept taken from how udacitys pets app's editor activity works i.e mPetHasChanged variable
     *
     *  */
   private boolean mHasClicked = false;


    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mHasClicked boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasClicked = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        // Set action bars title to "suggestion"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Suggestion");

        // Finding button and assigning onClick listener to it
        // The listener gets triggered on button click and sendEmail() method is called
        Button button = findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });


        // Find all relevant views that we will need to read user input from
        nameEditText = findViewById(R.id.user_input_name);
        lastNameEditText = findViewById(R.id.user_input_last_name);
        commentEditText = findViewById(R.id.user_input_comment);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the Activity without saving.
        nameEditText.setOnTouchListener(mTouchListener);
        lastNameEditText.setOnTouchListener(mTouchListener);
        commentEditText.setOnTouchListener(mTouchListener);
    }

    /**This callback method is triggered when user presses the back button
     * onBackPressed method is called which takes user to the previous activity
     * **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This is a helper method that sends an email intent which is done by any email app available on users device
     * When submit button is pressed, this method is called.
     * In the intent it adds users name, last name and suggestion.
     * **/
    public void sendEmail(){
        name = nameEditText.getText().toString();
        comment = commentEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();


        name = name + " " + lastName;
        comment = "Name: " + name+ "\n" + comment;



        if(!name.isEmpty() && !comment.isEmpty() && !lastName.isEmpty() ){
            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"myselfsuraj123@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"ScienceGlass: Suggestion for blog");
            intent.putExtra(Intent.EXTRA_TEXT, comment);


            if(intent.resolveActivity(getPackageManager()) != null){
                showConfirmSendDialog(intent);
            } else {
                Toast.makeText(SuggestionActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

            }

        } else if(name.isEmpty()){
            Toast.makeText(SuggestionActivity.this, "Please enter valid name", Toast.LENGTH_SHORT).show();


        } else if (lastName.isEmpty()){
            Toast.makeText(SuggestionActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();

        } else if (comment.isEmpty()){
            Toast.makeText(SuggestionActivity.this, "Please enter valid comment", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Handles what happens if a user presses back button.
     * If user has not clicked on input fields then user is redirected
     * to the MainActivity {@link blog.cosmos.home.scienceglass.MainActivity}
     *
     * If the user has unsaved changes than a dialog box appers which asks user
     * if the user wants to exit or keep typing
     * **/
    @Override
    public void onBackPressed() {
        // If the user hasn't clicked anything, continue with handling back button press
        if (!mHasClicked) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Yes" button which means discard, so close the current activity.
                        finish();  }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     * Helper method to show dialog box {@link AlertDialog} asking user
     * if user wants to save changes or exit.
     * **/
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have unsaved changes.\nDo you want to exit?");
        builder.setPositiveButton("Yes", discardButtonClickListener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "No" button which means Keep (Opposite od discard),
                // so dismiss the dialog
                // and continue putting the information.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Helper method to show dialog box {@link AlertDialog} asking user
     * if user wants to send email or keep editing.
     * **/
    public void showConfirmSendDialog (Intent intent){
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Send?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, and therefore start the intent
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){
                    dialogInterface.dismiss();
                }

            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}