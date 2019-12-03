package com.example.prototypeparksplushomescreen.data.EntityHelpers

import androidx.room.Embedded
import com.example.prototypeparksplushomescreen.data.entity.TrailEntity
import com.example.prototypeparksplushomescreen.data.entity.TrailPointsEntity

data class TrailAndTrailPointsEntityHelper(
    @Embedded
    val trail: TrailEntity,
    @Embedded
    val trailPoints: TrailPointsEntity
) {

}