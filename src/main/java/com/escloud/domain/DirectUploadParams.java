package com.escloud.domain;

import java.io.Serializable;

public class DirectUploadParams extends UploadParams implements Serializable {
    private Upload upload;

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }


    public class Upload {
        private String action;

        private String file_param_key;

        public Params params;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getFile_param_key() {
            return file_param_key;
        }

        public void setFile_param_key(String file_param_key) {
            this.file_param_key = file_param_key;
        }

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }
    }

    public class Params {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
