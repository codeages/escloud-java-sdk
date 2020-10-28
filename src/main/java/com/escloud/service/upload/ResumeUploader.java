package com.escloud.service.upload;

import com.escloud.httpClient.Client;
import com.escloud.util.Json;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public final class ResumeUploader {
    private final String upToken;
    private final String key;
    private final File f;
    private final long size;
    private final String mime;
    private final String[] contexts;
    private final byte[] blockBuffer;
    private final Recorder recorder;
    private final long modifyTime;
    private final ResumeUploader.RecordHelper helper;
    private FileInputStream file;
    private String host = "http://upload.qiqiuyun.net";

    public ResumeUploader(String upToken, String key, File file, String mime, Recorder recorder) {
        this.upToken = upToken;
        this.key = key;
        this.f = file;
        this.size = file.length();
        this.mime = mime == null ? "application/octet-stream" : mime;
        long count = (this.size + 4194304L - 1L) / 4194304L;
        this.contexts = new String[(int) count];
        this.blockBuffer = new byte[4194304];
        this.recorder = recorder;
        this.modifyTime = this.f.lastModified();
        this.helper = new ResumeUploader.RecordHelper();
    }

    public Map upload() {
        Map var1;
        try {
            var1 = this.upload0();
        } finally {
            this.close();
        }

        return var1;
    }

    private Map upload0() {
        long uploaded = this.helper.recoveryFromRecord();

        try {
            this.file = new FileInputStream(this.f);
        } catch (FileNotFoundException var24) {
            throw new RuntimeException(var24);
        }

        int var4 = this.blockIdx(uploaded);

        try {
            this.file.skip(uploaded);
        } catch (IOException var23) {
            this.close();
            throw new RuntimeException(var23);
        }

        while (uploaded < this.size) {
            int blockSize = this.nextBlockSize(uploaded);
            try {
                this.file.read(this.blockBuffer, 0, blockSize);
            } catch (IOException var22) {
                this.close();
                throw new RuntimeException(var22);
            }

            Map response = null;

            try {
                if (blockSize != this.blockBuffer.length) {
                    byte[] fileByte = new byte[blockSize];
                    int i = 0;
                    while (i < blockSize) {
                        fileByte[i] = this.blockBuffer[i];
                        i++;
                    }
                    response = this.makeBlock(fileByte, blockSize);
                    fileByte = null;
                } else {
                    response = this.makeBlock(this.blockBuffer, blockSize);
                }
            } catch (RuntimeException var27) {
                throw new RuntimeException(var27);
            }

            Map<String, String> blockInfo = response;
            this.contexts[var4++] = blockInfo.get("ctx");
            uploaded += (long) blockSize;
            this.helper.record(uploaded);
        }

        this.close();

        Map var29 = null;
        try {
            Map var28 = this.makeFile();
            return var28;
        } catch (RuntimeException | UnsupportedEncodingException | URISyntaxException var25) {
            try {
                var29 = this.makeFile();
            } catch (RuntimeException var20) {
                throw var20;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } finally {
            this.helper.removeRecord();
        }

        return var29;
    }

    private Map makeBlock(byte[] block, int blockSize) throws RuntimeException {
        String url = this.host + "/mkblk/" + blockSize;
        return this.post(url, block, 0, blockSize);
    }

    private void close() {
        try {
            this.file.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    private String fileUrl() throws UnsupportedEncodingException {
        Base64.Encoder encoder = Base64.getEncoder();
        String url = this.host + "/mkfile/" + this.size + "/mimeType/" + encoder.encodeToString(this.mime.getBytes("UTF-8")) + "/fname/" + encoder.encodeToString(this.f.getName().getBytes("UTF-8"));
        final StringBuilder b = new StringBuilder(url);
        if (this.key != null) {
            b.append("/key/");
            b.append(encoder.encodeToString(this.key.getBytes("UTF-8")));
        }
        return b.toString();
    }

    private Map makeFile() throws UnsupportedEncodingException, URISyntaxException {
        String url;
        url = this.fileUrl();
        String s = StringUtils.join(Arrays.asList(contexts), ",");
        return this.post(url, s.getBytes("UTF-8"));
    }

    private Map post(String url, byte[] data) throws RuntimeException, UnsupportedEncodingException, URISyntaxException {
        Client client = new Client();
        Map header = new HashMap<>();
        header.put("Authorization", "UpToken " + this.upToken);
        return Json.jsonToObject(client.request("POST", url, data, header), Map.class);
    }

    private Map post(String url, byte[] data, int offset, int size) throws RuntimeException {
        Client client = new Client();
        Map header = new HashMap<>();
        header.put("Authorization", "UpToken " + this.upToken);
        header.put("Content-Type", "application/octet-stream");
        header.put("User-Agent", userAgent());
        try {
            return Json.jsonToObject(client.request("POST", url, data, header), Map.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String userAgent() {
        final String javaVersion = "Java/" + System.getProperty("java.version");
        final String os = System.getProperty("os.name") + " "
                + System.getProperty("os.arch") + " " + System.getProperty("os.version");
        final String sdk = "QiniuJava/1.1.1";
        String userApp = "";
        return sdk + userApp + " (" + os + ") " + javaVersion;
    }

    private int nextBlockSize(long uploaded) {
        return this.size < uploaded + 4194304L ? (int) (this.size - uploaded) : 4194304;
    }

    private int blockIdx(long offset) {
        return (int) (offset / 4194304L);
    }

    private class RecordHelper {
        private RecordHelper() {
        }

        long recoveryFromRecord() {
            try {
                return this.recoveryFromRecord0();
            } catch (Exception var2) {
                var2.printStackTrace();
                return 0L;
            }
        }

        long recoveryFromRecord0() {
            if (ResumeUploader.this.recorder == null) {
                return 0L;
            } else {
                String recorderKey = ResumeUploader.this.recorder.recorderKeyGenerate(ResumeUploader.this.key, ResumeUploader.this.f);
                byte[] data = ResumeUploader.this.recorder.get(recorderKey);
                if (data == null) {
                    return 0L;
                } else {
                    String jsonStr = new String(data);
                    ResumeUploader.RecordHelper.Record r = (ResumeUploader.RecordHelper.Record) (new Gson()).fromJson(jsonStr, ResumeUploader.RecordHelper.Record.class);
                    if (r.offset != 0L && r.modify_time == ResumeUploader.this.modifyTime && r.size == ResumeUploader.this.size && r.contexts != null && r.contexts.length != 0) {
                        for (int i = 0; i < r.contexts.length; ++i) {
                            ResumeUploader.this.contexts[i] = r.contexts[i];
                        }

                        return r.offset;
                    } else {
                        return 0L;
                    }
                }
            }
        }

        void removeRecord() {
            try {
                if (ResumeUploader.this.recorder != null) {
                    String recorderKey = ResumeUploader.this.recorder.recorderKeyGenerate(ResumeUploader.this.key, ResumeUploader.this.f);
                    ResumeUploader.this.recorder.del(recorderKey);
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }

        void record(long offset) {
            try {
                if (ResumeUploader.this.recorder == null || offset == 0L) {
                    return;
                }

                String recorderKey = ResumeUploader.this.recorder.recorderKeyGenerate(ResumeUploader.this.key, ResumeUploader.this.f);
                String data = (new Gson()).toJson(new ResumeUploader.RecordHelper.Record(ResumeUploader.this.size, offset, ResumeUploader.this.modifyTime, ResumeUploader.this.contexts));
                ResumeUploader.this.recorder.set(recorderKey, data.getBytes());
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }

        private class Record {
            long size;
            long offset;
            long modify_time;
            String[] contexts;

            Record(long size, long offset, long modify_time, String[] contexts) {
                this.size = size;
                this.offset = offset;
                this.modify_time = modify_time;
                this.contexts = contexts;
            }
        }
    }
}
