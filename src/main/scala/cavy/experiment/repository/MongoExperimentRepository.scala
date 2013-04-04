package cavy.experiment.repository

import com.google.code.morphia.dao.BasicDAO
import cavy.experiment.domain.mongo.MongoExperiment
import cavy.experiment.domain.Experiment
import cavy.experiment.domain.mongo.MongoTreatment
import com.google.code.morphia.mapping.Mapper
import cavy.experiment.domain.Treatment
import scala.collection.JavaConversions._
import cavy.experiment.domain.TreatmentOverrides

// TODO: Switch over to Casbah/Salat
class MongoExperimentRepository(baseDAO: BasicDAO[MongoExperiment, String]) extends ExperimentRepository {
    
    def create(experiment: Experiment) {
        Option(experiment).map { e =>
            val mongoTreatments = e.treatments.map { t => MongoTreatment(t.id, t.name, t.weight, t.control) }
            val mongoExperiment = MongoExperiment(e.id, e.name, e.active, e.startDate, e.endDate, mongoTreatments, Map())
            baseDAO.save(mongoExperiment)
        }
    }
    
    def get(experimentId: String, fetchOverrides: Boolean) = {
        val query = baseDAO.createQuery().field(Mapper.ID_KEY).equal(experimentId)

        if (!fetchOverrides) {
            query.retrievedFields(false, MongoExperiment.OVERRIDES)
        }

        val mongoExperiment = query.get()
        
        Option(mongoExperiment).map { me =>
            val treatments = me.treatments.map { mt => Treatment(mt.id, mt.name, mt.weight, mt.control) }
            Experiment(me.id, me.name, me.active, me.startDate, me.endDate, treatments)
        }
    }

    def getOverrides() = {
        val mongoExperiments = baseDAO.find().asList();
        
        mongoExperiments.foldLeft(Map[String, TreatmentOverrides]()) { (map, me) =>
            map + (me.id -> TreatmentOverrides(me.id, me.overrides))
        }
    }
    
    def list() = {
        val mongoExperiments = baseDAO.find().asList();
        
        mongoExperiments.toList.map { me =>
            val treatments = me.treatments.map { mt => Treatment(mt.id, mt.name, mt.weight, mt.control) }
            Experiment(me.id, me.name, me.active, me.startDate, me.endDate, treatments)
        }
    }
    
    def removeOverride(experimentId: String, userId: String) {
        val q = baseDAO.createQuery().filter(Mapper.ID_KEY, experimentId);
        val ops = baseDAO.createUpdateOperations().removeAll(MongoExperiment.OVERRIDES, userId);
        baseDAO.updateFirst(q, ops);
    }
    
    def setOverride(experimentId: String, userId: String, treatmentId: String) {
        val q = baseDAO.createQuery().filter(Mapper.ID_KEY, experimentId);
        val ops = baseDAO.createUpdateOperations().set(MongoExperiment.OVERRIDES + "." + userId, treatmentId);
        baseDAO.updateFirst(q, ops);
    }

    def updateActiveState(experiment: Experiment) {
        val q = baseDAO.createQuery().filter(Mapper.ID_KEY, experiment.id);
        val ops = baseDAO.createUpdateOperations();
        ops.set(MongoExperiment.IS_ACTIVE, experiment.active);
        
        if (experiment.startDate != null) {
            ops.set(MongoExperiment.START_DATE, experiment.startDate);   
        }        
        
        if (experiment.endDate != null) {
            ops.set(MongoExperiment.END_DATE, experiment.endDate);            
        }

        baseDAO.updateFirst(q, ops);
    }

    def updateTreatments(experimentId: String , treatments: List[Treatment]) {
        val mongoTreatments = treatments.map { t => MongoTreatment(t.id, t.name, t.weight, t.control) } 
        val q = baseDAO.createQuery().filter(Mapper.ID_KEY, experimentId);
        val ops = baseDAO.createUpdateOperations().set(MongoExperiment.TREATMENT_LIST, mongoTreatments)
        baseDAO.updateFirst(q, ops);
    }

}