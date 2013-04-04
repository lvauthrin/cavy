package cavy.experiment.facade

import com.google.common.base.CharMatcher
import cavy.experiment.repository.ExperimentRepository
import cavy.experiment.domain.exception.InvalidExperimentException
import cavy.experiment.domain.exception.InvalidTreatmentException
import cavy.experiment.domain.Experiment
import com.google.common.base.Preconditions
import cavy.experiment.domain.exception.DuplicateExperimentException
import cavy.experiment.domain.Treatment
import cavy.experiment.domain.mongo.MongoTreatment
import java.util.Date

class SimpleExperimentServiceImpl(experimentRepository: ExperimentRepository) extends ExperimentService {
    val DEFAULT = CharMatcher.DIGIT.or(CharMatcher.JAVA_LETTER).or(CharMatcher.is('-'))

    @throws(classOf[InvalidExperimentException])
    @throws(classOf[InvalidTreatmentException])
    override def activate(experimentId: String): Experiment = {
        val experiment = get(experimentId)
        ExperimentValidations.validateHasAtLeastTwoTreatments(experiment.treatments)
        ExperimentValidations.validateAllocations(experiment.treatments)

        val activeExperiment = experiment.copy(active = true, startDate = new Date())
        experimentRepository.updateActiveState(activeExperiment)
        activeExperiment
    }

    @throws(classOf[InvalidExperimentException])
    @throws(classOf[InvalidTreatmentException])
    override def addTreatment(experimentId: String, id: String, name: String, weight:Double ) = {
        Preconditions.checkArgument(id != null, "Treatment id must be specified", List())
        Preconditions.checkArgument(DEFAULT.matchesAllOf(id), "Treatment id contains invalid characters", List())
        Preconditions.checkArgument(name != null && !name.isEmpty(), "Treatment name must be specified", List())
        Preconditions.checkArgument(weight >= 0d && weight <= 1d, "Treatment weight must between 0 and 1", List())

        val experiment = get(experimentId)
        ExperimentValidations.validateTreatmentDoesNotExist(id, experiment.treatments)
        ExperimentValidations.validateNotActive(experiment)

        val newTreatment = Treatment(id, name, weight, false)
        val updatedTreatments = experiment.treatments :+ newTreatment
        ExperimentValidations.validateAllocations(updatedTreatments)
        
        val updatedExperiment = experiment.copy(treatments = updatedTreatments)
        experimentRepository.updateTreatments(updatedExperiment.id, updatedExperiment.treatments)
        updatedExperiment
    }

    @throws(classOf[DuplicateExperimentException])
    override def create(id: String, name: String) = {
        Preconditions.checkArgument(id != null && !id.isEmpty(), "Experiment id must be specified", List())
        Preconditions.checkArgument(DEFAULT.matchesAllOf(id), "Experiment id contains invalid characters", List())
        Preconditions.checkArgument(name != null && !name.isEmpty(), "Experiment name must be specified", List())

        experimentRepository.get(id, false) match {
            case Some(_) => throw new DuplicateExperimentException(s"Experiment '$id' already exists.")
            case None => {
                val experiment = Experiment(id, name, false, null, null, List(Treatment.DEFAULT))
                experimentRepository.create(experiment)
                experiment
            }
        }
    }

    @throws(classOf[InvalidExperimentException])
    override def deactivate(experimentId: String) = {
        val experiment = get(experimentId)
        val updatedExperiment = experiment.copy(active = false, endDate = new Date())
        experimentRepository.updateActiveState(updatedExperiment)
        updatedExperiment
    }

    @throws(classOf[InvalidExperimentException])
    override def get(experimentId: String) = {
        experimentRepository.get(experimentId, false) match {
            case None => throw new InvalidExperimentException(s"No experiment found with id '$experimentId'")
            case Some(e) => e 
        }
    }

    override def list() = experimentRepository.list()

    @throws(classOf[InvalidExperimentException])
    override def updateTreatments(experimentId: String, allocations: Map[String, Double]) = {
        val experiment = get(experimentId)        
        val updatedTreatments = experiment.treatments.map { t => t.copy(weight = allocations.getOrElse(t.id, t.weight)) }
        ExperimentValidations.validateAllocations(updatedTreatments);
    
        val updatedExperiment = experiment.copy(treatments = updatedTreatments);
        experimentRepository.updateTreatments(updatedExperiment.id, updatedExperiment.treatments);
        updatedExperiment;
    }

}