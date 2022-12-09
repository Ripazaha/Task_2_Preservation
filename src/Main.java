import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void saveGame(List<GameProgress> saves, File path) { // выходной поток для записи в файл
        int countSave = 1;
        for (GameProgress save : saves) {
            try (FileOutputStream fos = new FileOutputStream(path + "\\save" + countSave++ + ".dat");
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(save); // запись экземпляра класса в файл
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void zipFiles(String thePathToTheFileZip, ArrayList<String> listSave) {
        int countSave = 1;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(thePathToTheFileZip))) {
            for (String save : listSave) {
                try (FileInputStream fis = new FileInputStream(save)) {
                    ZipEntry entry = new ZipEntry("Zip save " + countSave++ + ".dat");
                    zos.putNextEntry(entry);
                    byte[] bytes = new byte[fis.available()];
                    fis.read(bytes);
                    zos.write(bytes);
                    zos.closeEntry();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        GameProgress gameProgress = new GameProgress(94, 10, 2, 254.32);
        GameProgress gameProgress1 = new GameProgress(10, 2, 45, 150);
        GameProgress gameProgress2 = new GameProgress(50, 3, 13, 432);

        List<GameProgress> saves = Arrays.asList(gameProgress, gameProgress1, gameProgress2);

        // сохранения игры
        File saveGames = new File("C:\\User\\Games\\savegames");
        saveGame(saves, saveGames);

        // архивация
        ArrayList<String> listSave = new ArrayList<>();
        if (saveGames.isDirectory()) {
            for (File item : Objects.requireNonNull(saveGames.listFiles())) {
                if (item.isFile()) {
                    listSave.add(String.valueOf(item.getAbsoluteFile()));
                }
            }
        }

        File fileZip = new File(saveGames + "\\archiveSave.zip");
        if (fileZip.createNewFile()) {
            zipFiles(String.valueOf(fileZip), listSave);
        }

        // удаление ненужных сохранений
        for (String save : listSave) {
            File delSavesGame = new File(save);
            if (delSavesGame.delete())
                System.out.println("файл удалён!");
        }
    }
}

