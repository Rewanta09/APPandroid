package com.example.mygragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class FragmentA : Fragment() {

    private lateinit var editFirstName: EditText
    private lateinit var editStudentId: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editFirstName = view.findViewById(R.id.edit_first_name)
        editStudentId = view.findViewById(R.id.edit_student_id)
        btnLogin = view.findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            val username = editFirstName.text.toString()
            val password = editStudentId.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.Main).launch {
                performLogin(username, password)
            }
        }
    }

    private suspend fun performLogin(username: String, password: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.authApi.authenticate(Credentials(username, password))
            }

            if (response.isSuccessful && response.body()?.keypass != null) {
                navigateToDashboard(response.body()?.keypass ?: "animals")
            } else {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }

        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
        } catch (e: HttpException) {
            Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard(keypass: String) {
        // Use Navigation Component instead of Fragment Transactions
        val action = FragmentADirections.actionFragmentAToFragmentB(keypass)
        findNavController().navigate(action)
    }
}