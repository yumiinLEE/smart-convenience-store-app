package com.ssafy.finalpass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpass.R
import com.ssafy.finalpass.databinding.ItemProductBinding
import com.ssafy.finalpass.dto.Product
import com.ssafy.finalpass.dto.ProductComment

class ProductAdapter(
    private var items: List<Product>,
    private var comments: List<ProductComment>,
    private val onAddToCart: (Product) -> Unit,
    private val onDetailClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvProductName.text = item.name
            tvProductPrice.text = "${item.price}원"

            val filteredComments = comments.filter { it.productId.toInt() == item.id }
            val avgRating = if (filteredComments.isNotEmpty()) {
                filteredComments.map { it.rating }.average()
            } else {
                0.0
            }

            tvRating.text = "%.1f".format(avgRating)
            tvCommentCount.text = "(후기${filteredComments.size})"

            val context = imgProduct.context
            val resId = context.resources.getIdentifier(item.img, "drawable", context.packageName)
            imgProduct.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)


            btnAddToCart.setOnClickListener { onAddToCart(item) }
            btnDetail.setOnClickListener { onDetailClick(item) }
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newProducts: List<Product>, newComments: List<ProductComment>) {
        items = newProducts
        comments = newComments
        notifyDataSetChanged()
    }
}
