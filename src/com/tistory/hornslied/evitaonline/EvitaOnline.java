package com.tistory.hornslied.evitaonline;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.tistory.hornslied.evitaonline.balance.BalanceManager;
import com.tistory.hornslied.evitaonline.chat.ChatManager;
import com.tistory.hornslied.evitaonline.classes.ClassManager;
import com.tistory.hornslied.evitaonline.combat.CombatLogManager;
import com.tistory.hornslied.evitaonline.combat.OldCombatManager;
import com.tistory.hornslied.evitaonline.commands.AncientCityCommand;
import com.tistory.hornslied.evitaonline.commands.ChatCommand;
import com.tistory.hornslied.evitaonline.commands.EconomyCommand;
import com.tistory.hornslied.evitaonline.commands.SelectionCommand;
import com.tistory.hornslied.evitaonline.commands.TownCommand;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.commons.util.repeater.RepeatHandler;
import com.tistory.hornslied.evitaonline.db.DBManager;
import com.tistory.hornslied.evitaonline.dynmap.DynmapManager;
import com.tistory.hornslied.evitaonline.scoreboard.ScoreboardManager;
import com.tistory.hornslied.evitaonline.selection.SelectionManager;
import com.tistory.hornslied.evitaonline.system.SystemManager;
import com.tistory.hornslied.evitaonline.timer.TimerManager;
import com.tistory.hornslied.evitaonline.universe.UniverseManager;

import fr.minuskube.inv.InventoryManager;

public class EvitaOnline extends JavaPlugin {
	
	private static EvitaOnline instance;
	
	private CommandsManager<CommandSender> commands;
	
	private ConfigManager configManager;
	private DBManager dbManager;
	private UniverseManager universeManager;
	private SystemManager systemManager;
	private BalanceManager balanceManager;
	private OldCombatManager oldCombatManager;
	private ChatManager chatManager;
	private TimerManager timerManager;
	private CombatLogManager combatLogManager;
	private ScoreboardManager scoreboardManager;
	private DynmapManager dynmapManager;
	private ClassManager classManager;
	private SelectionManager selectionManager;
	
	private InventoryManager invManager;
	
	public static EvitaOnline getInstance() {
		return instance;
	}
	
	public ConfigManager getConfigManager() {
		return configManager;
	}

	public DBManager getDBManager() {
		return dbManager;
	}
	
	public UniverseManager getUniverseManager() {
		return universeManager;
	}
	
	public SystemManager getSystemManager() {
		return systemManager;
	}
	
	public BalanceManager getBalanceManager() {
		return balanceManager;
	}
	
	public OldCombatManager getOldCombatManager() {
		return oldCombatManager;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}
	
	public TimerManager getTimerManager() {
		return timerManager;
	}
	
	public CombatLogManager getCombatLogManager() {
		return combatLogManager;
	}
	
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}
	
	public DynmapManager getDynmapManager() {
		return dynmapManager;
	}
	
	public ClassManager getClassManager() {
		return classManager;
	}
	
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}
	
	public InventoryManager getInvManager() {
		return invManager;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		new RepeatHandler(this);
		invManager = new InventoryManager(this);
		invManager.init();
		
		configManager = new ConfigManager(this);
		dbManager = new DBManager(this);
		universeManager = new UniverseManager(this);
		systemManager = new SystemManager(this);
		balanceManager = new BalanceManager(this);
		oldCombatManager = new OldCombatManager(this);
		chatManager = new ChatManager(this);
		timerManager = new TimerManager(this);
		combatLogManager = new CombatLogManager(this);
		scoreboardManager = new ScoreboardManager(this);
        if(Bukkit.getPluginManager().getPlugin("dynmap") != null) {
        	dynmapManager = new DynmapManager(this);
        }
        classManager = new ClassManager(this);
		selectionManager = new SelectionManager(this);
        
		setupCommands();
	}
	
	private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        
        //Register commands
        cmdRegister.register(ChatCommand.class);
        cmdRegister.register(TownCommand.class);
        cmdRegister.register(AncientCityCommand.class);
        cmdRegister.register(EconomyCommand.class);
        cmdRegister.register(SelectionCommand.class);
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(P.Server + C.Red + "명령어 사용 권한이 없습니다.");
        } catch (MissingNestedCommandException e) {
            
        } catch (CommandUsageException e) {
            sender.sendMessage(P.Server + C.Red + "명령어 사용 방법: " + e.getUsage());
        } catch (WrappedCommandException e) {
        	e.printStackTrace();
            if (e.getCause() instanceof NumberFormatException) {
               
            } else {
                
            }
        } catch (CommandSenderTypeException e) { 
        	sender.sendMessage(P.Server + C.Red + "플레이어만 사용가능한 명령어입니다.");
        } catch (CommandException e) {
        	sender.sendMessage(P.Server + C.Red + e.getMessage());
        }
 
        return true;
    }
}
