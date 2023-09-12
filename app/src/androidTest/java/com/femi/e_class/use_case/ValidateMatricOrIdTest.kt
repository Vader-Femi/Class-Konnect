package com.femi.e_class.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.femi.e_class.domain.use_case.ValidateMatricOrId
import com.femi.e_class.domain.use_case.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateMatricOrIdTest {

    private lateinit var validateMatricOrId: ValidateMatricOrId

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        validateMatricOrId = ValidateMatricOrId(context)
    }
    
    @Test
    fun MatricOrIdIsBlankFails(){
        val result = validateMatricOrId.execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Matric/Id cannot be blank"
            )
        )
    }

    @Test
    fun matricOrIdIsNotDigitsOnlyFails(){
        val result = validateMatricOrId.execute("11m")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Matric/Id must be integer numbers"
            )
        )
    }

    @Test
    fun matricOrIdIsLessThan2DigitsFails(){
        val result = validateMatricOrId.execute("10")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Matric/Id must be more than 2 digits long"
            )
        )
    }

    @Test
    fun acceptableMatricOrIdSucceed(){
        val result = validateMatricOrId.execute("101")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }

}