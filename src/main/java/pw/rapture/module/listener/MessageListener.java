package pw.rapture.module.listener;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.event.cluster.NetworkChannelAuthClusterNodeSuccessEvent;
import pw.rapture.Constants;
import pw.rapture.configuration.Configuration;
import pw.rapture.module.Module;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/node/listener/CloudNetNPCMessageListener.java
public class MessageListener {

  private final Module module;

  public MessageListener(Module module) {
    this.module = module;
  }

  @EventListener
  public void handle(NetworkChannelAuthClusterNodeSuccessEvent event) {
    // sends the config when a node is successfully authenticated in the cluster
    event.getNode().sendCustomChannelMessage(
      Constants.EASYCLOUDNET_CHANNEL_NAME,
      Constants.EASYCLOUDNET_CHANNEL_UPDATE_CONFIGURATION_MESSAGE,
      new JsonDocument()
        .append(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY, this.module.getConfiguration())
    );
  }

  @EventListener
  public void handle(ChannelMessageReceiveEvent event) {
    if (event.getChannel().equalsIgnoreCase(Constants.EASYCLOUDNET_CHANNEL_NAME)) {

      switch (event.getMessage().toLowerCase()) {
        // if a update was send -> save the new config file
        case Constants.EASYCLOUDNET_CHANNEL_UPDATE_CONFIGURATION_MESSAGE: {
          Configuration configuration =
            event.getData().get(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY, Configuration.class);

          this.module.setConfiguration(configuration);
          this.module.saveConfiguration();
          break;
        }

        // if there was a requested update send only that service a update
        case Constants.EASYCLOUDNET_CHANNEL_REQUEST_CONFIGURATION_MESSAGE: {
          this.module.sendConfigurationUpdate(
            event.getData().get(Constants.EASYCLOUDNET_JSONDOCUMENT_KEY, String.class));
          break;
        }
      }

    }
  }
}
