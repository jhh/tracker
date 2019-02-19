package frc.team2767.tracker.subsystem

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team2767.tracker.Robot
import frc.team2767.tracker.command.TeleOpDriveCommand
import mu.KotlinLogging
import org.strykeforce.thirdcoast.swerve.SwerveDrive
import org.strykeforce.thirdcoast.swerve.SwerveDrive.DriveMode
import org.strykeforce.thirdcoast.swerve.SwerveDriveConfig
import org.strykeforce.thirdcoast.swerve.Wheel

private val logger = KotlinLogging.logger {}


private const val DRIVE_SETPOINT_MAX = 0.0
private const val ROBOT_LENGTH = 1.0
private const val ROBOT_WIDTH = 1.0

class DriveSubsystem : Subsystem() {

    private val swerve = SwerveDrive(
        SwerveDriveConfig().apply {
            wheels = wheels()
            gyro = AHRS(SPI.Port.kMXP)
            length = ROBOT_LENGTH
            width = ROBOT_WIDTH
            gyroLoggingEnabled = true
            summarizeTalonErrors = false
        }
    ).apply { zeroAzimuthEncoders() }

    override fun initDefaultCommand() {
        defaultCommand = TeleOpDriveCommand()
    }

    fun setDriveMode(mode: DriveMode) = swerve.setDriveMode(mode)

    fun resetGyro() {
        val gyro = swerve.gyro
        gyro.angleAdjustment = 0.0
        val adj = gyro.angle % 360
        gyro.angleAdjustment = -adj
        logger.info { "resetting gyro zero $adj" }
    }

    fun drive(forward: Double, strafe: Double, azimuth: Double) = swerve.drive(-forward, -strafe, -azimuth)

}

private fun swerveDrive() = SwerveDrive(SwerveDriveConfig().apply {
    wheels = wheels()
    gyro = AHRS(SPI.Port.kMXP)
    length = ROBOT_LENGTH
    width = ROBOT_WIDTH
    gyroLoggingEnabled = true
    summarizeTalonErrors = false
})

private fun wheels(): Array<Wheel> {
    val azimuthConfig = TalonSRXConfiguration().apply {
        primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative
        continuousCurrentLimit = 10
        peakCurrentLimit = 0
        peakCurrentDuration = 0
        slot_0.apply {
            kP = 10.0
            kI = 0.0
            kD = 100.0
            kF = 1.0
            integralZone = 0
            allowableClosedloopError = 0
        }
        motionAcceleration = 10_000
        motionCruiseVelocity = 800
    }


    val driveConfig = TalonSRXConfiguration().apply {
        primaryPID.selectedFeedbackSensor = FeedbackDevice.CTRE_MagEncoder_Relative
        continuousCurrentLimit = 40
        peakCurrentLimit = 0
        peakCurrentDuration = 0
    }
    val telemetryService = Robot.TELEMETRY
    telemetryService.stop()

    val timeout = 10

    return Array(4) {
        Wheel(
            TalonSRX(it).apply {
                configAllSettings(azimuthConfig, timeout)
                telemetryService.register(this)

            },
            TalonSRX(it + 10).apply {
                configAllSettings(driveConfig, timeout)
                setNeutralMode(NeutralMode.Brake)
                telemetryService.register(this)

            }, DRIVE_SETPOINT_MAX
        )
    }

}
