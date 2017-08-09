package belizenearme.infoservices.lue.belize.utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class FileCache {
    private File cacheDir;
    private Context context;

    public FileCache(Context context) {
        this.context = context;
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), AppConstants.storeDir);
        else
            cacheDir = context.getCacheDir();

        int permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
                try {
                    cacheDir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public File getFile(String url) {
        File file = new File(cacheDir.getAbsolutePath()+"/"+String.valueOf(url.hashCode()));
        int permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            try {
                if (!file.exists()) {
                    boolean create = file.createNewFile();
                    if (create)
                        System.out.println("file created");
                    else
                        System.out.println("file not created");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("file not created  getFile");
            }
        }
        return file;
    }

    public void deleteFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File file = new File(cacheDir, filename);
        if (file.exists()) {
            boolean deleted = file.delete();
            if(deleted)
                System.out.println("file deleted");
            else
                System.out.println("file not deleted");
        }
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}
