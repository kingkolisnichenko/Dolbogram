package com.konge.dolbogram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberAsyncImagePainter
import com.konge.dolbogram.R
import com.konge.dolbogram.models.CommonModel
import com.konge.dolbogram.sealed.DataState
import com.konge.dolbogram.ui.fragments.single_chat.SingleChatFragment
import com.konge.dolbogram.utilits.*
import com.konge.dolbogram.viewmodels.MainFragmentViewModel

//class MainFragment : Fragment(R.layout.fragment_chats) {
class MainFragment : Fragment() {

    val mViewModel: MainFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext())
    }

    override fun onResume() {
        super.onResume()

        (view as ComposeView).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SetData(mViewModel)
            }
        }

        APP_ACTIVITY.title = getString(R.string.app_title_chats)

        APP_ACTIVITY.mAppDrawer.enableDrawer()

        hideKeyboard()

    }


    @Composable
    fun SetData(viewModel: MainFragmentViewModel) {
        when (val result = viewModel.response.value) {

            is DataState.Loaging -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            is DataState.Success -> {
                LazyColumn{
                    items(result.data){msgCard ->
                        MessageCard(msg = msgCard)
                    }
                }
            }
            is DataState.Failrue -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = result.message)
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Ошибка загрузки данных")
                }
            }

        }

    }

    @Composable
    fun MessageCard(msg: CommonModel) {

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .clickable { replaceFragment(SingleChatFragment(msg)) }
        ){
            Row{
                Image(
                    painter = rememberAsyncImagePainter(msg.photoUrl),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = msg.fullname)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = msg.state)
                }
            }
        }

    }


}