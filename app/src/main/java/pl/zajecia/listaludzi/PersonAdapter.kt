package pl.zajecia.listaludzi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(
    private var personList: List<Person>,
    private val onRemoveClick: (Person) -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDetails: TextView = view.findViewById(R.id.textViewDetails)
        val buttonRemove: Button = view.findViewById(R.id.buttonRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.textViewDetails.text = """
            Imię: ${person.firstName}
            Nazwisko: ${person.lastName}
            Wiek: ${person.age}
            Wzrost: ${person.height} cm
            Waga: ${person.weight} kg
        """.trimIndent()

        holder.buttonRemove.setOnClickListener {
            onRemoveClick(person)
        }
    }

    override fun getItemCount(): Int = personList.size

    fun updateList(newList: List<Person>) {
        personList = newList
        notifyDataSetChanged()
    }
}
