package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.housify.Adapter.ExpensesAndTasksFragmentDivisionAdapter
import com.example.housify.R
import com.example.housify.databinding.FragmentChatBinding
import com.example.housify.databinding.FragmentExpensesTasksDivisionBinding
import com.google.android.material.tabs.TabLayout

class expensesAndTasksDivisionFragment : Fragment(R.layout.fragment_expenses_tasks_division) {
    private lateinit var binding: FragmentExpensesTasksDivisionBinding
    private lateinit var expensesStats: expensesFragment
    private lateinit var personalStats: personalStatsFragment
    private lateinit var roommateGroupStats : roommateFragment
    private lateinit var tasksStats: tasksFragment
    private lateinit var expensesAndTasksFragmentDivisionAdapter: ExpensesAndTasksFragmentDivisionAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpensesTasksDivisionBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expensesStats = expensesFragment()
        personalStats = personalStatsFragment()
        roommateGroupStats = roommateFragment()
        tasksStats = tasksFragment()
        setupViewPager(binding.viewPagerExpensesAndTasks)
        binding.tabLayoutExpensesAndTasks.setupWithViewPager(binding.viewPagerExpensesAndTasks)
        binding.tabLayoutExpensesAndTasks.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }
    private fun setupViewPager(viewPager: ViewPager) {

        expensesAndTasksFragmentDivisionAdapter = ExpensesAndTasksFragmentDivisionAdapter(childFragmentManager)
        expensesAndTasksFragmentDivisionAdapter.addFragment(personalStats, "Home")
        expensesAndTasksFragmentDivisionAdapter.addFragment(roommateGroupStats, "Roommates")
        expensesAndTasksFragmentDivisionAdapter.addFragment(expensesStats, "Expenses")
        expensesAndTasksFragmentDivisionAdapter.addFragment(tasksStats, "Tasks")
        viewPager.adapter = expensesAndTasksFragmentDivisionAdapter
    }

}