package ru.jekarus.jgui.event;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import ru.jekarus.jgui.argument.GuiArguments;
import ru.jekarus.jgui.gui.Gui;
import ru.jekarus.jgui.gui.content.GuiContent;
import ru.jekarus.jgui.gui.slot.SpongeGuiSlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class GuiEventHandler {

    private final Gui gui;
    private final GuiEventTypes eventTypes = GuiEventTypes.getInstance();

    public GuiEventHandler(Gui gui)
    {
        this.gui = gui;
    }

    private int getIndex(SlotTransaction transaction)
    {
        Optional<SlotIndex> optionalSlotIndex = transaction.getSlot().getInventoryProperty(SlotIndex.class);
        if (optionalSlotIndex.isPresent())
        {
            SlotIndex slotIndex = optionalSlotIndex.get();
            return slotIndex.getValue() == null ? -1 : slotIndex.getValue();
        }
        else
        {
            return -1;
        }
    }

    public void handleTransactions(ClickInventoryEvent event, HandleTransactionResult handleTransaction)
    {
        Optional<GuiContent> optionalContent = this.gui.getContent();
        if (!optionalContent.isPresent())
        {
            handleTransaction.result(Collections.emptyList(), event.getTransactions());
            return;
        }
        GuiContent content = optionalContent.get();

        Collection<SpongeGuiSlot> correct = new ArrayList<>();
        Collection<SlotTransaction> incorrect = new ArrayList<>();

        for (SlotTransaction transaction : event.getTransactions())
        {
            int index = getIndex(transaction);
            Optional<SpongeGuiSlot> optionalSlot = content.getSpongeSlot(index);
            if (optionalSlot.isPresent())
            {
                correct.add(optionalSlot.get());
            }
            else
            {
                incorrect.add(transaction);
            }
        }

        handleTransaction.result(correct, incorrect);
    }

    private <E extends ClickInventoryEvent> void fireEvent(
            Collection<SpongeGuiSlot> slots,
            GuiArguments arguments
    )
    {
        Optional<GuiEventType> optionalType = this.eventTypes.from(arguments.event.clazz);
        if (optionalType.isPresent())
        {
            arguments.event.type = optionalType.get();
            for (SpongeGuiSlot slot : slots)
            {
                slot.handleEvent(arguments);
            }
        }
    }

    private Optional<GuiArguments> createArguments(ClickInventoryEvent event)
    {
        Optional<Player> optionalPlayer = event.getCause().first(Player.class);
        if (!optionalPlayer.isPresent())
        {
            return Optional.empty();
        }

        Player player = optionalPlayer.get();
        GuiArguments arguments = new GuiArguments(player);
        arguments.event.instance = event;
        return Optional.of(arguments);
    }

    public void onEvent(ClickInventoryEvent.Primary event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Primary.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Middle event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Middle.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Secondary event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Secondary.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Shift.Primary event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Shift.Primary.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Shift.Secondary event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Shift.Secondary.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Double event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Double.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Drop.Single event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Drop.Single.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.Drop.Full event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.Drop.Full.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public void onEvent(ClickInventoryEvent.NumberPress event)
    {
        this.createArguments(event).ifPresent(arguments ->
        {
            handleTransactions(event, (correct, incorrect) ->
            {
                if (!correct.isEmpty())
                {
                    event.setCancelled(true);
                    arguments.event.clazz = ClickInventoryEvent.NumberPress.class;
                    this.fireEvent(correct, arguments);
                }
            });
        });
    }

    public interface HandleTransactionResult {

        void result(Collection<SpongeGuiSlot> correct, Collection<SlotTransaction> incorrect);

    }

}