package fragments

import activity.HomeActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.manohar.R
import org.json.JSONException
import org.json.JSONObject

class ProfileFragment : Fragment() {
    lateinit var Name:TextView
    lateinit var Mobile:TextView
    lateinit var Address:TextView
    lateinit var Email:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View= inflater.inflate(R.layout.fragment_profile,container,false)
        Name= view.findViewById(R.id.tvName)
        Mobile= view.findViewById(R.id.tvPhone)
        Address= view.findViewById(R.id.etAdd)
        Email= view.findViewById(R.id.tvEmail)
        val name=arguments?.getString("name")
        val mobile=arguments?.getString("mobile")
        val address=arguments?.getString("address")
        val email=arguments?.getString("email")

        Name.setText(name)
        Mobile.setText(mobile)
        Address.setText(address)
        Email.setText(email)
        // Inflate the layout for this fragment
        return view

    }


}
