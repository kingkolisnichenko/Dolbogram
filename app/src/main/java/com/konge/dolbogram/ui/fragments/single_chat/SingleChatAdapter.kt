package com.konge.dolbogram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.utilits.UUID
import com.konge.dolbogram.utilits.asTime
import kotlinx.android.synthetic.main.message_item.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCash = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {

        val blockUserMessage: ConstraintLayout = view.block_user_message
        val chatUserMessage: TextView = view.chat_user_message
        val chatUserMessageTime: TextView = view.chat_user_message_time

        val blockReceivedMessage: ConstraintLayout = view.block_received_message
        val chatReceivedMessage: TextView = view.chat_received_message
        val chatReceivedMessageTime: TextView = view.chat_received_message_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)

        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCash.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCash[position].from == UUID) {
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE

            holder.chatUserMessage.text = mListMessagesCash[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCash[position].timeStamp.toString().asTime()

        }else{
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE

            holder.chatReceivedMessage.text = mListMessagesCash[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCash[position].timeStamp.toString().asTime()
        }

    }

    fun setList(list: List<CommonModel>) {
        mListMessagesCash = list
        notifyDataSetChanged()
    }

}

