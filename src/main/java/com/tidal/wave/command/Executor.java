package com.tidal.wave.command;

import com.tidal.wave.data.IntervalTime;
import com.tidal.wave.data.MaxTime;
import com.tidal.wave.supplier.ObjectSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tidal.utils.data.GlobalData.addData;
import static com.tidal.utils.data.GlobalData.getData;


@SuppressWarnings("rawtypes")
public class Executor implements ExecutorCommands {

    private static final Logger logger = LoggerFactory.getLogger(Executor.class);
    Map<String, Command> commandCollection = new ConcurrentHashMap<>(200);
    CommandContext context = (CommandContext) ObjectSupplier.instanceOf(CommandContext.class);
    List<Class<? extends Command>> commands = new LinkedList<>();

    private List<String> locators;

    Command getInstance(Class<? extends Command> inputClass) {
        String className = inputClass.getSimpleName();
        try {
            if (commandCollection.get(className) == null) {
                logger.debug(String.format("No instance found for class %s, creating a new one", className));
                Command classInstance = inputClass.getDeclaredConstructor().newInstance();
                commandCollection.put(className, classInstance);
            }
        } catch (Exception e) {
            logger.error(String.format("Exception thrown with class initiation for %s", className));
            logger.error(e.getMessage());
        }
        return commandCollection.get(className);
    }

    /**
     * This method is used to invoke a command with a specific method.
     * This method will not store the commands for later execution.
     * Retry methods use this method to invoke the command.
     *
     * @param commandClass The class of the command to be invoked
     * @param method      The method to be invoked
     * @param <U>        The return type of the method
     * @return The return type of the method
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U invokeCommand(Class<? extends Command> commandClass, String method) {
        getInstance(commandClass).contextSetter(context);
        return (U) getInstance(commandClass).execute(method);
    }

    /**
     * This method is used to invoke a command with a specific method.
     * This method will store the commands for later execution.
     * The locator will be stored temporarily in the context and will be used later.
     * The reason is that the retry methods may overwrite the locator in the CommandContext class.
     *
     * @param commandClass The class of the command to be invoked
     * @return The return type of the method
     * @param <U>     The return type of the method
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U invokeCommand(Class<? extends Command> commandClass) {
        getInstance(commandClass).contextSetter(context);
        commands.add(commandClass);
        locators = context.getLocators();
        return (U) getInstance(commandClass).execute(Introspector.decapitalize(commandClass.getSimpleName()));
    }


    @Override
    public void invokeCommand() {
        context.setLocatorSet(locators);
        commands.forEach(command -> getInstance(command).execute(Introspector.decapitalize(command.getSimpleName())));
    }

    @Override
    public void clearCommands() {
        commands.clear();
    }

    @Override
    public Executor withMultipleElements(boolean isTrue) {
        context.setMultiple(isTrue);
        return this;
    }

    @Override
    public Executor withText(String text) {
        context.setTextInput(text);
        return this;
    }

    @Override
    public Executor withTabIndex(int index) {
        context.setTabIndex(index);
        return this;
    }

    @Override
    public Executor withTimeToWait(int seconds) {
        context.setHoverWaitTime(seconds);
        return this;
    }

    @Override
    public Executor withAttribute(String attributeName) {
        context.setAttributeName(attributeName);
        return this;
    }

    @Override
    public Executor withCharSequence(CharSequence... sequence) {
        context.setSequence(sequence);
        return this;
    }

    @Override
    public Executor withSelectIndex(int index) {
        context.setSelectIndex(index);
        return this;
    }

    @Override
    public Executor isVisible(boolean visible) {
        context.setVisibility(visible);
        return this;
    }

    @Override
    public Executor presenceOfShadowDom() {
        context.setShadowDomPresence();
        return this;
    }

    @Override
    public Executor usingLocator(List<String> locators) {
        context.setLocatorSet(locators);
        return this;
    }

    @Override
    public Executor withXYCords(int xCords, int yCords) {
        context.setXYCords(xCords, yCords);
        return this;
    }

    @Override
    public Executor withZoomLevel(double zoomLevel) {
        context.setZoomLevel(zoomLevel);
        return this;
    }

    @Override
    public Executor withElementIndex(int index) {
        context.setElementIndex(index);
        return this;
    }

    @Override
    public Executor pageRefreshData(MaxTime maxTime, IntervalTime intervalTime) {
        context.setMaxRefreshTime(maxTime);
        context.setIntervalTime(intervalTime);
        return this;
    }


    public Executor debugMode(boolean debugMode) {
        context.setDebugMode(debugMode);
        return this;
    }
}
