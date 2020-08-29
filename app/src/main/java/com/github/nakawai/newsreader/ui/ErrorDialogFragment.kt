package com.github.nakawai.newsreader.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment

class ErrorDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(requireArguments().getString(ARG_TITLE))
            .setMessage(requireArguments().getString(ARG_MESSAGE))
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    companion object {
        const val TAG = "PlainDialogFragment"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_MESSAGE = "ARG_MESSAGE"

        fun newInstance(title: String, message: String): ErrorDialogFragment {
            val fragment = ErrorDialogFragment()
            fragment.arguments = bundleOf(
                ARG_TITLE to title,
                ARG_MESSAGE to message
            )
            return fragment
        }
    }
}
