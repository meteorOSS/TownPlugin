package com.meteor.meteortown.events;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.data.town.invholder.*;
import com.meteor.meteortown.flag.IFlag;
import com.meteor.meteortown.util.InvUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

public class InventoryClickEvents implements Listener {
    MeteorTown plugin;
    MessageManager messageManager;
    public  InventoryClickEvents(){
        plugin = MeteorTown.Instance;
        messageManager = plugin.getTownManager().getMessageManager();
    }
    @EventHandler
    void createTown(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof CreateTownHolder){
            CreateTownHolder createTownHolder = (CreateTownHolder)clickEvent.getInventory().getHolder();
            Player p = (Player)clickEvent.getWhoClicked();
            clickEvent.setCancelled(true);
            switch (clickEvent.getRawSlot()){
                case 3:
                    plugin.getTownManager().createNewTown(p,createTownHolder.getSize(),true);
                    return;
                case 5:
                    plugin.getTownManager().createNewTown(p,createTownHolder.getSize(),false);
                    return;
                default:
                    return;
            }
        }
    }
    @EventHandler
    void upSize(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof LevelUpTownInvHolder
        &&clickEvent.getCurrentItem()!=null&&clickEvent.getCurrentItem().getType()!=Material.AIR){
            LevelUpTownInvHolder levelUpTownInvHolder = (LevelUpTownInvHolder)clickEvent.getInventory().getHolder();
            clickEvent.setCancelled(true);
            if(clickEvent.getRawSlot()==4&&!levelUpTownInvHolder.isLevelMax()){
                plugin.getTownManager().upSize(levelUpTownInvHolder.getTown(),levelUpTownInvHolder.getUpSize(),(Player)clickEvent.getWhoClicked(),false);
                InvUtil.openLevelUp((Player)clickEvent.getWhoClicked(),levelUpTownInvHolder.getTown());
                return;
            }else {
                return;
            }
        }
    }
    @EventHandler
    void globalAdminPerm(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof AdminPerMenuInvHolder
        &&clickEvent.getCurrentItem()!=null&&clickEvent.getCurrentItem().getType()!=Material.AIR){
            Player p = (Player)clickEvent.getWhoClicked();
            clickEvent.setCancelled(true);
            AdminPerMenuInvHolder adminPerMenuInvHolder = (AdminPerMenuInvHolder)clickEvent.getInventory().getHolder();
            int rawSlot = clickEvent.getRawSlot();
            Town town = adminPerMenuInvHolder.getTown();
            if(adminPerMenuInvHolder.getIntegerIFlagMap().containsKey(rawSlot)){
                IFlag iFlag = adminPerMenuInvHolder.getIntegerIFlagMap().get(rawSlot);
                town.setPerm(iFlag.getName(),town.isPermEnable(iFlag.getName(),iFlag.getDefaultSetting())?false:true);
                InvUtil.openAdminPerMenu(town,p,adminPerMenuInvHolder.getPageCur());
            }else{
                switch (rawSlot){
                    case 48:
                        if(adminPerMenuInvHolder.getPageCur()==1){
                            messageManager.sendMessage(p,"message.start-page");
                            return;
                        }
                        InvUtil.openAdminPerMenu(adminPerMenuInvHolder.getTown(),p,adminPerMenuInvHolder.getPageCur()-1);
                    case 50:
                        if(adminPerMenuInvHolder.getPageCur()==adminPerMenuInvHolder.getPageMax()){
                            messageManager.sendMessage(p,"message.end-page");
                            return;
                        }
                        InvUtil.openAdminPerMenu(adminPerMenuInvHolder.getTown(),p,adminPerMenuInvHolder.getPageCur()+1);
                    default:
                        return;
                }
            }
        }
    }
    @EventHandler
    void adminMembersPermission(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof AdminTownInvHolder&&
                clickEvent.getCurrentItem()!=null&&clickEvent.getCurrentItem().getType()!=Material.AIR){
            clickEvent.setCancelled(true);
            AdminTownInvHolder adminTownInvHolder = (AdminTownInvHolder)clickEvent.getInventory().getHolder();
            int rawSlot = clickEvent.getRawSlot();
            Player p = (Player)clickEvent.getWhoClicked();
            Town town = adminTownInvHolder.getTown();
            String member = adminTownInvHolder.getMember();
            if(adminTownInvHolder.getIntegerIFlagMap().containsKey(rawSlot)){
                IFlag iFlag = adminTownInvHolder.getIntegerIFlagMap().get(rawSlot);
                boolean bool = town.isHasPerm(member,iFlag.getName(),iFlag.getDefaultSetting())?false:true;
                town.setMembersPer(member,iFlag.getName(),bool);
                InvUtil.openAdminMemberMenu(adminTownInvHolder.getTown(),member,p,1);
                return;
            }else{
                switch (rawSlot){
                    case 48:
                        if(adminTownInvHolder.getPageCur()==1){
                            messageManager.sendMessage(p,"message.start-page");
                            return;
                        }
                        InvUtil.openAddMemberList(adminTownInvHolder.getTown(),p,adminTownInvHolder.getPageCur()-1);
                    case 50:
                        if(adminTownInvHolder.getPageCur()==adminTownInvHolder.getPageMax()){
                            messageManager.sendMessage(p,"message.end-page");
                            return;
                        }
                        InvUtil.openAddMemberList(adminTownInvHolder.getTown(),p,adminTownInvHolder.getPageCur()+1);
                    case 49:
                        InvUtil.openMemberList(adminTownInvHolder.getTown(),p);
                        return;
                    default:
                        return;
                }
            }
        }
    }
    @EventHandler
    void invitePlayerJoinTown(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof AddMemberListInvHolder&&
        clickEvent.getCurrentItem()!=null&&clickEvent.getCurrentItem().getType()!=Material.AIR){
            AddMemberListInvHolder addMemberListInvHolder = (AddMemberListInvHolder)clickEvent.getInventory().getHolder();
            clickEvent.setCancelled(true);
            int rawSlot = clickEvent.getRawSlot();
            Player p = (Player)clickEvent.getWhoClicked();
            if(addMemberListInvHolder.getPlayerMap().containsKey(rawSlot)){
                addMemberListInvHolder.getTown().getMembers().put(addMemberListInvHolder.getPlayerMap().get(rawSlot),new HashMap<>());
                InvUtil.openMemberList(addMemberListInvHolder.getTown(),p);
                return;
            }else{
                switch (rawSlot){
                    case 48:
                        if(addMemberListInvHolder.getPageCur()==1){
                            messageManager.sendMessage(p,"message.start-page");
                            return;
                        }
                        InvUtil.openAddMemberList(addMemberListInvHolder.getTown(),p,addMemberListInvHolder.getPageCur()-1);
                    case 50:
                        if(addMemberListInvHolder.getPageCur()==addMemberListInvHolder.getPageMax()){
                            messageManager.sendMessage(p,"message.end-page");
                            return;
                        }
                        InvUtil.openAddMemberList(addMemberListInvHolder.getTown(),p,addMemberListInvHolder.getPageCur()+1);
                    case 49:
                        InvUtil.openMemberList(addMemberListInvHolder.getTown(),p);
                        return;
                    default:
                        return;
                }
            }
        }
    }
    @EventHandler
    void onClick(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof MemberListInvHolder
        &&clickEvent.getCurrentItem()!=null&&clickEvent.getCurrentItem().getType()!= Material.AIR){
            clickEvent.setCancelled(true);
            MemberListInvHolder memberListInvHolder = (MemberListInvHolder)clickEvent.getInventory().getHolder();
            int rawSlot = clickEvent.getRawSlot();
            if(memberListInvHolder.getMeberMap().containsKey(rawSlot)){
                String member = memberListInvHolder.getMeberMap().get(rawSlot);
                if(clickEvent.getClick()== ClickType.LEFT||clickEvent.getClick()==ClickType.SHIFT_LEFT){
                    InvUtil.openAdminMemberMenu(memberListInvHolder.getTown(),member,(Player)clickEvent.getWhoClicked(),1);
                }else{
                    memberListInvHolder.getTown().getMembers().remove(member);
                    clickEvent.getWhoClicked().sendMessage(messageManager.getString("message.kick-member").replace("@m@",member));
                    InvUtil.openMemberList(memberListInvHolder.getTown(),(Player)clickEvent.getWhoClicked());
                    return;
                }
                return;
            }else{
                switch (clickEvent.getRawSlot()){
                    case 49:
                        InvUtil.openAddMemberList(memberListInvHolder.getTown(),(Player)clickEvent.getWhoClicked(),1);
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
