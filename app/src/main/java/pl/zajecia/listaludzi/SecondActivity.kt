package pl.zajecia.listaludzi

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SecondActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val personListKey = "person_list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Inicjalizacja SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Inicjalizacja RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pobierz listę osób
        val personList = getPersonList().toMutableList()

        // Ustaw adapter
        adapter = PersonAdapter(personList, ::removePerson)
        recyclerView.adapter = adapter
    }

    private fun getPersonList(): List<Person> {
        val json = sharedPreferences.getString(personListKey, null)
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<Person>>() {}.type
            Gson().fromJson(json, type)
        }
    }

    private fun savePersonList(personList: List<Person>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(personList)
        editor.putString(personListKey, json)
        editor.apply()
    }

    private fun removePerson(person: Person) {
        val personList = getPersonList().toMutableList()
        personList.remove(person)
        savePersonList(personList)
        adapter.updateList(personList)
        Toast.makeText(this, "Osoba została usunięta!", Toast.LENGTH_SHORT).show()
    }
}
