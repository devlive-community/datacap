package io.edurt.datacap.fs;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class IOUtils
{
    private IOUtils()
    {
    }

    /**
     * Copies a file from the source path to the target path.
     *
     * @param source the path of the file to be copied
     * @param target the path where the file should be copied to
     * @param createdDir a flag indicating whether the parent directories of the target path should be created if they do not exist
     * @return true if the file was successfully copied, false otherwise
     */
    public static boolean copy(String source, String target, boolean createdDir)
    {
        try {
            Path targetPath = Path.of(target);
            if (createdDir) {
                Files.createDirectories(targetPath.getParent());
            }

            Files.copy(Path.of(source), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a Reader object for the specified source.
     *
     * @param source the path of the file to be read
     * @return a Reader object for reading the file contents
     */
    public static Reader reader(String source)
    {
        try {
            return Files.newBufferedReader(Path.of(source));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
