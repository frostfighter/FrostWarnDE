package tm.info.felix;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public final class Warn extends JavaPlugin implements Listener {
	
	boolean ban;
	boolean kickonwarn;
	int warnstoban;
	
	public boolean cfgExists() {
		File f = new File("./" + this.getName() + "/config.yml");
		return f.exists();
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		if(!cfgExists()) {
			saveDefaultConfig();
		}
		getConfig();
		
		
		kickonwarn = getConfig().getBoolean("kickonwarn");
		ban = getConfig().getBoolean("ban");
		warnstoban = getConfig().getInt("warnstoban");
	}
	
	@Override
	public void onDisable() {
	}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("warn")){
    		if(args.length >= 2) {
                  getConfig();
                  saveConfig();
                  Player warnedplayer = getServer().getPlayer(args[0]);
                  
                  String reason = "";
                  for(int i = 1; i < args.length; i++) {
                	  reason = reason + args[i] + " ";
                  }
                  System.out.print("Der Spieler " + args[0] + " wurde gewarnt!");
                  
                  List<String> warnings = getConfig().getStringList("warnings.warningsof" + args[0]);
                  warnings.add(sender.getName() + ":" + reason);
                  try {
                  	  warnedplayer.sendMessage(ChatColor.YELLOW + "[Warnung] Du wurdest gewarnt: " + reason);
                  }
                  catch(Exception e) {
                  }
                  
                  sender.sendMessage(ChatColor.YELLOW + "[Warnung] Der Spieler " + args[0] + " wurde gewarnt!");
                  getConfig().set("warnings.warningsof" + args[0], warnings);
                  saveConfig();
                  
                  if(ban) {
                	  if(getConfig().getList("warnings.warningsof" + args[0]).size() >= warnstoban) {
                		  try {
                			  warnedplayer.kickPlayer(ChatColor.RED + "Bann - Zu viele Warnungen erhalten: " + reason);
                		  }
                		  catch(Exception e) { }
                	  getServer().dispatchCommand(getServer().getConsoleSender(), getConfig().getString("bancommand") + " " + args[0] + " Dritte Warnung erhalten!");
                	  }
                  }
                  if(kickonwarn) {
                	  try {
                		  warnedplayer.kickPlayer(ChatColor.RED + "Du hast eine Warnung erhalten: " + reason);
                	  }
                	  catch(Exception e) { }
                  }
                  return true;
    		}
    		else {
    			sender.sendMessage(ChatColor.BLUE + "Benutzung: /warn [Player] <Reason>");
    			return true;
    		}
    	}
    	
    	
    	else if(cmd.getName().equalsIgnoreCase("warnings")) {
    		if(args.length == 1) {
    			if(!sender.hasPermission("frostwarn.warnings.others")) {
    				sender.sendMessage("No permission!");
    				return true;
    			}
    			String player = args[0];
    			List<String> warnings = getConfig().getStringList("warnings.warningsof" + player);
    			sender.sendMessage(ChatColor.YELLOW + "Warnungen von " + player + ":");
    			for(int i=0; i < warnings.size(); i++) {
    				String content = warnings.get(i);
    				String by = content.split(":")[0];
    				String reason = content.split(":")[1];
    				sender.sendMessage(ChatColor.YELLOW + "Warnung " + (i+1) + " von " + by + ": " + reason);
    			}
    			return true;
    		}
    		else if(args.length == 0) {
    			String player = sender.getName();
    			List<String> warnings = getConfig().getStringList("warnings.warningsof" + player);
    			sender.sendMessage(ChatColor.YELLOW + "Deine Warnungen:");
    			for(int i=0; i < warnings.size(); i++) {
    				String content = warnings.get(i);
    				String by = content.split(":")[0];
    				String reason = content.split(":")[1];
    				sender.sendMessage("Warnung " + (i+1) + " von " + by + ": " + reason);
    			}
    			return true;
    		}
    		else {
    			sender.sendMessage(ChatColor.BLUE + "Benutzung: /warnings [Player]");
    		}
    	}
    	
    	
    	else if(cmd.getName().equalsIgnoreCase("clearwarns")) {
    		if(args.length == 1) {
    			String player = args[0];
    			getConfig().set("warnings.warningsof" + player, Arrays.asList());
    			saveConfig();
    			sender.sendMessage("Warnungen von " + player + " gelöscht!");
    			return true;
    		}
    	}
    	
    	
		return false;
    }
	
	


	
	
}
