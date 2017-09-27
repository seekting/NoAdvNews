package com.seekting.libcommon;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final boolean DEBUG = true;

    private static final String TIMESTAMP_EXT = ".timestamp";

    public static byte[] readFileByte(File file) {
        FileInputStream fis = null;
        FileChannel fc = null;
        byte[] data = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            data = new byte[(int) fc.size()];
            fc.read(ByteBuffer.wrap(data));
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return data;
    }

    public static boolean writeByteFile(byte[] bytes, File file) {
        if (bytes == null) {
            return false;
        } else {
            boolean flag = false;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(bytes);
                flag = true;
            } catch (FileNotFoundException var20) {
                var20.printStackTrace();
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "", e);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                    } catch (Exception e) {
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }
            return flag;
        }
    }

    /**
     * 比对assets 和 data/data/包名/files 中的时间戳文件，读取新文件，若files
     * 中不文件，则先将asserts中文件拷贝到files中，避免v5重复下载，消耗无用流量 到 files
     *
     * @param c
     * @param filename
     * @return
     */
    public static InputStream openLatestInputFile(Context c, String filename) {
        try {
            // 将相关文件复制到data/data/pkg/files中
            File file = new File(c.getFilesDir(), filename);
            if (!file.exists()) {
                FileUtils.copyAssetsFile(c, filename, file);
            }
            String timestampFileName = filename + TIMESTAMP_EXT;
            File timestampFile = new File(c.getFilesDir(), timestampFileName);
            if (!timestampFile.exists()) {
                FileUtils.copyAssetsFile(c, timestampFileName, timestampFile);
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "copy filter file fail", e);
            }
        }
        InputStream is = null;
        long timestampOfFile = FileUtils.getFileTimestamp(c, filename);
        long timestampOfAsset = FileUtils.getBundleTimestamp(c, filename);
        if (timestampOfFile >= timestampOfAsset) {
            // files 目录的时间戳更新，那么优先读取 files 目录的文件
            try {
                is = c.openFileInput(filename);
                if (DEBUG) {
                    Log.d(TAG, String.format("Opening %s in files directory", filename));
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.w(TAG, String.format("%s in files directory not found, skip.", filename), e);
                }
            }
        }
        if (is == null) {
            // is == null 表明没能从 files 目录读到文件，那么到 assets 目录去读读看
            try {
                is = c.getAssets().open(filename);
                if (DEBUG) {
                    Log.d(TAG, String.format("Opening %s in assets", filename));
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, filename + " in assets not found, open failed!", e);
                }
            }
        }
        return is;
    }

    public static void copyAssetsFile(Context context, String assetsFileName, File destFile) throws IOException {
        FileUtils.copyFile(context.getAssets().open(assetsFileName), destFile);
    }

    public static void copyDataFilesFile(Context context, String dataFilesFileName, File destFile) throws IOException {
        FileUtils.copyFile(context.openFileInput(dataFilesFileName), destFile);
    }

    public static void copyFile(InputStream is, File destFile) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFile);
            byte arrayByte[] = new byte[1024];
            int i = 0;
            while ((i = is.read(arrayByte)) != -1) {
                fos.write(arrayByte, 0, i);
            }
            fos.flush();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "copyFiles", e);
                    }
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "copyFile", e);
                    }
                }
            }
        }
    }

    /**
     * 读取文件的时间戳
     */
    public static long getFileTimestamp(Context c, String filename) {
        FileInputStream fis = null;
        try {
            fis = c.openFileInput(filename + TIMESTAMP_EXT);
        } catch (Exception e) {
        }

        if (fis != null) {
            return FileUtils.getTimestampFromStream(fis);
        } else {
            return 0;
        }
    }

    /**
     * 对于打包的文件，都是放在 assets 目录的，时间戳自然也在 assets 目录
     *
     * @param c
     * @param filename
     * @return
     */
    public static long getBundleTimestamp(Context c, String filename) {
        InputStream fis = null;
        try {
            fis = c.getAssets().open(filename + TIMESTAMP_EXT);
        } catch (Exception e) {
        }

        if (fis != null) {
            return FileUtils.getTimestampFromStream(fis);
        } else {
            return 0;
        }
    }

    private static long getTimestampFromStream(InputStream fis) {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(fis);
            String s = dis.readLine();
            return Long.parseLong(s);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "", e);
                }
            }
        }
        return 0;
    }

    public static void delete(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
    }
}
