package wisematches.client.gwt.core.client.widget;


/**
 * {@code MessagesPanel} is not a GWT widget and required a {@code Toolbar}
 * to display messages.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MessagesBar {
//    private ToolbarButton clearEvent;
//    private ToolbarButton clearAllEvent;
//    private ToolbarButton prevEvent;
//    private ToolbarButton nextEvent;
//
//    private ToolbarTextItem messageTextItems;
//    private ToolbarTextItem numberOfMessage;
//
//    private int currentIndex = -1;
//    private final List<String> messageEvents = new ArrayList<String>();
//
//    public MessagesBar() {
//    }
//
//    public void associateToolbar(Toolbar toolbar) {
//        clearEvent = new ToolbarButton();
//        clearEvent.setIcon(GWT.getModuleBaseURL() + "images/events/eventRemove.png");
//        clearEvent.setTooltip(COMMON.ttpClearActiveEvent());
//        clearEvent.setDisabled(true);
//        clearEvent.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                if (currentIndex != -1) {
//                    messageEvents.remove(currentIndex);
//                }
//                showEvent(currentIndex);
//            }
//        });
//
//        clearAllEvent = new ToolbarButton();
//        clearAllEvent.setIcon(GWT.getModuleBaseURL() + "images/events/eventAllRemove.png");
//        clearAllEvent.setTooltip(COMMON.ttpClearAllEvents());
//        clearAllEvent.setDisabled(true);
//        clearAllEvent.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                messageEvents.clear();
//                showLastEvent();
//            }
//        });
//
//        prevEvent = new ToolbarButton();
//        prevEvent.setIcon(GWT.getModuleBaseURL() + "images/events/eventPrevious.png");
//        prevEvent.setTooltip(COMMON.ttpShowPreviousEvent());
//        prevEvent.setDisabled(true);
//        prevEvent.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                showEvent(currentIndex - 1);
//            }
//        });
//
//        nextEvent = new ToolbarButton();
//        nextEvent.setIcon(GWT.getModuleBaseURL() + "images/events/eventNext.png");
//        nextEvent.setTooltip(COMMON.ttpShowNextEvent());
//        nextEvent.setDisabled(true);
//        nextEvent.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                showEvent(currentIndex + 1);
//            }
//        });
//
//        messageTextItems = new ToolbarTextItem("");
//        numberOfMessage = new ToolbarTextItem("0 " + COMMON.lblOf() + " 0");
//
//        toolbar.addItem(messageTextItems);
//        toolbar.addFill();
//        toolbar.addButton(clearEvent);
//        toolbar.addButton(clearAllEvent);
//        toolbar.addSpacer();
//        toolbar.addButton(prevEvent);
//        toolbar.addItem(numberOfMessage);
//        toolbar.addButton(nextEvent);
//    }
//
//    private void showEvent(int index) {
//        if (index < messageEvents.size()) {
//            currentIndex = index;
//            messageTextItems.setText(messageEvents.get(index));
//            checkButtons();
//        } else {
//            showLastEvent();
//        }
//
//    }
//
//    private void showLastEvent() {
//        final int count = messageEvents.size();
//        if (count != 0) {
//            currentIndex = count - 1;
//            messageTextItems.setText(messageEvents.get(currentIndex));
//        } else {
//            currentIndex = -1;
//            messageTextItems.setText("");
//        }
//        checkButtons();
//    }
//
//    public void addEvent(String event) {
//        messageEvents.add(event);
//        showLastEvent();
//    }
//
//    private void checkButtons() {
//        final int eventsCount = messageEvents.size();
//
//        numberOfMessage.setText((currentIndex + 1) + " " + COMMON.lblOf() + " " + eventsCount);
//
//        clearAllEvent.setDisabled(eventsCount == 0);
//        clearEvent.setDisabled(eventsCount == 0);
//        prevEvent.setDisabled(currentIndex <= 0);
//        nextEvent.setDisabled(currentIndex == eventsCount - 1);
//    }
}
