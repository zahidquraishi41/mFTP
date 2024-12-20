package com.zapp.mFTP;

import static android.content.Context.WIFI_SERVICE;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.util.io.PathUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;

public class SFTPServer {
    public static final Logger logger = Logger.getLogger(SFTPServer.class.getName());
    public static final String SFTPUsername = "admin";
    private static final int PORT = 2222;
    public static String SFTPPassword;
    private final SFTPListener listener;
    private SshServer sshd;

    public SFTPServer(SFTPListener listener) {
        this.listener = listener;
    }

    private String getAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        int ip = wifiManager.getConnectionInfo().getIpAddress();
        return String.format(
                Locale.ROOT,
                "%d.%d.%d.%d:%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff),
                PORT
        );
    }

    public void start(Context context) {
        SFTPPassword = PasswordGenerator.generatePassword(7);
        PathUtils.setUserHomeFolderResolver(() -> Paths.get(context.getFilesDir().getAbsolutePath()));
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(PORT);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get(context.getCacheDir().getAbsolutePath(), "hostkey.ser")));
        sshd.setPasswordAuthenticator((username, password, session) ->
                username.equals(SFTPUsername) && password.equals(SFTPPassword)
        );
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(Environment.getExternalStorageDirectory().getAbsolutePath())));
        try {
            sshd.start();
        } catch (IOException e) {
            logger.severe(e.toString());
            listener.onServerError(e.getMessage());
        }
        listener.onServerStart(getAddress(context), SFTPUsername, SFTPPassword);
    }

    public void stop() {
        if (sshd == null || sshd.isClosing() || sshd.isClosed()) return;

        try {
            sshd.stop();
            listener.onServerStop();
        } catch (IOException e) {
            logger.severe(e.toString());
            listener.onServerError(e.getMessage() == null ? "Unknown error" : e.getMessage());
        }
    }

    public boolean isStarted() {
        return (sshd != null && sshd.isStarted());
    }

    public void toggle(Context context) {
        if (isStarted()) stop();
        else start(context);
    }

    public interface SFTPListener {
        void onServerStart(String serverAddress, String username, String password);

        void onServerStop();

        void onServerError(String error);
    }

}
