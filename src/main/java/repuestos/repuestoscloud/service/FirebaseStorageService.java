package repuestos.repuestoscloud.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
            InputStream in;

            if (credentialsPath.startsWith("classpath:")) {
                String path = credentialsPath.replace("classpath:", "");
                Resource resource = new ClassPathResource(path);
                in = resource.getInputStream();
            } else {
                Resource resource = new ClassPathResource(credentialsPath);
                in = resource.getInputStream();
            }

            GoogleCredentials creds = GoogleCredentials.fromStream(in);

            return StorageOptions.newBuilder()
                    .setCredentials(creds)
                    .build()
                    .getService();

        } catch (Exception e) {
            throw new RuntimeException("Error conectando con Firebase Storage: " + e.getMessage(), e);
        }
    }

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String ext = "";
            String original = file.getOriginalFilename();

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }

            String objectName = folder + "/" + UUID.randomUUID() + ext;

            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName))
                    .setContentType(file.getContentType())
                    .build();

            Storage storage = storage();
            Blob blob = storage.create(blobInfo, file.getBytes());

            return blob.signUrl(3650, TimeUnit.DAYS).toString();

        } catch (Exception e) {
            throw new RuntimeException("Error subiendo imagen a Firebase Storage: " + e.getMessage(), e);
        }
    }
}