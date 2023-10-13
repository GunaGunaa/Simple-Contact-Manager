package com.example.jetpackcompose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.model.User

class CAViewSocialProfile() {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun SetSocialScreen(navController: NavHostController, result: User?) {
        val tabTitles = listOf("Requests", "Followers", "Following")
        var selectedTabIndex by remember { mutableStateOf(0) }
        val pagerState = rememberPagerState(initialPage = selectedTabIndex)
        Column(Modifier.background(MaterialTheme.colors.background)) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = colorResource(id = R.color.pink),
                    modifier = Modifier
                        .padding(start = 17.dp)
                        .align(Alignment.CenterStart),
                    fontSize = 17.sp
                )
                if (result != null) {
                    Text(
                        text = result.first_name,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Alignment.Center),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }
            TabRow(selectedTabIndex, indicator = {}, modifier = Modifier.padding(15.dp)) {
                tabTitles.forEachIndexed { index, title ->
                    val tabModifier = if (selectedTabIndex == index) {
                        Modifier
                            .background(colorResource(id = R.color.pink))
                            .height(40.dp)
                            .border(width = 1.dp, color = colorResource(id = R.color.pink))
                    } else {
                        Modifier
                            .background(MaterialTheme.colors.background)
                            .height(40.dp)
                            .border(width = 1.dp, color = colorResource(id = R.color.pink))
                    }
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = tabModifier
                    ) {
                        if (selectedTabIndex == index) {
                            Text(text = title, color = colorResource(id = R.color.white))
                        } else Text(text = title, color = colorResource(id = R.color.pink))
                    }
                }
            }
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(id = R.color.divider))
            )

            // Content of the selected tab
            when (selectedTabIndex) {
                0 -> TabContent("Yet to be implemented",R.color.teal_700)
                1 -> TabContent("Yet to be implemented",R.color.purple_700)
                2 -> TabContent("Yet to be implemented",R.color.pink)
            }
        }
    }

    @Composable
    fun TabContent(content: String,color:Int) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = color))) {
            Text(text = content, modifier = Modifier.align(Alignment.Center))
        }
    }
}
