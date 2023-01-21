package com.konge.dolbogram.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.utilits.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener: AppValueEventListener
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onResume() {
        super.onResume()

        APP_ACTIVITY.title = getString(R.string.app_title_contacts)

        initRecyclerView()

    }

    private fun initRecyclerView() {
        mRecyclerView = contact_recycler_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(UUID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()
        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_item, parent, false)

                return ContactsHolder(view)

            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {

                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    holder.fullname.text = contact.fullname
                    holder.state.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                }

                mRefUsers.addValueEventListener(mRefUsersListener)

                mapListeners[mRefUsers] = mRefUsersListener

            }

        }

        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()

    }

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fullname: TextView = view.contact_item_fullname
        val state: TextView = view.contact_item_state
        val photo: CircleImageView = view.contact_item_photo

    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()

        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }

    }
}
