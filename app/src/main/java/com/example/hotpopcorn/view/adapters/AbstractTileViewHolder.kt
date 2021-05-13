package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.databinding.ItemTileBinding

abstract class AbstractTileViewHolder(private val binding: ItemTileBinding) : RecyclerView.ViewHolder(binding.root) {
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

    // Displaying who was played or for what was responsible:
    protected fun displayCharacterOrDepartment(text : String?) {
        binding.tvCharacterOrDepartment.text = text
    }
}