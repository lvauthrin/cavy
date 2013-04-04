/**
 * *****************************************************************************
 * Unpublished work, Copyright 2013, Clear Channel Communications, All Rights Reserved
 * ****************************************************************************
 */

package cavy.experiment.facade

import cavy.experiment.domain.exception._

trait TreatmentService {

    /**
     * Get a treatment for a given experiment id and user id
     *
     * @param experimentId the experiment id
     * @param userId the user id
     *
     * @return the treatment; "control" if control or treatment id otherwise
     */
    def get(experimentId: String, userId: String): String

    /**
     * Record a treatment selection
     *
     * @param experimentId the experiment id
     * @param userId the user id
     * @param treatmentId the treatment id
     * @param params additional parameters to log
     */
    def record(experimentId: String, userId: String, treatmentId: String, params: String*): Unit

    /**
     * The treatment override to remove
     *
     * @param experimentId the experiment id
     * @param userId the user id
     * @throws InvalidExperimentException
     */
    @throws(classOf[ExperimentException])
    def remove(experimentId: String, userId: String): Unit 

    /**
     * Set treatment override for a give experiment id an user id
     *
     * @param experimentId the experiment id
     * @param userId the user id
     * @param treatmentId the treatment id
     *
     * @throws InvalidExperimentException if the experiment does not exist
     * @throws InvalidTreatmentException if the treatment does not exist for that experiment
     */
    @throws(classOf[ExperimentException])
    def set(experimentId: String, userId: String, treatmentId: String ): Unit
    
}