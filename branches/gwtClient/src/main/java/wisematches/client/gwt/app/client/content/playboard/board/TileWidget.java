package wisematches.client.gwt.app.client.content.playboard.board;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import wisematches.server.games.scribble.core.Position;
import wisematches.server.games.scribble.core.Tile;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class TileWidget extends ComplexPanel
        implements HasMouseDownHandlers, HasMouseUpHandlers, HasMouseMoveHandlers, HasMouseOutHandlers {
    private Tile tile;
    private final boolean showCost;

    private boolean selected = false;
    private Position position;
    private boolean pinned = false;

    private boolean selectionEnabled = false;
    private boolean disabled = false;

    private TileSelectionCallback selectionCallback;

    /**
     * Size of one tile in pixels.
     */
    public static final int TILE_SIZE = 22;

    public TileWidget(Tile tile) {
        this(tile, false);
    }

    public TileWidget(Tile tile, boolean showCost) {
        this.tile = tile;
        this.showCost = showCost;
        initPanel();
    }

    private void initPanel() {
        Element element = DOM.createDiv();
        setElement(element);

        addStyleName("tile");
        addStyleName("tile" + tile.getCost());

        element.setInnerHTML(getTileCaptionHTML(tile));

        disableSelection(element);
        disableContextMenu(element);

        addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (selectionEnabled) {
                    setSelected(!isSelected());
                }
            }
        }, ClickEvent.getType());

        updateImage();
    }

    public void setTileCharacter(char ch) {
        tile = new Tile(tile.getNumber(), ch, tile.getCost());
        getElement().setInnerHTML(getTileCaptionHTML(tile));
    }

    private String getTileCaptionHTML(Tile tile) {
        if (showCost) {
            return String.valueOf(Character.toUpperCase(tile.getLetter())) + "<sub>" + tile.getCost() + "</sub>";
        } else {
            return String.valueOf(Character.toUpperCase(tile.getLetter()));
        }
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

/*
    @Override
    public void onBrowserEvent(Event event) {
        if (disabled) {
            return;
        }

        switch (DOM.eventGetType(event)) {
            case Event.ONCLICK:
                if (selectionEnabled) {
                    setSelected(!isSelected());
                }
                break;
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEUP:
            case Event.ONMOUSEMOVE:
            case Event.ONMOUSEOVER:
            case Event.ONMOUSEOUT: {
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            }
        }
    }
*/

    public Tile getTile() {
        return tile;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;

            if (selected) {
                addStyleName("tile-selected");
            } else {
                removeStyleName("tile-selected");
            }
            updateImage();

            if (selectionCallback != null) {
                selectionCallback.tileSelected(this, selected);
            }
        }
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        if (this.pinned != pinned) {
            this.pinned = pinned;
            updateImage();
        }
    }

    public boolean isSelectionEnabled() {
        return selectionEnabled;
    }

    public void setSelectionEnabled(boolean selectionEnabled) {
        this.selectionEnabled = selectionEnabled;

        if (!selectionEnabled && selected) {
            setSelected(false);
        } else if (selectionEnabled && selected) {
            setSelected(true);
        }
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private void updateImage() {
        int y = 0;
        int x = -TILE_SIZE * tile.getCost(); // width of one tile

        if (selected) {
            y -= TILE_SIZE; // one tile height
        }

        if (pinned) {
            y -= TILE_SIZE + TILE_SIZE; // two tiles height
        }
        getElement().getStyle().setProperty("backgroundPosition", x + "px " + y + "px");
    }

    public TileSelectionCallback getSelectionCallback() {
        return selectionCallback;
    }

    public void setSelectionCallback(TileSelectionCallback selectionCallback) {
        this.selectionCallback = selectionCallback;
    }

    private native void disableSelection(Element elem) /*-{
        elem.onselectstart=function(a,b) {return false; };
    }-*/;

    private native void disableContextMenu(Element elem) /*-{
        elem.oncontextmenu=function(a,b) {return false;};
    }-*/;
}
