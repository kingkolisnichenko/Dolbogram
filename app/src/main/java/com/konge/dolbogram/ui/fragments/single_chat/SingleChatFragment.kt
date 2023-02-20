package com.konge.dolbogram.ui.fragments.single_chat

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.models.UserModel
import com.konge.dolbogram.ui.fragments.BaseFragment
import com.konge.dolbogram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mMessagesListener: AppChildEventListener

    private var mCountMessages = 20
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true

    override fun onResume() {
        super.onResume()

        initFields()

        initToolbar()

        initRecyclerView()
    }

    override fun onPause() {
        super.onPause()

        mToolbarInfo.visibility = View.GONE

        mRefUser.removeEventListener(mListenerInfoToolbar)

        mRefMessages.removeEventListener(mMessagesListener)

    }

    private fun initFields() {

        single_chat_input_message.addTextChangedListener(AppTextWatcher {
            val string = single_chat_input_message.text.toString()
            if (string.isEmpty()) {
                single_chat_image_send.visibility = View.GONE
                single_chat_image_atach.visibility = View.VISIBLE
            } else {
                single_chat_image_send.visibility = View.VISIBLE
                single_chat_image_atach.visibility = View.GONE
            }
        })

        single_chat_image_atach.setOnClickListener { attachFile() }

    }

    private fun attachFile() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .start(APP_ACTIVITY, this)
    }

    private fun initRecyclerView() {
        mLayoutManager = LinearLayoutManager(this.context)
        mRecyclerView = single_chat_recycler_view
        mSwipeRefreshLayout = single_chat_swipe_refresh

        mAdapter = SingleChatAdapter()
        mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
            .child(UUID)
            .child(contact.id)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.isNestedScrollingEnabled = false

        mMessagesListener = AppChildEventListener {

            val msg = it.getCommonModel()
            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBottom(msg) { mRecyclerView.smoothScrollToPosition(mAdapter.itemCount) }
            } else {
                mAdapter.addItemToTop(msg) { mSwipeRefreshLayout.isRefreshing = false }
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
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 3) {
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
                sendMessage(message, contact.id, TYPE_MESSAGE_TEXT) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val messageKey = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UUID).child(contact.id)
                .push().key.toString()

            val path = REF_STORAGE_ROOT.child(FOLDER_ATTACHED_FILES).child(messageKey)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    sendMessageAsFile(contact.id, it, messageKey)
                }
            }

        }
    }

}
