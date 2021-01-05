package pw.rapture.module.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.util.DefaultModuleHelper;
import de.dytanic.cloudnet.event.service.CloudServicePreStartEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import pw.rapture.module.Module;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/node/listener/IncludePluginListener.java
public class IncludePluginListener {

  private static final String PROTOCOLLIB_DOWNLOAD_URL =
      "https://github.com/dmulloy2/ProtocolLib/releases/latest/download/ProtocolLib.jar";

  private static final Path
      PROTOCOLLIB_CACHE_PATH = Paths
      .get(System.getProperty("cloudnet.tempDir", "temp"), "caches", "ProtocolLib.jar");

  private final Module module;

  public IncludePluginListener(Module module) {
    this.module = module;
    this.downloadProtocolLib();
  }

  private void downloadProtocolLib() {
    try {
      URLConnection urlConnection = new URL(PROTOCOLLIB_DOWNLOAD_URL).openConnection();

      urlConnection.setUseCaches(false);
      urlConnection.setDoOutput(false);

      urlConnection.setConnectTimeout(5000);
      urlConnection.setReadTimeout(5000);

      urlConnection.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      urlConnection.connect();

      try (InputStream inputStream = urlConnection.getInputStream()) {
        Files.copy(inputStream, PROTOCOLLIB_CACHE_PATH, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException exception) {
      CloudNetDriver.getInstance().getLogger().error("Unable to download ProtocolLib!", exception);
    }
  }

  @EventListener
  public void handle(CloudServicePreStartEvent event) {
    if (!event.getCloudService().getServiceConfiguration().getServiceId().getEnvironment()
        .isMinecraftJavaServer()) {
      return;
    }

    // noneMatch, if not found it will be true -> install the plugin
    boolean installPlugin = this.module.getConfiguration().getBlacklist().stream()
        .noneMatch(blacklistEntry -> Arrays
            .asList(event.getCloudService().getServiceConfiguration().getServiceId().getTaskName())
            .contains(blacklistEntry));

    // creates the "plugins" dir if there is none
    File pluginsFolder = new File(event.getCloudService().getDirectory(), "plugins");
    pluginsFolder.mkdirs();

    // removes the EasyCloudNet.jar from the plugins folder if there is one
    File file = new File(pluginsFolder, "EasyCloudNet.jar");
    file.delete();

    if (installPlugin) {
      // installs ProtocolLib
      try {
        Files.copy(PROTOCOLLIB_CACHE_PATH, pluginsFolder.toPath().resolve("ProtocolLib.jar"),
            StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException exception) {
        CloudNetDriver.getInstance().getLogger().error("Unable to copy ProtocolLib!", exception);
        return;
      }

      // copies the current jar
      if (DefaultModuleHelper
          .copyCurrentModuleInstanceFromClass(IncludePluginListener.class, file)) {
        DefaultModuleHelper.copyPluginConfigurationFileForEnvironment(
            IncludePluginListener.class,
            event.getCloudService().getServiceConfiguration().getProcessConfig().getEnvironment(),
            file
        );
      }
    }
  }

}
