package com.escloud.helper.upload;

import java.io.File;
import java.util.Map;

public final class UploadManager {
    private final Recorder recorder;

    public UploadManager(Recorder recorder) {
        this.recorder = recorder;
    }

    public Map upload(File file, String key, String token) {
        String mime = "application/octet-stream";
        ResumeUploader uploader = new ResumeUploader(token, key, file, mime, this.recorder);
        return uploader.upload();
    }
}
