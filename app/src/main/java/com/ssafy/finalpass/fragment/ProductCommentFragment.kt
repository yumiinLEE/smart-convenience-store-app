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


class ProductCommentFragment : Fragment() {

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

        viewModel.getProduct(productId.toInt())
        viewModel.getProductComments(productId.toInt())

        viewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
            if (product != null) {
                binding.tvProductName.text = product.name
                binding.tvProductPrice.text = "${product.price}원"
                val resId = resources.getIdentifier(
                    product.img,
                    "drawable",
                    requireContext().packageName
                )
                binding.ivProductImage.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)
            }
        }

        viewModel.productComments.observe(viewLifecycleOwner) { commentList ->
            adapter.submitList(commentList)

            if (commentList.isNotEmpty()) {
                val avgRating = commentList.map { it.rating }.average().toFloat()
                binding.ratingBarAverage.rating = avgRating
                binding.tvRating.text = "평점 ${String.format("%.1f", avgRating)}점"
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
