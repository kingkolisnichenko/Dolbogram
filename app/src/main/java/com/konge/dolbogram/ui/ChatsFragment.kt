package com.konge.dolbogram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.konge.dolbogram.R
import com.konge.dolbogram.databinding.FragmentChatsBinding

class ChatsFragment : Fragment() {

    private lateinit var mBinding: FragmentChatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentChatsBinding.inflate(layoutInflater)

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()



    }

}