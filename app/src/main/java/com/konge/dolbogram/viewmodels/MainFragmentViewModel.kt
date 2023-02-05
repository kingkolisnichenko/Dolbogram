package com.konge.dolbogram.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.sealed.DataState
import com.konge.dolbogram.utilits.*

class MainFragmentViewModel : ViewModel() {

    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init {
        getDataFromDatabase()
    }

    private fun getDataFromDatabase() {
        val mListMessages = mutableListOf<CommonModel>()

        response.value = DataState.Loaging

        REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UUID).addListenerForSingleValueEvent(
            AppValueEventListener { task1 ->

                task1.children.forEach { snapshot ->

                    REF_DATABASE_ROOT.child(NODE_USERS).child(snapshot.key.toString())
                        .addListenerForSingleValueEvent(
                            AppValueEventListener {
                                mListMessages.add(it.getCommonModel())
                                response.value = DataState.Success(mListMessages)
                            })
                }

            })

    }

}