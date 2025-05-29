package com.example.edoctor.fragments

import android.content.Context
import android.content.Intent
import com.example.edoctor.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edoctor.activitys.AddSecondActivity
import com.example.edoctor.databinding.FragmentAddPortBinding
import com.example.edoctor.doctor.DoctorAdapter
import com.example.edoctor.doctor.DoctorViewModel
import kotlinx.coroutines.launch

class AddFragment : Fragment() {
    private var _binding: FragmentAddPortBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels()
    private lateinit var adapter: DoctorAdapter
    private lateinit var searchHistoryAdapter: ArrayAdapter<String>
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val SEARCH_DELAY = 2000L
    private val MAX_HISTORY_ITEMS = 10
    private var isHistoryVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        setupSearchHistory()
        setupHistoryToggle()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = DoctorAdapter(emptyList()) { doctor ->
            val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            val userLogin = prefs.getString("user_login", null)

            if (userLogin == null) {
                Toast.makeText(requireContext(), "Ошибка: логин пользователя не найден", Toast.LENGTH_SHORT).show()
                return@DoctorAdapter
            }

            val intent = Intent(requireContext(), AddSecondActivity::class.java).apply {
                val doctorName = "${doctor.firstName} ${doctor.secondName}"
                putExtra("doctorId", doctor.id)
                putExtra("userLogin", userLogin)
                putExtra("doctorName", doctorName)
                putExtra("doctorType", doctor.specialization)
            }
            startActivity(intent)
        }
        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDoctors.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    addToSearchHistory(it)
                    performSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                newText?.let {
                    if (it.isNotEmpty()) {
                        searchRunnable = Runnable { performSearch(it) }
                        searchHandler.postDelayed(searchRunnable!!, SEARCH_DELAY)
                    }
                }
                return true
            }
        })
    }

    private fun setupSearchHistory() {
        // Используем mutableListOf для создания изменяемого списка
        searchHistoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            getSearchHistory().toMutableList() // Преобразуем в изменяемый список
        )
        binding.lvHistory.adapter = searchHistoryAdapter
        binding.lvHistory.setOnItemClickListener { _, _, position, _ ->
            val query = searchHistoryAdapter.getItem(position)
            query?.let {
                binding.searchView.setQuery(it, false)
                addToSearchHistory(it)
                performSearch(it)
                toggleHistoryVisibility()
            }
        }

        binding.lvHistory.visibility = View.GONE
    }

    private fun clearSearchHistory() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        prefs.edit {
            remove("search_history")
        }

        // Обновляем адаптер с пустым списком
        searchHistoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            mutableListOf() // Пустой изменяемый список
        )
        binding.lvHistory.adapter = searchHistoryAdapter

        // Скрываем историю после очистки
        binding.lvHistory.visibility = View.GONE
        isHistoryVisible = false

        // Можно показать Toast с подтверждением
        Toast.makeText(requireContext(), "История очищена", Toast.LENGTH_SHORT).show()
    }
    private fun setupHistoryToggle() {
        binding.tvShowHistory.setOnClickListener {
            toggleHistoryVisibility()
        }


        binding.tvClearHistory.setOnClickListener {
            clearSearchHistory()
        }
    }

    private fun toggleHistoryVisibility() {
        isHistoryVisible = !isHistoryVisible
        if (isHistoryVisible) {
            showSearchHistory()
        } else {
            hideSearchHistory()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.doctorsState.collect { doctors ->
                adapter.updateData(doctors)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun performSearch(query: String) {
        viewModel.filterDoctors(query)
    }

    private fun showSearchHistory() {
        val history = getSearchHistory()
        if (history.isNotEmpty()) {
            searchHistoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                history.toMutableList()
            )
            binding.lvHistory.adapter = searchHistoryAdapter
            binding.lvHistory.visibility = View.VISIBLE
            // Показываем кнопку очистки только если есть история
            binding.tvClearHistory.visibility = View.VISIBLE
        } else {
            binding.lvHistory.visibility = View.GONE
            binding.tvClearHistory.visibility = View.GONE
        }
    }

    private fun hideSearchHistory() {
        binding.lvHistory.visibility = View.GONE
        binding.tvClearHistory.visibility = View.GONE
    }

    private fun getSearchHistory(): List<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        // Возвращаем копию списка, а не оригинальный неизменяемый список
        return ArrayList(prefs.getStringSet("search_history", mutableSetOf()) ?: mutableSetOf())
    }

    private fun addToSearchHistory(query: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val history = prefs.getStringSet("search_history", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        history.remove(query)
        val newHistory = mutableSetOf(query).apply { addAll(history) }

        if (newHistory.size > MAX_HISTORY_ITEMS) {
            newHistory.remove(newHistory.last())
        }

        prefs.edit { putStringSet("search_history", newHistory) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchRunnable?.let { searchHandler.removeCallbacks(it) }
        _binding = null
    }



}