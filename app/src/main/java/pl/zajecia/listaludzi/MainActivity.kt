package pl.zajecia.listaludzi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextHeight: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonGoToScreen2: Button

    private lateinit var sharedPreferences: SharedPreferences
    private val personListKey = "person_list"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicjalizacja SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        // Inicjalizacja widoków
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextHeight = findViewById(R.id.editTextHeight)
        editTextWeight = findViewById(R.id.editTextWeight)
        buttonSave = findViewById(R.id.buttonSave)
        buttonGoToScreen2 = findViewById(R.id.buttonGoToScreen2)

        // Obsługa przycisku Zapisz
        buttonSave.setOnClickListener {
            savePerson()
        }

        // Obsługa przycisku Ekran2
        buttonGoToScreen2.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    private fun savePerson() {
        val firstName = editTextFirstName.text.toString()
        val lastName = editTextLastName.text.toString()
        val age = editTextAge.text.toString().toIntOrNull()
        val height = editTextHeight.text.toString().toDoubleOrNull()
        val weight = editTextWeight.text.toString().toDoubleOrNull()

        if (firstName.isBlank() || lastName.isBlank() || age == null || height == null || weight == null) {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola poprawnie!", Toast.LENGTH_SHORT).show()
            return
        }

        val person = Person(firstName, lastName, age, height, weight)
        val personList = getPersonList().toMutableList()
        personList.add(person)
        savePersonList(personList)

        Toast.makeText(this, "Dane zapisane!", Toast.LENGTH_SHORT).show()
        clearFields()
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

    private fun clearFields() {
        editTextFirstName.text.clear()
        editTextLastName.text.clear()
        editTextAge.text.clear()
        editTextHeight.text.clear()
        editTextWeight.text.clear()
    }
}
