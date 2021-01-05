package pw.rapture.bukkit.objects;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pw.rapture.bukkit.EasyCloudNet;

public class EasyPlayer {

  private final EasyCloudNet easyCloudNet;
  private final UUID uuid;
  private final pw.rapture.bukkit.objects.EasyNameTag easyNameTag;
  private final pw.rapture.bukkit.objects.EasyScoreboard easyScoreboard;

  public EasyPlayer(EasyCloudNet easyCloudNet, UUID uuid) {
    this.easyCloudNet = easyCloudNet;
    this.uuid = uuid;
    this.easyNameTag = new pw.rapture.bukkit.objects.EasyNameTag(easyCloudNet, this);
    this.easyScoreboard = new pw.rapture.bukkit.objects.EasyScoreboard(this);
  }

  /**
   * Returns the normal Player object.
   *
   * @return Player
   */
  public Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }

  public EasyNameTag getEasyNameTag() {
    return this.easyNameTag;
  }

  public EasyScoreboard getEasyScoreboard() {
    return this.easyScoreboard;
  }
}
