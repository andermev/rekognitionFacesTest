import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.rekognition.andermev.detect.DetectFacesExample;

import java.io.IOException;

public class MainRekognitionTest {

    private static final String IMAGE = "emoji-reactions/emotions-people.jpg";
    private static AWSCredentials credentials;
    private static AmazonRekognition rekognitionClient;

    public static void main(String[] args) throws IOException {

        try {
            credentials = new ProfileCredentialsProvider("rekognitionUser").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Usersuserid.aws/credentials), and is in a valid format.", e);
        }

        rekognitionClient = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        DetectFacesExample faces = new DetectFacesExample(rekognitionClient, IMAGE);
        faces.drawImage(faces.detectFacesRequest());

//        DetectLabelsExampleImageBytes labels = new DetectLabelsExampleImageBytes(rekognitionClient, IMAGE);
//        labels.detectLabelsRequest();

    }
}
