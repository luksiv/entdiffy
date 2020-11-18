package com.luksiv.entdiffy.processor.models

data class ModelData(
    val packageName: String,
    val modelName: String,
    val modelFields: List<ModelField>,
)