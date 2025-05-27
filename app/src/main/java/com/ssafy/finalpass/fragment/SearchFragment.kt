package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.adapter.ProductAdapter
import com.ssafy.finalpass.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

    private val recentKeywords = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvOrderList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvOrderList.visibility = View.GONE

        // 카테고리
        val initialCategory = arguments?.getString("category")
        if (!initialCategory.isNullOrBlank()) {
            viewModel.searchByCategory(initialCategory)
            binding.searchView.setQuery(initialCategory, false)
        }


        adapter = ProductAdapter(
            emptyList(),
            viewModel.productComments.value ?: emptyList(),
            onAddToCart = { product ->
                viewModel.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} 장바구니에 추가되었습니다", Toast.LENGTH_SHORT).show()
            },
            onDetailClick = { product ->
                val fragment = ProductCommentFragment.newInstance(product.id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        )

        binding.rvOrderList.adapter = adapter

        viewModel.productList.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
            Log.d("SearchFragment", "상품 개수: ${list.size}")
            binding.rvOrderList.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
        }

        setupRecentSearchUI()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        addRecentKeyword(it)
                        viewModel.searchProducts(it.trim())
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val isBlank = newText.isNullOrBlank()
                binding.rvOrderList.visibility = if (isBlank) View.GONE else View.VISIBLE

                if (isBlank) {
                    adapter.updateList(emptyList())
                } else {
                    viewModel.searchProducts(newText!!.trim())
                }

                return true
            }
        })

        binding.tvClearAll.setOnClickListener {
            recentKeywords.clear()
            updateRecentSearchUI()
        }
    }

    private fun setupRecentSearchUI() {
        updateRecentSearchUI()
    }

    private fun addRecentKeyword(keyword: String) {
        if (!recentKeywords.contains(keyword)) {
            recentKeywords.add(0, keyword)
            if (recentKeywords.size > 10) {
                recentKeywords.removeAt(recentKeywords.size - 1)
            }
            updateRecentSearchUI()
        }
    }

    private fun updateRecentSearchUI() {
        val context = requireContext()
        val flexboxLayout = binding.recentKeywordsLayout
        flexboxLayout.removeAllViews()

        recentKeywords.forEach { keyword ->
            val chip = LayoutInflater.from(context).inflate(R.layout.item_keyword_chip, flexboxLayout, false) as TextView
            chip.text = keyword
            chip.setOnClickListener {
                binding.searchView.setQuery(keyword, true)
            }
            chip.setOnLongClickListener {
                recentKeywords.remove(keyword)
                updateRecentSearchUI()
                true
            }
            flexboxLayout.addView(chip)
        }

        binding.recentContainer.visibility = if (recentKeywords.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }
}
