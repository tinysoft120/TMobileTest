package com.tinysoft.tmobiletest.ui.homelist

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.tinysoft.tmobiletest.App
import com.tinysoft.tmobiletest.R
import com.tinysoft.tmobiletest.adater.ItemListAdapter
import com.tinysoft.tmobiletest.databinding.FragmentHomeListBinding
import com.tinysoft.tmobiletest.dialog.CustomProgressDialog
import com.tinysoft.tmobiletest.ui.AbsMainFragment
import com.tinysoft.tmobiletest.ui.components.LoadingState
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeListFragment : AbsMainFragment(R.layout.fragment_home_list) {

    companion object {
        private const val TAG = "HomeListFragment"
        private const val KEY_STATE = "state"

        fun newInstance() = HomeListFragment()
    }

    private var _binding: FragmentHomeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<HomeListViewModel>()
    private val progressDialog: CustomProgressDialog by lazy { CustomProgressDialog() }

    private lateinit var listAdapter: ItemListAdapter

    private val isLandscape get() =
        App.getContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeListBinding.bind(view)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.title = null

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setupRecyclerView()
        setupToolbar()

        if (savedInstanceState != null) {
            val state: HomeListViewState? = savedInstanceState.getParcelable(KEY_STATE)
            state?.let {
                viewModel.viewState = it
                render(it)
            }
        } else {
            viewModel.clearListResult()
        }

        viewModel.apiResults.observe(viewLifecycleOwner) {
            Log.d(
                TAG, "search - " +
                    " result_count = ${it.items.size}," +
                    " total_count=${it.totalCount}")
            render(it)
        }

        viewModel.loadingState.observe(viewLifecycleOwner) {
            Log.d(TAG, "loading state = $it")
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, viewModel.viewState)
    }

    private fun render(state: LoadingState) {
        when (state) {
            LoadingState.Idle -> dismissLoading()
            LoadingState.Loading -> showLoading("Loading...",true)
            is LoadingState.LoadFailure -> {
                dismissLoading()
                Toast.makeText(requireContext(), state.errorMsg, Toast.LENGTH_SHORT).show()
                binding.recyclerView.isVisible = false
                binding.empty.isVisible = true
            }
        }
    }

    private fun render(viewState: HomeListViewState) {
        with(binding) {
            listAdapter.swapDataSet(viewState.items)
            if (viewState.offset == 0) {
                recyclerView.smoothScrollToPosition(0)
            }
            recyclerView.isVisible = viewState.items.isNotEmpty()
            empty.isVisible = viewState.items.isEmpty()
        }
    }

    private fun setupToolbar() {
        // TODO: set order and grid feature up
    }

    private fun setupRecyclerView() {
        listAdapter = ItemListAdapter(requireActivity())
        listAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()

                val height = dipToPix(52)
                binding.recyclerView.setPadding(0, 0, 0, height)
            }
        })

        val count = columnCount()
        val lm = if (count == 1) {
            LinearLayoutManager(requireContext())
        } else {
            StaggeredGridLayoutManager(count, GridLayoutManager.VERTICAL)
        }
        binding.recyclerView.apply {
            layoutManager = lm
            adapter = listAdapter
        }
    }

    private fun columnCount(): Int {
        return if (isLandscape) 2 else 1
    }

    private fun showLoading(desc: String? = null, force: Boolean = false) {
        if (force || !progressDialog.isShowing) {
            progressDialog.show(requireActivity(), desc)
        }
    }

    private fun dismissLoading() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}

fun Fragment.dipToPix(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}