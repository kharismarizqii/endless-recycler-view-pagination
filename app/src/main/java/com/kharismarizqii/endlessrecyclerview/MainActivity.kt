package com.kharismarizqii.endlessrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kharismarizqii.endlessrecyclerview.model.UsersResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: UsersAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var totalPage: Int = 1

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        swipeRefresh.setOnRefreshListener(this)
        setupRecyclerView()
        getUsers(false)

        rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                Log.d("MainActivity", "onScrollChange: ")
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total  = adapter.itemCount
                if (!isLoading && page < totalPage){
                    if (visibleItemCount + pastVisibleItem>= total){
                        page++
                        getUsers(false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getUsers(isOnRefresh: Boolean) {
        isLoading = true
        if (!isOnRefresh) progressBar.visibility = View.VISIBLE
        Handler().postDelayed({
            val parameters = HashMap<String, String>()
            parameters["page"] = page.toString()
            Log.d("PAGE", "$page")
            RetrofitClient.instance.getUsers(parameters).enqueue(object : Callback<UsersResponse>{
                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    isLoading = false
                    swipeRefresh.isRefreshing = false
                }

                override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                    totalPage = response.body()?.total_pages!!
                    Log.d("PAGE", "totalPage: $totalPage")
                    val listResponse = response.body()?.data
                    if (listResponse != null){
                        Log.d("PAGE", "listResponse != null")
                        adapter.addList(listResponse)
                    }
                    progressBar.visibility = View.GONE
                    isLoading = false
                    swipeRefresh.isRefreshing = false
                }
            })
        }, 4000)

    }

    private fun setupRecyclerView() {
        rvUsers.setHasFixedSize(true)
        rvUsers.layoutManager = layoutManager
        adapter = UsersAdapter()
        rvUsers.adapter = adapter
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getUsers(true)
    }
}
