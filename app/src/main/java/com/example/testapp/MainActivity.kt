package com.example.testapp

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testapp.data.LoginData
import com.example.testapp.frontend.AddUserPage
import com.example.testapp.frontend.Homepage
import com.example.testapp.respond.LoginRespond
import com.example.testapp.services.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences: SharedPreferences = LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val navController = rememberNavController()
            var startDestination: String
            var jwt = sharedPreferences.getString("jwt", "")
            if(jwt.equals("")){
                startDestination = "greeting"
            }else{
                startDestination = "homepage"
            }

            NavHost(navController, startDestination = startDestination) {
                composable(route = "greeting") {
                    Greeting(navController)
                }
                composable(route = "homepage"){
                    Homepage(navController)
                }
                composable(route = "newUser"){
                    AddUserPage(navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(navController: NavController,context: Context = LocalContext.current) {
    val preferencesManager = remember{PreferencesManager(context = context)}
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val baseurl = "http://10.0.2.2:1337/api/"
    var jwt by remember { mutableStateOf("") }

    jwt = preferencesManager.getData("jwt")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = username, onValueChange = { newText ->
            username = newText
        }, label = { Text("Username") })
        OutlinedTextField(value = password, onValueChange = { newText ->
            password = newText
        }, label = { Text("Password") })
        ElevatedButton(onClick = {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginService::class.java)
            val call = retrofit.getData(LoginData(username.text, password.text))
            call.enqueue(object : Callback<LoginRespond> {
                override fun onResponse(call: Call<LoginRespond>, response: Response<LoginRespond>) {
                    print(response.code())
                    if(response.code() == 200){
                        jwt = response.body()?.jwt!!
                        preferencesManager.saveData("jwt", jwt)
                        navController.navigate("homepage")
                    }else if(response.code() == 400){
                        print("error login")
                        var toast = Toast.makeText(context, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<LoginRespond>, t: Throwable) {
                    print(t.message)
                }
            })
        }) {
            Text(text = "Submit")
        }
        Text(text = jwt)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}