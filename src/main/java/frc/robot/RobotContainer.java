// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.REVPhysicsSim;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Intake.Intake;
import frc.robot.subsystems.drive.AutoTrajectory;
import frc.robot.subsystems.drive.DriveSubsystem;

public class RobotContainer {

  private static Intake INTAKE_SUBSYSTEM = new Intake();

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


    PRIMARY_CONTROLLER.leftTrigger().whileTrue(
      INTAKE_SUBSYSTEM.runIntake()
    );

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
