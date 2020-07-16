package com.example.renaultrx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val listView: RecyclerView = findViewById(R.id.listCommunes)
        // Créer des communes en dur
        val communes= listOf(
            CommuneModel("Toulouse", "31555"),
            CommuneModel("Lille", "59111")
        )

        val adapter = CommuneItemRecyclerViewAdapter()
        listView.adapter = adapter

        //...
        adapter.communes = listOf(
            CommuneModel("Toulouse", "31555"),
            CommuneModel("Lille", "59111")
        )
    }

    class CommuneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomView: TextView = this.itemView.findViewById(R.id.textView_communeNom)
        val nbBornesView: TextView = this.itemView.findViewById(R.id.textView_nbBornes)
    }

    class CommuneItemRecyclerViewAdapter :
        RecyclerView.Adapter<CommuneViewHolder>() {
        var communes: List<CommuneModel> = listOf()
            set(value) {
                val prevCommunes = this.communes
                field = listOf()
                this.notifyItemRangeRemoved(0, prevCommunes.size)
                field = value
                this.notifyItemRangeInserted(0, value.size)
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommuneViewHolder {
            return CommuneViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.commune_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return this.communes.size
        }

        override fun onBindViewHolder(holder: CommuneViewHolder, position: Int) {
            val commune = this.communes.get(position)
            holder.nomView.text = commune.nom
            holder.nbBornesView.text = commune.nbBornes.toString()
        }
    }
}