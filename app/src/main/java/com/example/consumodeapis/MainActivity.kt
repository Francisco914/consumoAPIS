package com.example.consumodeapis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumodeapis.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.temporal.TemporalQuery

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String){
        println(query)
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<DogsResponse> = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            println(call)
            val perritos: DogsResponse? = call.body();
            println(perritos)
            runOnUiThread(){
                if(call.isSuccessful){
                    println("exito")
                    //mostrar imagenes
                    val images:List<String> = perritos?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    println(dogImages)
                    adapter.notifyDataSetChanged()
                } else {
                    println("fallo")
                    //mostrar error
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByName(query.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}