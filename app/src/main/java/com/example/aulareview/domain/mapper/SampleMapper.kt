package com.example.aulareview.domain.mapper

import com.example.aulareview.domain.model.SampleModel
import com.example.aulareview.networking.model.SampleResponse


//Mapeia a response para o modelo da camada de Domain
fun List<SampleResponse>.toModel(): List<SampleModel>{
    return this.map { item->
        SampleModel(
            userId = item.userId ?: 0,
            id = item.id ?: 0,
            title = item.title.orEmpty(),
            body = item.body.orEmpty()
        )
    }
}