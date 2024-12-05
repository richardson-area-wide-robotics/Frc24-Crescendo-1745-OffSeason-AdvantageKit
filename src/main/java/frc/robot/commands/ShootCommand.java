package frc.robot.commands;

import frc.robot.subsystems.Shooter.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.ShooterConstants.ShooterState;
import frc.robot.subsystems.Shooter.Feeder;

public class ShootCommand extends Command {
    public ShootCommand(Shooter shooter, Feeder feeder) {
        Commands.runOnce(() -> {
          shooter.toggleState(ShooterState.SPEAKER);
        }, shooter)
        .andThen(feeder.shootNote())
        .andThen(Commands.runOnce(() -> {
          shooter.toggleState(ShooterState.IDLE);
        }));
    }
}
