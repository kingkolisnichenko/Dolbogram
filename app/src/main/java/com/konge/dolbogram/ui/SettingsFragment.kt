package com.konge.dolbogram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.konge.dolbogram.R
import com.konge.dolbogram.databinding.FragmentChatsBinding
import com.konge.dolbogram.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var mBinding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentSettingsBinding.inflate(layoutInflater)

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()



    }

}