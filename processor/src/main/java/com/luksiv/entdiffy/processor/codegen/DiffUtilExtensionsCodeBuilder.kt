package com.luksiv.entdiffy.processor.codegen

import com.luksiv.entdiffy.processor.models.ModelData
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class DiffUtilExtensionsCodeBuilder(
    private val data: ModelData,
) {

    private val modelClassName = ClassName(data.packageName, data.modelName)

    private val diffResultName = "${data.modelName}DiffResult"
    private val diffResultClassName = ClassName(data.packageName, diffResultName)

    private val diffUtilName = "${data.modelName}DiffUtil"

    private val diffUtilExtensionsName = "${data.modelName}DiffUtilExtensions"
    private val diffUtilExtensionsClassName = ClassName(data.packageName, diffUtilExtensionsName)

    fun build(): TypeSpec {
        return with(TypeSpec.objectBuilder(diffUtilExtensionsClassName)) {
            addFunction(with(FunSpec.builder("calculateDiff")) {
                receiver(modelClassName)

                addKdoc(
                    "Diffing utility extension function for [${data.modelName}] entity\n" +
                            "@param other second [${data.modelName}]\n" +
                            "@return [${diffResultName}] containing data of what are the differences of entities\n"
                )

                addParameter("other", modelClassName.copy(true))
                returns(diffResultClassName)

                addCode(with(CodeBlock.builder()) {
                    addStatement("return ${diffUtilName}.calculateDiff(this, other)")
                    build()
                })

                build()
            })

            build()
        }
    }
}