package com.driver;

import java.util.*;

public class WhatsappRepository {

    HashMap<String,User> UserstringHashMap = new HashMap<>();
    HashMap<Group, List<User>> groupUserHashMap = new HashMap<>();
    HashMap<Group, List<Message>> groupListHashMap = new HashMap<>();
    List<Message> list=new ArrayList<>();
    HashMap<User,List<Message>> userMessageList=new HashMap<>();
    private int g_Count=0;
    private int m_Count=0;

    public String createUser(String name, String mobile) throws Exception {
        if(UserstringHashMap.containsKey(mobile))
        {
            throw new Exception("User already exists");
        }

        User user=new User(name, mobile);
        UserstringHashMap.put(mobile,user);

        return "SUCCESS";
    }
    public int sendMessage(Message message , User sender , Group group) throws Exception {
        if(!groupUserHashMap.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }
        boolean isExist=false;
        for(User user:groupUserHashMap.get(group))
        {
            if(user.equals(sender))
            {
                isExist =true;
                break;
            }
        }
        if(!isExist)
        {
            throw new Exception("You are not allowed to send message");
        }

        if(groupListHashMap.containsKey(group))
        {
            groupListHashMap.get(group).add(message);
        }
        else
        {
            List<Message> messages=new ArrayList<>();
            messages.add(message);
            groupListHashMap.put(group,messages);
        }

        if(userMessageList.containsKey(sender))
        {
            userMessageList.get(sender).add(message);
        }
        else
        {
            List<Message> messages=new ArrayList<>();
            messages.add(message);
            userMessageList.put(sender,messages);
        }

        return groupListHashMap.get(group).size();
    }
    public int createMessage(String content){
        Message message=new Message(++m_Count,content);
        message.setTimestamp(new Date());
        list.add(message);
        return m_Count;
//        int messageId = 0;
//        Message message = new Message(messageId++,content,new Date());
//        return message.getId();
    }
    public Group createGroup(List<User> users){
        if(users.size()==2)
        {
            Group group=new Group(users.get(1).getName(),2);
            groupUserHashMap.put(group,users);
            return group;
        }
        Group group = new Group("Group "+ ++g_Count,users.size());
        groupUserHashMap.put(group,users);
        return group;
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserHashMap.containsKey(group))
        {
            throw new Exception("Group does not exist");
        }

        User pastAdmin=groupUserHashMap.get(group).get(0);

        if(!approver.equals(pastAdmin))
        {
            throw new Exception("Approver does not have rights");
        }

        boolean check=false;
        for(User user1:groupUserHashMap.get(group))
        {
            if(user1.equals(user))   check=true;
        }

        if(!check)
        {
            throw new Exception("User is not a participant");
        }

        User newAdmin=null;

        Iterator<User> userIterator = groupUserHashMap.get(group).iterator();

        while(userIterator.hasNext())
        {
            User user1= userIterator.next();
            if(user1.equals(user))
            {
                newAdmin = user1;
                userIterator.remove();
            }
        }

        groupUserHashMap.get(group).add(0,newAdmin);
        return  "SUCCESS";
    }
    public int RemoveUser(User user) throws Exception {
        boolean IsuserFound = false;
        int groupSize = 0;
        int messageCount = 0;
        int overallMessageCount = list.size();
        Group groupToRemoveFrom = null;
        for (Map.Entry<Group, List<User>> entry : groupUserHashMap.entrySet()) {
            List<User> groupUsers = entry.getValue();
            if (groupUsers.contains(user))
            {
                IsuserFound = true;
                groupToRemoveFrom = entry.getKey();
                if (groupUsers.get(0).equals(user))
                {
                    throw new Exception("Cannot remove admin");
                }
                groupUsers.remove(user);
                groupSize = groupUsers.size();
                break;
            }
        }
        if (!IsuserFound)
        {
            throw new Exception("User not found");
        }

        if (userMessageList.containsKey(user))
        {
            messageCount = userMessageList.get(user).size() - 2;
            userMessageList.remove(user);
        }
        return groupSize + messageCount + overallMessageCount;

    }
    public String findMessage(Date start, Date end, int K){
        return "Hii";
    }
}
