package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.example.hotpopcorn.databinding.ItemRowBinding

abstract class AbstractRowViewHolder(private val binding: ItemRowBinding) : AbstractNetworkViewHolder(binding.root) {
    // Displaying current movie's / TV show's poster or current person's photo:
    @SuppressLint("UseCompatLoadingForDrawables")
    protected fun displayPosterOrPhoto(posterOrPhotoPath : String?, placeholderID : Int) {
        if (posterOrPhotoPath != null) {
            val url = "https://image.tmdb.org/t/p/w185${posterOrPhotoPath}"
            Glide.with(binding.root).load(url).centerCrop().placeholder(placeholderID).into(binding.ivPosterOrPhoto)
        } else binding.ivPosterOrPhoto.setImageDrawable(binding.root.resources.getDrawable(placeholderID, binding.root.context.theme))
    }

    // Displaying current movie's / TV show's title or current person's name:
    protected fun displayTitleOrName(title : String) {
        binding.tvTitleOrName.text = title
    }

    // Displaying current Movie's release date / current TV Show's first episode release date:
    protected fun displayReleaseDate(releaseDate : String?) {
        try { binding.tvReleaseOrBirth.text = releaseDate?.slice(IntRange(0,3)) }
        catch (e: Exception) { binding.tvReleaseOrBirth.text = "" }
    }
}