package it.pack.utility.zip;

import it.pack.utility.logger.BasicLogger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static final String DEFAULT_ZIP_NAME = "zip_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".zip";
    private static final BasicLogger logger = BasicLogger.getLogger(ZipUtils.class);

    /**
     * Costruttore.
     */
    public ZipUtils() { /* ... */ }

    /**
     * Esegue lo zip del contenuto di una cartella.<br>
     * NB: il parametro {@code outputFolder} non deve essere uguale o una sottocartella dell'input
     * , altrimenti verrà zippato anche lo zip generato
     *
     * @param zipName nome dello zip generato
     * @param inputFolder la cartella input
     * @param outputFolder la cartella dove verrà generato lo zip. <br>
     *
     */
    public static void zip(String zipName, Path inputFolder, Path outputFolder) {
        if (zipName == null || zipName.isEmpty())
            zipName = DEFAULT_ZIP_NAME;

        if (!zipName.endsWith(".zip"))
            zipName += ".zip";

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(outputFolder.resolve(zipName)))) {
            // Compressione dei file e delle cartelle
            Files.walkFileTree(inputFolder, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    try {
                        Path targetFile = inputFolder.relativize(file);
                        zipOut.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] bytes = Files.readAllBytes(file);
                        zipOut.write(bytes, 0, bytes.length);
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        logger.error("Errore durante l'inserimento dei file all'interno dello zip.", e);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            logger.error("Errore durante lo zip dei file.", e);
        }
    }

    public static void unzip(Path zipPath, Path outputFolder) {
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipPath))) {
            for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
                Path resolvedPath = outputFolder.resolve(ze.getName()).normalize();
                if (!resolvedPath.startsWith(outputFolder)) {
                    // see: https://snyk.io/research/zip-slip-vulnerability
                    logger.error("Entry with an illegal path: " + ze.getName());
                    break;
                }

                if (ze.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zipIn, resolvedPath);
                }
            }
        } catch (Exception e) {
            logger.error("Errore durante l'unzip dei file.", e);
        }
    }
}
