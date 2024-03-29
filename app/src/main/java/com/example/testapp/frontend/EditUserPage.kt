package com.example.testapp.frontend

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.testapp.PreferencesManager
import com.example.testapp.data.RegisterData
import com.example.testapp.data.UpdateData
import com.example.testapp.respond.LoginRespond
import com.example.testapp.services.RegisterService
import com.example.testapp.services.UserServices
import com.example.testapp.ui.theme.TestAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserPage(navController: NavController, userId: String?, usernameParameter: String? ,context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }
    var username by remember { mutableStateOf(("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    if(usernameParameter != null){
        username = usernameParameter
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(value = username, onValueChange = { newText ->
                username = newText
            }, label = { Text("Username") })
            OutlinedTextField(value = password, onValueChange = { newText ->
                password = newText
            }, label = { Text("Password") })
            OutlinedTextField(value = email, onValueChange = { newText ->
                email = newText
            }, label = { Text("Email") })
            ElevatedButton(onClick = {
                var baseUrl = "http://10.0.2.2:1337/api/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UserServices::class.java)
                val call = retrofit.save(userId, UpdateData(username))
                call.enqueue(object : Callback<LoginRespond> {
                    override fun onResponse(
                        call: Call<LoginRespond>,
                        response: Response<LoginRespond>
                    ) {
                        print(response.code())
                        if (response.code() == 200) {
                            navController.navigate("Homepage")
                        } else if (response.code() == 400) {
                            print("error login")
                            var toast = Toast.makeText(
                                context,
                                "Username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginRespond>, t: Throwable) {
                        print(t.message)
                    }

                })
            }) {
                Text("Simpan")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TestAppTheme {

    }
}