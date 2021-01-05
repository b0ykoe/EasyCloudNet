package pw.rapture.bukkit.objects;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import pw.rapture.bukkit.EasyCloudNet;

public class EasyNameTag {

  private final EasyCloudNet easyCloudNet;
  private final EasyPlayer easyPlayer;

  private String groupName; // internal name
  private int sortId = -1; // smaller -> higher in tab, has to be same number count ex. 10 and 99
  private String display;
  private String prefix;
  private String suffix;
  private String color; // color for the tablist
  private boolean showDefaultGroup;
  private boolean autoUpdate = true;

  public EasyNameTag(EasyCloudNet easyCloudNet, EasyPlayer easyPlayer) {
    this.easyCloudNet = easyCloudNet;
    this.easyPlayer = easyPlayer;
  }

  private IPermissionGroup getDefaultPermissionGroup() {
    return CloudNetDriver.getInstance().getPermissionManagement().getDefaultPermissionGroup();
  }

  private IPermissionUser getIPermissionUser() {
    return CloudNetDriver.getInstance().getPermissionManagement()
        .getUser(this.easyPlayer.getPlayer().getUniqueId());
  }

  public IPermissionGroup getHighestIPermissionGroup() {
    return CloudNetDriver.getInstance().getPermissionManagement()
        .getHighestPermissionGroup(this.getIPermissionUser());
  }

  /**
   * Sets the group name.
   *
   * @param groupName  - string
   * @param autoUpdate - boolean
   */
  public void setGroupName(String groupName, boolean autoUpdate) {
    this.groupName = groupName;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  /**
   * Sets the sort id.
   *
   * @param sortId     - int
   * @param autoUpdate - boolean
   */
  public void setSortId(int sortId, boolean autoUpdate) {
    this.sortId = sortId;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  /**
   * Sets the display.
   *
   * @param display    - string
   * @param autoUpdate - boolean
   */
  public void setDisplay(String display, boolean autoUpdate) {
    this.display = display;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  /**
   * Sets the prefix and crops it to 64.
   *
   * @param prefix     - string
   * @param autoUpdate - boolean
   */
  public void setPrefix(String prefix, boolean autoUpdate) {
    if (prefix.length() > 64) {
      prefix = prefix.substring(0, 64);
    }

    this.prefix = prefix;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  /**
   * Sets the suffix and crops it to 62 (64 - 2 for the white color).
   *
   * @param suffix     - string
   * @param autoUpdate - boolean
   */
  public void setSuffix(String suffix, boolean autoUpdate) {
    if (suffix.length() > 62) {
      suffix = "§f" + suffix.substring(0, 62);
    }

    this.suffix = suffix;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  public void setColor(String color, boolean autoUpdate) {
    this.color = color;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  public void setShowDefaultGroup(boolean showDefaultGroup, boolean autoUpdate) {
    this.showDefaultGroup = showDefaultGroup;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  public void toggleShowDefaultGroup() {
    this.showDefaultGroup = !this.showDefaultGroup;
  }

  public void toggleShowDefaultGroup(boolean autoUpdate) {
    this.showDefaultGroup = !this.showDefaultGroup;
    if (autoUpdate) {
      this.updateNameTags();
    }
  }

  public void toggleAutoUpdate() {
    this.autoUpdate = !this.autoUpdate;
  }

  /**
   * Gets the SortId depending on a few cases. If showDefaultGroup is on it will always return the
   * defaults group value. Secondly it will look if a custom value is set, if so it returns that
   * else it returns the highest group one.
   *
   * @return int
   */
  public int getSortId() {
    if (this.showDefaultGroup) {
      return this.getDefaultPermissionGroup().getSortId();
    }

    if (this.sortId == -1) {
      return this.getHighestIPermissionGroup().getSortId();
    }

    return this.sortId;
  }

  public void setSortId(int sortId) {
    this.setSortId(sortId, this.autoUpdate);
  }

  /**
   * Gets the group name depending on a few cases. If showDefaultGroup is on it will always return
   * the defaults group value. Secondly it will look if a custom value is set, if so it returns that
   * else it returns the highest group one.
   *
   * @return String
   */
  public String getGroupName() {
    if (this.showDefaultGroup) {
      return this.getDefaultPermissionGroup().getName();
    }

    if (this.groupName == null) {
      return this.getHighestIPermissionGroup().getName();
    }

    return this.groupName;
  }

  public void setGroupName(String groupName) {
    this.setGroupName(groupName, this.autoUpdate);
  }

  /**
   * Gets the group name depending on a few cases. If showDefaultGroup is on it will always return
   * the defaults group value. Secondly it will look if a custom value is set, if so it returns that
   * else it returns the highest group one.
   *
   * @return String
   */
  public String getPrefix() {
    if (this.showDefaultGroup) {
      return this.getDefaultPermissionGroup().getPrefix();
    }

    if (this.prefix == null) {
      return this.getHighestIPermissionGroup().getPrefix();
    }

    return this.prefix;
  }

  public void setPrefix(String prefix) {
    this.setPrefix(prefix, this.autoUpdate);
  }

  /**
   * Gets the group name depending on a few cases. If showDefaultGroup is on it will always return
   * the defaults group value. Secondly it will look if a custom value is set, if so it returns that
   * else it returns the highest group one.
   *
   * @return String
   */
  public String getSuffix() {
    if (this.showDefaultGroup) {
      return this.getDefaultPermissionGroup().getSuffix();
    }

    if (this.suffix == null) {
      return this.getHighestIPermissionGroup().getSuffix();
    }

    return this.suffix;
  }

  public void setSuffix(String suffix) {
    this.setSuffix(suffix, this.autoUpdate);
  }

  /**
   * Gets the group name depending on a few cases. If showDefaultGroup is on it will always return
   * the defaults group value. Secondly it will look if a custom value is set, if so it returns that
   * else it returns the highest group one.
   *
   * @return String
   */
  public String getDisplay() {
    if (this.showDefaultGroup) {
      return this.getDefaultPermissionGroup().getDisplay();
    }

    if (this.display == null) {
      return this.getHighestIPermissionGroup().getDisplay();
    }

    return this.display;
  }

  public void setDisplay(String display) {
    this.setDisplay(display, this.autoUpdate);
  }

  /**
   * Gets the group name depending on a few cases. If showDefaultGroup is on it will always return
   * the defaults group value. Secondly it will look if a custom value is set, if so it returns that
   * else it returns the highest group one.
   *
   * @return String
   */
  public String getColor() {
    if (this.showDefaultGroup) {
      return this.getDefaultPermissionGroup().getColor();
    }

    if (this.color == null) {
      return this.getHighestIPermissionGroup().getColor();
    }

    return this.color;
  }

  public void setColor(String color) {
    this.setColor(color, this.autoUpdate);
  }

  /**
   * Returns if the default group should be shown or not.
   *
   * @return boolean
   */
  public boolean isShowDefaultGroup() {
    return this.showDefaultGroup;
  }

  public void setShowDefaultGroup(boolean showDefaultGroup) {
    this.setShowDefaultGroup(showDefaultGroup, this.autoUpdate);
  }

  /**
   * Returns if the setters should trigger updateNameTags.
   *
   * @return boolean
   */
  public boolean isAutoUpdate() {
    return this.autoUpdate;
  }

  public void setAutoUpdate(boolean autoUpdate) {
    this.autoUpdate = autoUpdate;
  }

  /**
   * updates the nameTags.
   */
  public void updateNameTags() {
    Player currentPlayer = this.easyPlayer.getPlayer();
    // If player was not found return
    if (currentPlayer == null) {
      return;
    }

    // loops through all players to update the scoreboard
    Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
      // inits the scoreboard if the player has none
      initScoreboard(loopPlayer);
      // adds the team entry for the current player
      addTeamEntry(currentPlayer, loopPlayer, this);

      // adds the team entry for all other players
      EasyNameTag loopNameTag = this.easyCloudNet.getEasyPlayer(loopPlayer).getEasyNameTag();
      addTeamEntry(loopPlayer, currentPlayer, loopNameTag);
    });
  }

  /**
   * adds a team entry given on the target, "all" player (looping) and the given nameTag for the
   * player
   *
   * @param target      Player who should get the entry
   * @param otherPlayer Player - the player that the entry comes from
   * @param nameTag     EasyNameTag - depends on the situation ^look above
   */
  private void addTeamEntry(Player target, Player otherPlayer, EasyNameTag nameTag) {
    String teamName = nameTag.getSortId() + nameTag.getGroupName();

    if (teamName.length() > 16) {
      teamName = teamName.substring(0, 16);
    }

    Team team = otherPlayer.getScoreboard().getTeam(teamName);
    if (team == null) {
      team = otherPlayer.getScoreboard().registerNewTeam(teamName);
    }

    // prefix & suffix have a max of 64 chars
    String prefix = (nameTag.getPrefix().length() > 64) ? nameTag.getPrefix().substring(0, 64) :
        nameTag.getPrefix();
    String suffix = (nameTag.getSuffix().length() > 64) ? nameTag.getSuffix().substring(0, 64) :
        nameTag.getSuffix();
    String color = nameTag.getColor();
    String playerName = nameTag.easyPlayer.getPlayer().getName();

    try {
      Method method = team.getClass().getDeclaredMethod("setColor", ChatColor.class);
      method.setAccessible(true);

      if (color != null && !color.isEmpty()) {
        ChatColor chatColor = ChatColor.getByChar(color.replaceAll("&", "").replaceAll("§", ""));
        if (chatColor != null) {
          method.invoke(team, chatColor);
        }
      } else {
        color = ChatColor.getLastColors(prefix.replace('&', '§'));
        if (!color.isEmpty()) {
          ChatColor chatColor = ChatColor.getByChar(color.replaceAll("&", "").replaceAll("§", ""));
          if (chatColor != null) {
            method.invoke(team, chatColor);
          }
        }
      }
    } catch (NoSuchMethodException ignored) {
    } catch (IllegalAccessException | InvocationTargetException exception) {
      exception.printStackTrace();
    }

    team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));

    team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

    team.addEntry(target.getName());

    target.setDisplayName(
        ChatColor.translateAlternateColorCodes('&', prefix + color + playerName + suffix));
    target.setPlayerListName(
        ChatColor.translateAlternateColorCodes('&', prefix + color + playerName + suffix));
  }

  /**
   * copyright by CloudNet. initializes a scoreboard if none is given.
   *
   * @param player Player
   */
  private void initScoreboard(Player player) {
    if (player.getScoreboard()
        .equals(player.getServer().getScoreboardManager().getMainScoreboard())) {
      player.setScoreboard(player.getServer().getScoreboardManager().getNewScoreboard());
    }
  }

  /**
   * replaces the normal string messages with the format given
   *
   * @param message message from the event event.getMessage()
   * @param format  format given
   * @return returns full format (event.setFormat())
   */
  public String replaceChatMessage(String message, String format) {
    final Player player = this.easyPlayer.getPlayer();
    final String prefix = getPrefix();
    final String suffix = getSuffix();
    final String color = getColor();
    final String display = getDisplay();
    final String group = getGroupName();

    message = message.replace("%", "%%");
    if (player.hasPermission("cloudnet.chat.color")) {
      message = ChatColor.translateAlternateColorCodes('&', message);
    }

    if (ChatColor.stripColor(message).trim().isEmpty()) {
      return null;
    }

    format = format
        .replace("%name%", player.getName())
        .replace("%uniqueId%", player.getUniqueId().toString());

    if (this.getHighestIPermissionGroup() != null) {
      format = ChatColor.translateAlternateColorCodes('&',
          format
              .replace("%group%", group)
              .replace("%display%", display)
              .replace("%prefix%", prefix)
              .replace("%suffix%", suffix)
              .replace("%color%", color)
      );
    } else {
      format = ChatColor.translateAlternateColorCodes('&',
          format
              .replace("%group%", "")
              .replace("%display%", "")
              .replace("%prefix%", "")
              .replace("%suffix%", "")
              .replace("%color%", "")
      );
    }

    return format.replace("%message%", message);
  }
}
