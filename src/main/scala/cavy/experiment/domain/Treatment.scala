package cavy.experiment.domain

object Treatment {
    final val CONTROL = "control";
    final val DEFAULT = Treatment(CONTROL, "Control Treatment", 1.0, true);
}

case class Treatment(val id: String, val name: String, val weight: Double, val control: Boolean)