package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.adapter.CartAdapter
import com.ssafy.finalpass.databinding.FragmentShoppingCartBinding

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cartAdapter = CartAdapter(
            onPlus = { product -> viewModel.increaseCount(product) },
            onMinus = { product -> viewModel.decreaseCount(product) },
            onDelete = { product -> viewModel.removeItem(product) }
        )

        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        viewModel.cartList.observe(viewLifecycleOwner) {
            cartAdapter.submitList(it.toList())
            binding.tvTotalPrice.text = "총 결제금액: ${it.sumOf { item -> item.price * item.count }}원"
        }

        binding.btnOrder.setOnClickListener {
            val userId = viewModel.user.value?.id ?: "testUser"
            val storeName = "GS구미점"  // 선택된 매장 넣어야함

            viewModel.submitOrder(userId.toString(), storeName)

            Toast.makeText(requireContext(), "결제 완료 🎉", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
