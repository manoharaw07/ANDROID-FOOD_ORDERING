package activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {

    lateinit var forgotEmail: EditText
    lateinit var forgotMobile: EditText
    lateinit var btnnext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotEmail=findViewById(R.id.etForgotEmail)
        forgotMobile=findViewById(R.id.etForgotMobile)
        btnnext=findViewById(R.id.btNext)

        btnnext.setOnClickListener {


            /* intent.putExtra("User", user)
             intent.putExtra("Password", pass)*/
            val queue = Volley.newRequestQueue(this@ForgotPassword)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", forgotMobile.text.toString())
            jsonParams.put("email", forgotEmail.text.toString())

            val jsonRequest = object: JsonObjectRequest(
                Method.POST, url,
                jsonParams, Response.Listener {

                    println("Response is $it")

                    try{
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if(success){

                            val response = data.getBoolean("first_try")

                            if(response){
                                println("Response is $it")
                            }
                            /*sharedPreferences.edit().putString("user_id",
                                response.getString("user-id")).apply()

                            sharedPreferences.edit().putString("user_name",
                                response.getString("user_name")).apply()

                            sharedPreferences.edit().putString("user_mobile_number",
                                response.getString("mobile_number")).apply()

                            sharedPreferences.edit().putString("user_address",
                                response.getString("address")).apply()

                            sharedPreferences.edit().putString("user_email",
                                response.getString("email")).apply()*/
                            val intent = Intent(this@ForgotPassword, ForgetPasswordOTP::class.java)

                            intent.putExtra("mobile", etForgotMobile.text.toString())

                            startActivity(intent)
                            finish()
                        }else{
                            btnnext.visibility = View.VISIBLE

                            val errorMessage = data.getString("errorMessage")

                            Toast.makeText(this@ForgotPassword, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }catch(e: JSONException){
                        e.printStackTrace()
                    }

                }, Response.ErrorListener {
                    println("Error is $it")
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(jsonRequest)

        }


    }




override fun onPause() {
        super.onPause()
    finish()
    }
}

