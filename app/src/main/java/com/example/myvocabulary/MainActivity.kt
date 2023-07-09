package com.example.myvocabulary

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myvocabulary.databinding.ActivityMainBinding
import com.example.myvocabulary.models.Model
import com.example.myvocabulary.roomDB.*
import com.example.myvocabulary.streakDB.streak
import com.example.myvocabulary.streakDB.streakDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    //For binding to work, must include layout file
    private lateinit var binding:ActivityMainBinding
    private var t1: TextToSpeech? = null
    private lateinit var word:String
    private var mProgressDialog: Dialog?=  null
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var savedViewModel: SavedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        val streakDao= streakDatabase.getInstance(this).streakDao
        binding.button.setOnClickListener{
            getWord()
            lifecycleScope.launch {
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                streakDao.addStreak(streak(0,sdf.format(Date())))}
//            dates.add(Date())
        }
        t1 = TextToSpeech(
            applicationContext
        ) { status ->
            if (status != TextToSpeech.ERROR) {
                t1?.setLanguage(Locale.UK)
            }
        }
        binding.speak.setOnClickListener(View.OnClickListener {
            val toSpeak: String = binding.meaningId.getText().toString()
            Toast.makeText(applicationContext, toSpeak, Toast.LENGTH_SHORT).show()
            t1!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
        })

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.streak -> {
                    val intent=Intent(this,StreakActivity::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
                    val intent=Intent(this,AccountActivity::class.java)
                    startActivity(intent)
                }
                R.id.settings -> {
                    val intent=Intent(this,SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.savedVocab -> {
                    val intent=Intent(this,SaveActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        val dao = SavedVocabularyDatabase.getInstance(application).savedVocabularyDao
        val repository = SavedVocabularyRepository(dao)
        val factory = SavedViewModelFactory(repository)
        savedViewModel = ViewModelProvider(this,factory)[SavedViewModel::class.java]
        binding.myViewModel = savedViewModel
        binding.lifecycleOwner = this

    }
     private fun getWord(){

         word= binding.editText.text.toString()

        if(Constants.isNetworkAvailable(this)){
            Toast.makeText(this,"Your data is on",Toast.LENGTH_LONG).show()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            //Converter can transform data into right format i.e, GSON format here
            val retrofit :Retrofit= Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                /** Add converter factory for serialization and deserialization of objects. */
                /**
                 * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
                 * decoding from JSON (when no charset is specified by a header) will use UTF-8.
                 */
                .addConverterFactory(GsonConverterFactory.create(gson))
                /** Create the Retrofit instances. */
                .build()
            val service :WordService= retrofit
                .create(WordService::class.java)
            val listCall: Call<List<Model>> = service.meaning(word)
            showCustomProgressDialog()
            // Toast.makeText(this,"Your word is $word",Toast.LENGTH_LONG).show()
            listCall.enqueue(object : Callback<List<Model>> {
                @SuppressLint("SetTextI18n")
                //Successfull HTTP response

                override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                    if (response.isSuccessful) {
                        hideProgressDialog()
                        val wordList: List<Model>? =response.body()
                        setupUI(wordList!!)
                        //Toast.makeText(this@MainActivity,"Response Result $wordList",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Error!!",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message.toString(),Toast.LENGTH_LONG).show()
                }

            })

        }else{
            Toast.makeText(this@MainActivity, "Please connect to internet.",Toast.LENGTH_LONG).show()
        }
     }
    private fun showCustomProgressDialog(){
        mProgressDialog=Dialog(this)
        /*Set the screen content from a layout resource. The resource will be inflated ,adding all top views to screen*/
        mProgressDialog!!.setContentView(R.layout.progressbar)
        // start the dialog and display it on screen
        mProgressDialog!!.show()
    }
    private fun hideProgressDialog(){
        if(mProgressDialog!=null){
            //closes the progress dialog
            mProgressDialog!!.dismiss()
        }
    }
    
    private fun setupUI(wordList:List<Model>){

           for (i in wordList.indices) {

                  Log.i("WordList", wordList.toString())
                  binding.wordId.text = word
                  binding.meaningId.text = wordList[i].meanings[i].definitions[i].definition
                  binding.posId.text=wordList[i].meanings[i].partOfSpeech
                  binding.exampleId.text=wordList[i].meanings[i].definitions[i].example
                  val speak: String =wordList[i].phonetics[0].audio
               val mp = MediaPlayer()
               binding.imageView.setOnClickListener{
               try {
                   //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                   mp.setDataSource(speak)
                   mp.prepare()
                   mp.start()
               } catch (e: Exception) {
                   e.printStackTrace()
               }



               }
              /* binding.imageView.setOnClickListener {


                      // below line is use to set the audio
                      // stream type for our media player.
                      //player.setAudioStreamType(AudioManager.STREAM_MUSIC)
                          try{
                              player.setDataSource(Audio)
                              player.prepare()
                              player.start()
                          }catch (e:Exception) {
                              Toast.makeText(this, "Eroor", Toast.LENGTH_LONG).show()
                          }


                  }*/
           }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.side_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked=true
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else{
            when(item.itemId){
                R.id.screenshot->{
                    val save=getScreenShotFromView(binding.screenView)
                    captured(save)
                }
            }
            super.onOptionsItemSelected(item)
        }
    }
    private fun captured(save: Bitmap?) {
        val calender = Calendar.getInstance()

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val date = "$day-$month-$year"


            binding.myViewModel?.insert(SaveItemList(0,save,date))
            Toast.makeText(this, "ho gya bhai ho gya", Toast.LENGTH_SHORT).show()
    }
    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }
}