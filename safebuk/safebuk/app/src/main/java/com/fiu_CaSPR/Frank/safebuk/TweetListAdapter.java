package com.fiu_CaSPR.Frank.safebuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ivan.minev on 22.1.2015 г..
 */
public class TweetListAdapter extends ArrayAdapter<Tweet>
{

    public TweetListAdapter(Context context, int resource, Tweet[] objects)
    {
        super(context, resource, objects);
    }

    private static class ViewHolder
    {
        ImageView picture;

        TextView name;

        TextView username;

        TextView tweetText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = convertView;
        if(v == null)
        {

            v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tweet_item_view, parent);

            final ViewHolder holder=new ViewHolder();

            holder.picture= (ImageView) v.findViewById(R.id.authorPicture);
            holder.name= (TextView) v.findViewById(R.id.authorName);
            holder.username= (TextView) v.findViewById(R.id.username);
            holder.tweetText= (TextView) v.findViewById(R.id.tweetText);
            v.setTag(holder);
        }

        final Tweet tweet=getItem(position);

        final ViewHolder viewHolder= (ViewHolder) v.getTag();

        viewHolder.picture.setImageURI(tweet.getImageUri());
        viewHolder.username.setText(tweet.getUsername());
        viewHolder.name.setText(tweet.getAuthor());
        viewHolder.tweetText.setText(tweet.getText());

        return v;
    }
}
