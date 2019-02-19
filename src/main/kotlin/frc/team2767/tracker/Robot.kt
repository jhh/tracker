package frc.team2767.tracker


import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team2767.tracker.control.Controls
import frc.team2767.tracker.subsystem.DriveSubsystem
import mu.KotlinLogging
import org.strykeforce.thirdcoast.telemetry.TelemetryController
import org.strykeforce.thirdcoast.telemetry.TelemetryService
import java.util.*
import java.util.function.Function


private val logger = KotlinLogging.logger {}


class Robot : TimedRobot() {

    override fun robotInit() {
        TELEMETRY.start()
    }

    override fun teleopPeriodic() {
        Scheduler.getInstance().run()
    }

    companion object {

        // Instantiate this before Subsystems because they use telemetry service.
        val TELEMETRY = TelemetryService(Function { TelemetryController(it) })

        val DRIVE = DriveSubsystem()

        // Controls initialize Commands so this should be instantiated last to prevent
        // NullPointerExceptions in commands that require() Subsystems above.
        val CONTROLS = Controls()

    }

}
