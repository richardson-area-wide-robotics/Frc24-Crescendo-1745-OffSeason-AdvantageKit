package frc.robot.subsystems.Intake;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.IntakeConstants.IntakeState;

public class Intake extends SubsystemBase {

    private final CANSparkMax m_intakeMotor;

    private IntakeState m_intakeState = IntakeState.IDLE;

    // private ShuffleboardTab intakeTab = Shuffleboard.getTab("Intake");
    // private GenericEntry m_speedEntry = intakeTab.add("Intake Speed", 0).getEntry();
    // private GenericEntry m_currentEntry = intakeTab.add("Intake Current", 0).getEntry();
    // private GenericEntry m_stateEntry = intakeTab.add("Intake State", m_intakeState).getEntry();

    public Intake() {
    m_intakeMotor = new CANSparkMax(IntakeConstants.kIntakeCANID, CANSparkMax.MotorType.kBrushless);

    m_intakeMotor.restoreFactoryDefaults();
    m_intakeMotor.setSmartCurrentLimit(IntakeConstants.kIntakeCurrentLimit);
    m_intakeMotor.setInverted(IntakeConstants.kIntakeMotorInverted);
    m_intakeMotor.setIdleMode(IntakeConstants.kIntakeIdleMode);
    m_intakeMotor.burnFlash();


    this.getDefaultCommand();
    }

    public void SPINTHEFUCKIGINTAKE()
    {
        m_intakeMotor.set(IntakeConstants.kIntakeSpeed);
    }

    /**
     * Commands the nntake to spin in the positive direction - intaking
     */
    public Command runIntake() {
        m_intakeState = IntakeState.INTAKE;
        return Commands.run(()-> m_intakeMotor.set(IntakeConstants.kIntakeSpeed), this);
    }

    /**
     * Commands the intake stop
     */
    public Command runStop() {
        m_intakeState = IntakeState.OUTTAKE;
        return Commands.run(()-> m_intakeMotor.set(0), this);
    }

     /**
     * Commands the intake to spin in the negative direction - outtakeing
     */
    public Command runOuttake() {
        m_intakeState = IntakeState.IDLE;
        return Commands.run(()-> m_intakeMotor.set(IntakeConstants.kOuttakeSpeed), this);
    }


    /**
     * Set the default Command for the subsystem
     */
    public void setDefaultCommand() {
        m_intakeState = IntakeState.IDLE;
    }
    
} 

