package pw.rapture.bukkit;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import pw.rapture.Constants;
import pw.rapture.configuration.Configuration;
import pw.rapture.configuration.OverrideEntry;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/AbstractNPCManagement.java
public class Management {

  protected Configuration configuration;

  protected OverrideEntry ownOverrideEntry;

  @EventListener
  public void handle(ChannelMessageReceiveEvent event) {
    if (event.getChannel().equals(Constants.EASYCLOUDNET_CHANNEL_NAME)) {

      switch (event.getMessage().toLowerCase()) {
        //refreshes the configuration
        case Constants.EASYCLOUDNET_CHANNEL_UPDATE_CONFIGURATION_MESSAGE: {
          Configuration configuration =
            event.getData().get(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY, Configuration.class);
          this.setConfiguration(configuration);
        }
        break;

        default:
          break;
      }

    }
  }

  /**
   * Requests a update of the configuration file from the module.
   */
  public void requestConfiguration() {
    CloudNetDriver
      .getInstance().getMessenger().sendChannelMessage(
      Constants.EASYCLOUDNET_CHANNEL_NAME,
      Constants.EASYCLOUDNET_CHANNEL_REQUEST_CONFIGURATION_MESSAGE,
      new JsonDocument(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY,
        Wrapper.getInstance().getServiceId().getUniqueId().toString())
    );
  }

  public Configuration getConfiguration() {
    return this.configuration;
  }

  /**
   * Sets the configuration for this wrapper instance
   *
   * @param configuration Configuration
   */
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
    this.ownOverrideEntry = configuration.getOverrides().stream()
      .filter(entry -> entry.getTask()
        .equals(Wrapper.getInstance().getServiceConfiguration().getServiceId().getTaskName()))
      .findFirst()
      .orElse(configuration.getDefaultEntry());
    if (this.ownOverrideEntry == null) {
      this.ownOverrideEntry = new OverrideEntry(null);
    }
  }

  public OverrideEntry getOwnOverrideEntry() {
    return this.ownOverrideEntry;
  }
}
