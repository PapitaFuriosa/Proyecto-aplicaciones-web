package repuestos.repuestoscloud.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseStorageService {

    @Value("${app.firebase.credentials}")
    private String credentialsPath;

    @Value("${app.firebase.bucket}")
    private String bucket;

    @Value("${app.firebase.folder:productos}")
    private String folder;

    private Storage storage() {
        try {
            String path = credentialsPath.replace("classpath:", "");
            InputStream serviceAccount = new ClassPathResource(path).getInputStream();

            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            return StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();

        } catch (Exception e) {
            throw new RuntimeException("Error conectando con Firebase Storage", e);
        }
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String original = file.getOriginalFilename();
            String ext = "";

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String objectName = folder + "/" + UUID.randomUUID() + ext;

            BlobId blobId = BlobId.of(bucket, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            Storage storage = storage();
            Blob blob = storage.create(blobInfo, file.getBytes());

            return blob.signUrl(3650, TimeUnit.DAYS).toString();

        } catch (Exception e) {
            throw new RuntimeException("Error subiendo imagen a Firebase Storage", e);
        }
    }
}