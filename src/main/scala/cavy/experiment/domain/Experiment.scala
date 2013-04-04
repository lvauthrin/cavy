package cavy.experiment.domain

import java.util.Date

case class Experiment(
    val id: String,
    val name: String,
    val active: Boolean,
    val startDate: Date,
    val endDate: Date,
    val treatments: List[Treatment]
)