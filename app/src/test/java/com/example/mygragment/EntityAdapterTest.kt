package com.example.mygragment.RecyclerView

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mygragment.Entity
import com.example.mygragment.databinding.ItemEntityBinding
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class EntityAdapterTest {

    private lateinit var adapter: EntityAdapter

    @Suppress("UNCHECKED_CAST")
    private val mockClickListener = mock(Function1::class.java) as (Entity) -> Unit

    private val testEntities = listOf(
        Entity("African Elephant", "Loxodonta africana", "Savanna", "Herbivore", "Vulnerable", 60, "The largest land animal..."),
        Entity("Giant Panda", "Ailuropoda melanoleuca", "Temperate broadleaf and mixed forests", "Herbivore", "Vulnerable", 20, "A bear native to China..."),
        Entity("Blue Whale", "Balaenoptera musculus", "Ocean", "Carnivore", "Endangered", 80, "The largest animal known..."),
        Entity("Komodo Dragon", "Varanus komodoensis", "Tropical savanna", "Carnivore", "Vulnerable", 30, "The largest living species of lizard..."),
        Entity("Emperor Penguin", "Aptenodytes forsteri", "Antarctic coast", "Carnivore", "Near Threatened", 20, "The tallest and heaviest penguin..."),
        Entity("Red Panda", "Ailurus fulgens", "Temperate forests", "Omnivore", "Endangered", 8, "Small mammal with red fur..."),
        Entity("Platypus", "Ornithorhynchus anatinus", "Freshwater", "Carnivore", "Near Threatened", 17, "Unique egg-laying mammal...")
    )

    @Before
    fun setUp() {
        adapter = EntityAdapter(testEntities, mockClickListener)
    }

    @Test
    fun `item count matches test data size`() {
        assertEquals(7, adapter.itemCount)
    }

    @Test
    fun `binds first entity correctly`() {
        val binding = getBindingAtPosition(0)
        assertEquals("African Elephant", binding.tvSpecies.text)
        assertEquals("Savanna", binding.tvHabitat.text)
        assertEquals("Herbivore", binding.tvDiet.text)
        assertEquals("Vulnerable", binding.tvConservationStatus.text)
        assertEquals("Avg Lifespan: 60 years", binding.tvAverageLifespan.text)
    }

    @Test
    fun `binds middle entity correctly`() {
        val binding = getBindingAtPosition(3)
        assertEquals("Komodo Dragon", binding.tvSpecies.text)
        assertEquals("Tropical savanna", binding.tvHabitat.text)
        assertEquals("Carnivore", binding.tvDiet.text)
        assertEquals("Vulnerable", binding.tvConservationStatus.text)
        assertEquals("Avg Lifespan: 30 years", binding.tvAverageLifespan.text)
    }

    @Test
    fun `binds last entity correctly`() {
        val binding = getBindingAtPosition(6)
        assertEquals("Platypus", binding.tvSpecies.text)
        assertEquals("Freshwater", binding.tvHabitat.text)
        assertEquals("Carnivore", binding.tvDiet.text)
        assertEquals("Near Threatened", binding.tvConservationStatus.text)
        assertEquals("Avg Lifespan: 17 years", binding.tvAverageLifespan.text)
    }

    @Test
    fun `click triggers callback with correct entity`() {
        val position = 4 // Emperor Penguin
        val binding = getBindingAtPosition(position)
        binding.root.performClick()
        verify(mockClickListener).invoke(testEntities[position])
    }

    @Test
    fun `viewholder displays correct lifespan format`() {
        val binding = getBindingAtPosition(1) // Giant Panda
        assertEquals("Avg Lifespan: 20 years", binding.tvAverageLifespan.text)
    }

    private fun getBindingAtPosition(position: Int): ItemEntityBinding {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val inflater = LayoutInflater.from(context)
        val binding = ItemEntityBinding.inflate(inflater)
        val viewHolder = EntityAdapter.EntityViewHolder(binding)
        adapter.onBindViewHolder(viewHolder, position)
        return binding
    }
}
