package pw.rapture.bukkit.listeners;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateGroupEvent;
import de.dytanic.cloudnet.driver.event.events.permission.PermissionUpdateUserEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import pw.rapture.bukkit.EasyCloudNet;
import pw.rapture.bukkit.objects.EasyPlayer;

public class PlayerListener implements Listener {

  private final pw.rapture.bukkit.EasyCloudNet easyCloudNet;
  private final Plugin plugin;

  public PlayerListener(EasyCloudNet easyCloudNet, Plugin plugin) {
    this.easyCloudNet = easyCloudNet;
    this.plugin = plugin;
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    this.easyCloudNet.addEasyPlayer(event.getPlayer());

    Bukkit.getOnlinePlayers().forEach(k -> {
      this.easyCloudNet.getEasyPlayer(k).getEasyNameTag().updateNameTags();
    });
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.easyCloudNet.removeEasyPlayer(event.getPlayer());
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    pw.rapture.bukkit.objects.EasyPlayer playerObject = this.easyCloudNet.getEasyPlayer(event.getPlayer());

    String message = playerObject.getEasyNameTag().replaceChatMessage(event.getMessage(),
      this.easyCloudNet.getManagement().getOwnOverrideEntry().getFormat());
    if (message == null || message.isEmpty()) {
      event.setCancelled(true);
      return;
    }

    event.setFormat(message);
  }

  @EventListener
  public void handle(PermissionUpdateUserEvent event) {
    Bukkit.getScheduler()
      .runTaskAsynchronously(this.plugin, () -> Bukkit.getOnlinePlayers().stream()
        .filter(player -> player.getUniqueId().equals(event.getPermissionUser().getUniqueId()))
        .findFirst()
        .ifPresent(player -> {
          pw.rapture.bukkit.objects.EasyPlayer easyPlayer = this.easyCloudNet.getEasyPlayer(player.getUniqueId());
          easyPlayer.getEasyNameTag().updateNameTags();
        }));
  }

  @EventListener
  public void handle(PermissionUpdateGroupEvent event) {
    Bukkit.getScheduler()
      .runTaskAsynchronously(this.plugin, () -> Bukkit.getOnlinePlayers().stream()
        .filter(player -> this.easyCloudNet.getEasyPlayer(player).getEasyNameTag()
          .getHighestIPermissionGroup().equals(event.getPermissionGroup()))
        .forEach(player -> {
          EasyPlayer easyPlayer = this.easyCloudNet.getEasyPlayer(player.getUniqueId());
          easyPlayer.getEasyNameTag().updateNameTags();
        }));
  }

}
