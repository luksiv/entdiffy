package com.luksiv.entdiffy.processor.models

import javax.lang.model.type.TypeMirror

data class ModelField(
    val fieldName: String,
    val fieldTypeMirror: TypeMirror
)