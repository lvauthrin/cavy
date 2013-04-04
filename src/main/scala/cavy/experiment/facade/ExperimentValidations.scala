package cavy.experiment.facade

import cavy.experiment.domain.exception.InvalidAllocationException
import cavy.experiment.domain.Treatment
import cavy.experiment.domain.exception.ExperimentAlreadyActiveException
import cavy.experiment.domain.Experiment
import cavy.experiment.domain.exception.InvalidTreatmentException
import cavy.experiment.domain.exception.InvalidExperimentException

object ExperimentValidations {
    
    @throws(classOf[InvalidAllocationException])
    def validateAllocations(treatments: List[Treatment]) = {        
        val weightSum = treatments.foldLeft(0d)((sum, t) => sum + t.weight)

        if (weightSum != 1.0) {
            throw new InvalidAllocationException("Experiment not allocated at 100%");
        }
    }
    
    @throws(classOf[InvalidAllocationException])
    def validateHasAtLeastTwoTreatments(treatments: List[Treatment]) = {
        if (treatments.size < 2) {
            throw new InvalidAllocationException("At least two treatments are required to activate an experiment.");
        }
    }

    @throws(classOf[ExperimentAlreadyActiveException])
    def validateNotActive(experiment: Experiment) = {
        if (experiment.active) {
            throw new ExperimentAlreadyActiveException(s"Experiment '${experiment.id}' cannot be active.");
        }
    }

    @throws(classOf[InvalidTreatmentException])
    def validateTreatmentDoesNotExist(treatmentId: String, treatments: List[Treatment]) = {
        val treatmentExists = treatments.exists(t => treatmentId == t.id)
        
        if (treatmentExists) {
            throw new InvalidTreatmentException(s"Treatment '$treatmentId' already exists.");            
        }
    }

    @throws(classOf[InvalidTreatmentException])
    def validateTreatmentExists(treatmentId: String, treatments: List[Treatment]) = {
        val treatment = treatments.find(t => treatmentId == t.id)
        
        if (treatment.isEmpty) {
            throw new InvalidTreatmentException(s"Treatment '$treatmentId' is invalid");
        }
    }
   
}