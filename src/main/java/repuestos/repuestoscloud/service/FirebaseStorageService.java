package repuestos.repuestoscloud.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
public class FirebaseStorageService {

  @Value("${app.firebase.credentials}")
  private String credentialsPath;

  @Value("${app.firebase.bucket}")
  private String bucket;

  @Value("${app.firebase.folder:productos}")
  private String folder;

  private Storage storage() throws IOException {
    try (InputStream in = new FileInputStream(credentialsPath)) {
      GoogleCredentials creds = GoogleCredentials.fromStream(in);
      return StorageOptions.newBuilder().setCredentials(creds).build().getService();
    }
  }

  public String upload(MultipartFile file) {
    if (file == null || file.isEmpty()) return null;

    try {
      String ext = "";
      String original = file.getOriginalFilename();
      if (original != null && original.contains(".")) ext = original.substring(original.lastIndexOf('.'));

      String objectName = folder + "/" + UUID.randomUUID() + ext;

      BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName))
          .setContentType(file.getContentType())
          .build();

      storage().create(blobInfo, file.getBytes());

      return "https://storage.googleapis.com/" + bucket + "/" + objectName;

    } catch (Exception e) {
      throw new RuntimeException("Error subiendo imagen a Firebase Storage: " + e.getMessage(), e);
    }
  }
}