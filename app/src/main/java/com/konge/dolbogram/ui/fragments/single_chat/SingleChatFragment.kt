package com.konge.dolbogram.ui.fragments.single_chat

import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.models.UserModel
import com.konge.dolbogram.ui.fragments.BaseFragment
import com.konge.dolbogram.utilits.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener
    private var mCountMessages = 15
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true

    override fun onResume() {
        super.onResume()

        initToolbar()

        initRecyclerView()
    }

    override fun onPause() {
        super.onPause()

        mToolbarInfo.visibility = View.GONE

        mRefUser.removeEventListener(mListenerInfoToolbar)

        mRefMessages.removeEventListener(mMessagesListener)

    }

    private fun initRecyclerView() {
        mRecyclerView = single_chat_recycler_view
        mSwipeRefreshLayout = single_chat_swipe_refresh

        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(UUID)
            .child(contact.id)

        mRecyclerView.adapter = mAdapter

        mMessagesListener = AppChildEventListener {

            mAdapter.adItem(it.getCommonModel(), mSmoothScrollToPosition) {
                if (mSmoothScrollToPosition) mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                mSwipeRefreshLayout.isRefreshing = false
            }


        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                mIsScrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }
        })

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
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
            mSmoothScrollToPosition = true
            val message = single_chat_input_message.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message, contact.id, TYPE_TEXT) {
                    single_chat_input_message.setText("")

                    if (mReceivingUser.state == AppStates.OFFLINE.state && mReceivingUser.messaging_token.isNotEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            notifyRecingUser(mReceivingUser, message)
                        }
                    }

                }
            }
        }
    }

    private fun notifyRecingUser(receivingUser: UserModel, message: String) {

        val serverKey = SERVER_KEY_MESSAGING
        val deviceToken = receivingUser.messaging_token.toString()

        val url = URL("https://fcm.googleapis.com/fcm/send")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.doInput = true
        connection.useCaches = false
        connection.setRequestProperty("Authorization", "key=$serverKey")
        connection.setRequestProperty("Content-Type", "application/json")

        val json = """
                {
                    "to": "$deviceToken",
                    "notification": {
                        "title": "New message in Dolbogram",
                        "body": "$message",
                        "click_action": "OPEN_ACTIVITY_MAIN"
                    }
                }
                """

        val outputStream = connection.outputStream
        val writer = OutputStreamWriter(outputStream, "UTF-8")
        writer.write(json)
        writer.flush()
        writer.close()
        outputStream.close()

        val responseCode = connection.responseCode
        if (responseCode == 200) {
            println("Push Notification sent successfully.")
        } else {
            println("Push Notification failed with response code: $responseCode")
        }

    }

    private fun initToolbarInfo() {

        mToolbarInfo.single_chat_toolbar_image.downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.single_chat_toolbar_fullname.text =
            if (mReceivingUser.fullname.isEmpty()) contact.fullname
            else mReceivingUser.fullname

        mToolbarInfo.single_chat_toolbar_state.text = mReceivingUser.state

    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessagesListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }


}
