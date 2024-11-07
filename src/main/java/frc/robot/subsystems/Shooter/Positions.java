
package frc.robot.subsystems.Shooter;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Radians;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkLowLevel.PeriodicFrame;
import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Measure;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.PivotConstants.PivotDirection;

//CODE DOES NOT CODE YET AHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

public class Positions extends SubsystemBase {

    public Positions() {

        //m_PivotEncoder = Pivot.
        //m_setPoint = Radians.of(getEncoderPosition());
    }
    public void pivotFromCamera(Measure<Angle> angle){
        //m_setPoint = angle;
    }
    public void pivotTo(Measure<Angle> angle) {
        //m_setPoint = angle;
        //m_PivotPIDController.setReference(angle.in(Radians), ControlType.kPosition);
    }

    public Command pivotToAMP(){
        return Commands.run(()-> pivotTo(PivotConstants.kPivotPresetAmpv2), this);
    }

    public Command pivotToSpeaker(){
        return Commands.run(()-> pivotTo(PivotConstants.kPivotPresetSubwooferv2), this);
    }

    public Command pivotToRange(){
        return Commands.run(()-> pivotTo(PivotConstants.kPivotPresetRangev2), this);
    }

    public Command pivotToRest(){
        return Commands.run(()-> pivotTo(PivotConstants.kPivotPresetRest), this);
    }
}