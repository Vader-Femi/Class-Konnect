package com.femi.e_class.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.femi.e_class.domain.use_case.ValidateEmail
import com.femi.e_class.domain.use_case.ValidateLogInPassword
import com.femi.e_class.domain.use_case.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateLogInPasswordTest {

    private lateinit var validateLogInPassword: ValidateLogInPassword

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        validateLogInPassword = ValidateLogInPassword(context)
    }

    @Test
    fun passwordIsBlankFails(){
        val result = validateLogInPassword.execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Password cannot be empty"
            )
        )
    }

    @Test
    fun acceptablePasswordSucceed(){
        val result = validateLogInPassword.execute("p")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }
}