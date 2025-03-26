package com.example.memeapp.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.memeapp.Routes
import com.example.memeapp.models.Meme
import com.example.memeapp.R

@Composable
fun MainScreen(
    memesList: List<Meme> = emptyList(),
    navcontroller: NavHostController
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val textState = remember { mutableStateOf("") }

        SearchView(state = textState, placeholder = "Search here ...")
        val searchedText = textState.value
        if(memesList.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = Color(0xffffc107)
                )
            }
        }
        else{
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(
                    memesList.filter { it.name.contains(searchedText, ignoreCase = true) },
                    key = { it.id }) { item ->
                    MemeItem(
                        itemName = item.name,
                        itemUrl = item.url,
                        navController = navcontroller
                    )

                }
            }
        }
    }
}

@Composable
fun SearchView(
    state: MutableState<String>,
    placeholder: String,
    modifier: Modifier = Modifier
){
    TextField(
        value = state.value,
        onValueChange = {state.value = it},
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 10.dp, start = 10.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray,RoundedCornerShape(30.dp)),
        placeholder = {
            Text(text = placeholder)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xffffc107),
            unfocusedContainerColor = Color(0xffffc107)
        ),
        textStyle = TextStyle(color = Color.Black)
    )

}

@Composable
fun MemeItem(
    itemUrl: String,
    navController: NavHostController,
    itemName: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable{
                navController.navigate(route = Routes.DetailsScreen(itemName,itemUrl))
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffc107)
        )
    ){
        Column(
            modifier = modifier
                .padding(6.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(
                model = itemUrl,
                contentDescription = itemName,
                modifier = modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                placeholder = painterResource(id = R.drawable.baseline_image_search_24)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = itemName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxSize()
                    .basicMarquee()
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}