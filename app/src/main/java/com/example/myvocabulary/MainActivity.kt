package com.example.myvocabulary

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.example.myvocabulary.databinding.ActivityMainBinding
import com.example.myvocabulary.models.Model
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.util.*


class MainActivity : AppCompatActivity() {
    //For binding to work, must include layout file
    private lateinit var binding:ActivityMainBinding
    private var t1: TextToSpeech? = null
    private lateinit var word:String
    private var mProgressDialog: Dialog?=  null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.button.setOnClickListener{
            getWord()
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
}