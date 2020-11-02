import com.codeages.escloud.Auth;
import com.codeages.escloud.service.ResourceService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class UploadDemo {
    String accessKey = "your access key";
    String secretKey = "your secret key";
    Auth auth = new Auth(accessKey, secretKey);
    ResourceService resourceService = new ResourceService(auth, new HashMap<String, String>());

    public Map upload() {
        File file = new File("/var/www/Downloads/edusoho.mp4");
        try {
            return resourceService.upload(file, "1111123123");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        new UploadDemo().upload();
    }
}
