package com.ordermanagement.s3config;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/v1_aws_s3_bucket")
@Api(description = "AwsS3 Bucket Service", tags = { "AwsS3BucketAPI" })
public class S3Controller {

	private final S3Service s3Service;

	public S3Controller(S3Service s3Service) {
		this.s3Service = s3Service;
	}

	@GetMapping
	public String health() {
		return "UP";
	}

	@PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		s3Service.uploadFile(file.getOriginalFilename(), file);
		return "File uploaded";
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(s3Service.getFile(fileName).getObjectContent()));
	}

	@GetMapping("/view/{fileName}")
	public ResponseEntity<InputStreamResource> viewFile(@PathVariable String fileName) {
		var s3Object = s3Service.getFile(fileName);
		var content = s3Object.getObjectContent();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG) // This content type can change by your file :)
				.header(HttpHeaders.CONTENT_TYPE, "image/png")
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(s3Object.getObjectMetadata().getContentLength()))
				.body(new InputStreamResource(content));
	}

	@DeleteMapping("/delete/{fileName}")
	public String deleteFile(@PathVariable String fileName) {
		s3Service.deleteObject(fileName);
		return "File deleted";
	}
}
