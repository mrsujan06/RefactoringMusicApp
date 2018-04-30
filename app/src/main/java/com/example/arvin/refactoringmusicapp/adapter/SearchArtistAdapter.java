package com.example.arvin.refactoringmusicapp.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arvin.refactoringmusicapp.DetailActivity;
import com.example.arvin.refactoringmusicapp.R;
import com.example.arvin.refactoringmusicapp.model.Artist;
import java.util.LinkedList;
import java.util.List;

public class SearchArtistAdapter extends RecyclerView.Adapter<SearchArtistAdapter.SearchArtistAdapterViewHolder> {

    List<Artist> mResult = new LinkedList<>();



    @Override
    public SearchArtistAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchArtistAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchArtistAdapterViewHolder holder, int position) {
        holder.bind(mResult.get(position));

    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    public void addArtistResponses(List<Artist> result) {
        mResult.clear();
        mResult.addAll(result);
        notifyDataSetChanged();
    }


    //Top Single View Holder
    public class SearchArtistAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView artistImageView;
        final TextView artistName;
        private Artist response;


        SearchArtistAdapterViewHolder(View itemView) {
            super(itemView);
            artistImageView = itemView.findViewById(R.id.artistImageView);
            artistName = itemView.findViewById(R.id.artistName);
            itemView.setOnClickListener(this);

        }

        void bind(Artist response) {
            this.response = response;
            this.artistName.setText(response.getStrArtist());
            Glide.with(this.artistImageView.getContext()).load(response.getStrArtistThumb()).into(artistImageView);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("image_url", response.getStrArtistThumb());
            intent.putExtra("bio", response.getStrBiographyEN());
            intent.putExtra("logo", response.getStrArtistLogo());
            intent.putExtra("fanart", response.getStrArtistFanart());
            intent.putExtra("fanart2", response.getStrArtistFanart2());
            intent.putExtra("fanart3", response.getStrArtistFanart3());
            intent.putExtra("banner", response.getStrArtistBanner());

            v.getContext().startActivity(intent);

            Toast.makeText(v.getContext(), mResult.get(getAdapterPosition()).getStrArtist(), Toast.LENGTH_SHORT).show();
        }

    }


}
