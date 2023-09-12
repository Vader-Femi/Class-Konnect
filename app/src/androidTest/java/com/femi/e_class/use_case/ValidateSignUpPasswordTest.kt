package com.femi.e_class.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.femi.e_class.domain.use_case.ValidateSignUpPassword
import com.femi.e_class.domain.use_case.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateSignUpPasswordTest {

    private lateinit var validateSignUpPassword: ValidateSignUpPassword

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        validateSignUpPassword = ValidateSignUpPassword(context)
    }
    
    @Test
    fun passwordLessThat8Fails(){
        val result = validateSignUpPassword.execute("eight")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Password cannot be less than 8 characters"
            )
        )
    }

    @Test
    fun passwordDoesntContainLettersFails(){
        val result = validateSignUpPassword.execute("1234567890")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must contain at least one letter (A-Z or a-z)"
            )
        )
    }

    @Test
    fun passwordDoesntContainDigitsFails(){
        val result = validateSignUpPassword.execute("password")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must contain at least one digit (0-9)"
            )
        )
    }

    @Test
    fun passwordDoesntContainSpecialCharactersFails(){
        val result = validateSignUpPassword.execute("password2")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must contain at least 1 special character"
            )
        )
    }

    @Test
    fun passwordContainsSpaceFails(){
        val result = validateSignUpPassword.execute("password2 .")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must no contain spaces"
            )
        )
    }

    @Test
    fun acceptablePasswordSucceed(){
        val result = validateSignUpPassword.execute("password2.")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }

}