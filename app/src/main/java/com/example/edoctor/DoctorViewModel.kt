import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import androidx.lifecycle.viewModelScope
import com.example.edoctor.Doctor
import com.example.edoctor.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorViewModel : ViewModel() {
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctorsState = _doctors.asStateFlow()

    init {
        fetchDoctors()
    }

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                _doctors.value = RetrofitClient.apiService.getDoctors()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun filterDoctors(query: String) {
        viewModelScope.launch {
            val allDoctors = RetrofitClient.apiService.getDoctors()
            _doctors.value = if (query.isEmpty()) {
                allDoctors
            } else {
                allDoctors.filter { doctor ->
                    doctor.firstName.contains(query, ignoreCase = true) ||
                            doctor.lastName.contains(query, ignoreCase = true)
                }
            }
        }
    }
}

