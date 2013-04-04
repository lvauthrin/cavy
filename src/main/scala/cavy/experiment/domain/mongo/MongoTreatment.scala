package cavy.experiment.domain.mongo

import com.google.code.morphia.annotations.Property
import com.google.code.morphia.annotations.Entity
import com.google.code.morphia.annotations.Embedded
import scala.beans.BeanProperty

object MongoTreatment {
    final val TREATMENT_ID = "tId";
    final val NAME = "dN";
    final val WEIGHT = "w";
    final val IS_CONTROL = "c";
}

@Embedded
@Entity(noClassnameStored = true)
case class MongoTreatment(
    @BeanProperty @Property(MongoTreatment.TREATMENT_ID) id: String,
    @BeanProperty @Property(MongoTreatment.NAME) name: String, 
    @BeanProperty @Property(MongoTreatment.WEIGHT) weight: Double,
    @BeanProperty @Property(MongoTreatment.IS_CONTROL) control: Boolean
)