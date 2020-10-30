package com.codeages.escloud.helper.upload;

import java.io.File;

public interface Recorder {
    void set(String var1, byte[] var2);

    byte[] get(String var1);

    void del(String var1);

    String recorderKeyGenerate(String var1, File var2);

    String recorderKeyGenerate(String var1, String var2, String var3, String var4);
}
