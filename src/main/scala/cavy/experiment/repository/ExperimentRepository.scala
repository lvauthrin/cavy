package cavy.experiment.repository

import cavy.experiment.domain.Experiment
import cavy.experiment.domain.TreatmentOverrides
import cavy.experiment.domain.Treatment

trait ExperimentRepository {

    /**
     * Create an experiment
     *
     * @param experiment the experiment
     */
    def create(experiment: Experiment): Unit

    /**
     * Get an experiment by id
     *
     * @param experimentId the experiment id
     * @param fetchOverrides whether to retrieve overrides
     *
     * @return the experiment
     */
    def get(experimentId: String, fetchOverrides: Boolean): Option[Experiment]

    /**
     * @return the experiment overrides
     */
    def getOverrides(): Map[String, TreatmentOverrides]

    /**
     * Get all experiments
     *
     * @return all experiments
     */
    def list(): List[Experiment]

    /**
     * Removes a treatment override
     *
     * @param experimentId the experiment id
     * @param userId the user id
     */
    def removeOverride(experimentId: String, userId: String): Unit

    /**
     * Set a treatment override
     *
     * @param experimentId the experiment id
     * @param userId the user id
     * @param treatmentId the treatment id
     */
    def setOverride(experimentId: String, userId: String, treatmentId: String): Unit

    /**
     * Update an experiment
     *
     * @param experiment
     *            the experiment id
     */
    def updateActiveState(experiment: Experiment): Unit

    /**
     * Updates the list of treatments for an experiment
     *
     * @param experimentId
     *            the experiment id
     * @param treatments
     *            the treatments
     */
    def updateTreatments(experimentId: String, treatments: List[Treatment]): Unit
}