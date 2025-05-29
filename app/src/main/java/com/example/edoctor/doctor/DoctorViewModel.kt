package com.example.edoctor.doctor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edoctor.API.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorViewModel : ViewModel() {
    private val _allDoctors = mutableListOf<Doctor>() // хранит оригинал
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    private val _isLoading = MutableStateFlow(false)

    val doctorsState = _doctors.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchDoctors()
    }

    fun fetchDoctors() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.doctorApi.getDoctors()
                _allDoctors.clear()
                _allDoctors.addAll(response.map {
                    Doctor(
                        id = it.id,
                        firstName = it.firstName,
                        secondName = it.secondName,
                        thirdName = it.thirdName,
                        specialization = it.specialization
                    )
                })
                _doctors.value = _allDoctors
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterDoctors(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _doctors.value = if (query.isEmpty()) {
                    _allDoctors
                } else {
                    _allDoctors.filter { doctor ->
                        doctor.firstName.contains(query, ignoreCase = true) ||
                                doctor.secondName.contains(query, ignoreCase = true) ||
                                doctor.specialization.contains(query, ignoreCase = true)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
