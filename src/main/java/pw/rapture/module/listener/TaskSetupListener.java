package pw.rapture.module.listener;

import de.dytanic.cloudnet.console.animation.questionlist.QuestionListEntry;
import de.dytanic.cloudnet.console.animation.questionlist.answer.QuestionAnswerTypeBoolean;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.service.ServiceEnvironmentType;
import de.dytanic.cloudnet.event.setup.SetupCompleteEvent;
import de.dytanic.cloudnet.event.setup.SetupResponseEvent;
import pw.rapture.Constants;
import pw.rapture.configuration.Configuration;
import pw.rapture.configuration.OverrideEntry;
import pw.rapture.module.Module;

// Original: https://github.com/CloudNetService/CloudNet-v3/blob/329c21f03a9e668e1ffc2eb5b575c007c533ba44/cloudnet-modules/cloudnet-npcs/src/main/java/eu/cloudnetservice/cloudnet/ext/npcs/node/listener/NPCTaskSetupListener.java
public class TaskSetupListener {

  private final Module module;

  public TaskSetupListener(Module module) {
    this.module = module;
  }

  @EventListener
  public void handleSetupComplete(SetupCompleteEvent event) {
    if (!event.getSetup().getName().equals("TaskSetup")) {
      return;
    }

    String taskName = (String) event.getSetup().getResult("name");

    Configuration configuration = this.module.getConfiguration();

    System.out.println(event.getSetup().getResults());

    if (event.getSetup().hasResult(Constants.EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE)) {
      boolean generateOverride =
          (Boolean) event.getSetup().getResult(Constants.EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE);

      if (configuration.getOverrides().stream()
          .noneMatch(entry -> entry.getTask() == taskName) && generateOverride) {
        configuration.getOverrides().add(new OverrideEntry(taskName));
      }
    }

    if (event.getSetup().hasResult(Constants.EASYCLOUDNET_TASK_SETUP_BLACKLIST)) {
      boolean generateBlacklist =
          (Boolean) event.getSetup().getResult(Constants.EASYCLOUDNET_TASK_SETUP_BLACKLIST);

      if (configuration.getBlacklist().stream()
          .noneMatch(entry -> entry == taskName) && generateBlacklist) {
        configuration.getBlacklist().add(taskName);
      }
    }

    this.module.saveConfiguration();
  }

  @EventListener
  public void handleSetupResponse(SetupResponseEvent event) {
    if (!event.getSetup().getName().equals("TaskSetup") ||
        !(event.getResponse() instanceof ServiceEnvironmentType)) {
      return;
    }

    ServiceEnvironmentType environment = (ServiceEnvironmentType) event.getResponse();
    if (environment != ServiceEnvironmentType.MINECRAFT_SERVER) {
      return;
    }

    // if there is a entry for the override skip it
    if (!event.getSetup().hasResult(Constants.EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE)) {
      event.getSetup().addEntry(new QuestionListEntry<>(
          Constants.EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE,
          this.module.getConfiguration().getMessages()
              .getOrDefault(Constants.EASYCLOUDNET_TASK_SETUP_GENERATE_OVERRIDE,
                  "Do you want to add a new Override for the service?"),
          new QuestionAnswerTypeBoolean() {
            @Override
            public String getRecommendation() {
              return super.getFalseString();
            }
          }
      ));
    }

    // if there is a entry for the blacklist skip it
    if (!event.getSetup().hasResult(Constants.EASYCLOUDNET_TASK_SETUP_BLACKLIST)) {
      event.getSetup().addEntry(new QuestionListEntry<>(
          Constants.EASYCLOUDNET_TASK_SETUP_BLACKLIST,
          this.module.getConfiguration().getMessages()
              .getOrDefault(Constants.EASYCLOUDNET_TASK_SETUP_BLACKLIST,
                  "Do you want to add a new blacklist this service? EasyCloudNet wont be installed on that service."),
          new QuestionAnswerTypeBoolean() {
            @Override
            public String getRecommendation() {
              return super.getFalseString();
            }
          }
      ));
    }
  }
}
