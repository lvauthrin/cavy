package cavy.experiment.domain.exception

abstract class ExperimentException(msg: String) extends Exception(msg)
case class DuplicateExperimentException(msg: String) extends ExperimentException(msg)
case class ExperimentAlreadyActiveException(msg: String) extends ExperimentException(msg)
case class InvalidAllocationException(msg: String) extends ExperimentException(msg)
case class InvalidExperimentException(msg: String) extends ExperimentException(msg)
case class InvalidTreatmentException(msg: String) extends ExperimentException(msg)