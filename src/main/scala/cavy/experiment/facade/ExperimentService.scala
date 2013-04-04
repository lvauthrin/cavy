package cavy.experiment.facade

import cavy.experiment.domain.exception.ExperimentException
import cavy.experiment.domain.Experiment
import cavy.experiment.domain.exception.DuplicateExperimentException
import cavy.experiment.domain.exception.InvalidExperimentException

trait ExperimentService {

    /**
     * Activate an experiment.
     * 
     * @param experimentId the experiment
     * 
     * @return the updated experiment
     * 
     * @throws ExperimentException if there was a problem activating the experiment
     */
    @throws(classOf[ExperimentException])
    def activate(experimentId: String): Experiment;

    /**
     * Add treatments to an experiment
     *
     * @param experimentId the experiment id
     * @param id the treatment id
     * @param name the treatment name
     * @param weight the treatment weight
     * 
     * @return the updated experiment
     * 
     * @throws ExperimentException if there was a problem adding a treatment
     */
    @throws(classOf[ExperimentException])
    def addTreatment(experimentId: String, id: String, name: String, weight: Double): Experiment

    /**
     * Creates an experiment.
     * 
     * @param id  the experiment id
     * @param name the experiment name
     *
     * @return the new experiment
     * 
     * @throws DuplicateExperimentException if the experiment already exists
     */
    @throws(classOf[DuplicateExperimentException])
    def create(id: String, name: String): Experiment

    /**
     * Deactivate experiment.
     * 
     * @param experimentId the experiment
     * 
     * @return the updated experiment
     * 
     * @throws InvalidExperimentException if the experiment does not exist
     */
    @throws(classOf[InvalidExperimentException])
    def deactivate(experimentId: String): Experiment

    /**
     * Returns an experiment given an experiment identifier.
     * 
     * @param experimentId the experiment identifier
     * 
     * @return the experiment
     * 
     * @throws InvalidExperimentException if the experiment is not found
     */
    @throws(classOf[InvalidExperimentException])
    def get(experimentId: String): Experiment

    /**
     * Retrieves a list of all experiments.
     * 
     * @return the list of experiments
     */
    def list(): List[Experiment]

    /**
     * Update treatment allocations
     *
     * @param experimentId the experiment id
     * @param allocations a map of treatment ids to new weights
     *
     * @return the experiment
     *
     * @throws ExperimentException if there was a problem updating the treatments
     */
    @throws(classOf[ExperimentException])
    def updateTreatments(experimentId: String, allocations: Map[String, Double]): Experiment
    
}