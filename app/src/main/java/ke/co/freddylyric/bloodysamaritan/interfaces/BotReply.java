package ke.co.freddylyric.bloodysamaritan.interfaces;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface BotReply {

 void callback (DetectIntentResponse returnResponse);
}
