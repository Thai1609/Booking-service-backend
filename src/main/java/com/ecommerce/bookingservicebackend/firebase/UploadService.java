package com.ecommerce.bookingservicebackend.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class UploadService {
	@NonFinal
	@Value("${firebases.storage}")
	protected String STORAGE;

	private String uploadFile(File file, String fileName) throws IOException {
		BlobId blobId = BlobId.of(STORAGE, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		InputStream inputStream = UploadService.class.getClassLoader()
				.getResourceAsStream("data-images-12d9b-firebase-adminsdk-pghiq-19f991d79d.json");

		Credentials credentials = GoogleCredentials.fromStream(inputStream);
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		storage.create(blobInfo, Files.readAllBytes(file.toPath()));

		String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + STORAGE + "/o/%s?alt=media";
		return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
	}

	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
		File tempFile = new File(fileName);
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(multipartFile.getBytes());
			fos.close();
		}
		return tempFile;
	}

	public String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public String upload(MultipartFile multipartFile, String folder) {
		try {

			String fileName = multipartFile.getOriginalFilename(); // to get original file name
			fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName)); // to generated random string
																							// values for file name.

			File file = this.convertToFile(multipartFile, fileName); // to convert multipartFile to File
			fileName = folder + fileName;
			String URL = this.uploadFile(file, fileName); // to get uploaded file link

			file.delete();
			return URL;
		} catch (Exception e) {
			e.printStackTrace();
			return "Image couldn't upload, Something went wrong";
		}
	}
}
