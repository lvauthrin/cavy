package cavy.experiment.domain.mongo

import java.util.Date
import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Property
import com.google.code.morphia.annotations.Embedded
import com.google.code.morphia.annotations.Id
import scala.beans.BeanProperty

object MongoExperiment {
    final val NAME = "nm";
    final val IS_ACTIVE = "a";
    final val START_DATE = "sD";
    final val END_DATE = "eD";
    final val TREATMENT_LIST = "tl";
    final val OVERRIDES = "ov";
}

@Entity(value = "experiment", noClassnameStored = true)
case class MongoExperiment(
    @BeanProperty @Id id: String,
    @BeanProperty @Property(MongoExperiment.NAME) name: String,
    @BeanProperty @Property(MongoExperiment.IS_ACTIVE) active: Boolean,
    @BeanProperty @Property(MongoExperiment.START_DATE) startDate: Date,
    @BeanProperty @Property(MongoExperiment.END_DATE) endDate: Date,
    @BeanProperty @Embedded(MongoExperiment.TREATMENT_LIST) treatments: List[MongoTreatment],
    @BeanProperty @Property(MongoExperiment.OVERRIDES) overrides: Map[String, String] 
)