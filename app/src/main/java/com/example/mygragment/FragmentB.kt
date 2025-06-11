package com.example.mygragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import com.example.mygragment.RecyclerView.EntityAdapter
import com.example.mygragment.Entity

class FragmentB : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val args: FragmentBArgs by navArgs() // Use navArgs instead of arguments

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val keypass = args.keypass
        Log.d("FragmentB", "KeyPass: $keypass")

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (keypass.isNotEmpty()) {
            fetchEntities(keypass)
        } else {
            Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
        }

        val logoutBtn: View = view.findViewById(R.id.btn_logout)
        logoutBtn.setOnClickListener {
            onLogoutClick()
        }

        val backBtn: View = view.findViewById(R.id.btn_back)
        backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun fetchEntities(keypass: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.dashboardApi.getEntities(keypass)
                }

                if (response.isSuccessful && response.body() != null) {
                    val dashboardResponse = response.body()
                    val entities = dashboardResponse?.entities ?: emptyList()

                    Log.d("FragmentB", "Fetched ${entities.size} entities")

                    recyclerView.adapter = EntityAdapter(entities) { selectedEntity ->
                        Log.d("FragmentB", "Entity clicked: ${selectedEntity.species}")
                        val action = FragmentBDirections.actionFragmentBToFragmentC(entity = selectedEntity)
                        findNavController().navigate(action)
                    }
                } else {
                    Log.e("FragmentB", "Failed to fetch entities: ${response.code()}")
                    Toast.makeText(requireContext(), "Failed to load entities", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Log.e("FragmentB", "HTTP error: ${e.code()}", e)
                Toast.makeText(requireContext(), "HTTP error: ${e.code()}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("FragmentB", "Error: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onLogoutClick() {
        // Use Navigation Component consistently
        findNavController().navigate(R.id.action_fragmentB_to_fragmentA)
    }
}