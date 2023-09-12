package com.femi.e_class.domain.use_case

//import android.util.Patterns
import android.content.Context
import androidx.core.util.PatternsCompat
import com.femi.e_class.R

class ValidateEmail(val context: Context) {

    fun execute(email: String): ValidationResult{
        if (email.isBlank()){
            return ValidationResult(
                false,
                context.getString(R.string.email_blank_error)
            )
        }
        if (email.any { it.isUpperCase() }){
            return ValidationResult(
                false,
                context.getString(R.string.email_contains_uppercase_error)
            )
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                false,
                context.getString(R.string.email_format_invalid_error)
            )
        }
        return ValidationResult(
            true
        )
    }
}