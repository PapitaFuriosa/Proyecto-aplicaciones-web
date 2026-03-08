package repuestos.repuestoscloud.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${app.firebase.credentials}")
    private Resource firebaseCredentials;

    @Value("${app.firebase.bucket}")
    private String firebaseBucket;

    @PostConstruct
    public void initFirebase() {
        try (InputStream serviceAccount = firebaseCredentials.getInputStream()) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(firebaseBucket)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Firebase conectado correctamente");

        } catch (Exception e) {
            System.out.println("Error conectando Firebase");
            e.printStackTrace();
        }
    }
}
