import android.content.Context
import android.content.SharedPreferences

class PreferncesManager (context: Context){
    private val sharedPreferences:SharedPreferences = context.getSharedPreferences("auth",
        android.content.Context.MODE_PRIVATE)

    fun saveData(key: String,value: String){
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun getData(key: String){
        sharedPreferences.getString()
    }

}