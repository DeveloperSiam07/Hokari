package com.hokari.customer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.hokari.customer.R
import com.hokari.customer.adapter.DashboardListAdapter
import com.hokari.customer.database.AppController
import com.hokari.customer.databinding.FragmentDashboardBinding
import com.hokari.customer.model.Product
import com.hokari.customer.ui.activities.CartListActivity
import com.hokari.customer.ui.activities.SettingsActivity

class DashboardFragment : UiComponentsFragment() {

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false) // Initializing the binding
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getDashboardList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.cartMenuButton -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardList(dashboardList: ArrayList<Product>) {
        hideProgressBar()
        if (dashboardList.size > 0) {
            binding.tvNoDashboardItemsFound.visibility = View.GONE
            binding.rvDashboardItems.visibility = View.VISIBLE
            binding.rvDashboardItems.layoutManager = GridLayoutManager(activity, 2)
            binding.rvDashboardItems.setHasFixedSize(true)
            val adapterDashboard = DashboardListAdapter(requireActivity(), dashboardList)
            binding.rvDashboardItems.adapter = adapterDashboard
        } else {
            binding.rvDashboardItems.visibility = View.GONE
            binding.tvNoDashboardItemsFound.visibility = View.VISIBLE
        }
    }

    private fun getDashboardList() {
        showProgressBar(getString(R.string.please_wait))
        AppController().getItemsForDashboard(this@DashboardFragment)
    }
}