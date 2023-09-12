package com.femi.e_class.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.femi.e_class.domain.use_case.ValidateName
import com.femi.e_class.domain.use_case.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateNameTest {

    private lateinit var validateName: ValidateName

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        validateName = ValidateName(context)
    }

    @Test
    fun nameIsBlankFails(){
        val result = validateName.execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Name can't be black"
            )
        )
    }

    @Test
    fun acceptableNameSucceed(){
        val result = validateName.execute("a")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }
}