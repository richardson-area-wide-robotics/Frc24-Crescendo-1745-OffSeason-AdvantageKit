// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.ShooterConstants.ShooterState;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.Shooter.Pivot;
import frc.robot.subsystems.Shooter.Shooter;
import frc.robot.subsystems.drive.AutoTrajectory;
import frc.robot.subsystems.drive.DriveSubsystem;

public class RobotContainer {

  private static Intake INTAKE_SUBSYSTEM = new Intake();

  private static Shooter SHOOTER_SUBSYSTEM = new Shooter();

  private static Pivot PIVOT_SUBSYSTEM = new Pivot();

  private static final DriveSubsystem DRIVE_SUBSYSTEM = new DriveSubsystem(
    DriveSubsystem.initializeHardware(),
    Constants.Drive.DRIVE_ROTATE_PID,
    Constants.Drive.DRIVE_CONTROL_CENTRICITY,
    Constants.Drive.DRIVE_THROTTLE_INPUT_CURVE,
    Constants.Drive.DRIVE_TURN_INPUT_CURVE,
    Constants.Drive.DRIVE_TURN_SCALAR,
    Constants.HID.CONTROLLER_DEADBAND,
    Constants.Drive.DRIVE_LOOKAHEAD
  );

 

  private static final CommandXboxController PRIMARY_CONTROLLER = new CommandXboxController(Constants.HID.PRIMARY_CONTROLLER_PORT);

  private static final SendableChooser<Command> automodeChooser = new SendableChooser<>();

  public RobotContainer() {
    // Set drive command
    DRIVE_SUBSYSTEM.setDefaultCommand(
      DRIVE_SUBSYSTEM.driveCommand(
              PRIMARY_CONTROLLER::getLeftY,
              PRIMARY_CONTROLLER::getLeftX,
              PRIMARY_CONTROLLER::getRightX
      )
    );

    SHOOTER_SUBSYSTEM.kickerConfig(new CANSparkFlex(11, MotorType.kBrushless));
    SHOOTER_SUBSYSTEM.shooterConfig(new CANSparkFlex(13, MotorType.kBrushless), false);
    SHOOTER_SUBSYSTEM.shooterConfig(new CANSparkFlex(14, MotorType.kBrushless), true);



    // Setup AutoBuilder
    DRIVE_SUBSYSTEM.configureAutoBuilder();

    autoModeChooser();
    SmartDashboard.putData(Constants.SmartDashboard.SMARTDASHBOARD_AUTO_MODE, automodeChooser);

    // Bind buttons and triggers
    configureBindings();
  }

  private void configureBindings() {
    // Start button - toggle traction control
    PRIMARY_CONTROLLER.start().onTrue(DRIVE_SUBSYSTEM.toggleTractionControlCommand());
  
    // A button - go to amp
    PRIMARY_CONTROLLER.a().whileTrue(
      DRIVE_SUBSYSTEM.goToPoseCommand(
        Constants.Field.AMP
      )
    );



    //Intake + Outtake
    PRIMARY_CONTROLLER.leftBumper().whileTrue(
      INTAKE_SUBSYSTEM.runIntake()
    ).whileFalse(INTAKE_SUBSYSTEM.runStop());

    PRIMARY_CONTROLLER.rightBumper().whileTrue(
      INTAKE_SUBSYSTEM.runOuttake()
    ).whileFalse(INTAKE_SUBSYSTEM.runStop());

    //pivot up/down
    PRIMARY_CONTROLLER.leftTrigger().whileTrue(
      PIVOT_SUBSYSTEM.pivotUp()
    ).whileFalse(PIVOT_SUBSYSTEM.pivotIdle());

    PRIMARY_CONTROLLER.rightTrigger().whileTrue(
      PIVOT_SUBSYSTEM.pivotDown()
    ).whileFalse(PIVOT_SUBSYSTEM.pivotIdle());



    // shooter speaker
    PRIMARY_CONTROLLER.y().onTrue(
      Commands.runOnce(() -> {
            SHOOTER_SUBSYSTEM.toggleState(ShooterState.SPEAKER);
        }, SHOOTER_SUBSYSTEM));

    // shooter amp
    PRIMARY_CONTROLLER.x().onTrue(
      Commands.runOnce(() -> {
            SHOOTER_SUBSYSTEM.toggleState(ShooterState.AMP);
        }, SHOOTER_SUBSYSTEM));

    // B button - go to source
    PRIMARY_CONTROLLER.b().whileTrue(DRIVE_SUBSYSTEM.goToPoseCommand(Constants.Field.SOURCE));

    PRIMARY_CONTROLLER.povLeft().onTrue(DRIVE_SUBSYSTEM.resetPoseCommand(Pose2d::new));

   

  }

  /**
   * Run simlation related methods
   */
  public void simulationPeriodic() {
    REVPhysicsSim.getInstance().run();
  }

    /**
   * Add auto modes to chooser
   */
  private void autoModeChooser() {
    automodeChooser.setDefaultOption("Do nothing", Commands.none());
    automodeChooser.addOption("Leave", new AutoTrajectory(DRIVE_SUBSYSTEM, "Leave").getCommand());
    automodeChooser.addOption("Preload + 3 Ring", new AutoTrajectory(DRIVE_SUBSYSTEM, "Preload + 3 Ring").getCommand());
    automodeChooser.addOption("Preload + 1", new AutoTrajectory(DRIVE_SUBSYSTEM, "Preload + 1").getCommand());
  }

  /**
   * Get currently selected autonomous command
   * @return Autonomous command
   */
  public Command getAutonomousCommand() {
    return automodeChooser.getSelected();
  }
}
