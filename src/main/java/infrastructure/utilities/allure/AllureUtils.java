package infrastructure.utilities.allure;


import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import lombok.extern.log4j.Log4j2;
import org.slf4j.event.Level;

import java.io.*;

@Log4j2
public class AllureUtils {

    public static void allureAttachText(String attachName, String message) {
        String logMessage =
                "[Allure]["
                        .concat(attachName)
                        .concat("]: ")
                        .concat(message).concat("\n");

        switch (attachName.toUpperCase()) {
            case "ERROR" -> log.error(logMessage);
            case "WARN" -> log.warn(logMessage);
            default -> log.info(logMessage);
        }

        Allure.attachment(attachName, message);
    }

    public static void allureAttachText(Level level, String message) {
        allureAttachText(level.toString(), message);
    }

    public static void allureAttachScreenshot(String attachmentName, byte[] byteArray) {
        Allure.attachment(attachmentName, new ByteArrayInputStream(byteArray));
    }

    public static void allureAttachVideo(String attachmentName, InputStream is) {
        if (is != null) {
            Allure.getLifecycle().addAttachment(attachmentName, "video/webm", is, AttachmentOptions.withFileExtension(".webm"));
        } else log.error("Can't attach video file, specified input stream is null");
    }

    /**
     * Attaches a Playwright trace; Allure 3 renders this content type in its embedded trace viewer.
     * Fallback: download the .zip and open it with {@code npx playwright show-trace <file>}
     */
    public static void allureAttachTrace(String attachmentName, InputStream is) {
        if (is != null) {
            Allure.getLifecycle().addAttachment(attachmentName, "application/vnd.allure.playwright-trace", is, AttachmentOptions.withFileExtension(".zip"));
        } else log.error("Can't attach trace file, specified input stream is null");
    }

    public static InputStream fileToInputStream(String path) {
        File file = new File(path);
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            log.error("Unable to get InputStream from file: %s.\n%s".formatted(path, e));
        }
        return null;
    }

}
