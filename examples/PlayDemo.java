import com.codeages.escloud.Auth;
import com.codeages.escloud.service.PlayService;

import java.util.HashMap;

public class PlayDemo {
    String accessKey = "your access key";
    String secretKey = "your secret key";
    Auth auth = new Auth(accessKey, secretKey);
    PlayService playService = new PlayService(auth, new HashMap<String, String>());

    public String play() {
        return playService.makePlayToken("0572bd577fe44d829004edadd3b5acf8", 60, new HashMap<String, Object>());
    }


    public static void main(String[] args) {
        new PlayDemo().play();
    }
}
