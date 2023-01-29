package com.konge.dolbogram.ui.fragments.single_chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.models.UserModel
import com.konge.dolbogram.ui.fragments.BaseFragment
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRefMessagesListener: ChildEventListener

    override fun onResume() {
        super.onResume()

        initToolbar()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = single_chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(UUID)
            .child(contact.id)

        mRecyclerView.adapter = mAdapter

        mRefMessagesListener = AppChildEventListener {
            mAdapter.adItem(it.getCommonModel())
            mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }

        mRefMessages.addChildEventListener(mRefMessagesListener)

    }

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE

        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initToolbarInfo()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)

        mRefUser.addValueEventListener(mListenerInfoToolbar)

        single_chat_image_send.setOnClickListener {
            val message = single_chat_input_message.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    single_chat_input_message.setText("")
                }
            }
        }
    }

    private fun initToolbarInfo() {

        mToolbarInfo.single_chat_toolbar_image.downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.single_chat_toolbar_fullname.text =
            if (mReceivingUser.fullname.isEmpty()) contact.fullname
            else mReceivingUser.fullname

        mToolbarInfo.single_chat_toolbar_state.text = mReceivingUser.state

    }

    override fun onPause() {
        super.onPause()

        mToolbarInfo.visibility = View.GONE

        mRefUser.removeEventListener(mListenerInfoToolbar)
        mRefMessages.removeEventListener(mRefMessagesListener)

    }



}
