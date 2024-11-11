package frc.robot.subsystems.Shooter;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FeederConstants;

public class Feeder extends SubsystemBase {
    private final CANSparkFlex m_feederMotor;
    private final DigitalInput m_sensor;
    public boolean hasNote;
    // private ShuffleboardTab feederTab = Shuffleboard.getTab("Feeder");

    // private GenericEntry m_sensorEntry = feederTab.add("Has Note",
    // false).getEntry();
    // private GenericEntry m_speedEntry = feederTab.add("Feeder Speed",
    // 0).getEntry();
    // private GenericEntry m_currentEntry = feederTab.add("Feeder Current",
    // 0).getEntry();

    public Feeder() {
        m_feederMotor = new CANSparkFlex(FeederConstants.kFeederCANID, CANSparkFlex.MotorType.kBrushless);
        m_sensor = new DigitalInput(FeederConstants.kFeederSensorPort);

        m_feederMotor.restoreFactoryDefaults();
        m_feederMotor.setSmartCurrentLimit(FeederConstants.kFeederCurrentLimit);
        m_feederMotor.setInverted(FeederConstants.kFeederMotorInverted);
        m_feederMotor.setIdleMode(IdleMode.kBrake);
        m_feederMotor.burnFlash();

        setDefaultCommand();
    }

    /*
     * Method that runs periodically
     */
    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Indicator", getIndicator());
        // m_sensorEntry.setBoolean(hasNote());
        // m_speedEntry.setDouble(getSpeed());
        // m_currentEntry.setDouble(m_feederMotor.getOutputCurrent());
    }

    /**
     * @return true if the sensor is blocked
     *         have to invert signal due to the sensor being a break beam sensor
     */
    public boolean hasNote() {
        return !m_sensor.get();
    }

    /**
     * Method that spins the feeder motor at full positive speed - intaking
     */
    public void intake() {
        m_feederMotor.set(FeederConstants.kFeederSpeed);
    }

    /**
     * Method that spins the feeder motor at full negative speed - outtaking
     */
    public void outtake() {
        m_feederMotor.set(-FeederConstants.kFeederSpeed);
    }

    /**
     * Sets the default command for the feeder
     */
    public void setDefaultCommand() {
        super.setDefaultCommand(Commands.run(() -> m_feederMotor.stopMotor(), this));
    }

    /**
     * Command that spits the note back towards the intake
     */
    public Command spitNote() {
        return Commands.run(() -> outtake(), this);
    }

    public void setIndicator(boolean note) {
        hasNote = note;
    }

    public boolean getIndicator() {
        return hasNote;
    }

    /**
     * Functional Command that recieves the note from the intake and spins the
     * feeder motor until the note is detected
     */
    public Command feedNote() {
        return new FunctionalCommand(
                () -> {
                    return;
                },
                () -> {
                    intake();
                },
                (interrupted) -> {
                    m_feederMotor.stopMotor();
                    setIndicator(true);

                },
                () -> {
                    return hasNote();
                },
                this);
    }

    /**
     * Functional Command that shoots the note from the feeder into the shooter
     * wheels and stops when the note is no longer detected
     */
    public Command shootNote() {
        return new FunctionalCommand(
                () -> {
                    return;
                },
                () -> {
                    intake();
                },
                (interrupted) -> {
                    m_feederMotor.stopMotor();
                },
                () -> {
                    return false;
                },
                this);
    }

}
