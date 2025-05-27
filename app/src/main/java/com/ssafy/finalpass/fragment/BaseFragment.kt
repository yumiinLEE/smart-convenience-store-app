package com.ssafy.finalpass.fragment

import androidx.fragment.app.Fragment
import com.ssafy.finalpass.MainActivity

abstract class BaseFragment : Fragment() {
    open fun showBottomUI(): Boolean = true

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setUIVisible(showBottomUI())
    }
}

