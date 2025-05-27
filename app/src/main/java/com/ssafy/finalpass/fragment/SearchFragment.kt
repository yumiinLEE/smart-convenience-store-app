package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.adapter.ProductAdapter
import com.ssafy.finalpass.databinding.FragmentSearchBinding
import com.ssafy.finalpass.dto.Product
import com.ssafy.finalpass.dto.ProductComment

class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

    private val recentKeywords = mutableListOf<String>()

    private var currentProducts: List<Product>? = null
    private var currentComments: List<ProductComment>? = null

    private fun tryUpdateAdapter() {
        if (currentProducts != null && currentComments != null) {
            adapter.updateList(currentProducts!!, currentComments!!)
            binding.rvOrderList.visibility = if (currentProducts!!.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvOrderList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvOrderList.visibility = View.GONE

        // 댓글과 상품 전체 요청
        viewModel.getAllProductComments()
        viewModel.getAllProduct()

        // 카테고리 필터
        val initialCategory = arguments?.getString("category")
        if (!initialCategory.isNullOrBlank()) {
            viewModel.searchByCategory(initialCategory)
            binding.searchView.setQuery(initialCategory, false)
        }

        // 어댑터 초기화 (빈 상태로)
        adapter = ProductAdapter(
            emptyList(),
            emptyList(),
            onAddToCart = { product ->
                val user = viewModel.user.value

                if (user == null) {
                    showLoginRequiredDialog()
                } else {
                    viewModel.addToCart(product)
                    Toast.makeText(requireContext(), "${product.name} 장바구니에 추가되었습니다", Toast.LENGTH_SHORT).show()
                }
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

        // LiveData 관찰
        viewModel.productList.observe(viewLifecycleOwner) { list ->
            currentProducts = list
            tryUpdateAdapter()
            Log.d("SearchFragment", "상품 개수: ${list.size}")
        }

        viewModel.productComments.observe(viewLifecycleOwner) { comments ->
            currentComments = comments
            tryUpdateAdapter()
        }

        setupRecentSearchUI()

        // 검색 이벤트 처리
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
                    adapter.updateList(emptyList(), emptyList())
                } else {
                    viewModel.searchProducts(newText!!.trim())
                }

                return true
            }
        })

        // 최근 검색어 전체 삭제
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
            val chip = LayoutInflater.from(context).inflate(R.layout.item_keyword_chip, flexboxLayout, false)
            val tvKeyword = chip.findViewById<TextView>(R.id.tvKeyword)
            val btnRemove = chip.findViewById<View>(R.id.btnRemove)

            tvKeyword.text = keyword

            chip.setOnClickListener {
                binding.searchView.setQuery(keyword, true)
            }

            btnRemove.setOnClickListener {
                recentKeywords.remove(keyword)
                updateRecentSearchUI()
            }

            flexboxLayout.addView(chip)
        }

        binding.recentContainer.visibility = if (recentKeywords.isEmpty()) View.GONE else View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoginRequiredDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("로그인 필요")
            .setMessage("장바구니에 상품을 추가하려면 로그인이 필요합니다.")
            .setPositiveButton("로그인") { dialog, _ ->
                dialog.dismiss()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment())
                    .addToBackStack(null)
                    .commit()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }
            .setCancelable(false)
            .show()
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
