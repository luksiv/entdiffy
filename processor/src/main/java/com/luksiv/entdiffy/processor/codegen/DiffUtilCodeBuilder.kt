package com.luksiv.entdiffy.processor.codegen

import com.luksiv.entdiffy.processor.models.ModelData
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class DiffUtilCodeBuilder(
    private val data: ModelData,
) {

    private val modelClassName = ClassName(data.packageName, data.modelName)

    private val diffResultName = "${data.modelName}DiffResult"
    private val diffResultClassName = ClassName(data.packageName, diffResultName)

    private val diffUtilName = "${data.modelName}DiffUtil"
    private val diffUtilClassName = ClassName(data.packageName, diffUtilName)

    fun build(): TypeSpec {
        return with(TypeSpec.objectBuilder(diffUtilClassName)) {
            val code = """
                
                return ${diffResultName} (
                ${
            data.modelFields.map {
                "${it.fieldName}Changed = first.${it.fieldName} != second.${it.fieldName}"
            }.joinToString(",\n")
            }
                )
                
            """.trimIndent()

            val diffFun = FunSpec.builder("calculateDiff")
                .addParameter("first", modelClassName)
                .addParameter("second", modelClassName)
                .addCode(code)

                .returns(diffResultClassName)
                .build()

            addFunction(diffFun)

            build()
        }
    }
}