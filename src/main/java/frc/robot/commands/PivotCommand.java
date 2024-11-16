package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ShooterConstants.ShooterState;
import frc.robot.subsystems.Shooter.Pivot;

public class PivotCommand extends Command {
    public PivotCommand(Pivot pivot, ShooterState shooterState) {
        switch (shooterState) {
            case SPEAKER:
            pivot.pivotPresetSpeaker();
            break;
            case AMP:
            pivot.pivotPresetAMP();
            break;
        }
    }
}
