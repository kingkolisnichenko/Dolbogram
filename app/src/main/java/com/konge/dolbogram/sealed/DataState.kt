package com.konge.dolbogram.sealed

import com.konge.dolbogram.models.CommonModel

sealed class DataState {
    class Success(val data: MutableList<CommonModel>) : DataState()
    class Failrue(val message: String) : DataState()
    object Loaging : DataState()
    object Empty : DataState()

}