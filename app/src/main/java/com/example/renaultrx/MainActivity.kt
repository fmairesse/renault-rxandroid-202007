package com.example.renaultrx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val LOG_TAG = MainActivity::class.simpleName
    val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Créer l'adapter permettant de remplir la liste
        val adapter = CommuneItemRecyclerViewAdapter()

        // Appliquer l'adapter à la liste
        listCommunes.adapter = adapter

        val communesService = CommunesService()
        // Observer les changements du champ text
        subscriptions.add(editTextTextCommune.afterTextChangeEvents()
            // transformer les event de afterTextChangeEvents en String
            .map { e -> e.editable.toString() }
            // associer une recherche à chaque terme de recherche émis
            .flatMap { term -> communesService.search(term) }
            // Retrofit tourne sur un thread à part
            // Pour mettre à jour l'UI, il faut le faire sur le thread UI
            .observeOn(AndroidSchedulers.mainThread())
            // Mettre à jour l'UI avec la liste de communes reçues
            .subscribe { result ->
                adapter.communes = result
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
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