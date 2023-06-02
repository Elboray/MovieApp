package com.chocolatecake.movieapp.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.chocolatecake.movieapp.ui.login.LoginViewModel

@BindingAdapter("onUserNameChanged")
fun setOnUserNameChangedListener(editText: EditText, viewModel: LoginViewModel) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.onUserNameChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}

@BindingAdapter("onPasswordChanged")
fun setOnPasswordChangedListener(editText: EditText, viewModel: LoginViewModel) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.onPasswordChanged(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}

@BindingAdapter("app:setTipError")
fun EditText.setTipError(errorMessage: String?) {
    if (errorMessage == null) return
    else this.error = errorMessage
}

@BindingAdapter("app:isVisible")
fun View.isVisible(isVisible: Boolean?) {
    visibility = if (isVisible == true) View.VISIBLE else View.INVISIBLE
}