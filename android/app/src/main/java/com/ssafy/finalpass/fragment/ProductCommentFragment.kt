package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.adapter.ProductCommentAdapter
import com.ssafy.finalpass.databinding.FragmentProductCommentBinding


class ProductCommentFragment : BaseFragment() {
    override fun showBottomUI(): Boolean = false

    private var _binding: FragmentProductCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: ProductCommentAdapter
    private lateinit var productId: String

    companion object {
        fun newInstance(productId: String): ProductCommentFragment {
            val fragment = ProductCommentFragment()
            fragment.arguments = Bundle().apply {
                putString("productId", productId)
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString("productId") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductCommentAdapter()
        binding.recyclerReview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReview.adapter = adapter

        val productId = arguments?.getString("productId")?.toIntOrNull()
        productId?.let { id ->
            // 1. 상품 정보 및 댓글 요청
            viewModel.getProduct(id)
            viewModel.getProductComments(id)

            // 2. 상품 정보 표시
            viewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
                if (product != null) {
                    binding.tvProductName.text = product.name
                    binding.tvProductPrice.text = "${product.price}원"
                    val resId = resources.getIdentifier(
                        product.img, "drawable", requireContext().packageName
                    )
                    binding.ivProductImage.setImageResource(
                        if (resId != 0) resId else R.drawable.ic_launcher_foreground
                    )
                }
            }

            // 3. 리뷰 목록 표시
            viewModel.productComments.observe(viewLifecycleOwner) { commentList ->
                val filteredComments = commentList.filter { it.productId == id }

                adapter.submitList(filteredComments)

                if (filteredComments.isNotEmpty()) {
                    val avgRating = filteredComments.map { it.rating }.average().toFloat()
                    binding.ratingBarAverage.rating = avgRating
                    binding.tvRating.text = "평점 ${String.format("%.1f", avgRating)}점"
                } else {
                    binding.ratingBarAverage.rating = 0f
                    binding.tvRating.text = "리뷰 없음"
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
