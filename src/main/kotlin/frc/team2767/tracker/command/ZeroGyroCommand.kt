package frc.team2767.tracker.command

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team2767.tracker.Robot

class ZeroGyroCommand : InstantCommand() {
    private val swerve = Robot.DRIVE

    init {
        requires(swerve)
    }

    override fun initialize() = swerve.resetGyro()
}