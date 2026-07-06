package com.clinic.doctor_app_backend.main;//package com.clinic.doctor_app_backend.main;
//
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class DownloadReportService {
//
//    public static void main(String[] args) {
//        String fileUrl =
//                "https://ldmtest.alborgdx.com/Reports/api/External/Report/DownloadCombinedReportServices"
//                        + "?params=bNX58c0qu4lGqSG01LObF6%2bRvLQFXI5e6tQZn4PVCudZHFHX8h2fWweqOnpLdJXA9CvzXQbVKZaJgC9W7hv1h0eEjZYfa%2b3517W%2f0W5ynnyhzRloeu%2fw2QBXHOCxJOYS0j%2b9vbcKqrhfwa0IbNlIgesQoVDOdiGU8e8C367Zjs3v2MPW1awIDAO4cnBZU7SAR4%2bTIY5vgQvOaLLQk4%2bzqAa6YuT%2fN2UHHcKZ5jkSfsjhnzRqk6BsC%2b3LWI6u5b%2bB39dJwudvJaOGOHSDac08n58oMTjqX1JbU7twllct8xFnxPg2ychuS3BaUDT8M%2bdXz6CiIxK14iTQc0vgIa35cO%2f1cb11922ad";
//
//        String outputPath = "C:\\pdf-recevied\\CombinedReport.pdf"; // change extension if needed
//
//        try {
//            URL url = new URL(fileUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(15000);
//            connection.setReadTimeout(15000);
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                try (InputStream inputStream = connection.getInputStream();
//                     FileOutputStream outputStream = new FileOutputStream(outputPath)) {
//
//                    byte[] buffer = new byte[4096];
//                    int bytesRead;
//
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//                }
//
//                System.out.println("File downloaded successfully to: " + outputPath);
//            } else {
//                System.out.println("Failed to download file. HTTP code: " + responseCode);
//            }
//
//            connection.disconnect();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DownloadReportService {

    public static void main(String[] args) {

        String fileUrl =
                "https://ldmtest.alborgdx.com/Reports/api/External/Report/DownloadCombinedReportServices"
                        + "?params=bNX58c0qu4lGqSG01LObF6%2bRvLQFXI5e6tQZn4PVCudZHFHX8h2fWweqOnpLdJXA9CvzXQbVKZaJgC9W7hv1h0eEjZYfa%2b3517W%2f0W5ynnyhzRloeu%2fw2QBXHOCxJOYS0j%2b9vbcKqrhfwa0IbNlIgesQoVDOdiGU8e8C367Zjs3v2MPW1awIDAO4cnBZU7SAR4%2bTIY5vgQvOaLLQk4%2bzqAa6YuT%2fN2UHHcKZ5jkSfsjhnzRqk6BsC%2b3LWI6u5b%2bB39dJwudvJaOGOHSDac08n58oMTjqX1JbU7twllct8xFnxPg2ychuS3BaUDT8M%2bdXz6CiIxK14iTQc0vgIa35cO%2f1cb11922ad";

        // Generate 10-digit unique number
        long uniqueNumber = 1000000000L + new Random().nextLong(9000000000L);

        // File name: LDM + 10 digit number
        String outputPath = "C:\\pdf-recevied\\LDM" + uniqueNumber + ".pdf"; // change extension if needed

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(outputPath)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                System.out.println("File downloaded successfully as: " + outputPath);
            } else {
                System.out.println("Download failed. HTTP Code: " + connection.getResponseCode());
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
