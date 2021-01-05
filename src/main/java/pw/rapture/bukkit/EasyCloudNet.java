package pw.rapture.bukkit;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pw.rapture.bukkit.listeners.PlayerListener;
import pw.rapture.bukkit.objects.EasyPlayer;

public final class EasyCloudNet extends JavaPlugin {

  private static EasyCloudNet easyCloudNet;
  private final ArrayList<pw.rapture.bukkit.objects.EasyPlayer> easyPlayers = new ArrayList<>();
  private Management management;

  public static EasyCloudNet getInstance() {
    return easyCloudNet;
  }

  @Override
  public void onEnable() {
    easyCloudNet = this;
    this.management = new Management();

    PlayerListener playerListener = new PlayerListener(this, this);
    this.getServer().getPluginManager().registerEvents(playerListener, this);
    CloudNetDriver.getInstance().getEventManager().registerListener(playerListener);
    CloudNetDriver.getInstance().getEventManager().registerListener(this.management);

    // requests a initial configuration update
    this.management.requestConfiguration();

    // adds all EasyPlayers for each player online
    this.getServer().getOnlinePlayers().forEach(this::addEasyPlayer);
  }

  @Override
  public void onDisable() {
    this.easyPlayers.clear();
    CloudNetDriver.getInstance().getEventManager()
        .unregisterListeners(this.getClass().getClassLoader());
    Wrapper.getInstance().unregisterPacketListenersByClassLoader(this.getClass().getClassLoader());
  }

  public Management getManagement() {
    return this.management;
  }

  /**
   * Returns the easyPlayer with that Bukkit Player.
   *
   * @param player Player
   * @return EasyPlayer or Null
   */
  public pw.rapture.bukkit.objects.EasyPlayer getEasyPlayer(Player player) {
    return this.easyPlayers.stream().filter(p -> p.getPlayer().equals(player)).findFirst()
        .orElse(null);
  }

  /**
   * Returns the easyPlayer with that uuid.
   *
   * @param uuid UUID
   * @return EasyPlayer or Null
   */
  public pw.rapture.bukkit.objects.EasyPlayer getEasyPlayer(UUID uuid) {
    return this.easyPlayers.stream().filter(p -> p.getPlayer().getUniqueId() == uuid).findFirst()
        .orElse(null);
  }

  /**
   * Adds an easyPlayer, with through Bukkit player, to the arraylist.
   *
   * @param player Player
   * @return EasyPlayer
   */
  public pw.rapture.bukkit.objects.EasyPlayer addEasyPlayer(Player player) {
    pw.rapture.bukkit.objects.EasyPlayer easyPlayer = new pw.rapture.bukkit.objects.EasyPlayer(this,
        player.getUniqueId());
    this.easyPlayers.add(easyPlayer);
    return easyPlayer;
  }

  /**
   * Adds an easyPlayer, with trough a uuid, to the arraylist.
   *
   * @param uuid UUID
   * @return EasyPlayer
   */
  public pw.rapture.bukkit.objects.EasyPlayer addEasyPlayer(UUID uuid) {
    pw.rapture.bukkit.objects.EasyPlayer easyPlayer = new EasyPlayer(this, uuid);
    this.easyPlayers.add(easyPlayer);
    return easyPlayer;
  }

  /**
   * Removes an easyPlayer if there is one, depends on the Bukkit player.
   *
   * @param player Player
   */
  public void removeEasyPlayer(Player player) {
    this.easyPlayers.removeIf(p -> p.getPlayer().equals(player));
  }

  /**
   * Removes an easyPlayer if there is one, depends on the uuid.
   *
   * @param uuid UUID
   */
  public void removeEasyPlayer(UUID uuid) {
    this.easyPlayers.removeIf(p -> p.getPlayer().getUniqueId() == uuid);
  }
}
