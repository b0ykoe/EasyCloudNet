package pw.rapture.module;

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.module.NodeCloudNetModule;
import java.util.UUID;
import pw.rapture.Constants;
import pw.rapture.configuration.Configuration;
import pw.rapture.module.listener.IncludePluginListener;
import pw.rapture.module.listener.MessageListener;
import pw.rapture.module.listener.TaskSetupListener;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/node/CloudNetNPCModule.java
public class Module extends NodeCloudNetModule {

  private Configuration configuration;

  @ModuleTask(event = ModuleLifeCycle.STARTED, order = 127)
  public void loadConfiguration() {
    // creates the directory in `/modules` if not exists
    super.getModuleWrapper().getDataFolder().mkdirs();
    // tries to load the config file, creates a new one if none exists
    this.configuration = super.getConfig().get("config", Configuration.class, new Configuration());

    // sends the configuration to the channel, will get received in the plugin and refreshed
    // probably needed for cluster updates, module reloads
    this.sendConfigurationUpdate();
    // saves the config, will get created if not exists
    this.saveConfiguration();
  }

  /**
   * Saves and creates a new file on the drive.
   */
  public void saveConfiguration() {
    super.getConfig().append("config", this.configuration);
    super.saveConfig();
  }

  /**
   * Sends a update of the configuration file to all services and nodes.
   */
  public void sendConfigurationUpdate() {
    CloudNet.getInstance().getMessenger()
      .sendChannelMessage(
        Constants.EASYCLOUDNET_CHANNEL_NAME,
        Constants.EASYCLOUDNET_CHANNEL_UPDATE_CONFIGURATION_MESSAGE,
        new JsonDocument(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY, this.getConfiguration()));
  }

  /**
   * Sends a update of the configuration file to the service with the following uuid.
   *
   * @param targetUuid String
   */
  public void sendConfigurationUpdate(String targetUuid) {
    ServiceInfoSnapshot
      serviceInfoSnapshot =
      CloudNetDriver.getInstance().getCloudServiceProvider().getCloudService(
        UUID.fromString(targetUuid));
    if (serviceInfoSnapshot == null) {
      return;
    }
    CloudNet.getInstance().getMessenger()
      .sendChannelMessage(
        serviceInfoSnapshot,
        Constants.EASYCLOUDNET_CHANNEL_NAME,
        Constants.EASYCLOUDNET_CHANNEL_UPDATE_CONFIGURATION_MESSAGE,
        new JsonDocument(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY, this.getConfiguration()));
  }

  @ModuleTask(event = ModuleLifeCycle.STARTED, order = 126)
  public void registerListeners() {
    super.registerListeners(
      new IncludePluginListener(this),
      new MessageListener(this),
      new TaskSetupListener(this)
    );
  }

  public Configuration getConfiguration() {
    return this.configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
