package frc.robot.subsystems.Shooter;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.ShooterConstants.ShooterState;

public class Shooter extends SubsystemBase {
    private final CANSparkFlex kickerMotor;
    private final CANSparkFlex shooterLeftMotor;
    private final CANSparkFlex shooterRightMotor;

    private ShooterState shooterState;

    private double leftTargetSpeed = 0.0;
    private double rightTargetSpeed = 0.0;
    private boolean leftPIDActive = false;
    private boolean rightPIDActive = false;

    public void kickerConfig(CANSparkFlex motor) {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(ShooterConstants.kKickerMotorCurrentLimit);

        motor.setIdleMode(ShooterConstants.kKickerMotorIdleMode);

        motor.setInverted(ShooterConstants.kKickerMotorInvert);

        SparkPIDController kickerPIDController = motor.getPIDController();

        RelativeEncoder kickerEncoder = motor.getEncoder();

        shooterState = ShooterState.IDLE;

    }

    public void shooterConfig(CANSparkFlex motor, boolean shooterLeftSide) {
        motor.restoreFactoryDefaults();

        motor.setSmartCurrentLimit(ShooterConstants.kShooterMotorCurrentLimit);

        motor.setIdleMode(ShooterConstants.kShooterMotorIdleMode);

        if (shooterLeftSide) {
            motor.setInverted(ShooterConstants.kShooterLeftMotorInverted);
            SparkPIDController shooterLeftPIDController = motor.getPIDController();
            RelativeEncoder shooterLeftEncoder = motor.getEncoder();
            shooterLeftPIDController.setP(ShooterConstants.PVal);
            shooterLeftEncoder.setPositionConversionFactor(ShooterConstants.REL_ENC_CONVERSION);
            shooterLeftEncoder.setVelocityConversionFactor(ShooterConstants.REL_ENC_CONVERSION);
        }

        if (!shooterLeftSide) {
            motor.setInverted(ShooterConstants.kShooterRightMotorInverted);
            SparkPIDController shooterRightPIDController = motor.getPIDController();
            RelativeEncoder shooterRightEncoder = motor.getEncoder();
            shooterRightPIDController.setP(ShooterConstants.PVal);
            shooterRightEncoder.setPositionConversionFactor(ShooterConstants.REL_ENC_CONVERSION);
            shooterRightEncoder.setVelocityConversionFactor(ShooterConstants.REL_ENC_CONVERSION);
        }
    }

    public Shooter() {
        kickerMotor = new CANSparkFlex(ShooterConstants.kKickerMotorCANID, MotorType.kBrushless);
        shooterLeftMotor = new CANSparkFlex(ShooterConstants.kShooterLeftCANID, MotorType.kBrushless);
        shooterRightMotor = new CANSparkFlex(ShooterConstants.kShooterRightCANID, MotorType.kBrushless);

        kickerConfig(kickerMotor);
        shooterConfig(shooterLeftMotor, true);
        shooterConfig(shooterRightMotor, false);

        SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(
                ShooterConstants.FEED_FORWARDKS,
                ShooterConstants.FEED_FORWARDKV,
                ShooterConstants.FEED_FORWARDKA);

        kickerMotor.burnFlash();
        shooterLeftMotor.burnFlash();
        shooterRightMotor.burnFlash();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        switch (shooterState) {
            case INTAKE:
                intake();
                break;
            case OUTTAKE:
                outtake();
                break;
            case SPEAKER:
                speakerMode();
                break;
            //case AMP:
                //ampSpeed();
                //break;
            case REVERSE:
                reverse();
            case IDLE:
                idle();
                break;
        }
    }

    public void reverse() {
        shooterState = ShooterState.REVERSE;
        shooterLeftMotor.set(ShooterConstants.kShooterReverseSpeed);
        shooterRightMotor.set(ShooterConstants.kShooterReverseSpeed);
    }

    public void toggleState(ShooterState state) {
        if (shooterState == state) {
            shooterState = ShooterState.IDLE;
        } else {
            shooterState = state;
        }
    }

    //public void setStateSpeaker(ShooterState state) {
        //shooterState = state;
    //}

    public void idle() {
        shooterState = ShooterState.IDLE;
        shooterLeftMotor.set(ShooterConstants.kShooterIdleSpeed);
        shooterRightMotor.set(ShooterConstants.kShooterIdleSpeed);
        kickerMotor.set(ShooterConstants.kShooterIdleSpeed);
    }

    public void stopAll() {
        shooterState = ShooterState.IDLE;
        kickerMotor.stopMotor();
        shooterLeftMotor.stopMotor();
        shooterRightMotor.stopMotor();
    }

    public void intake() {
        shooterState = ShooterState.INTAKE;
    }

    public void outtake() {
        shooterState = ShooterState.OUTTAKE;
    }

    //private void ampSpeed() {
        //shooterLeftMotor.set(0.1);
        //shooterRightMotor.set(0.1);
        //kickerMotor.set(0.1);
    //}

    /**
     * While called, angles the pivot of the shooter and sets the shooter to the
     * output speed necessary
     * to score from the bot's distance from the shooter. However, does not shoot
     * the note.
     */
    private void speakerMode() {
        shooterLeftMotor.set(0.6);
        shooterRightMotor.set(0.4);
        kickerMotor.set(0.75); // TODO: change to constant
    }

    public CANSparkFlex getShooterLeftMotor() {
        return shooterLeftMotor;
    }

    public double getLeftTargetSpeed() {
        return leftTargetSpeed;
    }

    public void setLeftTargetSpeed(double leftTargetSpeed) {
        this.leftTargetSpeed = leftTargetSpeed;
    }

    public double getRightTargetSpeed() {
        return rightTargetSpeed;
    }

    public void setRightTargetSpeed(double rightTargetSpeed) {
        this.rightTargetSpeed = rightTargetSpeed;
    }

    public boolean isLeftPIDActive() {
        return leftPIDActive;
    }

    public void setLeftPIDActive(boolean leftPIDActive) {
        this.leftPIDActive = leftPIDActive;
    }

    public boolean isRightPIDActive() {
        return rightPIDActive;
    }

    public void setRightPIDActive(boolean rightPIDActive) {
        this.rightPIDActive = rightPIDActive;
    }
}