package blog.cosmos.home.scienceglass;

import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * {@link PostsAdapter} is an adapter for a recyclerview
 * that uses a list {@link java.util.ArrayList} of posts {@link Post} as its data source.
 * This adapter knows how to populate recyclerview with posts data
 * in the MainAcitivity {@link MainActivity} i.e Main UI of screen.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    List<Post> allPosts;

    /**
     * Constructs a new {@link PostsAdapter}.
     * @param allPosts list of posts the adapter will use populate the screen
     */
    public PostsAdapter(List<Post> allPosts) {
        this.allPosts = allPosts;
    }

    /**
     *Constructs a new viewholder
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
        return new ViewHolder(v);
    }


   /**
    * Binds view holder
    * **/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.postTitle.setText(allPosts.get(position).getTitle());

        // Extract featured image from image url using Picasso and
        // then putting that image into holders postImage variable
        Picasso.get().load(allPosts.get(position).getFeature_image()).into(holder.postImage);


        // When a viewholder is clicked, user is redirected to details activity for
        // more detailed post data on the respect

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start new intent
                Intent i = new Intent(view.getContext(), DetailsActivity.class);

                //Put details in the intent which DetailsAcitivty will extract.
                i.putExtra("title",allPosts.get(position).getTitle());
                i.putExtra("content",allPosts.get(position).getContent());
                i.putExtra("feature_image",allPosts.get(position).getFeature_image());

                // Start the details activity
                view.getContext().startActivity(i);
            }
        });

    }

    // Returns total number of posts available in allPost list variable
    @Override
    public int getItemCount() {
        return allPosts.size();
    }


    /**
     * Helper method which clears the existing dataset of the recyclerview adapter.
     */
    public void clear(){
        if(allPosts!=null && !allPosts.isEmpty()) {
            int size = allPosts.size();
            allPosts.clear();

            // Notify the adapter that items were removed so adapter can update the recyclerview accordingly.
            notifyItemRangeRemoved(0, size);
        }

    }

    /**
     * Updates the adapter with new data
     * **/
    public void addAll(List<Post> data){
        if (data != null && !data.isEmpty()) {
            // If new data is not empty then update allPosts List
            allPosts = data;
            //Notify the adapter for the change in dataset
            notifyDataSetChanged();
        }
    }


    /**
     * Class to hold metadata for a single post and show the same in
     * recyclerview {@link RecyclerView} via PostAdapter {@link PostsAdapter}
     * **/
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView postTitle;

        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            postImage = itemView.findViewById(R.id.post_image);
            postTitle = itemView.findViewById(R.id.post_title);

        }
    }
}
