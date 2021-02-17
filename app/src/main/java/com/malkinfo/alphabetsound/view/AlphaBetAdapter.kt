package com.malkinfo.alphabetsound.view

import android.app.AlertDialog
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.alphabetsound.R
import com.malkinfo.alphabetsound.model.AlphaBets
import java.util.*
import kotlin.collections.ArrayList

class AlphaBetAdapter(val c:Context,val alphaList:ArrayList<AlphaBets>):
 RecyclerView.Adapter<AlphaBetAdapter.AlphaViewHolder>()

{
    private lateinit var mTTS :TextToSpeech
    inner class AlphaViewHolder(val v:View):RecyclerView.ViewHolder(v){
        val alphaImgs = v.findViewById<ImageView>(R.id.alphaImg)
        val alphaName = v.findViewById<TextView>(R.id.alphaName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_list,parent,false)
        return AlphaViewHolder(v)
    }

    override fun onBindViewHolder(holder: AlphaViewHolder, position: Int) {
        /**call mTTs */
        mTTS = TextToSpeech(c){status->
            if (status==TextToSpeech.SUCCESS){
                val result = mTTS.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("TTs","Language is not supported")
                }
                else{
                    holder.v.isEnabled = true
                }
            }
            else{
                Log.e("TTs","Initialization failed")
            }

        }
       val alphaList = alphaList[position]
        holder.alphaImgs.setImageResource(alphaList.alphaImg)
        holder.alphaName.text = alphaList.alphaName
        holder.v.setOnClickListener {
            /**set speak*/
            speak(alphaList.alphaName)
            /**set Dialog*/
            showAlpha(alphaList.alphaImg,alphaList.alphaName)
        }

    }

    private fun showAlpha(img: Int, name: String) {
        val inflater = LayoutInflater.from(c)
        val setView = inflater.inflate(R.layout.show_item,null)
        /**set view */
        val nameAlpha = setView.findViewById<TextView>(R.id.alphaNames)
        val imgAlpha = setView.findViewById<ImageView>(R.id.alphaImgs)
        val btnCancel = setView.findViewById<ImageView>(R.id.btnCancel)
        nameAlpha.text = name
        imgAlpha.setImageResource(img)
        val showDialog = AlertDialog.Builder(c)
        showDialog.setCancelable(true)
        showDialog.setView(setView)
        val openDialog = showDialog.create()
        btnCancel.setOnClickListener { openDialog.dismiss() }
        openDialog.show()

    }

    private fun speak(name: String) {
        /**set TextToSpeech*/
        mTTS.setPitch(1f)
        mTTS.setSpeechRate(1.1f)
        mTTS.speak(name,TextToSpeech.QUEUE_FLUSH,null)

    }

    override fun getItemCount(): Int {
        return alphaList.size
    }
}