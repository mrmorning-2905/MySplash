package com.psd.learn.mysplash.ui.feed.photos.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.psd.learn.mysplash.databinding.InfoGridItemBinding

class PhotoInfoGridAdapter(context: Context, infoList: ArrayList<InfoModel>) : ArrayAdapter<InfoModel>(context, 0, infoList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView != null) InfoGridItemBinding.bind(convertView)
        else InfoGridItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val infoItem = getItem(position)
        binding.infoTitle.text = infoItem?.title
        binding.infoDescription.text = infoItem?.description
        return binding.root
    }
}

data class InfoModel(
    val title: String,
    val description: String
)