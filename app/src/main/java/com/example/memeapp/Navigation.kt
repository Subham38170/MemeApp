package com.example.memeapp

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memeapp.models.Meme
import com.example.memeapp.screens.DetailsScreen
import com.example.memeapp.screens.MainScreen
import kotlinx.serialization.Serializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.toRoute
import coil.network.HttpException
import com.example.memeapp.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

sealed class Routes{
    @Serializable
    object MainScreen: Routes()
    @Serializable
    data class DetailsScreen(
        val name: String,
        val url: String
    ): Routes()
}

@Composable
fun NavGraph(
){
    val navController = rememberNavController()
    var memeList by remember {
        mutableStateOf(listOf<Meme>())
    }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current.applicationContext
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO){
           val response = try {
               RetrofitInstance.api.getMemesList()
           }catch (e: IOException){
               Toast.makeText(context,"App Error : ${e.toString()}", Toast.LENGTH_SHORT).show()
               return@launch
           }catch (e: HttpException){
               Toast.makeText(context,"HTTP Error : ${e.toString()}", Toast.LENGTH_SHORT).show()
               return@launch
           }
            if(response.isSuccessful && response.body()!=null){
                withContext(Dispatchers.Main){
                    memeList = response.body()!!.data.memes
                }
            }
            else{
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = Routes.MainScreen
    ){
        composable<Routes.MainScreen>{
            MainScreen(
                memeList,
                navcontroller = navController
            )
        }
        composable<Routes.DetailsScreen>{
            val data = it.toRoute<Routes.DetailsScreen>()
            DetailsScreen(
                name = data.name,
                url = data.url
            )
        }
    }
}