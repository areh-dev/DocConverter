package ru.areh.DocConverter;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class ConverterController {

    private static final int fileConvertTimeout;


    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    static {
        fileConvertTimeout = parseInt(System.getenv("FILE_CONVERT_TIMEOUT"), 120);
    }


    @GetMapping("/status/")
    public ResponseEntity<String> status() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @PostMapping("/convert/")
    public ResponseEntity<Object> convert(@RequestBody byte[] requestBody) {
        if (requestBody == null || requestBody.length == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            var resultFile = ConvertFile(requestBody);

            if (resultFile == null)
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);

            var responseBody = Files.readAllBytes(resultFile);
            Files.delete(resultFile);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (IOException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private Path ConvertFile(byte[] originalFileContent) throws IOException, InterruptedException {
        var tempPathName = "temp";
        var tempPath = Paths.get(tempPathName);
        if (!Files.isDirectory(tempPath)) {
            Files.createDirectories(tempPath);
        }

        var baseFileName = Paths.get(tempPath.toAbsolutePath().toString(), UUID.randomUUID().toString());
        var originalFile = Paths.get(baseFileName + ".request");
        var pdfFile = Paths.get(baseFileName + ".pdf");
        Files.write(originalFile, originalFileContent);

        var convertResult = startConverter(tempPath, originalFile);
        Files.delete(originalFile);
        return convertResult ? pdfFile : null;
    }


    private boolean startConverter(Path tempPath, Path originalFile) throws IOException, InterruptedException {
        var tempOfficeProfile = Paths.get(originalFile + ".profile/");
        var processBuilder = new ProcessBuilder(
                "libreoffice",
                "-env:UserInstallation=file://" + tempOfficeProfile,
                "--headless",
                "--convert-to", "pdf",
                "--outdir", tempPath.toAbsolutePath().toString(),
                originalFile.toString());

        var officeProcess = processBuilder.start();
        var result = officeProcess.waitFor(fileConvertTimeout, TimeUnit.SECONDS);
        FileUtils.deleteDirectory(new File(tempOfficeProfile.toString()));
        return result;
    }
}
