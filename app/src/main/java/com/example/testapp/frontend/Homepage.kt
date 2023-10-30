package com.example.testapp.frontend

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testapp.respond.UserGet
import com.example.testapp.services.UserServices
import com.example.testapp.ui.theme.TestAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController, context: Context = LocalContext.current) {
    //var listUser: List<UserRespon> = remember
    val listUser = remember { mutableListOf<UserGet>() }
    //var listUser: List<UserRespon> by remember { mutableStateOf(List<UserRespon>()) }
    var baseUrl = "http://10.0.2.2:1337/api/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserServices::class.java)
    val call = retrofit.getData()
    call.enqueue(object : Callback<List<UserGet>> {
        override fun onResponse(
            call: Call<List<UserGet>>,
            response: Response<List<UserGet>>
        ) {
            if (response.code() == 200) {
                listUser.clear()
                response.body()?.forEach{ userRespon ->
                    listUser.add(userRespon)
                }
            } else if (response.code() == 400) {
                print("error login")
                var toast = Toast.makeText(
                    context,
                    "Username atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<List<UserGet>>, t: Throwable) {
            print(t.message)
        }

    })
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("createuserpage")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Homepage") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },) {
            innerPadding ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        ) {
            LazyColumn{
                listUser.forEach { user ->
                    item {
                        Row (modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = user.username)
                            ElevatedButton(onClick = {
                                val retrofit = Retrofit.Builder()
                                    .baseUrl(baseUrl)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(UserServices::class.java)
                                val call = retrofit.delete(user.id)
                                call.enqueue(object : Callback<UserGet> {
                                    override fun onResponse(call: Call<UserGet>, response: Response<UserGet>) {
                                        print(response.code())
                                        if(response.code() == 200){
                                        }else if(response.code() == 400){
                                            print("error login")
                                            var toast = Toast.makeText(context, "Username atau password salah", Toast.LENGTH_SHORT).show()

                                        }
                                    }

                                    override fun onFailure(call: Call<UserGet>, t: Throwable) {
                                        print(t.message)
                                    }

                                })
                            }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }

        }
    }
}
