package com.example.universityapp.messages

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.example.universityapp.databinding.FragmentMessagesBinding
import org.json.JSONObject

class MessageFragment : Fragment() {

    private val TAG = MessageFragment::class.java.toString()
    private lateinit var binding: FragmentMessagesBinding

    companion object {
        fun newInstance() = MessageFragment()
    }

    private lateinit var viewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        //getStudentinfo()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        // TODO: Use the ViewModel
        getStudentinfo()
    }

    /*private fun authenticateWithOAuth(username: String, password: String) {
        val url = "https://papi.mrsu.ru/v1/User"
        val clientId = "8"
        val clientSecret = "qweasd"
        val stringRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                handleOAuthResponse(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["grant_type"] = "password"
                        params["client_id"] = clientId
                        params["client_secret"] = clientSecret
                        params["username"] = username
                        params["password"] = password
                        return params
                    }

                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer NqaKmwpOVEilwa-iumkJ9Zo3rNIhriSNy-H1Z7fCgDLna9G_poVF2wgXVI6fUFKnjl2LqwKoRPhPtqyjBp5EFN6rEPszFS8PTuJo7b_ePaQXRH9l064MiSrszHXrE5sjHprKm9fqT5z_yckiuTtH-EFL58DMz9wtzvQpeKmn9j8y3ckPi7kuEP4vLb5yeF2Tg6DNJZFbZ6EF-OKbCtbOavusRdJ7wffF_20-QBJMvfkqwY-RegcR5zOEIsCZLrcYgNss-TWV5h_oYZMytXxE9ORQllyDpMRbiKhwOakYwrkK65uR0fMaDgOp4bfUOt8XTG7Jt_bV0t3FznfAqL3VcRVvmr0e-iaCSKB0KriaV6JLViPbODrtzxpSey6JEI5TW7l73N32P_zmhd32Yb__8yXe3fAvMZpXDt4ngnKx0HXLlWEMDT_fjrpjReY2gS6986qYa2-w8G_ATq8r6yiJU7EQ-hRPja9yBak_gvFVXPk6UCaRW8pbZVv9MVvyN5XwaCSZBR7aoOfvIy8_-K4vN4rc_Ox6bhD--6rPGcuRKZTkrOqv355hM9Y4-abj7zBFOmk3rg"
                        // Add any required headers
                        return headers
                    }
                }
    }

    private fun handleOAuthResponse(response: String) {
        try {
            val jsonResponse = JSONObject(response)
            val accessToken = jsonResponse.getString("access_token")
            Log.d(TAG, "Access Token: $accessToken")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
    private fun getStudentinfo() {
        lateinit var url: String
        try {
            url = "https://papi.mrsu.ru/v1/User"
        } catch (e: Exception) {
            print(e)
        }

        val queue = Volley.newRequestQueue(requireContext())

        val stringRequest = object : StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val obj = JSONObject(response)
                binding.textMessages.text = "FIO: "+obj.getString("FIO")
            },
            {
                binding.textMessages.text = "Invalid, please try again"
                Log.d(TAG, "error")
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer NqaKmwpOVEilwa-iumkJ9Zo3rNIhriSNy-H1Z7fCgDLna9G_poVF2wgXVI6fUFKnjl2LqwKoRPhPtqyjBp5EFN6rEPszFS8PTuJo7b_ePaQXRH9l064MiSrszHXrE5sjHprKm9fqT5z_yckiuTtH-EFL58DMz9wtzvQpeKmn9j8y3ckPi7kuEP4vLb5yeF2Tg6DNJZFbZ6EF-OKbCtbOavusRdJ7wffF_20-QBJMvfkqwY-RegcR5zOEIsCZLrcYgNss-TWV5h_oYZMytXxE9ORQllyDpMRbiKhwOakYwrkK65uR0fMaDgOp4bfUOt8XTG7Jt_bV0t3FznfAqL3VcRVvmr0e-iaCSKB0KriaV6JLViPbODrtzxpSey6JEI5TW7l73N32P_zmhd32Yb__8yXe3fAvMZpXDt4ngnKx0HXLlWEMDT_fjrpjReY2gS6986qYa2-w8G_ATq8r6yiJU7EQ-hRPja9yBak_gvFVXPk6UCaRW8pbZVv9MVvyN5XwaCSZBR7aoOfvIy8_-K4vN4rc_Ox6bhD--6rPGcuRKZTkrOqv355hM9Y4-abj7zBFOmk3rg"
                return headers
            }
        }
        queue.add(stringRequest)
    }
}