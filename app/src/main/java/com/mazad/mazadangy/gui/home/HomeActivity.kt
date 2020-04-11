package com.mazad.mazadangy.gui.home

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.mazad.mazadangy.R
import com.mazad.mazadangy.adapter.AdsAdapter
import com.mazad.mazadangy.gui.AddAds.AddAdsActivity
import com.mazad.mazadangy.gui.AddAds.AddPostActivity
import com.mazad.mazadangy.gui.favorite.FavoritePostActivity
import com.mazad.mazadangy.model.AdsModel
import com.mazad.mazadangy.utels.StaticMethod
import com.mazad.mazadangy.utels.ToastUtel
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), HomeInterface,
    NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var homePresenter: HomePresenter
     lateinit var adsAdapter: AdsAdapter
    lateinit var intent_obj: Intent
    lateinit var catCheck: String
    private var progressDialog: ProgressDialog? = null
    var navDrawer: NavigationView? = null
    var listAds:ArrayList<AdsModel>?=null
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navDrawer?.setNavigationItemSelectedListener(this)

        setupNavButton()
        nav_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                drawer_layout.openDrawer(GravityCompat.END)
            }
        })
        PostsRecycler.layoutManager = LinearLayoutManager(this)
        homePresenter = HomePresenter(this)
        checkCat()
        refresh()

    }

    private fun refresh() {
        itemsswipetorefresh.setOnRefreshListener {
            checkCat()
        }
    }

   

    private fun setupNavButton() {
        val bottomNavigationView =
            findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.getItemId()) {
                    R.id.upload -> {

                        if (catCheck.equals("posts")) {
                            intent_obj = Intent(this@HomeActivity, AddPostActivity::class.java)
                            startActivity(intent_obj)

                        } else {
                            intent_obj = Intent(this@HomeActivity, AddAdsActivity::class.java)
                            startActivity(intent_obj)

                        }
                    }
                    R.id.home -> {
                        finish()
                    }
                    R.id.favorit -> {

                        if (catCheck.equals("posts")) {
                            intent_obj = Intent(this@HomeActivity, FavoritePostActivity::class.java)
                            intent_obj.putExtra("fromActivity", "posts")
                            startActivity(intent_obj)


                        } else {
                            intent_obj = Intent(this@HomeActivity, FavoritePostActivity::class.java)
                            intent_obj.putExtra("fromActivity", "money_post")
                            startActivity(intent_obj)


                        }


                    }
                }
                return true
            }

        })
    }


    private fun checkCat() {
        progressDialog = StaticMethod.createProgressDialog(this)
        progressDialog?.show()
        listAds?.clear()
        var checkIntent: Intent = getIntent()
        catCheck = checkIntent.getStringExtra("category")
        homePresenter.getDataPosts(catCheck, this)

    }

    override fun noConnection() {
        progressDialog?.cancel()
        ToastUtel.errorToast(this, "تحقق من وجود الانترنت")
        itemsswipetorefresh.isRefreshing = false

    }


    override fun sucuss(adsList: ArrayList<AdsModel>) {
        listAds=adsList
        adsAdapter = AdsAdapter(this, listAds!!, catCheck)
        PostsRecycler.adapter = adsAdapter
        progressDialog?.cancel()
        itemsswipetorefresh.isRefreshing = false
        adsAdapter.notifyDataSetChanged()
        //  ToastUtel.errorToast(this,"data is done")


    }

    override fun onCancelled() {
        progressDialog?.cancel()
        itemsswipetorefresh.isRefreshing = false
        ToastUtel.errorToast(this, "cancelled")

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.getItemId()) {
            R.id.pesonProfile -> {

            }
            R.id.app_info -> {

            }
            R.id.nav_AboutUs -> {

            }
            R.id.contact_menu -> {

            }
            R.id.logout -> {

            }
        }


        return true

    }


}
