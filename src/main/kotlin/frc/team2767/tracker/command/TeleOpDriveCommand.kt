package frc.team2767.tracker.command


import edu.wpi.first.wpilibj.command.Command
import frc.team2767.tracker.Robot
import mu.KotlinLogging
import org.strykeforce.thirdcoast.swerve.SwerveDrive.DriveMode.TELEOP

private val logger = KotlinLogging.logger {}


private const val DEADBAND = 0.05


private val swerve = Robot.DRIVE
private val controls = Robot.CONTROLS.driverControls


class TeleOpDriveCommand : Command() {

    init {
        requires(swerve)
    }

    override fun initialize() = swerve.setDriveMode(TELEOP)

    override fun execute() {
        val forward = controls.forward.deadband()
        val strafe = controls.strafe.deadband()
        val azimuth = controls.yaw.deadband()

        swerve.drive(forward, strafe, azimuth)
    }

    override fun isFinished() = false

    override fun end() = swerve.drive(0.0, 0.0, 0.0)

    private fun Double.deadband() = if (Math.abs(this) < DEADBAND) 0.0 else this

}
