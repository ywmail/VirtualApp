/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.InputDevice
 *  android.view.InputDevice$MotionRange
 */
package com.kook.deviceinfo.impClassMethods;

import android.content.Context;
import android.view.InputDevice;
import com.kook.deviceinfo.impClasses.BuildInfo;
import com.kook.deviceinfo.models.InputModel;
import java.util.ArrayList;
import java.util.List;

public class InputDeviceMethod {
    private final Context context;
    ArrayList<InputModel> inputList = new ArrayList();
    List<InputDevice.MotionRange> motionRangeList;
    int[] id;
    BuildInfo buildInfo;
    int i = 0;

    public InputDeviceMethod(Context context) {
        this.context = context;
        this.id = InputDevice.getDeviceIds();
        this.inputDevices();
    }

    private void inputDevices() {
        this.buildInfo = new BuildInfo(this.context);
        for (int facing : this.id) {
            String keyboardType = null;
            String axis = null;
            String range = null;
            String flat = null;
            String fuzz = null;
            String resol = null;
            String source = null;
            String s = null;
            boolean hasMotionRange = false;
            StringBuilder stringBuilder = new StringBuilder();
            this.motionRangeList = null;
            InputDevice inputDevice = InputDevice.getDevice((int)facing);
            String name = inputDevice.getName();
            String vendorId = String.valueOf(inputDevice.getVendorId());
            String proId = String.valueOf(inputDevice.getProductId());
            String hasVibrator = String.valueOf(inputDevice.getVibrator().hasVibrator());
            switch (inputDevice.getKeyboardType()) {
                case 0: {
                    keyboardType = "None";
                    break;
                }
                case 1: {
                    keyboardType = "Non-Alphabetic";
                    break;
                }
                case 2: {
                    keyboardType = "Alphabetic";
                }
            }
            String deviceId = String.valueOf(inputDevice.getId());
            String desc = inputDevice.getDescriptor();
            String sources = String.valueOf(inputDevice.getSources());
            if ((inputDevice.getSources() & 0x101) == 257) {
                stringBuilder.append("Keyboard, ");
            }
            if ((inputDevice.getSources() & 0x1000010) == 0x1000010) {
                stringBuilder.append("JoyStick, ");
            }
            if ((inputDevice.getSources() & 0x201) == 513) {
                stringBuilder.append("Dpad, ");
            }
            if ((inputDevice.getSources() & 0x2002) == 8194) {
                stringBuilder.append("Mouse, ");
            }
            if ((inputDevice.getSources() & 0x401) == 1025) {
                stringBuilder.append("GamePad, ");
            }
            if ((inputDevice.getSources() & 0x100008) == 0x100008) {
                stringBuilder.append("TouchPad, ");
            }
            if ((inputDevice.getSources() & 0x10004) == 65540) {
                stringBuilder.append("TrackBall, ");
            }
            if ((inputDevice.getSources() & 0x4002) == 16386) {
                stringBuilder.append("Stylus, ");
            }
            if ((inputDevice.getSources() & 0x1002) == 4098) {
                stringBuilder.append("TouchScreen, ");
            }
            if (stringBuilder.toString().length() > 0) {
                s = sources + " (" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2) + ")";
            }
            this.motionRangeList = inputDevice.getMotionRanges();
            if (this.motionRangeList.size() != 0) {
                hasMotionRange = true;
                for (int i = 0; i < this.motionRangeList.size(); ++i) {
                    axis = this.buildInfo.getAxis(this.motionRangeList.get(i).getAxis());
                    range = String.valueOf(this.motionRangeList.get(i).getRange());
                    resol = String.valueOf(this.motionRangeList.get(i).getResolution());
                    flat = String.valueOf(this.motionRangeList.get(i).getFlat());
                    fuzz = String.valueOf(this.motionRangeList.get(i).getFuzz());
                    source = "0x" + Integer.toHexString(this.motionRangeList.get(i).getSource());
                }
            }
            if (Integer.parseInt(deviceId) < 0) continue;
            if (this.i == 1) {
                this.inputList.add(new InputModel(name, desc, vendorId, proId, hasVibrator, keyboardType, deviceId, s, axis, range, flat, fuzz, resol, source, hasMotionRange));
            }
            this.inputList.add(new InputModel(name, desc, vendorId, proId, hasVibrator, keyboardType, deviceId, s, axis, range, flat, fuzz, resol, source, hasMotionRange));
            ++this.i;
        }
    }

    public ArrayList<InputModel> getInputList() {
        return this.inputList;
    }
}

