package frc.robot.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import edu.wpi.first.wpilibj.Filesystem;

public class FileUtils {
    public static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public static String getDeployPath() {
        return Filesystem.getDeployDirectory().getPath() + "/";
    }
}