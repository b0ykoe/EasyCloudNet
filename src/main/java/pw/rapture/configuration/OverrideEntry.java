package pw.rapture.configuration;

public class OverrideEntry {
  private String prefix = "ECN";
  private String task = null;
  private String format = "%display%%name% &8:&f %message%";

  public OverrideEntry(String task) {
    this.task = task;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String commandPrefix) {
    this.prefix = commandPrefix;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}
