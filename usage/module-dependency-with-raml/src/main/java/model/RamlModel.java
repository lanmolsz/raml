package model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * Created by E355 on 2016/4/20.
 */
public class RamlModel {
    private Boolean prop1;
    private boolean prop2;
    private byte prop3;
    private char prop4;
    private short prop5;
    private int prop6;
    private long prop7;
    private float prop8;
    private double prop9;
    private Gender gender;
    private String text;


    public Boolean getProp1() {
        return prop1;
    }

    public void setProp1(Boolean prop1) {
        this.prop1 = prop1;
    }

    public boolean isProp2() {
        return prop2;
    }

    public void setProp2(boolean prop2) {
        this.prop2 = prop2;
    }

    public byte getProp3() {
        return prop3;
    }

    public void setProp3(byte prop3) {
        this.prop3 = prop3;
    }

    public char getProp4() {
        return prop4;
    }

    public void setProp4(char prop4) {
        this.prop4 = prop4;
    }

    public short getProp5() {
        return prop5;
    }

    public void setProp5(short prop5) {
        this.prop5 = prop5;
    }

    public int getProp6() {
        return prop6;
    }

    public void setProp6(int prop6) {
        this.prop6 = prop6;
    }

    public long getProp7() {
        return prop7;
    }

    public void setProp7(long prop7) {
        this.prop7 = prop7;
    }

    public float getProp8() {
        return prop8;
    }

    public void setProp8(float prop8) {
        this.prop8 = prop8;
    }

    public double getProp9() {
        return prop9;
    }

    public void setProp9(double prop9) {
        this.prop9 = prop9;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
