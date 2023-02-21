package com.konge.dolbogram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.message_item.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCash = mutableListOf<CommonModel>()
    private lateinit var mDiffUtilResult: DiffUtil.DiffResult

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Text
        val blockUserMessage: ConstraintLayout = view.block_user_message
        val chatUserMessage: TextView = view.chat_user_message
        val chatUserMessageTime: TextView = view.chat_user_message_time

        val blockReceivedMessage: ConstraintLayout = view.block_received_message
        val chatReceivedMessage: TextView = view.chat_received_message
        val chatReceivedMessageTime: TextView = view.chat_received_message_time

        // Image
        val blockUserImageMessage: ConstraintLayout = view.block_user_image_message
        val chatUserImageMessage: ImageView = view.chat_user_image
        val chatUserImageMessageTime: TextView = view.chat_user_message_time

        val blockReceivedImageMessage: ConstraintLayout = view.block_received_image_message
        val chatReceivedImageMessage: ImageView = view.chat_received_image
        val chatReceivedImageMessageTime: TextView = view.chat_received_image_message_time

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)

        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCash.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {

        when(mListMessagesCash[position].type){

            TYPE_MESSAGE_TEXT -> drawMessageText(holder, position)
            
            TYPE_MESSAGE_IMAGE -> drawMessageImage(holder, position)

        }
        
    }

    private fun drawMessageImage(holder: SingleChatHolder, position: Int) {
        holder.blockUserMessage.visibility = View.GONE
        holder.blockReceivedMessage.visibility = View.GONE

        val message = mListMessagesCash[position]

        if (message.from == UUID) {
            holder.blockUserImageMessage.visibility = View.VISIBLE
            holder.blockReceivedImageMessage.visibility = View.GONE

            holder.chatUserImageMessage.downloadAndSetImage(message.image_url)
            holder.chatUserImageMessageTime.text =
                message.timeStamp.toString().asTime()

        } else {
            holder.blockUserImageMessage.visibility = View.GONE
            holder.blockReceivedImageMessage.visibility = View.VISIBLE

            holder.chatReceivedImageMessage.downloadAndSetImage(message.image_url)
            holder.chatReceivedImageMessageTime.text =
                message.timeStamp.toString().asTime()
        }
    }

    private fun drawMessageText(holder: SingleChatHolder, position: Int) {
        holder.blockUserImageMessage.visibility = View.GONE
        holder.blockReceivedImageMessage.visibility = View.GONE

        val message = mListMessagesCash[position]

        if (message.from == UUID) {
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE

            holder.chatUserMessage.text = message.text
            holder.chatUserMessageTime.text =
                message.timeStamp.toString().asTime()

        } else {
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE

            holder.chatReceivedMessage.text = message.text
            holder.chatReceivedMessageTime.text =
                message.timeStamp.toString().asTime()
        }
    }


    fun addItemToBottom(item: CommonModel, onSuccess: () -> Unit) {
        if (!mListMessagesCash.contains(item)) {
            mListMessagesCash.add(item)
            notifyItemInserted(mListMessagesCash.indexOf(item))
            //notifyItemChanged(mListMessagesCash.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: CommonModel, onSuccess: () -> Unit) {
        if (!mListMessagesCash.contains(item)) {
            mListMessagesCash.add(item)
            mListMessagesCash.sortBy { it.timeStamp.toString() }
            notifyItemInserted(mListMessagesCash.indexOf(item))
//            notifyItemChanged(mListMessagesCash.indexOf(item))
        }
        onSuccess()
    }
}

