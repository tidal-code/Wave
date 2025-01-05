package dev.tidalcode.wave.verification.conditions;

import dev.tidalcode.wave.command.Executor;
import dev.tidalcode.wave.commands.GetAttribute;
import dev.tidalcode.wave.data.WaitTime;
import dev.tidalcode.wave.exceptions.TestAssertionError;
import dev.tidalcode.wave.wait.FluentWait;
import org.openqa.selenium.StaleElementReferenceException;

import java.time.Duration;

import static dev.tidalcode.wave.data.WaitTimeData.getWaitTime;

public class AttributeOnlyCondition extends Condition {
    private final String attribute;

    public AttributeOnlyCondition(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public void verify(Executor executor) {

        String duration = getWaitTime(WaitTime.EXPLICIT_WAIT_TIME) == null
                ? getWaitTime(WaitTime.DEFAULT_WAIT_TIME)
                : getWaitTime(WaitTime.EXPLICIT_WAIT_TIME);

        Duration waitDuration = Duration.ofSeconds(Integer.parseInt(duration));

        new FluentWait<>(executor)
                .pollingEvery(Duration.ofMillis(500))
                .forDuration(waitDuration)
                .ignoring(StaleElementReferenceException.class)
                .throwing(TestAssertionError.class)
                .withMessage(String.format("Failed to find attribute '%s'", attribute))
                .until(e -> e.withAttribute(attribute).invokeCommand(GetAttribute.class));
    }
}
