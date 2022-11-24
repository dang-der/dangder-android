package com.viewpoint.dangder.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class InputVerifyWatcher(
    private val textInputLayout: TextInputLayout,
    private val errorMessage: String,
    private val pattern: Regex
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {

        if (!validateInputValue(p0.toString())) {
            textInputLayout.error = errorMessage
            textInputLayout.isErrorEnabled = true
            return
        }

        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }

    private fun validateInputValue(input: String): Boolean {
        return pattern.matches(input)
    }
}