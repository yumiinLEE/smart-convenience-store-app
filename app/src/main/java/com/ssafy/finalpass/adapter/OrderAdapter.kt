package com.ssafy.finalpass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpass.R
import com.ssafy.finalpass.databinding.ItemOrderBinding
import com.ssafy.finalpass.dto.Order
import com.ssafy.finalpass.dto.OrderDetail

class OrderAdapter(
    private val onReviewClick: (OrderDetail) -> Unit,
    private val onReorderClick: (Order) -> Unit,
    private val onDetailClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(diffUtil) {

    private var expandedPosition = -1

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order, position: Int) = with(binding) {
            tvStore.text = item.storeName
            tvTime.text = item.orderTime
            val totalPrice = item.details.sumOf { it.unitPrice * it.quantity }
            tvTotalPrice.text = "%,d원".format(totalPrice)

            val firstProduct = item.details.firstOrNull()
            if (firstProduct != null) {
                val resId = root.context.resources.getIdentifier(firstProduct.img, "drawable", root.context.packageName)
                tvOrderImage.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_foreground)

                val productCount = item.details.size
                tvProductName.text = if (productCount > 1) {
                    "${firstProduct.productName} 외 ${productCount - 1}건"
                } else {
                    firstProduct.productName
                }
            }

            layoutOrderDetail.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE
            layoutOrderDetail.removeAllViews()

            val context = binding.root.context
            item.details.forEach { detail ->
                val detailView = LayoutInflater.from(context)
                    .inflate(R.layout.item_order_detail, layoutOrderDetail, false)

                detailView.findViewById<TextView>(R.id.tvProductName).text = detail.productName
                detailView.findViewById<TextView>(R.id.tvQuantity).text = "수량: ${detail.quantity}"
                detailView.findViewById<TextView>(R.id.tvPrice).text = "금액: ${detail.unitPrice * detail.quantity}원"

                val resId = context.resources.getIdentifier(detail.img, "drawable", context.packageName)
                detailView.findViewById<ImageView>(R.id.ivProductImage).setImageResource(
                    if (resId != 0) resId else R.drawable.ic_launcher_foreground
                )

                detailView.findViewById<Button>(R.id.btnWriteReview).setOnClickListener {
                    onReviewClick(detail)
                }

                layoutOrderDetail.addView(detailView)
            }

            btnReorder.setOnClickListener {
                onReorderClick(item)
            }

            btnDetail.setOnClickListener {
                expandedPosition = if (position == expandedPosition) -1 else position
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
        }
    }
}
