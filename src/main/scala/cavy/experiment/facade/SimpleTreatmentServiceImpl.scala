package cavy.experiment.facade

import org.slf4j.LoggerFactory
import scala.math.BigDecimal
import cavy.experiment.domain.TreatmentOverrides
import cavy.experiment.domain.Treatment
import cavy.experiment.repository.ExperimentRepository
import com.google.common.hash.Hashing
import java.nio.charset.Charset
import cavy.experiment.domain.exception.InvalidExperimentException
import cavy.experiment.domain.exception.InvalidTreatmentException
import cavy.experiment.domain.Experiment

class SimpleTreatmentServiceImpl(experimentRepository: ExperimentRepository) extends TreatmentService {
    val log = LoggerFactory.getLogger(this.getClass())
    val experimentLog = LoggerFactory.getLogger("ExperimentLog")
    val max128BitValue = BigDecimal(BigInt(1).setBit(128));

    @volatile
    var overrides = Map[String, TreatmentOverrides]()
    updateOverrides()
    
    def get(experimentId: String, userId: String) = {
        try {
            getInner(experimentId, userId)
        } catch {
            case e: Exception => { 
                log.error(e.getMessage(), e)
                Treatment.CONTROL 
            }
        }
    }

    @throws(classOf[InvalidExperimentException])
    def getAll() = experimentRepository.getOverrides()

    private def getInner(experimentId: String, userId: String) = {
        val experiment = experimentRepository.get(experimentId, false)
        val activeExperiment = experiment.filter(e => e.active)
        
        activeExperiment match {
            case None => Treatment.CONTROL
            case Some(e) => {
                val treatment = getOverride(experimentId, userId)
                treatment match {
                    case None => getTreatmentFromPercentage(experiment.get, userId)
                    case Some(t) => t
                }
            }
        }
    }
    
    private def getPercentage(experimentId: String, userId: String, buckets: Int) = {
        val function = Hashing.md5();
        val experimentHash = function.hashString(s"${experimentId}-${userId}", Charset.forName("UTF-8"))
        Hashing.consistentHash(experimentHash, buckets) / buckets
    }
    
    private def getTreatmentFromPercentage(experiment: Experiment, userId: String) = {
        val bucket = getPercentage(experiment.id, userId, experiment.treatments.size);

        // TODO: Clean this up
        var sum = 0d
        val treatments = experiment.treatments.takeWhile { t =>
            sum += t.weight
            bucket < sum
        }
        
        treatments match {
            case List() => Treatment.CONTROL
            case list => treatments.last.id
        }
    }

    private def getOverride(experimentId: String, userId: String) = {
        val experimentOverrides = this.overrides.get(experimentId);

        experimentOverrides.flatMap { o =>
            o.userIdToTreatment.get(userId)
        }
    }

    def record(experimentId: String, userId: String, treatmentId: String, params: String*) {
        val logItem = if (params == null || params.isEmpty) {
            "%d|%s|%s|%s".format(System.currentTimeMillis(), experimentId, userId, treatmentId);
        } else {
            "%d|%s|%s|%s|%s".format(System.currentTimeMillis(), experimentId, userId, treatmentId, params.mkString("|"))
        }

        experimentLog.info(logItem);
    }

    @throws(classOf[InvalidExperimentException])
    def remove(experimentId: String, userId: String) {
        val experiment = experimentRepository.get(experimentId, false);

        experiment match {
            case None => throw new InvalidExperimentException(s"No experiment found with id '$experimentId'")
            case Some(e) => experimentRepository.removeOverride(experimentId, userId);
        }        
    }

    @throws(classOf[InvalidExperimentException])
    @throws(classOf[InvalidTreatmentException])
    def set(experimentId: String, userId: String, treatmentId: String) = {
        val experiment = experimentRepository.get(experimentId, false);

        experiment match {
            case None => throw new InvalidExperimentException(s"No experiment found with id '$experimentId'")
            case Some(e) => {
                ExperimentValidations.validateTreatmentExists(treatmentId, e.treatments)
                experimentRepository.setOverride(experimentId, userId, treatmentId)
            }
        }
    }

    @throws(classOf[InvalidExperimentException])
    def updateOverrides() {
        this.overrides = getAll();
    }

}