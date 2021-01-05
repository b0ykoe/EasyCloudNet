package pw.rapture;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/NPCConstants.java
public class Constants {

  public static final String
      EASYCLOUDNET_CHANNEL_NAME =
      "cloudnet_addon_easycloudnet";
  public static final String
      EASYCLOUDNET_JSONDOCUMENT_KEY =
      "easyCloudNet_configuration";
  public static final String
      EASYCLOUDNET_CHANNEL_REQUEST_CONFIGURATION_MESSAGE =
      "request_easycloudnet_configuration";
  public static final String
      EASYCLOUDNET_CHANNEL_UPDATE_CONFIGURATION_MESSAGE =
      "update_easycloudnet_configuration";
  public static final String
      EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE = "easyCloudNet_generate_OverrideEntry";
  public static final String
      EASYCLOUDNET_TASK_SETUP_BLACKLIST = "easyCloudNet_generate_BlacklistEntry";

  private Constants() {
    throw new UnsupportedOperationException();
  }
}
