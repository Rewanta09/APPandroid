package com.example.mygragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class FragmentC : Fragment() {

    private val args: FragmentCArgs by navArgs()
    private lateinit var entity: Entity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entity = args.entity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val speciesText: TextView = view.findViewById(R.id.tv_species)
        val scientificNameText: TextView = view.findViewById(R.id.tv_scientific_name)
        val descriptionText: TextView = view.findViewById(R.id.tv_description)

        // Populate views with Entity data
        speciesText.text = entity.species
        scientificNameText.text = entity.scientificName
        descriptionText.text = entity.description

        // Back button
        view.findViewById<Button>(R.id.btn_back).setOnClickListener {
            findNavController().navigateUp()
        }

        // Logout button
        view.findViewById<Button>(R.id.btn_logout).setOnClickListener {
            findNavController().navigate(R.id.action_fragmentC_to_fragmentA)
        }
    }
}
