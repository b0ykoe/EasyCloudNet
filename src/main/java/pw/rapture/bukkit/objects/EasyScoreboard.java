package pw.rapture.bukkit.objects;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EasyScoreboard {

  private final EasyPlayer easyPlayer;
  private final String scoreboardUuid = UUID.randomUUID().toString().substring(0, 16);
  private Scoreboard scoreboard;

  public EasyScoreboard(EasyPlayer easyPlayer) {
    this.easyPlayer = easyPlayer;
  }

  /**
   * initializes the Scoreboard (Sidebar). Will be cropped to the first 128 Chars.
   *
   * @param scoreboardTitle String - title of the scoreboard
   */
  public void init(String scoreboardTitle) {
    // if playerObject is set, else nonsense
    if (this.easyPlayer == null) {
      return;
    }
    // max 128 chars
    scoreboardTitle =
        (scoreboardTitle.length() > 128) ? scoreboardTitle.substring(0, 128) : scoreboardTitle;
    Player player = this.easyPlayer.getPlayer();
    this.scoreboard = this.easyPlayer.getPlayer().getScoreboard();

    // if there is no scoreboard with the uuid create one
    if (scoreboard.getObjective(this.scoreboardUuid) == null) {
      scoreboard.registerNewObjective(this.scoreboardUuid, "dummy", scoreboardTitle);
    }

    // get the Scoreboard null check is nonsense since its created above
    Objective objective = player.getScoreboard().getObjective(this.scoreboardUuid);
    if (objective != null) {
      objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    // set the scoreboard to the player
    player.setScoreboard(scoreboard);
  }

  /**
   * Setter for the title in the scoreboard. Will be cropped to the first 128 Chars.
   *
   * @param scoreboardTitle String
   */
  public void setTitle(String scoreboardTitle) {
    // if playerObject is set, else nonsense
    if (this.easyPlayer == null) {
      return;
    }

    // max 128 chars
    scoreboardTitle =
        (scoreboardTitle.length() > 128) ? scoreboardTitle.substring(0, 128) : scoreboardTitle;

    Objective objective = this.scoreboard.getObjective(this.scoreboardUuid);

    // if there is a scoreboard with the uuid create one
    if (objective != null) {
      objective.setDisplayName(scoreboardTitle);
    }
  }

  /**
   * Setter for the text in the scoreboard. Will be cropped to the first 126 Chars.
   *
   * @param id   int - ID in the scoreboard can be 0 - 15 (max 15 entries)
   * @param text string - text of the line, has a maximum of 126 chars
   */
  public void setLine(int id, String text) {
    // gets the objective
    Objective objective = this.scoreboard.getObjective(this.scoreboardUuid);
    // if the Objective is somehow null aka it got not initialized yet
    if (objective == null) {
      return;
    }
    // sets a score with the color of the id and converts it to a string
    Score score = objective.getScore(ChatColor.values()[id] + "");
    // set the Score of the score (number right on the scoreboard)
    score.setScore(id);

    // if the team with the value does not exist create it
    if (this.scoreboard.getTeam(ChatColor.values()[id] + "") == null) {
      this.scoreboard.registerNewTeam(ChatColor.values()[id] + "");
    }

    // get the team, should never be null since it gets created above
    Team team = scoreboard.getTeam(ChatColor.values()[id] + "");

    // sanity check if it got removed somehow
    // if the team has no entry with the color value set one
    if (team != null && !team.hasEntry(ChatColor.values()[id] + "")) {
      team.addEntry(ChatColor.values()[id] + "");
    }

    // adds the team text
    this.setTeamText(team, text);
  }

  /**
   * updates a line in the scoreboard. Will be cropped to the first 126 Chars.
   *
   * @param id   int - ID in the scoreboard can be 0 - 15 (max 15 entries)
   * @param text string - text of the line, has a maximum of 128 chars
   */
  public void updateLine(int id, String text) {
    // gets the team of the color value as string
    Team team = this.easyPlayer.getPlayer().getScoreboard().getTeam(ChatColor.values()[id] + "");

    // updates the team text
    this.setTeamText(team, text);
  }

  private void setTeamText(Team team, String text) {
    // sanity check if it got removed somehow
    // if the text is not long enough set it and return since anything beyond here is for longer text
    if (text.length() <= 64 && team != null) {
      team.setPrefix(text);
      return;
    }

    // If the text actually goes above 32, cut it to 32 to prevent kicks and errors
    if (text.length() > 128) {
      text = text.substring(0, 128);
    }

    // replace the text empty spaces to 100% hit the color code and fetch it
    String temp = text.replace(" ", "");
    String colorCode = ChatColor.getLastColors(temp);
    if (colorCode.equals("")) {
      colorCode = "§f";
    }

    // sanity check if it got removed somehow#
    // Set the prefix to the first 64 characters
    if (team != null) {
      team.setPrefix(text.substring(0, 64));
    }
    // sanity check if it got removed somehow#
    // Now use the last 62 characters and put them into the suffix
    if (team != null) {
      team.setSuffix(colorCode + text.substring(64, 126));
    }
  }
}
