package com.ssafy.finalpass.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.R
import com.ssafy.finalpass.adapter.ProductAdapter
import com.ssafy.finalpass.databinding.FragmentMyFavoritesBinding

class MyFavoritesFragment : BaseFragment() {

    private var _binding: FragmentMyFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ProductAdapter(
            emptyList(), emptyList(),
            onAddToCart = { product ->
                viewModel.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} 장바구니에 추가", Toast.LENGTH_SHORT).show()
            },
            onDetailClick = { product ->
                val fragment = ProductCommentFragment.newInstance(product.id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onFavoriteToggle = { product, isFavorite ->
                if (isFavorite) {
                    viewModel.addFavorite(product.id)
                    Toast.makeText(requireContext(), "${product.name} 찜 추가", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.removeFavorite(product.id)
                    Toast.makeText(requireContext(), "${product.name} 찜 해제", Toast.LENGTH_SHORT).show()

                    // 바로 갱신
                    val updatedFavorites = viewModel.favoriteIds.value?.toMutableSet() ?: mutableSetOf()
                    updatedFavorites.remove(product.id)

                    val filteredProducts = viewModel.productList.value?.filter { it.id in updatedFavorites } ?: emptyList()
                    val comments = viewModel.productComments.value ?: emptyList()

                    adapter.updateList(filteredProducts, comments)
                    adapter.updateFavorites(updatedFavorites)
                }
            }
        )

        binding.recyclerView.adapter = adapter

        // 반드시 먼저 observe 설정
        viewModel.favoriteIds.observe(viewLifecycleOwner) { favoriteSet ->
            val filteredProducts = viewModel.productList.value?.filter { it.id in favoriteSet } ?: emptyList()
            adapter.updateList(filteredProducts, viewModel.productComments.value ?: emptyList())
            adapter.updateFavorites(favoriteSet)
        }

        //  그리고 나서 즉시 호출
        viewModel.getAllProduct()
        viewModel.getAllProductComments()
        viewModel.fetchFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

