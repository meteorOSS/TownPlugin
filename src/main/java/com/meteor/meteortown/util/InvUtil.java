package com.meteor.meteortown.util;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteorlib.util.PageUtil;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.PayType;
import com.meteor.meteortown.data.town.TakeItem;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.data.town.UpSize;
import com.meteor.meteortown.data.town.invholder.*;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.flag.IFlag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvUtil {
    private static MeteorTown plugin = MeteorTown.Instance;
    private static MessageManager messageManager = plugin.getTownManager().getMessageManager();
    public static void openMemberList(Town town, Player player){
        MemberListInvHolder memberListInvHolder = new MemberListInvHolder();
        memberListInvHolder.setTown(town);
        memberListInvHolder.setMeberMap(town.getMembers().keySet());
        Inventory inv = Bukkit.createInventory(memberListInvHolder,6*9,messageManager.getString("title.member-list"));
        Arrays.asList(45,48,50,46,47,51,52,53).forEach(a->inv.setItem(a,getItem("item.flag")));
        inv.setItem(49,getItem("item.addMember"));
        player.openInventory(inv);
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            memberListInvHolder.getMeberMap().values().forEach(meber->{
                ItemStack head = new ItemStack(Material.SKULL_ITEM);
                head.setDurability((short)3);
                SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
                skullMeta.setOwner(meber);
                skullMeta.setDisplayName(messageManager.getString("item.member.name").replace("@member@",meber));
                List<String> lore = new ArrayList<>();
                messageManager.getStringList("item.member.lore").forEach(l->lore.add(l.replace("@online@",Bukkit.getPlayerExact(meber)==null
                ?"§c离线":"§a在线")));
                skullMeta.setLore(lore);
                head.setItemMeta(skullMeta);
                inv.addItem(head);
            });
        });
    }
    public static void openCreateTownGui(Player player,int[] size){
        CreateTownHolder createTownHolder = new CreateTownHolder();
        createTownHolder.setPlayer(player);
        createTownHolder.setSize(size);
        Inventory inv = Bukkit.createInventory(createTownHolder,9,messageManager.getString("title.create-town"));
        Arrays.asList(0,1,2,4,6,7,8).forEach(a->inv.setItem(a,getItem("item.flag")));
        inv.setItem(3,getItem("item.paste"));
        inv.setItem(5,getItem("item.no-paste"));
        player.openInventory(inv);
    }
    public static void openAddMemberList(Town town,Player player,int page){
        AddMemberListInvHolder addMemberListInvHolder =  new AddMemberListInvHolder();
        addMemberListInvHolder.setPageCur(page);
        addMemberListInvHolder.setTown(town);
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        playerList.removeIf(a->town.getMembers().containsKey(a.getName())||a.getName().equalsIgnoreCase(player.getName()));
        List<Player> players = PageUtil.getPageList(playerList,page,57);
        addMemberListInvHolder.setPlayerMap(players);
        addMemberListInvHolder.setPageMax((int)Math.ceil(playerList.size()/57.0D));
        Inventory inv = Bukkit.createInventory(addMemberListInvHolder,6*9,messageManager.getString("title.addmember-list"));
        Arrays.asList(45,46,47,51,52,53).forEach(a->inv.setItem(a,getItem("item.flag")));
        inv.setItem(48,getItem("item.pre"));
        inv.setItem(50,getItem("item.next"));
        inv.setItem(49,getItem("item.back"));
        player.openInventory(inv);
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            players.forEach(a->{
                ItemStack head = new ItemStack(Material.SKULL_ITEM);
                head.setDurability((short)3);
                SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
                skullMeta.setOwner(a.getName());
                skullMeta.setDisplayName(messageManager.getString("item.memberAdd.name").replace("@member@",a.getName()));
                skullMeta.setLore(messageManager.getStringList("item.memberAdd.lore"));
                head.setItemMeta(skullMeta);
                inv.addItem(head);
            });
        });
    }
    public static void openLevelUp(Player player,Town town){
        LevelUpTownInvHolder levelUpTownInvHolder = new LevelUpTownInvHolder();
        String group = Util.getUpLevelGroup(player);
        String path = "up-size."+group;
        ConfigurationSection items = plugin.getConfig().getConfigurationSection(path+".items");
        List<TakeItem> takeItems = new ArrayList<>();
        items.getKeys(false).forEach(item->{
            ItemStack itemStack = new ItemStack(Material.valueOf(item),1,(short)items.getInt(item+".data"));
            itemStack.setAmount(items.getInt(item+".amount"));
            takeItems.add(new TakeItem(items.getString(item+".name"),itemStack));
        });
        UpSize upSize = new UpSize(plugin.getConfig().getInt(path+".x"),plugin.getConfig().getInt(path+".y"),plugin.getConfig().getInt(path+".z"),
                PayType.valueOf(plugin.getConfig().getString(path+".type")),plugin.getConfig().getInt(path+".vaul"),takeItems);
        levelUpTownInvHolder.setUpSize(upSize);
        levelUpTownInvHolder.setTown(town);
        if(town.getSize()[0]>=plugin.getConfig().getInt("Setting.up-max.x")&&town.getSize()[1]>=plugin.getConfig().getInt("Setting.up-max.y")
                &&town.getSize()[2]>=plugin.getConfig().getInt("Setting.up-max.z")){
            levelUpTownInvHolder.setLevelMax(true);
        }
        Inventory inv = Bukkit.createInventory(levelUpTownInvHolder,9,messageManager.getString("title.level-up"));
        List<String> lore = new ArrayList<>();
        if(!levelUpTownInvHolder.isLevelMax()){
            ItemStack itemStack = getItem("item.levelUp");
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getLore().forEach(a->{
                if(a.equalsIgnoreCase("@items@")){
                    upSize.getTakeItemList().forEach(item->{
                        String itemLore = messageManager.getString("message.item-req").replace("@i@",item.getName()).replace("@a@",item.getItemStack().getAmount()+"");
                        lore.add(itemLore);
                    });
                    return;
                }
                a = a.replace("@cx@",town.getSize()[0]+"")
                        .replace("@cy@",town.getSize()[1]+"")
                        .replace("@cz@",town.getSize()[2]+"")
                        .replace("@lx@",town.getSize()[0]+upSize.getX()+"")
                        .replace("@ly@",town.getSize()[1]+upSize.getY()+"")
                        .replace("@lz@",town.getSize()[2]+upSize.getZ()+"")
                        .replace("@type@", upSize.getPayType()==PayType.POINTS?"点卷":"金币")
                        .replace("@v@",upSize.getPoints()+"");
                lore.add(a);
            });
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inv.setItem(4,itemStack);
        }else{
            inv.setItem(4,getItem("item.upMax"));
        }
        Arrays.asList(0,1,2,3,5,6,7,8).forEach(a->inv.setItem(a,getItem("item.flag")));
        player.openInventory(inv);
    }
    public static void openAdminPerMenu(Town town,Player owner,int page){
        AdminPerMenuInvHolder adminPerMenuInvHolder = new AdminPerMenuInvHolder();
        adminPerMenuInvHolder.setTown(town);
        List<IFlag> iFlagList = FlagManager.getFlagList(false);
        List<IFlag> pageIFlags = PageUtil.getPageList(iFlagList,page,57);
        adminPerMenuInvHolder.setIntegerIFlagMap(pageIFlags);
        adminPerMenuInvHolder.setPageCur(page);
        adminPerMenuInvHolder.setPageMax((int)Math.ceil(iFlagList.size()/57.0D));
        Inventory inv = Bukkit.createInventory(adminPerMenuInvHolder,6*9,messageManager.getString("title.admin-perm"));
        Arrays.asList(45,46,47,51,52,53,49).forEach(a->inv.setItem(a,getItem("item.flag")));
        inv.setItem(48,getItem("item.pre"));
        inv.setItem(50,getItem("item.next"));
        owner.openInventory(inv);
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            adminPerMenuInvHolder.getIntegerIFlagMap().values().forEach(flag -> {
                boolean bool = town.isPermEnable(flag.getName(),flag.getDefaultSetting());
                inv.addItem(flag.getIconItem(bool));
            });
        });
    }
    public static void openAdminMemberMenu(Town town,String members,Player owner,int page){
        AdminTownInvHolder adminTownInvHolder = new AdminTownInvHolder();
        adminTownInvHolder.setTown(town);
        adminTownInvHolder.setMember(members);
        List<IFlag> flagList = PageUtil.getPageList(FlagManager.getFlagList(true),page,57);
        adminTownInvHolder.setPageCur(page);
        adminTownInvHolder.setPageMax((int)Math.ceil(FlagManager.getFlagList(true).size()/57.0D));
        adminTownInvHolder.setIntegerIFlagMap(flagList);
        Inventory inv = Bukkit.createInventory(adminTownInvHolder,6*9,messageManager.getString("title.admin-member").replace("@m@",members));
        Arrays.asList(45,46,47,51,52,53).forEach(a->inv.setItem(a,getItem("item.flag")));
        inv.setItem(48,getItem("item.pre"));
        inv.setItem(50,getItem("item.next"));
        inv.setItem(49,getItem("item.back"));
        owner.openInventory(inv);
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            adminTownInvHolder.getIntegerIFlagMap().values().forEach(flag -> {
                boolean bool = town.isHasPerm(members,flag.getName(),flag.getDefaultSetting());
                inv.addItem(flag.getIconItem(bool));
            });
        });
    }

    private static ItemStack getItem(String path){
        ItemStack itemStack = new ItemStack(Material.valueOf(messageManager.getString(path+".id")),1,Short.valueOf(messageManager.getString(path+".data")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(messageManager.getString(path+".name"));
        itemMeta.setLore(messageManager.getStringList(path+".lore"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
