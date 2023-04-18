package com.example.whatwear

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("data")
	val data: Data? = null
)

data class TimelinesItem(

	@field:SerializedName("intervals")
	val intervals: List<IntervalsItem?>? = null,

	@field:SerializedName("timestep")
	val timestep: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null
)

data class Data(

	@field:SerializedName("timelines")
	val timelines: List<TimelinesItem?>? = null
)

data class Values(

	@field:SerializedName("temperature")
	val temperature: Double? = null
)

data class IntervalsItem(

	@field:SerializedName("values")
	val values: Values? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null
)
