package com.femi.e_class.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.femi.e_class.domain.use_case.ValidateEmail
import com.femi.e_class.domain.use_case.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        validateEmail = ValidateEmail(context)
    }

    @Test
    fun emailIsBlankFails(){
        val result = validateEmail.execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Email can't be black"
            )
        )
    }

    @Test
    fun emailContainsUppercaseCharactersFails(){
        val result = validateEmail.execute("Stealingmoviez@gmail.com")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Email must not contain upper-case letters"
            )
        )
    }

    @Test
    fun emailIsInvalidFails(){
        val result = validateEmail.execute("stealingmoviez?@gmail.com")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "That's not a valid email"
            )
        )
    }

    @Test
    fun emailIsAcceptableSucceeds(){
        val result = validateEmail.execute("stealingmoviez.@gmail.com")
        assertThat(result).isEqualTo(
            ValidationResult(
                true
            )
        )
    }

}