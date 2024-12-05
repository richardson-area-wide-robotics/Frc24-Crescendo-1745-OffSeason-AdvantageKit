package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.Shooter.Feeder;

public class IntakeCommand extends Command {
    public IntakeCommand(Feeder feeder, Intake intake) {
        feeder.feedNote()
        .alongWith(intake.runIntake());
    }
}
