package com.ecommerce.bookingservicebackend.firebase;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseStorageService {

	public List<String> getAllJpgFiles() {
		List<String> jpgFiles = new ArrayList<>();
		Bucket bucket = StorageClient.getInstance().bucket();

		for (Blob blob : bucket.list().iterateAll()) {
			if (blob.getName().endsWith(".jpg")) {
				jpgFiles.add(blob.getName());
			}
		}

		return jpgFiles;
	}
}
