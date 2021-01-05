package pw.rapture.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pw.rapture.Constants;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/configuration/NPCConfiguration.java
public class Configuration {

  public static final Map<String, String> DEFAULT_MESSAGES = new HashMap<>();

  static {
    DEFAULT_MESSAGES.put(Constants.EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE,
        "Do you want to add a new OverrideEntry for the service?");
    DEFAULT_MESSAGES.put(Constants.EASYCLOUDNET_TASK_SETUP_BLACKLIST,
        "Do you want to add a new blacklistEntry for this service? EasyCloudNet wont be installed "
            + "on that "
            + "service.");
  }

  private final OverrideEntry defaultEntry = new OverrideEntry(null);
  private Map<String, String> messages = DEFAULT_MESSAGES;
  private List<OverrideEntry> overrides = new ArrayList<>();

  private List<String> blacklist = new ArrayList<>();


  public Configuration() {
  }

  /**
   * Configuration object for the EasyCloudNet module.
   *
   * @param messages  Map  - String, String
   * @param blacklist List - String
   * @param overrides List - OverrideEntry
   */
  public Configuration(Map<String, String> messages, List<String> blacklist,
      List<OverrideEntry> overrides) {
    this.messages = messages;
    this.overrides = overrides;
    this.blacklist = blacklist;
  }

  public Map<String, String> getMessages() {
    return this.messages;
  }

  public OverrideEntry getDefaultEntry() {
    return this.defaultEntry;
  }

  public List<OverrideEntry> getOverrides() {
    return this.overrides;
  }

  public List<String> getBlacklist() {
    return this.blacklist;
  }
}
