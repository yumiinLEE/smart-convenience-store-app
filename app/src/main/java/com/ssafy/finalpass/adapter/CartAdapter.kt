package com.ssafy.finalpass.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpass.R
import com.ssafy.finalpass.databinding.ItemCartBinding
import com.ssafy.finalpass.dto.Product

class CartAdapter(
    private val onPlus: (Product) -> Unit,
    private val onMinus: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : ListAdapter<Product, CartAdapter.CartViewHolder>(diffUtil) {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) = with(binding) {
            tvProductName.text = item.name
            tvProductPrice.text = "${item.price}원"
            tvCount.text = item.count.toString()

            val context = root.context
            val resId = context.resources.getIdentifier(item.img, "drawable", context.packageName)

            imgProduct.setImageResource(
                if (resId != 0) resId else R.drawable.ic_launcher_foreground
            )

            btnPlus.setOnClickListener { onPlus(item) }
            btnMinus.setOnClickListener { onMinus(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
            override fun areContentsTheSame(old: Product, new: Product) = old == new
        }
    }
}
