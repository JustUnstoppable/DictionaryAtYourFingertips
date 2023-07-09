package com.example.myvocabulary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myvocabulary.databinding.ActivitySaveBinding
import com.example.myvocabulary.roomDB.SaveItemList
import com.example.myvocabulary.roomDB.SavedVocabularyDatabase
import com.example.myvocabulary.roomDB.SavedVocabularyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveActivity : AppCompatActivity() {
    lateinit var binding:ActivitySaveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvView.layoutManager = GridLayoutManager(this,2)
//        val data = ArrayList<SaveItemList>()


        //binding.rvView.adapter=adapter
        lifecycleScope.launch(Dispatchers.Main) {
            val dao = SavedVocabularyDatabase.getInstance(application).savedVocabularyDao
            val repository = SavedVocabularyRepository(dao)
           repository.savedLists.observe(this@SaveActivity){ items->
                items.let {  val adapter=SaveAdapter(it)
                    binding.rvView.adapter=adapter
                    }
            }
            //val adapter=SaveAdapter(dao.getAllSaved())

        }


//
//        for (i in 1..12) {
//            data.add(SaveItemList(R.drawable.ic_done, "Item $i"))
//        }
//        val adapter=SaveAdapter(dao.getAllSaved())
//        binding.rvView.adapter=adapter
    }
}