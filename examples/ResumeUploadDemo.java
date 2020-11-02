import com.codeages.escloud.Auth;
import com.codeages.escloud.helper.upload.FileRecorder;
import com.codeages.escloud.helper.upload.Recorder;
import com.codeages.escloud.helper.upload.UploadManager;
import com.codeages.escloud.service.ResourceService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ResumeUploadDemo {
    String accessKey = "your access key";
    String secretKey = "your secret key";
    Auth auth = new Auth(accessKey, secretKey);
    ResourceService resourceService = new ResourceService(auth, new HashMap<String, String>());

    public Map upload() {
        try {
            File file = new File("/var/www/edusoho.mp4");
            Map params = new HashMap();
            params.put("name", file.getName());
            params.put("extno", "1111123123");
            Map token = resourceService.startUpload(params);
            //实现自己的业务，将no保存下来，并记录上传状态，如果后续失败了可以调用upload(File file, String extno, String resumeNo)
            String recordPath = "logs/tmp/";
            Recorder recorder = new FileRecorder(recordPath);
            UploadManager uploadManager = new UploadManager(recorder);
            uploadManager.upload(file, token.get("reskey").toString(), token.get("uploadToken").toString());
            Map resource = resourceService.finishUpload(token.get("no").toString());
            //实现自己的业务，完成上传，修改自己的本地记录状态
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        new ResumeUploadDemo().upload();
    }
}
