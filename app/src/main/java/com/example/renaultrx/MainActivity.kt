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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

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
        val bornesServices = BornesService()
        // Observer les changements du champ text
        subscriptions.add(editTextTextCommune.afterTextChangeEvents()
            // émettre seulement 300 ms après la dernière saisie de l'utilisateur
            .debounce(300, TimeUnit.MILLISECONDS)
            // transformer les event de afterTextChangeEvents en String
            .map { e -> e.editable.toString() }
            // Ne laisser passer que les mots de + de 3 caractères
            .filter { text -> text.length > 3 }
            // associer une recherche à chaque terme de recherche émis
            //
            .switchMap { term ->
                communesService
                    .search(term)
                    // renvoyer une liste vide en cas d'erreur réseau
                    .onErrorReturnItem(listOf<CommuneModel>(
                            CommuneModel("Mufflins", "1223")))
                    /// renvoyer une liste qui ne dépasse pas 10 éléments
                    // Méthode simple
//                    .map{l -> l.subList(0, Math.min(l.size, 10))}
                    // Méthode compliquée ...
                    .flatMap { communes -> Observable.fromIterable(communes) }
                    .take(3)
                    .flatMap({ commune ->
                        bornesServices.search(commune.code)
                            .onErrorReturnItem(BornesResponse(-2))
                            .map{ bornesResponse ->
                                CommuneModel(commune.nom, commune.code, bornesResponse.nhits)}
                    }, false, 2)
                    .toList().toObservable()
            }
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