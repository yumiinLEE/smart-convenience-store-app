package com.ssafy.finalpass.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpass.MainActivityViewModel
import com.ssafy.finalpass.adapter.ProductCommentAdapter
import com.ssafy.finalpass.databinding.DialogWriteReviewBinding
import com.ssafy.finalpass.databinding.FragmentMyReviewsBinding

class MyReviewsFragment  : Fragment() {

    private var _binding: FragmentMyReviewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: ProductCommentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductCommentAdapter(
            onEdit = { comment ->
                val dialog = Dialog(requireContext())
                val dialogBinding = DialogWriteReviewBinding.inflate(layoutInflater)
                dialog.setContentView(dialogBinding.root)

                dialogBinding.tvProductName.text = "수정할 리뷰"
                dialogBinding.tvUserId.text = "작성자: ${comment.userId}"
                dialogBinding.ratingBar.rating = comment.rating
                dialogBinding.etReview.setText(comment.comment)

                dialogBinding.btnSubmit.setOnClickListener {
                    val updatedRating = dialogBinding.ratingBar.rating
                    val updatedComment = dialogBinding.etReview.text.toString().trim()

                    if (updatedComment.isNotBlank()) {
                        val updated = comment.copy(rating = updatedRating, comment = updatedComment)
                        viewModel.updateComment(updated)
                        viewModel.getUserComments(comment.userId)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.show()
            },
            onDelete = { comment ->
                AlertDialog.Builder(requireContext())
                    .setTitle("리뷰 삭제")
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ ->
                        viewModel.deleteComment(comment.id!!, comment.productId)
                        viewModel.getUserComments(comment.userId)
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        )

        binding.recyclerMyReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMyReviews.adapter = adapter

        val userId = viewModel.user.value?.id ?: return
        viewModel.getUserComments(userId)

        viewModel.userComments.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

