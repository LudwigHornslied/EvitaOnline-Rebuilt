package com.tistory.hornslied.evitaonline.dynmap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.dynmap.markers.PlayerSet;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;
import com.tistory.hornslied.evitaonline.universe.UniverseManager;
import com.tistory.hornslied.evitaonline.universe.nation.Color;
import com.tistory.hornslied.evitaonline.universe.nation.Nation;
import com.tistory.hornslied.evitaonline.universe.plot.Plot;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class DynmapManager implements Listener {
    private Plugin dynmap;
    private DynmapAPI api;
    private MarkerAPI markerapi;
    private EvitaOnline plugin;
    private UniverseManager universe;
    
    private MarkerSet set;
    private Set<String> visible;
    private Set<String> hidden;
    private boolean reload = false;
    private boolean stop;
    
    public DynmapManager(EvitaOnline plugin) {
    	this.plugin = plugin;
    	
        getDynmap();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        
        if(dynmap.isEnabled()) {
            activate();
        }
    }
    
    private void getDynmap() {
        dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if(dynmap == null)
            return;
        
        api = (DynmapAPI) dynmap;
    }

    private class Update extends BukkitRunnable {
        public void run() {
            if(!stop) {
                updateTowns();
                updateNationPlayerSets();
                this.runTaskLaterAsynchronously(plugin, 6000);
            }
        }
    }

    /*
    private class TownyUpdateReq implements Runnable {
        public void run() {
            if(!stop) {
                pending_upd_req = null;
                
                updateTowns();
            }
        }
    }
    private TownyUpdateReq pending_upd_req = null;
    private void requestUpdateTownMap(Town t) {
        info("requestUpdateTownMap("+ t.getTag() + ")");
        if(pending_upd_req == null) {
            pending_upd_req = new TownyUpdateReq();
            getServer().getScheduler().scheduleSyncDelayedTask(this, pending_upd_req, 20);
        }
    }*/

    private void updateNation(Nation nation) {
        Set<String> plids = new HashSet<String>();
        Set<EvitaPlayer> residents = nation.getResidents();
        for(EvitaPlayer evitaPlayer : residents) {
            plids.add(evitaPlayer.getName());
        }
        String setid = "towny.nation." + nation.getName();
        PlayerSet set = markerapi.getPlayerSet(setid);  /* See if set exists */
        if(set == null) {
            set = markerapi.createPlayerSet(setid, true, plids, false);
        } else {
            set.setPlayers(plids);
        }
    }

    private void updateNationPlayerSets() {
        for(Nation n : plugin.getUniverseManager().getNations()) {
            updateNation(n);
        }
    }

    /* Cannot do this until towny add/remove player events are fixed
    private class PlayerUpdate implements Runnable {
        public void run() {
            pending_upd = null;
            if(stop) return;
                
            for(Town t : town_to_upd) {
                updateTown(t);
            }
            for(Nation n : nation_to_upd) {
                updateNation(n);
            }
            town_to_upd.clear();
            nation_to_upd.clear();
        }
    }
    
    private HashSet<Town> town_to_upd = new HashSet<Town>();
    private HashSet<Nation> nation_to_upd = new HashSet<Nation>();
    private PlayerUpdate pending_upd;
    
    private void requestUpdateTownPlayers(Town t) {
        if(playersbytown)
            town_to_upd.add(t);
        try {
            if(playersbynation && t.hasNation())
                nation_to_upd.add(t.getNation());
        } catch (NotRegisteredException nrx) {
        }
        if(pending_upd == null) {
            pending_upd = new PlayerUpdate();
            getServer().getScheduler().scheduleSyncDelayedTask(this, pending_upd, 20);
        }
    }
    */
    
    private Map<String, AreaMarker> resareas = new HashMap<String, AreaMarker>();
    private Map<String, Marker> resmark = new HashMap<String, Marker>();
    
    private String formatInfoWindow(Town town) {
        String v = "<div class=\"regioninfo\">"
        		+ "<div class=\"infowindow\"><span style=\"font-size:120%;\">%regionname% (%nation%)</span><br /> "
        		+ "시장 <span style=\"font-weight:bold;\">%playerowners%</span></div>"
        		+"</div>";
        
        v = v.replace("%regionname%", town.getName());
        v = v.replace("%playerowners%", town.getMayor().getName());
        v = v.replace("%nation%", (town.hasNation()) ?  town.getNation().getName() : "무소속");
        
        return v;
    }
    
    private boolean isVisible(String id, String worldname) {
        if((visible != null) && (visible.size() > 0)) {
            if((visible.contains(id) == false) && (visible.contains("world:" + worldname) == false)) {
                return false;
            }
        }
        if((hidden != null) && (hidden.size() > 0)) {
            if(hidden.contains(id) || hidden.contains("world:" + worldname))
                return false;
        }
        return true;
    }
        
    private void addStyle(Nation nation, AreaMarker m) {
        Color color;
        
        if(nation == null)
        	color = Color.GRAY;
        else
        	color = nation.getColor();
        
        m.setLineStyle(3, 0.8, color.getHexColor());
        m.setFillStyle(0.35, color.getHexColor());
        m.setRangeY(64, 64);
        m.setBoostFlag(false);
    }
    
    private MarkerIcon getMarkerIcon(Town town) {
        if(town.isCapital())
            return markerapi.getMarkerIcon("king");
        else
            return markerapi.getMarkerIcon("default");
    }
 
    enum direction { XPLUS, ZPLUS, XMINUS, ZMINUS };
        
    /**
     * Find all contiguous blocks, set in target and clear in source
     */
    private int floodFillTarget(TileFlags src, TileFlags dest, int x, int y) {
        int cnt = 0;
        ArrayDeque<int[]> stack = new ArrayDeque<int[]>();
        stack.push(new int[] { x, y });
        
        while(stack.isEmpty() == false) {
            int[] nxt = stack.pop();
            x = nxt[0];
            y = nxt[1];
            if(src.getFlag(x, y)) { /* Set in src */
                src.setFlag(x, y, false);   /* Clear source */
                dest.setFlag(x, y, true);   /* Set in destination */
                cnt++;
                if(src.getFlag(x+1, y))
                    stack.push(new int[] { x+1, y });
                if(src.getFlag(x-1, y))
                    stack.push(new int[] { x-1, y });
                if(src.getFlag(x, y+1))
                    stack.push(new int[] { x, y+1 });
                if(src.getFlag(x, y-1))
                    stack.push(new int[] { x, y-1 });
            }
        }
        return cnt;
    }
    
    /* Handle specific town */
    private void handleTown(Town town, Map<String, AreaMarker> newmap, Map<String, Marker> newmark) {
        String name = town.getName();
        double[] x = null;
        double[] z = null;
        int poly_index = 0; /* Index of polygon for given town */
                
        /* Handle areas */
    	Set<Plot> plots = town.getPlots();
    	if(plots.isEmpty())
    	    return;
        /* Build popup */
        String desc = formatInfoWindow(town);

    	HashMap<String, TileFlags> blkmaps = new HashMap<String, TileFlags>();
        LinkedList<Plot> nodevals = new LinkedList<Plot>();
        EvitaWorld curworld = null;
        TileFlags curblks = null;
        boolean vis = false;
    	/* Loop through blocks: set flags on blockmaps for worlds */
    	for(Plot p : plots) {
    	    
    	    if(p.getWorld() != curworld) { /* Not same world */
    	        String wname = p.getWorld().getName();
    	        vis = isVisible(name, wname);  /* See if visible */
    	        if(vis) {  /* Only accumulate for visible areas */
    	            curblks = blkmaps.get(wname);  /* Find existing */
    	            if(curblks == null) {
    	                curblks = new TileFlags();
    	                blkmaps.put(wname, curblks);   /* Add fresh one */
    	            }
    	        }
    	        curworld = p.getWorld();
    	    }
    	    if(vis) {
    	        curblks.setFlag(p.getX(), p.getZ(), true); /* Set flag for block */
    	        nodevals.addLast(p);
    	    }
    	}
        /* Loop through until we don't find more areas */
        while(nodevals != null) {
            LinkedList<Plot> ournodes = null;
            LinkedList<Plot> newlist = null;
            TileFlags ourblks = null;
            int minx = Integer.MAX_VALUE;
            int minz = Integer.MAX_VALUE;
            for(Plot node : nodevals) {
                int nodex = node.getX();
                int nodez = node.getZ();
                if(ourblks == null) {   /* If not started, switch to world for this block first */
                    if(node.getWorld() != curworld) {
                        curworld = node.getWorld();
                        curblks = blkmaps.get(curworld.getName());
                    }
                }
                /* If we need to start shape, and this block is not part of one yet */
                if((ourblks == null) && curblks.getFlag(nodex, nodez)) {
                    ourblks = new TileFlags();  /* Create map for shape */
                    ournodes = new LinkedList<Plot>();
                    floodFillTarget(curblks, ourblks, nodex, nodez);   /* Copy shape */
                    ournodes.add(node); /* Add it to our node list */
                    minx = nodex; minz = nodez;
                }
                /* If shape found, and we're in it, add to our node list */
                else if((ourblks != null) && (node.getWorld() == curworld) &&
                    (ourblks.getFlag(nodex, nodez))) {
                    ournodes.add(node);
                    if(nodex < minx) {
                        minx = nodex; minz = nodez;
                    }
                    else if((nodex == minx) && (nodez < minz)) {
                        minz = nodez;
                    }
                }
                else {  /* Else, keep it in the list for the next polygon */
                    if(newlist == null) newlist = new LinkedList<Plot>();
                    newlist.add(node);
                }
            }
            nodevals = newlist; /* Replace list (null if no more to process) */
            if(ourblks != null) {
                /* Trace outline of blocks - start from minx, minz going to x+ */
                int init_x = minx;
                int init_z = minz;
                int cur_x = minx;
                int cur_z = minz;
                direction dir = direction.XPLUS;
                ArrayList<int[]> linelist = new ArrayList<int[]>();
                linelist.add(new int[] { init_x, init_z } ); // Add start point
                while((cur_x != init_x) || (cur_z != init_z) || (dir != direction.ZMINUS)) {
                    switch(dir) {
                        case XPLUS: /* Segment in X+ direction */
                            if(!ourblks.getFlag(cur_x+1, cur_z)) { /* Right turn? */
                                linelist.add(new int[] { cur_x+1, cur_z }); /* Finish line */
                                dir = direction.ZPLUS;  /* Change direction */
                            }
                            else if(!ourblks.getFlag(cur_x+1, cur_z-1)) {  /* Straight? */
                                cur_x++;
                            }
                            else {  /* Left turn */
                                linelist.add(new int[] { cur_x+1, cur_z }); /* Finish line */
                                dir = direction.ZMINUS;
                                cur_x++; cur_z--;
                            }
                            break;
                        case ZPLUS: /* Segment in Z+ direction */
                            if(!ourblks.getFlag(cur_x, cur_z+1)) { /* Right turn? */
                                linelist.add(new int[] { cur_x+1, cur_z+1 }); /* Finish line */
                                dir = direction.XMINUS;  /* Change direction */
                            }
                            else if(!ourblks.getFlag(cur_x+1, cur_z+1)) {  /* Straight? */
                                cur_z++;
                            }
                            else {  /* Left turn */
                                linelist.add(new int[] { cur_x+1, cur_z+1 }); /* Finish line */
                                dir = direction.XPLUS;
                                cur_x++; cur_z++;
                            }
                            break;
                        case XMINUS: /* Segment in X- direction */
                            if(!ourblks.getFlag(cur_x-1, cur_z)) { /* Right turn? */
                                linelist.add(new int[] { cur_x, cur_z+1 }); /* Finish line */
                                dir = direction.ZMINUS;  /* Change direction */
                            }
                            else if(!ourblks.getFlag(cur_x-1, cur_z+1)) {  /* Straight? */
                                cur_x--;
                            }
                            else {  /* Left turn */
                                linelist.add(new int[] { cur_x, cur_z+1 }); /* Finish line */
                                dir = direction.ZPLUS;
                                cur_x--; cur_z++;
                            }
                            break;
                        case ZMINUS: /* Segment in Z- direction */
                            if(!ourblks.getFlag(cur_x, cur_z-1)) { /* Right turn? */
                                linelist.add(new int[] { cur_x, cur_z }); /* Finish line */
                                dir = direction.XPLUS;  /* Change direction */
                            }
                            else if(!ourblks.getFlag(cur_x-1, cur_z-1)) {  /* Straight? */
                                cur_z--;
                            }
                            else {  /* Left turn */
                                linelist.add(new int[] { cur_x, cur_z }); /* Finish line */
                                dir = direction.XMINUS;
                                cur_x--; cur_z--;
                            }
                            break;
                    }
                }
                /* Build information for specific area */
                String polyid = town.getName() + "__" + poly_index;
                int sz = linelist.size();
                x = new double[sz];
                z = new double[sz];
                for(int i = 0; i < sz; i++) {
                    int[] line = linelist.get(i);
                    x[i] = (double)line[0] * (double)16;
                    z[i] = (double)line[1] * (double)16;
                }
                /* Find existing one */
                AreaMarker m = resareas.remove(polyid); /* Existing area? */
                if(m == null) {
                    m = set.createAreaMarker(polyid, name, false, curworld.getName(), x, z, false);
                    if(m == null) {
                        return;
                    }
                }
                else {
                    m.setCornerLocations(x, z); /* Replace corner locations */
                    m.setLabel(name);   /* Update label */
                }
                m.setDescription(desc); /* Set popup */
            
                /* Set line and fill properties */
                Nation nation = null;
                if(town.hasNation())
                	nation = town.getNation();
                addStyle(nation, m);

                /* Add to map */
                newmap.put(polyid, m);
                poly_index++;
            }
        }
        
        Location home = town.getSpawn();
            
		if (isVisible(name, home.getWorld().getName())) {
			String markid = town.getName() + "__home";
			MarkerIcon ico = getMarkerIcon(town);
			if (ico != null) {
				Marker homeMarker = resmark.remove(markid);
				if (homeMarker == null) {
					homeMarker = set.createMarker(markid, name + " [home]", home.getWorld().getName(), home.getX(), 64,
							home.getZ(), ico, false);
					if (homeMarker == null)
						return;
				} else {
					homeMarker.setLocation(home.getWorld().getName(), home.getX(), 64, home.getZ());
					homeMarker.setLabel(name + " [home]"); /* Update label */
					homeMarker.setMarkerIcon(ico);
				}
				homeMarker.setDescription(desc); /* Set popup */
				newmark.put(markid, homeMarker);
			}
		}
	}
    
    /* Update Towny information */
    private void updateTowns() {
        Map<String,AreaMarker> newmap = new HashMap<String,AreaMarker>(); /* Build new map */
        Map<String,Marker> newmark = new HashMap<String,Marker>(); /* Build new map */
        
        for(Town t : universe.getTowns()) {
    		handleTown(t, newmap, newmark);
        }
        /* Now, review old map - anything left is gone */
        for(AreaMarker oldm : resareas.values()) {
            oldm.deleteMarker();
        }
        for(Marker oldm : resmark.values()) {
            oldm.deleteMarker();
        }
        /* And replace with new map */
        resareas = newmap;
        resmark = newmark;
                
    }
    
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event) {
		Plugin p = event.getPlugin();
		String name = p.getDescription().getName();
		if (name.equals("dynmap")) {
			if (dynmap.isEnabled()) {
				activate();
			}
		}
	}
        /*
        @EventHandler(priority=EventPriority.MONITOR)
        public void onChangePlot(PlayerChangePlotEvent event) {
            WorldCoord fromblk = event.getFrom();
            WorldCoord toblk = event.getFrom();
            try {
                TownBlock tb = fromblk.getTownBlock();
                if(tb != null) {
                    Town t = tb.getTown();
                    if(t != null) {
                        requestUpdateTownMap(t);
                    }
                }
            } catch (NotRegisteredException nrx) {
            }
            try {
                TownBlock tb = toblk.getTownBlock();
                if(tb != null) {
                    Town t = tb.getTown();
                    if(t != null) {
                        requestUpdateTownMap(t);
                    }
                }
            } catch (NotRegisteredException nrx) {
            }
        }
        */
    
    private void activate() {
        markerapi = api.getMarkerAPI();
        if(markerapi == null) {
        	
            return;
        }
        /* Connect to towny API */
        universe = plugin.getUniverseManager();
        /* Load configuration */
        if(reload) {
            plugin.getConfigManager().reload();
            if(set != null) {
                set.deleteMarkerSet();
                set = null;
            }
        }
        else {
            reload = true;
        }
        
        /* Now, add marker set for mobs (make it transient) */
        set = markerapi.getMarkerSet("universe.markerset");
        if(set == null) {
            set = markerapi.createMarkerSet("universe.markerset", "Universe", null, false);
    	} else {
            set.setMarkerSetLabel("Universe");
    	}
        
        if(set == null)
            return;
        
        set.setLayerPriority(10);
        set.setHideByDefault(false);
        
        new Update().runTaskLaterAsynchronously(plugin, 40);   /* First time is 2 seconds */
    }

    public void onDisable() {
        if(set != null) {
            set.deleteMarkerSet();
            set = null;
        }
        resareas.clear();
        stop = true;
    }

}