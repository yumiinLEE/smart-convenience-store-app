package com.ssafy.finalpass.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpass.databinding.ItemSuggestionBinding
import com.ssafy.finalpass.dto.Product

class GptProductAdapter(
    private var allProducts: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<GptProductAdapter.GptProductViewHolder>() {

    private var matchedProducts: List<Product> = emptyList()

    inner class GptProductViewHolder(private val binding: ItemSuggestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "${product.price}원"

            val context = binding.root.context
            val resId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
            binding.imgProduct.setImageResource(
                if (resId != 0) resId else android.R.drawable.ic_menu_report_image
            )

            binding.btnAddToCart.setOnClickListener {
                onAddToCart(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GptProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSuggestionBinding.inflate(inflater, parent, false)
        return GptProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GptProductViewHolder, position: Int) {
        holder.bind(matchedProducts[position])
    }

    override fun getItemCount(): Int = matchedProducts.size

    fun updateList(newProducts: List<Product>) {
        matchedProducts = newProducts
        Log.d("GPT", "Matched: ${matchedProducts.joinToString { it.name }}")

        notifyDataSetChanged()
    }
}
