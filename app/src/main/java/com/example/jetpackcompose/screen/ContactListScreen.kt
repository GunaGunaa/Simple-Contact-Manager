package com.example.jetpackcompose.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.jetpackcompose.R
import com.example.jetpackcompose.designs.Components
import com.example.jetpackcompose.model.UserData
import com.example.jetpackcompose.navigation.Screen
import com.example.jetpackcompose.ui.theme.Shapes
import com.example.jetpackcompose.viewmodel.ContactScreenViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import javax.inject.Inject

class ContactListScreen() {

    @Composable
    fun SetContactList(
        navController: NavHostController,
        viewModel: ContactScreenViewModel = hiltViewModel(),
    ) {
        val components = Components()
        val context = LocalContext.current
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // close the screen
                (context as? Activity)?.finish()
            }
        }
        components.FullScreenProgressDialog(isOpen = viewModel.contactListUiState.isLoading)
        val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        DisposableEffect(onBackPressedCallback) {
            dispatcher?.addCallback(onBackPressedCallback)
            onDispose {
                onBackPressedCallback.remove()
            }
        }
        if (viewModel.contactListUiState.isInitialLoad) {
            LaunchedEffect(viewModel.contactListUiState.isInitialLoad) {
                viewModel.contactListUiState = viewModel.contactListUiState.copy(
                    currentPage = 1,
                    searchList = emptyList(),
                    isInitialLoad = false,
                    contactList = emptyList(),
                )
                if (viewModel.contactListUiState.refreshProgress) {
                    viewModel.contactListUiState =
                        viewModel.contactListUiState.copy(isLoading = true)
                }
                viewModel.getContactList()
            }
        }
        val listState = rememberLazyListState()
        val lastItem by remember {
            derivedStateOf {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
            }
        }

        LaunchedEffect(lastItem) {
            if (lastItem && viewModel.contactListUiState.searchState) {
                viewModel.getContactList()
                viewModel.contactListUiState=viewModel.contactListUiState.copy(paginationProgress = true)
            }
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            Card(
                backgroundColor = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth(),
                elevation = 1.dp
            ) {
                Column() {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.groups),
                            color = colorResource(id = R.color.pink),
                            modifier = Modifier
                                .padding(start = 17.dp)
                                .align(Alignment.CenterStart),
                            fontSize = 17.sp
                        )
                        Text(
                            text = stringResource(R.string.contacts),
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_account),
                            contentDescription = stringResource(R.string.add_button),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 17.dp)
                                .clickable {
                                    navController.navigate(Screen.DetailsScreen.route)
                                }
                        )
                    }
                    TextField(
                        value = viewModel.contactListUiState.searchQuery,
                        onValueChange = {
                            viewModel.setSearchQuery(it)
                            viewModel.getSearchList()
                        },
                        placeholder = {
                            Text(
                                stringResource(R.string.search),
                                color = colorResource(id = R.color.steel)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 1.dp)
                            .height(50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = colorResource(id = R.color.transparent),
                            unfocusedIndicatorColor = colorResource(id = R.color.transparent),
                            backgroundColor = colorResource(id = R.color.lightGrey)
                        ),
                        shape = Shapes.large
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (viewModel.contactListUiState.success) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = viewModel.contactListUiState.refresh),
                    onRefresh = {
                        viewModel.contactListUiState =
                            viewModel.contactListUiState.copy(
                                isInitialLoad = true,
                                refresh = true,
                                refreshProgress = false,
                                paginationProgress = false
                            )
                    }) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.Center, state = listState
                        ) {
                            items(viewModel.contactListUiState.searchList, itemContent = {
                                Card(
                                    modifier = Modifier
                                        .height(72.dp)
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, bottom = 6.dp)
                                        .clickable {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "user", it
                                            )
                                            navController.navigate(Screen.DetailsScreen.route)
                                        }, elevation = 5.dp
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Image(
                                            painter = painterResource(id = R.drawable.img),
                                            contentDescription = stringResource(R.string.profile),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(
                                                    RoundedCornerShape(percent = 50)
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = it.first_name,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = it.last_name)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = stringResource(R.string.follow),
                                            modifier = Modifier.padding(end = 6.dp),
                                            style = TextStyle(color = colorResource(id = R.color.pink))
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            })
                            if (viewModel.contactListUiState.paginationProgress) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
//                        if (lastItem && viewModel.contactListUiState.searchState) {
//                            CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
//                        }
                        if (viewModel.contactListUiState.isLastPage) {
                            Toast.makeText(
                                context,
                                stringResource(R.string.no_more_data),
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.contactListUiState =
                                viewModel.contactListUiState.copy(
                                    isLastPage = false,
                                    paginationProgress = false
                                )
                        }
                    }
                }
            }
        }
        if (viewModel.contactListUiState.error) {
            components.AlertDialog(
                message = stringResource(id = R.string.server_error), onClick = {})
        }
    }
}
