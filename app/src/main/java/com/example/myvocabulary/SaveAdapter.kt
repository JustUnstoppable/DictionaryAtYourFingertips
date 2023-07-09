package com.example.myvocabulary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.myvocabulary.roomDB.SaveItemList

class SaveAdapter(private val sList: List<SaveItemList>):RecyclerView.Adapter<SaveAdapter.ViewHolder>() {
    private var saveList = emptyList<SaveItemList>()
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgList)
        val textView: TextView = itemView.findViewById(R.id.dateList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= sList[position]

        holder.imageView.setImageBitmap(item.imageSave)

        holder.textView.text=item.dateSave
    }

    override fun getItemCount(): Int {
        return sList.size
    }
    fun setData(saveItemList: List<SaveItemList>){
        this.saveList = saveItemList
        notifyDataSetChanged()
    }
}