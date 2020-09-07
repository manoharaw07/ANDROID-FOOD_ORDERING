package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import fragments.HomeFragment
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var btLogin: Button
    lateinit var tvForgot: TextView
    lateinit var tvRegister: TextView
    lateinit var etUser: EditText
    lateinit var etPassword: EditText
    lateinit var confirmPassword: EditText

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if(isLoggedIn)
        {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        btLogin=findViewById(R.id.btLogin)

        tvForgot=findViewById(R.id.tvForgot)
        tvRegister=findViewById(R.id.tvSignUp)
        etUser=findViewById(R.id.etUser)
        etPassword=findViewById(R.id.etPassword)
        btLogin.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPassword.text.toString()


            if(user.isEmpty())
            {
                etUser.setError("please enter phone number")
                etUser.requestFocus()
            }
            else if (pass.isEmpty())
            {
                etPassword.setError("password is empty")
                etPassword.requestFocus()
            }
            else {

                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result"
                val jsonParam = JSONObject()

                jsonParam.put("mobile_number", user)
                jsonParam.put("password", pass)


                val jsonRequest = object : JsonObjectRequest(
                    Method.POST, url,
                    jsonParam, com.android.volley.Response.Listener {

                        println("Response is $it")

                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")

                            if (success) {

                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                val response = data.getJSONObject("data")
                                val userId: String = response.getString("user_id")
                                val name = response.getString("name")
                                val mobile = response.getString("mobile_number")
                                val address = response.getString("address")
                                val email = response.getString("email")

                                sharedPreferences.edit().putString("User_id", userId).apply()

                                sharedPreferences.edit().putString("Name", name).apply()

                                sharedPreferences.edit().putString("Mobile", mobile).apply()

                                sharedPreferences.edit().putString("Address", address).apply()

                                sharedPreferences.edit().putString("Email", email).apply()
                                savePrefrences()

                                startActivity(intent)
                                finish()

                            } else {


                                val errorMessage = data.getString("errorMessage")

                                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }, com.android.volley.Response.ErrorListener {
                        println("Error is $it")
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "534a6f3dd3fb32"
                        return headers
                    }
                }

                queue.add(jsonRequest)
            }


        }

        tvForgot.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassword::class.java)
            startActivity(intent)
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun savePrefrences() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()


    }



    override fun onPause() {
        super.onPause()
        finish()
    }
}


