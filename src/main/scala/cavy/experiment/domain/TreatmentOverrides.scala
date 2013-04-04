package cavy.experiment.domain

case class TreatmentOverrides(val experimentId: String, val userIdToTreatment: Map[String, String])