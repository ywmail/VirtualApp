/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.kook.deviceinfo.persistence;

import android.os.Parcel;
import com.kook.common.utils.HVLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class PersistenceLayer {
    protected int VERSION = 3;
    private File mPersistenceFile;

    public PersistenceLayer(File persistenceFile) {
        this.mPersistenceFile = persistenceFile;
    }

    public final File getPersistenceFile() {
        return this.mPersistenceFile;
    }

    public abstract int getCurrentVersion();

    public void writeMagic(Parcel p) {
    }

    public boolean verifyMagic(Parcel p) {
        return true;
    }

    public abstract void writePersistenceData(Parcel var1);

    public abstract void readPersistenceData(Parcel var1, int var2);

    public void onPersistenceFileDamage() {
    }

    public void save() {
        Parcel p = Parcel.obtain();
        try {
            this.writeMagic(p);
            p.writeInt(this.getCurrentVersion());
            this.writePersistenceData(p);
            HVLog.d("mPersistenceFile:" + this.mPersistenceFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(this.mPersistenceFile);
            fos.write(p.marshall());
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            p.recycle();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void read() {
        File file = this.mPersistenceFile;
        Parcel p = Parcel.obtain();
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            int len = fis.read(bytes);
            fis.close();
            if (len != bytes.length) {
                throw new IOException("Unable to read Persistence file.");
            }
            p.unmarshall(bytes, 0, bytes.length);
            p.setDataPosition(0);
            if (!this.verifyMagic(p)) {
                this.onPersistenceFileDamage();
                throw new IOException("Invalid persistence file.");
            }
            int fileVersion = p.readInt();
            this.readPersistenceData(p, fileVersion);
        }
        catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
            }
        }
        finally {
            p.recycle();
        }
    }
}

