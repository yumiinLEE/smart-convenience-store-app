package com.ssafy.finalpass.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.adapter.OrderAdapter
import com.ssafy.finalpass.databinding.DialogWriteReviewBinding
import com.ssafy.finalpass.databinding.FragmentOrderBinding
import com.ssafy.finalpass.dto.ProductComment

class OrderFragment : BaseFragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        orderAdapter = OrderAdapter(
            onReviewClick = { detail ->
                showReviewDialog(detail.productId.toString(), detail.productName)
            },
            onReorderClick = { order ->
//                reorderItems(order)
            },
            onDetailClick = { order ->
                // 상세내역은 adapter에서 토글하도록 설정됨
            }
        )

        binding.recyclerOrder.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.getUserOrders(it.id)
            }
        }

        val btnPeriodSelect = binding.btnPeriodSelect
        val recyclerView = binding.recyclerOrder
        val emptyLayout = binding.emptyLayout

        btnPeriodSelect.setOnClickListener {
            val userId = viewModel.user.value?.id ?: return@setOnClickListener
            showBottomSheetMenu(userId)
        }

        viewModel.orderList.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                emptyLayout.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyLayout.visibility = View.GONE
                orderAdapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBottomSheetMenu(userId: String) {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_period_filter, null)

        view.findViewById<TextView>(R.id.option_1month).setOnClickListener {
            viewModel.getUserOrders(userId, 1)
            binding.btnPeriodSelect.text = "1개월"
            bottomSheet.dismiss()
        }

        view.findViewById<TextView>(R.id.option_3months).setOnClickListener {
            viewModel.getUserOrders(userId, 3)
            binding.btnPeriodSelect.text = "3개월"
            bottomSheet.dismiss()
        }

        view.findViewById<TextView>(R.id.option_6months).setOnClickListener {
            viewModel.getUserOrders(userId, 6)
            binding.btnPeriodSelect.text = "6개월"
            bottomSheet.dismiss()
        }

        view.findViewById<TextView>(R.id.option_all).setOnClickListener {
            viewModel.getUserOrders(userId)
            binding.btnPeriodSelect.text = "전체"
            bottomSheet.dismiss()
        }

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun showReviewDialog(productId: String, productName: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogWriteReviewBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val userId = viewModel.user.value?.id ?: "익명"

        dialogBinding.tvProductName.text = "상품명: $productName"
        dialogBinding.tvUserId.text = "작성자: $userId"

        viewModel.getProduct(productId.toInt())

        viewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
            product?.let {
                val resId = resources.getIdentifier(
                    it.img,
                    "drawable",
                    requireContext().packageName
                )
                dialogBinding.ivProductImage.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)
            }
        }

        dialogBinding.btnSubmit.setOnClickListener {
            val rating = dialogBinding.ratingBar.rating
            val content = dialogBinding.etReview.text.toString().trim()

            if (content.isBlank()) {
                Toast.makeText(requireContext(), "리뷰 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val comment = ProductComment(
                userId = userId,
                productId = productId.toInt(),
                rating = rating,
                comment = content
            )

            viewModel.postReview(comment)
        }

        viewModel.reviewResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }.onFailure {
                Toast.makeText(requireContext(), "등록 실패: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
