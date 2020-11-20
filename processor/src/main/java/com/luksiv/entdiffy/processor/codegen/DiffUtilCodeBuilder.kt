package com.luksiv.entdiffy.processor.codegen

import com.luksiv.entdiffy.processor.models.ModelData
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
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
            addFunction(getCalculateDiffFunSpec())
            addKdoc("Diffing utility class for [${data.modelName}] entity")
            build()
        }
    }

    private fun getCalculateDiffFunSpec(): FunSpec {
        return with(FunSpec.builder("calculateDiff")) {
            addKdoc(
                "Diffing utility object for [${data.modelName}] entity\n" +
                        "@param first first [${data.modelName}]\n" +
                        "@param second second [${data.modelName}]\n" +
                        "@return [${diffResultName}] containing data of what are the differences of entities\n"
            )

            addParameter("first", modelClassName.copy(true))
            addParameter("second", modelClassName.copy(true))

            addCode(with(CodeBlock.builder()) {

                // Check if either are null
                beginControlFlow("if(first == null && second != null || first != null && second == null)")
                addStatement("return $diffResultName(")
                indent()
                data.modelFields.forEach {
                    addStatement("${it.fieldName}Changed = true,")
                }
                unindent()
                addStatement(")")

                // Check if both are null
                nextControlFlow("else if(first == null && second == null)")
                addStatement("return $diffResultName(")

                indent()
                data.modelFields.forEach {
                    addStatement("${it.fieldName}Changed = false,")
                }
                unindent()
                addStatement(")")

                // Check each parameter if checked
                nextControlFlow("else")
                addStatement("return $diffResultName(")
                indent()
                data.modelFields.forEach {
                    addStatement("${it.fieldName}Changed = first?.${it.fieldName} != second?.${it.fieldName},")
                }
                unindent()
                addStatement(")")

                endControlFlow()

                build()

            })

            returns(diffResultClassName)
            build()
        }
    }
}