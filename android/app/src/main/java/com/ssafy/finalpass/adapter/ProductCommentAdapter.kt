package com.ssafy.finalpass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpass.adapter.OrderAdapter.Companion.diffUtil
import com.ssafy.finalpass.databinding.ItemProductCommentBinding
import com.ssafy.finalpass.dto.ProductComment

class ProductCommentAdapter(
    private val onEdit: ((ProductComment) -> Unit)? = null,
    private val onDelete: ((ProductComment) -> Unit)? = null
) : ListAdapter<ProductComment, ProductCommentAdapter.CommentViewHolder>(diffUtil) {

    inner class CommentViewHolder(private val binding: ItemProductCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: ProductComment) = with(binding) {
            tvUserId.text = comment.userId
            tvComment.text = comment.comment
            ratingBar.rating = comment.rating

            tvEdit.apply {
                visibility = if (onEdit != null) View.VISIBLE else View.GONE
                setOnClickListener { onEdit?.invoke(comment) }
            }

            tvDelete.apply {
                visibility = if (onDelete != null) View.VISIBLE else View.GONE
                setOnClickListener { onDelete?.invoke(comment) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemProductCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ProductComment>() {
            override fun areItemsTheSame(oldItem: ProductComment, newItem: ProductComment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductComment, newItem: ProductComment): Boolean {
                return oldItem == newItem
            }
        }
    }
}

